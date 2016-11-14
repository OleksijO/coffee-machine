package coffee_machine.controller.security;

import static coffee_machine.controller.command.Pages.ADMIN;
import static coffee_machine.controller.command.Pages.ADMIN_LOGIN_PATH;
import static coffee_machine.controller.command.Pages.USER;
import static coffee_machine.controller.command.Pages.USER_LOGIN_PATH;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

//@WebFilter("/*")
public class AuthentificationFilter implements Filter {
	private static final Logger logger = Logger.getLogger(AuthentificationFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = ((HttpServletRequest) request);
		HttpSession session = req.getSession();

		if ((req.getRequestURI().startsWith(ADMIN)) && (session.getAttribute("adminId") == null)) {
			logger.debug("Admin not authentificated. redirectiong to admin login page");
			req.getRequestDispatcher(ADMIN_LOGIN_PATH).forward(request, response);
			return;
		}

		if ((req.getRequestURI().startsWith(USER)) && (session.getAttribute("userId") == null)) {
			logger.debug("User not authentificated. redirectiong to user login page");
			req.getRequestDispatcher(USER_LOGIN_PATH).forward(request, response);
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
