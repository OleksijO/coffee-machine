package coffee.machine.controller.security;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static coffee.machine.view.Attributes.ADMIN_ID;
import static coffee.machine.view.Attributes.USER_ID;
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
    public void testDoFilterPassesNonLoggedInUsersToRootPath() throws Exception {
        performTest("", null, null, "", 0);
        performTest("/", null, null, "/", 0);
        performTest("/", 1, null, "/", 0);
    }

    /**
     * Performs test, configured by params
     *
     * @param path                          request's URI (where request is going to)
     * @param adminId                       Admin's id in attribute in session (admin with which id is logged in)
     * @param userId                        User's id in attribute in session (user with which id is logged in)
     * @param expectedPath                  URI, where filter should route request with specified id's and path
     * @param expectedDispatcherCallTimes   Number of times, when forwarding should be performed
     * @throws ServletException             In case of container problems
     * @throws IOException                  In case of container problems
     */
    private void performTest(String path, Integer adminId, Integer userId, String expectedPath,
                             int expectedDispatcherCallTimes) throws ServletException, IOException {
        setUpMocks(path, adminId, userId);
        filter.doFilter(request, response, chain);
        if (expectedDispatcherCallTimes > 0) {
            verify(request, times(expectedDispatcherCallTimes)).getRequestDispatcher(expectedPath);
        } else {
            verify(request, never()).getRequestDispatcher(expectedPath);
        }
    }

    private void setUpMocks(String uri, Integer adminId, Integer userId) {
        when(request.getRequestURI()).thenReturn(uri);
        when(session.getAttribute(ADMIN_ID)).thenReturn(adminId);
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
        performTest(USER_ORDER_HISTORY_PATH, null, 1, USER_ORDER_HISTORY_PATH, 0);
        performTest("/user/any", null, 1, "/user/any", 0);
    }

    @Test
    public void testDoFilterPassesAuthorizedAdminToAdminPages() throws Exception {
        performTest(ADMIN_HOME_PATH, 1, null, ADMIN_HOME_PATH, 0);
        performTest("/admin/any", 1, null, "/admin/any", 0);
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
        performTest(ADMIN_HOME_PATH, null, 1, LOGIN_PATH, 1);
    }

    @Test
    public void testDoFilterForwardsAuthorizedAdminFromUserPagesToLoginPath() throws Exception {
        performTest(USER_ORDER_HISTORY_PATH, 1, null, LOGIN_PATH, 1);
    }
}