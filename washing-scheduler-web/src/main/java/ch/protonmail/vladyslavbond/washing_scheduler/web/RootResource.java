package ch.protonmail.vladyslavbond.washing_scheduler.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static ch.protonmail.vladyslavbond.washing_scheduler.web.ViewFactory.*;

import java.io.IOException;

@Path("")
public class RootResource extends WashingSchedulerResource {
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response retrieve() throws IOException {
		getViewFactory().process(getHttpServletRequest(), getHttpServletResponse(), "/requests/create");
		return Response.ok().build();
	}
}
