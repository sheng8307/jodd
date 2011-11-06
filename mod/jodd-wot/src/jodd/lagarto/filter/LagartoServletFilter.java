// Copyright (c) 2003-2011, Jodd Team (jodd.org). All Rights Reserved.

package jodd.lagarto.filter;

import jodd.io.FileNameUtil;
import jodd.servlet.DispatcherUtil;
import jodd.servlet.filter.CharArrayResponseWrapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Lagarto servlet filter takes HTML content and invokes user defined parser on it.
 * This filter is a generic one and does not use Lagarto parser explicitly.
 * It just gives a placeholder where user can add it's own parsing mechanism.
 */
public abstract class LagartoServletFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	/**
	 * Wraps the response and parse it using Lagarto parser.
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String actionPath = DispatcherUtil.getServletPath(request);

		if (acceptActionPath(request, actionPath) == false) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		CharArrayResponseWrapper wrapper = new CharArrayResponseWrapper(response);
		filterChain.doFilter(servletRequest, wrapper);
		char[] content = wrapper.toCharArray();

		if ((content != null) && (content.length != 0)) {
			content = parse(content, request);
			Writer out = servletResponse.getWriter();
			out.write(content);
		}
	}

	/**
	 * Accepts action path for further parsing. By default, only <code>*.htm(l)</code>
	 * requests are passed through and those without any extension.
	 */
	protected boolean acceptActionPath(HttpServletRequest request, String actionPath) {
		String extension = FileNameUtil.getExtension(actionPath);
		if (extension.length() == 0) {
			return true;
		}
		if (extension.equals("html") || extension.equals("htm")) {
			return true;
		}
		return false;
	}

	/**
	 * Main method that parses content. It can use Lagarto parser
	 * or any other custom parsing technology.
	 */
	protected abstract char[] parse(char[] content, HttpServletRequest request);

}