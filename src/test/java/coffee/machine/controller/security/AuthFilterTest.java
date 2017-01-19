package coffee.machine.controller.security;

import coffee.machine.model.entity.user.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static coffee.machine.view.Attributes.USER_ID;
import static coffee.machine.view.Attributes.USER_ROLE;
import static coffee.machine.view.PagesPaths.*;
import static org.mockito.Mockito.*;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class AuthFilterTest {
    @Mock
    HttpServletRequest request;
    @Mock
    ServletResponse response;
    @Mock
    FilterChain chain;
    @Mock
    HttpSession session;
    @Mock
    RequestDispatcher requestDispatcher;

    private Filter filter = new AuthFilter();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testDoFilterPassesNonLoggedInUsersToRootApplicationPath() throws Exception {
        performTest("", null, null, "", 0);
        performTest("/", null, null, "/", 0);
    }

    @Test
    public void testDoFilterPassesLoggedInUsersToRootApplicationPath() throws Exception {
        performTest("/", UserRole.USER, 1, "/", 0);
        performTest("/", UserRole.ADMIN, 1, "/", 0);
    }

    /**
     * Performs test, configured by params
     *
     * @param path                          request's URI (where request is going to)
     * @param role                          User's role in attribute in session
     * @param userId                        User's id in attribute in session (user with which id is logged in)
     * @param expectedPath                  URI, where filter should route request with specified id's and path
     * @param expectedDispatcherCallTimes   Number of times, when forwarding should be performed
     * @throws ServletException             In case of container problems
     * @throws IOException                  In case of container problems
     */
    private void performTest(String path, UserRole role, Integer userId, String expectedPath,
                             int expectedDispatcherCallTimes) throws ServletException, IOException {
        setUpMocks(path, role, userId);
        filter.doFilter(request, response, chain);
        if (expectedDispatcherCallTimes > 0) {
            verify(request, times(expectedDispatcherCallTimes)).getRequestDispatcher(expectedPath);
        } else {
            verify(request, never()).getRequestDispatcher(expectedPath);
        }
    }

    private void setUpMocks(String uri, UserRole role, Integer userId) {
        when(request.getRequestURI()).thenReturn(uri);
        when(session.getAttribute(USER_ROLE)).thenReturn(role);
        when(session.getAttribute(USER_ID)).thenReturn(userId);
    }

    @Test
    public void testDoFilterPassesNonAuthorizedToHomePath() throws Exception {
        performTest(HOME_PATH, null, null, HOME_PATH, 0);
    }

    @Test
    public void testDoFilterForwardsNonAuthorizedToHomePathFromAnyUnRestrictedPath() throws Exception {
        performTest("/kdfvblkjsdfbjkv", null, null, HOME_PATH, 0);
    }


    @Test
    public void testDoFilterPassesNonAuthorizedToLoginPath() throws Exception {
        performTest("/login", null, null, "/login", 0);
    }

    @Test
    public void testDoFilterPassesNonAuthorizedToRegisterPath() throws Exception {
        performTest("/user/register", null, null, "/user/register", 0);
    }

    @Test
    public void testDoFilterPassesAuthorizedUserToUserPages() throws Exception {
        performTest(USER_ORDER_HISTORY_PATH, UserRole.USER, 1, USER_ORDER_HISTORY_PATH, 0);
        performTest("/user/any", UserRole.USER, 1, "/user/any", 0);
    }

    @Test
    public void testDoFilterPassesAuthorizedAdminToAdminPages() throws Exception {
        performTest(ADMIN_HOME_PATH, UserRole.ADMIN, 1, ADMIN_HOME_PATH, 0);
        performTest("/admin/any", UserRole.ADMIN, 1, "/admin/any", 0);
    }

    @Test
    public void testDoFilterForwardsAllNonAuthorizedFromUserPagesToLoginPath() throws Exception {
        performTest("/user/any", null, null, LOGIN_PATH, 1);
    }

    @Test
    public void testDoFilterForwardsAllNonAuthorizedFromAdminPagesToLoginPath() throws Exception {
        performTest("/admin/any", null, null, LOGIN_PATH, 1);
    }

    @Test
    public void testDoFilterForwardsAuthorizedUserFromAdminPagesToLoginPath() throws Exception {
        performTest(ADMIN_HOME_PATH, UserRole.USER, 1, LOGIN_PATH, 1);
    }

    @Test
    public void testDoFilterForwardsAuthorizedAdminFromUserPagesToLoginPath() throws Exception {
        performTest(USER_ORDER_HISTORY_PATH, UserRole.USER, null, LOGIN_PATH, 1);
    }
}