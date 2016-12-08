package coffee.machine.controller.security;

import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

        // in case of not logged admin and try to go on any admin page - forwarding to admin login path
        if ((req.getRequestURI().startsWith(PagesPaths.ADMIN)) && (session.getAttribute(Attributes.ADMIN_ID) == null)
                && (!req.getRequestURI().startsWith(PagesPaths.ADMIN_LOGIN_PATH))) {
            req.getRequestDispatcher(PagesPaths.ADMIN_LOGIN_PATH).forward(request, response);
            return;
        }

        // in case of not logged user and try to go on any user page - forwarding to user login path
        if ((req.getRequestURI().startsWith(PagesPaths.USER))
                && (session.getAttribute(Attributes.USER_ID) == null)
                && (!req.getRequestURI().startsWith(PagesPaths.USER_LOGIN_PATH))
                && (!req.getRequestURI().startsWith(PagesPaths.USER_REGISTER_PATH))) {
            req.getRequestDispatcher(PagesPaths.USER_LOGIN_PATH).forward(request, response);
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
