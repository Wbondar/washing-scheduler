package ch.protonmail.vladyslavbond.washing_scheduler.web;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static ch.protonmail.vladyslavbond.washing_scheduler.datasource.NativeConnectionFactory.*;
import static ch.protonmail.vladyslavbond.washing_scheduler.web.ViewFactory.*;

import ch.protonmail.vladyslavbond.washing_scheduler.domain.*;

import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.thymeleaf.context.WebContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("requests")
public class RequestsResource extends WashingSchedulerResource {
	private final static URI SESSIONS_CREATE_URI;
	private final static URI QUEUE_URI;
	private static final URI REQUESTS_CREATE;
	
	static {
		try {
			SESSIONS_CREATE_URI = new URI("/sessions/create");
			QUEUE_URI = new URI("/requests/queue");
			REQUESTS_CREATE = new URI("/requests/create");
		} catch (URISyntaxException e) {
			throw new AssertionError(e);
		}
	}
	
	@GET
	@Path("queue")
	@Produces("text/html; charset=UTF-8")
	public Response retrieve() throws IOException {
		final List<Map<String, Object>> requests = new ArrayList<>();
		try (Connection connection = getConnection();) 
		{
			try 
			(
				PreparedStatement queueLookUpStatement = connection.prepareCall("SELECT * FROM current_reservations;");
				PreparedStatement accountLookUpStatement = connection.prepareStatement("SELECT * FROM accounts JOIN account_providers ON accounts.provider_id = account_providers.id WHERE owner_id = ? LIMIT 1;");
			) 
			{
				try (ResultSet resultSet = queueLookUpStatement.executeQuery();) 
				{
					final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
					final ObjectMapper objectMapper = getObjectMapper();
					while (resultSet.next()) 
					{

						Map<String, Object> tuple = new HashMap<>();
						
						int i = resultSetMetaData.getColumnCount();
						while (i > 0) {
							tuple.put(resultSetMetaData.getColumnLabel(i), resultSet.getObject(i));
							i--;
						}
				
						requests.add(tuple);
						
						try
						{	
							accountLookUpStatement.setObject(1, resultSet.getObject("client_id"));
							try 
							(
								ResultSet accountResultSet = accountLookUpStatement.executeQuery();
							) 
							{
								accountResultSet.next();
								String accountProviderMeta = accountResultSet.getString("meta");
								WashingSchedulerOAuthService service = WashingSchedulerOAuthService.valueOf(accountProviderMeta);
								OAuthRequest request = new OAuthRequest(Verb.GET, String.format(service.getProperty("localizedProfile"), getHttpServletRequest().getLocale().toString()));
								Token accessToken = new Token(accountResultSet.getString("token"), service.getProperty("apiSecret"));
								service.signRequest(accessToken, request);
								org.scribe.model.Response oauthResponse = request.send();
								
								
								if (oauthResponse.isSuccessful()) 
								{
									JsonNode myData = objectMapper.readTree(oauthResponse.getBody());
									String clientName = myData.get("name").asText();
									if (clientName == null || clientName.isEmpty()) 
									{
										throw new RuntimeException ("Name is missing. Got instead: \"" + myData.asText() + "\".");
									}
									tuple.put("name", clientName);

									String link = myData.get("link").asText();
									if (link == null || link.isEmpty())
									{
										throw new RuntimeException ("Link is missing. Got instead: \"" + myData.asText() + "\".");	
									}
									tuple.put("link", link);
								}
							}
						} catch (SQLException e) {
							e.printStackTrace();
							tuple.put("name", "Unknown");
							tuple.put("link", "/users/" + resultSet.getObject("client_id"));
							continue;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		final WebContext webContext = getWebContext();
		webContext.setVariable("requests", requests);
		getViewFactory().process(webContext);
		return Response.ok().encoding("UTF-8").build();
	}
	
	@POST
	@Path("create")
	@Produces(MediaType.TEXT_HTML)
	public Response create(@FormParam("time") ReservationDuration reservationTime) throws URISyntaxException, IOException {
		if (getHttpSession() == null || getHttpSession().getAttribute("id") == null) {
			getHttpSession().setAttribute("myCallback", REQUESTS_CREATE);
			return Response
					.seeOther(new URI(WashingSchedulerOAuthService.FACEBOOK.getAuthorizationUrl(Token.empty())))
					.build();
		}
		try (Connection connection = getConnection();) {
			try (CallableStatement statement = connection.prepareCall("{CALL request_create(?::SMALLINT, NOW(), ?::INTERVAL)}");) {
				statement.setObject(1, getHttpSession().getAttribute("id"));
				statement.setObject(2, reservationTime);
				if (statement.execute()) {
					return Response.seeOther(QUEUE_URI).build();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/* TODO Implement comprehensive error messages. */
		return Response.serverError().build();
	}
	
	@GET
	@Path("create")
	@Produces(MediaType.TEXT_HTML)
	public Response create() throws IOException {
		getViewFactory().process(getHttpServletRequest(), getHttpServletResponse());
		return Response.ok().build();
	}
}
