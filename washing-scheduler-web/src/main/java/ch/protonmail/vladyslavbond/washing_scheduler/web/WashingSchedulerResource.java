package ch.protonmail.vladyslavbond.washing_scheduler.web;

import static ch.protonmail.vladyslavbond.washing_scheduler.web.ViewFactory.getViewFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.scribe.model.Token;
import org.thymeleaf.context.WebContext;

import com.fasterxml.jackson.databind.ObjectMapper;

abstract class WashingSchedulerResource {
	@Context
	private          transient HttpServletRequest  request               = null;
	
	@Context 
	private          transient HttpServletResponse response              = null;
	
	private   static transient ObjectMapper        objectMapper          = null;
	
	protected static final     String              USER_ID_ATTRIBUTE_KEY = "id";
	
	private   static final     URI                 SESSIONS_CREATE_URI;
	
	static {
		try {
			SESSIONS_CREATE_URI = new URI("/sessions/create");
		} catch (URISyntaxException e) {
			throw new AssertionError(e);
		}
	}
	
	protected final static ObjectMapper getObjectMapper() {
		/* TODO Implement ObjectMapper factory. */
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		return objectMapper;
	}
	
	protected HttpServletRequest getHttpServletRequest() {
		return request;
	}
	
	protected HttpServletResponse getHttpServletResponse() {
		return response;
	}
	
	protected HttpSession getHttpSession(boolean flag) {
		if (request == null) {
			return null;
		}
		return request.getSession(flag);
	}
	
	protected HttpSession getHttpSession() {
		if (request == null) {
			return null;
		}
		return request.getSession();
	}
	
	protected WebContext getWebContext() {
		return ViewFactory.getContext(request, response);
	}
	
	/*
	 * Returns a particular HTML page,
	 * depending on injected HttpServletRequset object's properties.
	 */
	protected Response getDefaultResponse() throws IOException {
		getViewFactory().process(request, response);
		return Response.ok().build();
	}
	
	protected boolean isAuthenticated() {
		final HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute(USER_ID_ATTRIBUTE_KEY) != null) {
			return true;
		}
		return false;
	}
	
	/*
	 * @return redirection response if current user is not authenticated; null otherwise.
	 */
	protected Response getRedirectionResponseIfNotAuthenticated() {
		if (!isAuthenticated()) {
			return Response
					.seeOther(SESSIONS_CREATE_URI)
					.build();
		}
		return null;
	}
}
