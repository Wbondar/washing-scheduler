package ch.protonmail.vladyslavbond.washing_scheduler.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public final class UnicodeEnforcer
extends Object
implements Filter
{
	@Override
	public void init (FilterConfig config)
	throws ServletException
	{

	}

	@Override
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
	throws ServletException, IOException
	{
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);
	}

	@Override
	public void destroy ( ) { }
}