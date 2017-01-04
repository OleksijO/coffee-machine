package coffee.machine.controller.security;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static coffee.machine.view.Attributes.ADMIN_ID;
import static coffee.machine.view.Attributes.USER_ID;
import static coffee.machine.view.PagesPaths.*;

/**
 * This class represents security filter to restrict access of unauthorized users and admins to pages,
 * where they should not be. If filter detects mentioned access it redirects to login page for that access area.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AuthFilter implements Filter {
    private static final Logger logger = Logger.getLogger(AuthFilter.class);
    private static final String ACCESS_DENIED_LOG_MESSAGE_FORMAT =
            "Access denied. Requested URI='%s', userId='%s', adminId='%s'";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = ((HttpServletRequest) request);
        HttpSession session = req.getSession();
        String uri = req.getRequestURI();
        Object adminId = session.getAttribute(ADMIN_ID);
        Object userId = session.getAttribute(USER_ID);

        if (isAuthorized(uri, adminId, userId)) {
            req.getRequestDispatcher(LOGIN_PATH).forward(request, response);
            logger.info(String.format(ACCESS_DENIED_LOG_MESSAGE_FORMAT, uri, userId, adminId));
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isAuthorized(String uri, Object adminId, Object userId) {
        return isUserAuthorized(uri, userId) || isAdminAuthorized(uri, adminId);
    }

    private boolean isUserAuthorized(String uri, Object userId) {
        return (userId == null)
                && (uri.startsWith(USER))
                && (!uri.startsWith(USER_REGISTER_PATH));
    }

    private boolean isAdminAuthorized(String uri, Object adminId) {
        return uri.startsWith(ADMIN)
                && (adminId == null);
    }


    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
