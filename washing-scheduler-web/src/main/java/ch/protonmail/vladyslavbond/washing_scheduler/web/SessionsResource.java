package ch.protonmail.vladyslavbond.washing_scheduler.web;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import javax.ws.rs.core.Response.Status;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;

import static ch.protonmail.vladyslavbond.washing_scheduler.datasource.NativeConnectionFactory.*;

import static ch.protonmail.vladyslavbond.washing_scheduler.web.WashingSchedulerOAuthService.*;

import static ch.protonmail.vladyslavbond.washing_scheduler.web.ViewFactory.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@Path("sessions")
public class SessionsResource extends WashingSchedulerResource {
	private static final URI REQUESTS_CREATE;
	
	static {
		try {
			REQUESTS_CREATE = new URI("/requests/create");
		} catch (URISyntaxException e) {
			throw new AssertionError(e);
		}
	}

	@POST
	@Path("create/{service}")
	public Response create(@PathParam("service") WashingSchedulerOAuthService service) throws Exception {
		return Response
				.seeOther(new URI(service.getAuthorizationUrl(Token.empty())))
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
						Short userId = null;
						if (resultSet.next()) {
							userId = resultSet.getShort(1);	
						} else {
							try (CallableStatement statement = connection
							.prepareCall("{CALL user_create(?, ?)}");) {
								statement.setObject(1, service.name());
								statement.setObject(2, id);
								resultSet = statement.executeQuery();
								resultSet.next();
								userId = resultSet.getShort(1);
							}
						} 
						if (userId == null || userId <= 0){
							throw new AssertionError("Failed to retrieve identificator of a user while logging in.");
						}
						HttpSession session = getHttpSession(false);
						URI myCallback = null;
						if (session != null) {
							myCallback = (URI)session.getAttribute("myCallback");
							session.invalidate();
						}
						session = getHttpSession();
						session.setAttribute("id", userId);
						if (myCallback != null) {
							getHttpServletResponse().setHeader("Retry-After", "6");
							return Response.temporaryRedirect(myCallback).build();
						}
						return Response.seeOther(REQUESTS_CREATE).build();
					}
				}
			}
			return Response.seeOther(new URI("create")).build();
		} catch (Exception e) {
			e.printStackTrace(getHttpServletResponse().getWriter());
			throw e;
		}
	}

	@GET
	@Path("create")
	@Produces(MediaType.TEXT_HTML)
	public Response create() throws IOException {
		getViewFactory().process(getHttpServletRequest(), getHttpServletResponse());
		return Response.ok().build();
	}
	
	@GET
	@Path("destroy")
	@Produces(MediaType.TEXT_HTML)
	public Response getSessionTerminationForm() throws IOException {
		getViewFactory().process(getHttpServletRequest(), getHttpServletResponse());
		return Response.ok().build();
	}
	
	@POST
	@Path("destroy")
	@Produces(MediaType.TEXT_HTML)
	public Response destroy() throws IOException {
		HttpSession session = getHttpServletRequest().getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return create();
	}
}
