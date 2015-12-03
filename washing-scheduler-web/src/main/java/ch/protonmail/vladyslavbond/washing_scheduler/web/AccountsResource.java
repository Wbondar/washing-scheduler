package ch.protonmail.vladyslavbond.washing_scheduler.web;

import java.net.URI;

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

import static ch.protonmail.vladyslavbond.washing_scheduler.web.WashingSchedulerOAuthService.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@Path("accounts")
public class AccountsResource {
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
				service.setProperty(getSession(), "id", id);
				return Response.ok().entity("Well, hello, user of ID " + id + "!").build();
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
		return Response.ok().entity("<!DOCTYPE html><html><head></head><body><form method='POST' action='https://washing-scheduler.herokuapp.com/accounts/create/" + FACEBOOK.name() +"'><label>Sign in with Facebook.<input type='submit'/></label></form></body></html>").build();
	}
}
