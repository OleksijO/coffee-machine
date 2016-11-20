package coffee_machine.controller.security;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static coffee_machine.controller.Attributes.ADMIN_ID;
import static coffee_machine.controller.Attributes.USER_ID;
import static coffee_machine.controller.PagesPaths.*;

//@WebFilter("/*")
public class AuthentificationFilter implements Filter {
	private static final Logger logger = Logger.getLogger(AuthentificationFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = ((HttpServletRequest) request);
		HttpSession session = req.getSession();
		logger.debug("Authentification Filter processing");
		logger.debug("user_id = "+session.getAttribute(USER_ID));
		logger.debug("admin_id = "+session.getAttribute(ADMIN_ID));
		if ((req.getRequestURI().startsWith(ADMIN)) && (session.getAttribute(ADMIN_ID) == null)
				&&(!req.getRequestURI().startsWith(ADMIN_LOGIN_SUBMIT_PATH))) {
			logger.debug("Admin not authentificated. redirectiong to admin email page");
			req.getRequestDispatcher(ADMIN_LOGIN_PATH).forward(request, response);
			return;
		}

		if ((req.getRequestURI().startsWith(USER)) && (session.getAttribute(USER_ID) == null)
				&&(!req.getRequestURI().startsWith(USER_LOGIN_SUBMIT_PATH))) {
			logger.debug("User not authentificated. redirectiong to user email page");
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
