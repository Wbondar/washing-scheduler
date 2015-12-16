package ch.protonmail.vladyslavbond.washing_scheduler.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static ch.protonmail.vladyslavbond.washing_scheduler.datasource.NativeConnectionFactory.*;

@Path("users")
public class UsersResource extends WashingSchedulerResource {
	@GET
	@Path("{id}")
	@Produces(MediaType.TEXT_HTML)
	public Response retrieve(@PathParam("id") Short userId) throws URISyntaxException {
		try (Connection connection = getConnection();) {
			try (PreparedStatement statement = connection.prepareStatement("SELECT external_id FROM accounts WHERE owner_id = ? LIMIT 1;");) {
				statement.setObject(1, userId);
				try (ResultSet resultSet = statement.executeQuery();) {
					if (resultSet.next()) {
						final String facebookAccountId = resultSet.getString(1);
						if (facebookAccountId != null && !facebookAccountId.isEmpty()) {
							return Response.seeOther(new URI("https://www.facebook.com/" + facebookAccountId)).build();	
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AssertionError(e);
		}
		return Response.serverError().build();
	}
}
