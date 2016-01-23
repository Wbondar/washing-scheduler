package ch.protonmail.vladyslavbond.washing_scheduler.web;

import java.sql.Types;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;

import static ch.protonmail.vladyslavbond.washing_scheduler.datasource.NativeConnectionFactory.*;

import static ch.protonmail.vladyslavbond.washing_scheduler.web.WashingSchedulerOAuthService.*;

import static ch.protonmail.vladyslavbond.washing_scheduler.web.ViewFactory.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("sessions")
public class SessionsResource extends WashingSchedulerResource {
	private static final URI REQUESTS_CREATE;
	private static final URI HOME;
	
	static {
		try {
			REQUESTS_CREATE = new URI("/requests/create");
			HOME = new URI("/");
		} catch (URISyntaxException e) {
			throw new AssertionError(e);
		}
	}

	@POST
	@Path("create/{service}")
	public Response create(@PathParam("service") WashingSchedulerOAuthService service) throws Exception {
		/*
		 * Delegate command to particular OAuth service provider (API).
		 */
		return Response
				.seeOther(new URI(service.getAuthorizationUrl(Token.empty())))
				.build();
	}
	
	@GET
	@Path("callback/{service}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response callback(@PathParam("service") WashingSchedulerOAuthService service, @QueryParam("code") String code) throws JsonProcessingException, IOException 
	{
		/*
		 * Send OAuth request to remote service.
		 */
		Token accessToken = service.getAccessToken(Token.empty(), new Verifier(code));
		OAuthRequest request = new OAuthRequest(Verb.GET, service.getProperty("profile"));
		service.signRequest(accessToken, request);
		org.scribe.model.Response oauthResponse = request.send();
		
		/*
		 * Terminate program if something went wrong.
		 */
		if (!oauthResponse.isSuccessful()) 
		{
			return Response.serverError().build();
		}
		
		/*
		 * Read remote service's answer, if everything went fine this far.
		 * Update database if necessary and create user session, 
		 * using internal id of the application.
		 */
		final String externalId = getObjectMapper().readTree(oauthResponse.getBody()).get("id").asText();
		try 
		(
			Connection connection = getConnection();
		) 
		{
			try 
			(
				CallableStatement statement = connection.prepareCall("{CALL user_update(?, ?, ?, ?)}");
			) 
			{
				/*
				 * Create new user using data from remote service,
				 * or update an existing one.
				 */
				statement.setObject(1, service.name());
				statement.setObject(2, externalId);
				statement.setObject(3, accessToken.getToken());
				statement.registerOutParameter(4, Types.SMALLINT);
				
				statement.execute();
				
				/*
				 * Create session using internal user id,
				 * returned by the executed database stored procedure.
				 */
				Short internalId = statement.getShort(4);
				if (internalId == null || internalId <= 0)
				{
					throw new RuntimeException("Internal id is missing.");
				}
				HttpSession session = getHttpSession(false);
				if (session != null) 
				{
					session.invalidate();
				}
				session = getHttpSession();
				session.setAttribute(USER_ID_ATTRIBUTE_KEY, internalId);
				return Response.seeOther(REQUESTS_CREATE).build();	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Response.serverError().build();
	}

	@GET
	@Path("create")
	@Produces(MediaType.TEXT_HTML)
	public Response create() throws IOException {
		return getDefaultResponse();
	}
	
	@GET
	@Path("destroy")
	@Produces(MediaType.TEXT_HTML)
	public Response getSessionTerminationForm() throws IOException {
		return getDefaultResponse();
	}
	
	@POST
	@Path("destroy")
	@Produces(MediaType.TEXT_HTML)
	public Response destroy() throws IOException {
		HttpSession session = getHttpServletRequest().getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return Response.seeOther(HOME).build();
	}
}
