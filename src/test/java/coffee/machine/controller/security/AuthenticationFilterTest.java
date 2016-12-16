package coffee.machine.controller.security;

import coffee.machine.view.Attributes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static coffee.machine.view.PagesPaths.*;
import static org.mockito.Mockito.*;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class AuthenticationFilterTest {
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

    private Filter filter = new AuthenticationFilter();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testDoFilterNotLoggedRootPaths() throws Exception {
        performTest("", null, null, "", 0);
        performTest("/", null, null, "/", 0);
        performTest("/", 1, null, "/", 0);
    }

    @Test
    public void testDoFilterNotLoggedHomePath() throws Exception {
        performTest(HOME_PATH, null, null, HOME_PATH, 0);
    }

    @Test
    public void testDoFilterNotLoggedAnyUnRestrictedPath() throws Exception {
        performTest("/kdfvblkjsdfbjkv", null, null, HOME_PATH, 0);
    }


    @Test
    public void testDoFilterNotLoggedLoginPath() throws Exception {
        performTest("/login", null, null, "/login", 0);
    }

    @Test
    public void testDoFilterNotLoggedRegisterPath() throws Exception {
        performTest("/user/register", null, null, "/user/register", 0);
    }

    @Test
    public void testDoFilterUserLoggedInNoForward() throws Exception {
        performTest(USER_ORDER_HISTORY_PATH, null, 1, USER_ORDER_HISTORY_PATH, 0);
        performTest("/user/any", null, 1, "/user/any", 0);
    }

    @Test
    public void testDoFilterAdminLoggedInNoForward() throws Exception {
        performTest(ADMIN_HOME_PATH, 1, null, ADMIN_HOME_PATH, 0);
        performTest("/admin/any", 1, null, "/admin/any", 0);
    }

    @Test
    public void testDoFilterNotLoggedInTryToGetUserPages() throws Exception {
        performTest("/user/any", null, null, LOGIN_PATH, 1);
    }

    @Test
    public void testDoFilterNotLoggedInTryToGetAdminPages() throws Exception {
        performTest("/admin/any", null, null, LOGIN_PATH, 1);
    }

    @Test
    public void testDoFilterUserLoggedInTryToGetAdminPages() throws Exception {
        performTest(ADMIN_HOME_PATH, null, 1, LOGIN_PATH, 1);
    }

    @Test
    public void testDoFilterAdminLoggedInTryToGetUserPages() throws Exception {
        performTest(USER_ORDER_HISTORY_PATH, 1, null, LOGIN_PATH, 1);
    }

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
        when(session.getAttribute(Attributes.ADMIN_ID)).thenReturn(adminId);
        when(session.getAttribute(Attributes.USER_ID)).thenReturn(userId);
    }

}