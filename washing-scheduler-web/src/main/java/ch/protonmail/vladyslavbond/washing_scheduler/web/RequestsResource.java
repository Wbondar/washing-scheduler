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

import org.scribe.model.Token;
import org.thymeleaf.context.WebContext;

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
	@Produces(MediaType.TEXT_HTML)
	public Response retrieve() throws IOException {
		WebContext webContext = getContext(getHttpServletRequest(), getHttpServletResponse());
		List<Map<String, Object>> requests = new ArrayList<>();
		try (Connection connection = getConnection();) {
			try (PreparedStatement statement = connection.prepareCall("SELECT * FROM current_reservations;");) {
				try (ResultSet resultSet = statement.executeQuery();) {
					ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
					while (resultSet.next()) {
						Map<String, Object> tuple = new HashMap<>();
						int i = resultSetMetaData.getColumnCount();
						while (i > 0) {
							tuple.put(resultSetMetaData.getColumnLabel(i), resultSet.getObject(i));
							i--;
						}
						requests.add(tuple);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		webContext.setVariable("requests", requests);
		getViewFactory().process(getHttpServletRequest(), getHttpServletResponse(), webContext);
		return Response.ok().build();
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
