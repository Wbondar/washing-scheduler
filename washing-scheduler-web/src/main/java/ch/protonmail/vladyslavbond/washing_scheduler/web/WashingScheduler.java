package ch.protonmail.vladyslavbond.washing_scheduler.web;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("")
public class WashingScheduler extends ResourceConfig {
	public WashingScheduler() {
		packages("ch.protonmail.vladyslavbond.washing_scheduler.web");
	}
}
