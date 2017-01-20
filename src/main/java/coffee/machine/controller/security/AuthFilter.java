package coffee.machine.controller.security;

import coffee.machine.model.entity.user.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static coffee.machine.view.Attributes.USER_ID;
import static coffee.machine.view.Attributes.USER_ROLE;
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

    private Authorizer guestAuthorizer = (uri, userId) -> checkUriForGuest(uri);
    private Authorizer userAuthorizer = (uri, userId) -> (userId != null) && checkUriForUser(uri);
    private Authorizer adminAuthorizer = (uri, userId) -> (userId != null) && checkUriForAdmin(uri);

    private Map<UserRole, Authorizer> authorizeByRole = new HashMap<>() ;
    {
        authorizeByRole.put(UserRole.USER, userAuthorizer);
        authorizeByRole.put(UserRole.ADMIN, adminAuthorizer);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        String uri = req.getRequestURI();
        UserRole role = (UserRole) session.getAttribute(USER_ROLE);
        Object userId = session.getAttribute(USER_ID);

        if (!isAuthorized(uri, userId, role)) {
            req.getRequestDispatcher(LOGIN_PATH).forward(request, response);
            logger.info(String.format(ACCESS_DENIED_LOG_MESSAGE_FORMAT, uri, userId, role));
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isAuthorized(String uri, Object userId, UserRole role) {
        Authorizer authorizer = authorizeByRole.getOrDefault(role, guestAuthorizer);
        return authorizer.check(uri, userId);
    }

    @FunctionalInterface
    private interface Authorizer {
        boolean check(String uri, Object userId);
    }

    private boolean checkUriForGuest(String uri) {
        return (!uri.startsWith(ADMIN)) && (uri.startsWith(USER_REGISTER_PATH) || !uri.startsWith(USER));
    }

    private boolean checkUriForUser(String uri) {
        return !uri.startsWith(ADMIN);
    }

    private boolean checkUriForAdmin(String uri) {
        return !uri.startsWith(USER);
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
