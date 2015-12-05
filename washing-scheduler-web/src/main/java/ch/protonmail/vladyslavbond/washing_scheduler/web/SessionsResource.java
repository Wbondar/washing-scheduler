package ch.protonmail.vladyslavbond.washing_scheduler.web;

import java.net.URI;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;

import static ch.protonmail.vladyslavbond.washing_scheduler.datasource.NativeConnectionFactory.*;

import static ch.protonmail.vladyslavbond.washing_scheduler.web.WashingSchedulerOAuthService.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@Path("sessions")
public class SessionsResource {
	@Context
	private HttpServletResponse response;
	@Context 
	private HttpServletRequest request;
	
	private HttpSession getSession() {
		return request.getSession();
	}
	
	private final static ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
	
	@POST
	@Path("create/{service}")
	public Response create(@PathParam("service") WashingSchedulerOAuthService service) throws Exception {
		return Response.seeOther(new URI(service.getAuthorizationUrl(Token.empty())))
				.build();
	}
	
	@GET
	@Path("callback/{service}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response callback(@PathParam("service") WashingSchedulerOAuthService service, @QueryParam("code") String code) throws Exception {
		try
		{
			Token accessToken = service.getAccessToken(Token.empty(), new Verifier(code));
			OAuthRequest request = new OAuthRequest(Verb.GET, service.getProperty("profile"));
			service.signRequest(accessToken, request);
			org.scribe.model.Response oauthResponse = request.send();
			if (oauthResponse.isSuccessful()) {
				final String id = getObjectMapper().readTree(oauthResponse.getBody()).get("id").asText();
				try (Connection connection = getConnection();) {
					try (PreparedStatement preparedStatement = connection
							.prepareStatement("SELECT owner_id FROM service_accounts WHERE account = ? LIMIT 1;");) {
						preparedStatement.setString(1, service.name().concat(id));
						ResultSet resultSet = preparedStatement.executeQuery();
						resultSet.next();
						Short userId = resultSet.getShort(1);
						if (userId == null || userId <= 0) {
							try (CallableStatement statement = connection
							.prepareCall("{CALL user_create(?, ?)}");) {
								statement.setObject(1, service.name());
								statement.setObject(2, id);
								resultSet = statement.executeQuery();
								userId = resultSet.getShort(1);
							}
						}
						service.setProperty(getSession(), "id", userId);
						return Response.ok().entity("Well, hello, user of ID " + userId + "!").build();
					}
				}
			}
			return Response.seeOther(new URI("create")).build();
		} catch (Exception e) {
			e.printStackTrace(response.getWriter());
			throw e;
		}
	}

	@GET
	@Path("create")
	@Produces(MediaType.TEXT_HTML)
	public Response create() {
		return Response.ok().entity("<!DOCTYPE html><html><head></head><body><form method='POST' action='/sessions/create/" + FACEBOOK.name() +"'><label>Sign in with Facebook.<input type='submit'/></label></form></body></html>").build();
	}
	
	@GET
	@Path("destroy")
	@Produces(MediaType.TEXT_HTML)
	public Response getSessionTerminationForm() {
		return Response.ok().entity("<!DOCTYPE html><html><head></head><body><main><article><h1>Terminate session.</h1><form method='POST' action='/sessions/destroy'><input type='submit'></form></article></main></body></html>").build();
	}
	
	@POST
	@Path("destroy")
	@Produces(MediaType.TEXT_HTML)
	public Response destroy() {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return create();
	}
}
