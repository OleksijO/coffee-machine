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

import org.apache.log4j.Logger;

//@WebFilter("/*")
public class AuthentificationFilter implements Filter {
	private static final Logger logger = Logger.getLogger(AuthentificationFilter.class);
	private LoggedPersonsHolder loggedPersonsHolder = LoggedPersonsHolder.getInstance();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = ((HttpServletRequest) request);

		if (req.getRequestURI().startsWith(ADMIN)) {
			if (loggedPersonsHolder.isSessionAdminAuthentificated(req.getSession())) {
				req.setAttribute("admin", loggedPersonsHolder.getLoggedAdmin(req.getSession()));
			} else {
				req.getRequestDispatcher(ADMIN_LOGIN_PATH).forward(request, response);
				return;
			}
		}

		if (req.getRequestURI().startsWith(USER)) {
			if (loggedPersonsHolder.isSessionUserAuthentificated(req.getSession())) {
				logger.debug("user authentificated");
				req.setAttribute("user", loggedPersonsHolder.getLoggedUser(req.getSession()));
			} else {
				logger.debug("user not authentificated. redirectiong to login page");
				req.getRequestDispatcher(USER_LOGIN_PATH).forward(request, response);
				return;
			}
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
