package ch.protonmail.vladyslavbond.washing_scheduler.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static ch.protonmail.vladyslavbond.washing_scheduler.datasource.NativeConnectionFactory.*;

import ch.protonmail.vladyslavbond.washing_scheduler.domain.*;

@Path("requests")
public class RequestsResource extends WashingSchedulerResource {
	private final static URI SESSIONS_CREATE_URI;
	
	static {
		try {
			SESSIONS_CREATE_URI = new URI("/sessions/create");
		} catch (URISyntaxException e) {
			throw new AssertionError(e);
		}
	}
	
	@POST
	@Path("create")
	@Produces(MediaType.TEXT_HTML)
	public Response create(@FormParam("time") ReservationDuration reservationTime) throws URISyntaxException {
		if (getHttpSession() == null || getHttpSession().getAttribute("id") == null || ((String)getHttpSession().getAttribute("id")).isEmpty()) {
			return Response.seeOther(SESSIONS_CREATE_URI).build();
		}
		try (Connection connection = getConnection();) {
			try (CallableStatement statement = connection.prepareCall("{CALL request_create(?::SMALLINT, ?::INTERVAL)}");) {
				statement.setShort(1, (Short)getHttpSession().getAttribute("id"));
				statement.setObject(2, reservationTime);
				if (statement.execute()) {
					/* TODO Implement redirection to queue list on successful reservation request. */
					return Response.ok().build();
				}
				/* TODO Implement comprehensive error messages. */
				return Response.serverError().build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Response.serverError().build();
		}
		return Response.ok().build();
	}
}
