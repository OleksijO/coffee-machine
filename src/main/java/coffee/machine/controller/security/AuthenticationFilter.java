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
public class AuthenticationFilter implements Filter {
    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class);
    private static final String ACCESS_DENIED_LOG_MESSAGE_FORMAT =
            "Access denied. Requested URI='%s', userId='%s', adminId='%s'";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = ((HttpServletRequest) request);
        HttpSession session = req.getSession();
        String uri = req.getRequestURI();
        Object adminId = session.getAttribute(Attributes.ADMIN_ID);
        Object userId = session.getAttribute(Attributes.USER_ID);

        if (((userId == null) && (uri.startsWith(PagesPaths.USER)) && (!uri.startsWith(PagesPaths.USER_REGISTER_PATH)))
                || ((uri.startsWith(PagesPaths.ADMIN) && (adminId == null)))) {
            req.getRequestDispatcher(PagesPaths.LOGIN_PATH).forward(request, response);
            logger.info(String.format(ACCESS_DENIED_LOG_MESSAGE_FORMAT, uri, userId, adminId));
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
