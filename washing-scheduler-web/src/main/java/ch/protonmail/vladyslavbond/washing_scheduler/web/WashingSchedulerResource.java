package ch.protonmail.vladyslavbond.washing_scheduler.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import org.thymeleaf.context.WebContext;

import com.fasterxml.jackson.databind.ObjectMapper;

abstract class WashingSchedulerResource {
	@Context
	private        transient HttpServletRequest  request      = null;
	
	@Context 
	private        transient HttpServletResponse response     = null;
	
	private static transient ObjectMapper        objectMapper = null;
	
	protected final static ObjectMapper getObjectMapper() {
		/* TODO Implement ObjectMapper factory. */
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		return objectMapper;
	}
	
	public HttpServletRequest getHttpServletRequest() {
		return request;
	}
	
	public HttpServletResponse getHttpServletResponse() {
		return response;
	}
	
	public HttpSession getHttpSession(boolean flag) {
		if (request == null) {
			return null;
		}
		return request.getSession(flag);
	}
	
	public HttpSession getHttpSession() {
		if (request == null) {
			return null;
		}
		return request.getSession();
	}
	
	public WebContext getWebContext() {
		return ViewFactory.getContext(request, response);
	}
}
