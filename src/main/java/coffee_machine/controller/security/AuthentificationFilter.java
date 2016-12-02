package coffee_machine.controller.security;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static coffee_machine.view.Attributes.ADMIN_ID;
import static coffee_machine.view.Attributes.USER_ID;
import static coffee_machine.view.PagesPaths.*;

/**
 * This class represents security filter to restrict access of unauthorized users and admins to pages,
 * where they should not be. If filter detects mentioned access it redirects to login page for that access area.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AuthentificationFilter implements Filter {
    static final Logger logger = Logger.getLogger(AuthentificationFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = ((HttpServletRequest) request);
        HttpSession session = req.getSession();

        /* in case of not logged admin and try to go on any admin page - forwarding to admin login path */
        if ((req.getRequestURI().startsWith(ADMIN)) && (session.getAttribute(ADMIN_ID) == null)
                && (!req.getRequestURI().startsWith(ADMIN_LOGIN_PATH))) {
            req.getRequestDispatcher(ADMIN_LOGIN_PATH).forward(request, response);
            return;
        }

        /* in case of not logged user and try to go on any user page - forwarding to user login path */
        if ((req.getRequestURI().startsWith(USER)) && (session.getAttribute(USER_ID) == null)
                && (!req.getRequestURI().startsWith(USER_LOGIN_PATH))) {
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
