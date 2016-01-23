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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
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
import javax.ws.rs.core.Response.Status;

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
					final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).withLocale(getHttpServletRequest().getLocale());
					
					while (resultSet.next()) 
					{

						Map<String, Object> tuple = new HashMap<>();
						
						int i = resultSetMetaData.getColumnCount();
						while (i > 0) {
							Object value = resultSet.getObject(i);
							System.out.println(value.getClass().getName());
							if (value instanceof java.sql.Timestamp) {
								java.sql.Timestamp timestamp = ((java.sql.Timestamp)value);
								LocalDateTime localDateTime = timestamp.toLocalDateTime();
								value = localDateTime.format(formatter);
							} else if (value instanceof org.postgresql.util.PGInterval) {
								org.postgresql.util.PGInterval interval = ((org.postgresql.util.PGInterval)value);
								int amountOfMinutes = interval.getHours() * 60 + interval.getMinutes();
								value = String.format("%02d:%02d", amountOfMinutes / 60, amountOfMinutes % 60);
							}
							tuple.put(resultSetMetaData.getColumnLabel(i), value);	
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
	public Response create(@FormParam("datepicker") String inputDate, @FormParam("timepicker") String inputTime, @FormParam("duration") ReservationDuration reservationDuration) throws URISyntaxException, IOException {
		/*
		 * Check if the user is authenticated to the system.
		 * If not, redirect them.
		 */
		final Response redirect = getRedirectionResponseIfNotAuthenticated();
		if (redirect != null) {
			return redirect;
		}
		
		/*
		 * Convert input date and time to timestamp object,
		 * to use it to query the database.
		 * If input is invalid, set the status to "bad request".
		 */
		java.sql.Timestamp effectiveAt = null;
		try
		{
			if (inputDate == null || inputDate.isEmpty()) {
				throw new RuntimeException("Date is missing.");
			}
			if (inputTime == null || inputTime.isEmpty()) {
				throw new RuntimeException("Time is missing.");
			}
			effectiveAt = java.sql.Timestamp.valueOf(inputDate.concat(inputTime));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException(String.format("Unacceptable input. Got: \"%s\" and \"%s\" for date and time, respectively.", inputDate, inputTime));
		} catch (RuntimeException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		/*
		 * If everything went fine this far,
		 * query the database for reservation.
		 * If something went wrong, set the status to "server error".
		 */
		try (Connection connection = getConnection();) {
			try (CallableStatement statement = connection.prepareCall("{CALL request_create(?::SMALLINT, ?::TIMESTAMP, ?::INTERVAL)}");) {
				statement.setObject(1, getHttpSession().getAttribute(USER_ID_ATTRIBUTE_KEY));
				if (effectiveAt == null || effectiveAt.toLocalDateTime().isBefore(LocalDateTime.now())) {
					effectiveAt = java.sql.Timestamp.valueOf(LocalDateTime.now());
				}
				statement.setTimestamp(2, effectiveAt);
				statement.setObject(3, reservationDuration);
				if (statement.execute()) {
					return Response.seeOther(QUEUE_URI).build();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/* TODO Implement comprehensive error messages. */
		return Response.serverError().build();
	}
	
	@GET
	@Path("create")
	@Produces(MediaType.TEXT_HTML)
	public Response create() throws IOException {
		final Response redirect = getRedirectionResponseIfNotAuthenticated();
		if (redirect != null) {
			return redirect;
		}
		return getDefaultResponse();
	}
}
