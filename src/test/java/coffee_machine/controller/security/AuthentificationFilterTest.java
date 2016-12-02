package coffee_machine.controller.security;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static coffee_machine.view.Attributes.ADMIN_ID;
import static coffee_machine.view.Attributes.USER_ID;
import static coffee_machine.view.PagesPaths.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author oleksij.onysymchuk@gmail.com 29.11.2016.
 */
public class AuthentificationFilterTest {
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
    @Captor
    ArgumentCaptor<String> dispatcherArgCaptor;
    int dispatcherCallCounter = 0;

    private Filter filter = new AuthentificationFilter();

    private static final String FORMAT = "Input data: path='%s', adminId=%d, userId=%d. Expected path = '%s'.";


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(request.getRequestDispatcher(dispatcherArgCaptor.capture())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testDoFilter() throws Exception {
        performTest("", null, null, "", 0);
        performTest("/", null, null, "/", 0);
        performTest("/", 1, null, "/", 0);
        performTest("/", 2, 1, "/", 0);
        performTest(HOME_PATH, null, null, HOME_PATH, 0);
        performTest("/home/", null, null, HOME_PATH, 0);
        performTest("/kdfvblkjsdfbjkv", null, null, HOME_PATH, 0);

        performTest("/user/login", null, null, "/user/login", 0);
        performTest("/admin/login", null, null, "/admin/login", 0);

        performTest("/user/any", null, null, USER_LOGIN_PATH, 1);
        performTest(USER_HISTORY_PATH, null, null, USER_LOGIN_PATH, 1);
        performTest(USER_HISTORY_PATH, 1, null, USER_LOGIN_PATH, 1);
        performTest(USER_HISTORY_PATH, null, 1, USER_HISTORY_PATH, 0);
        performTest("/user/any", null, 1, "/user/any", 0);

        performTest("/admin/any", null, null, ADMIN_LOGIN_PATH, 1);
        performTest(ADMIN_HOME_PATH, null, null, ADMIN_LOGIN_PATH, 1);
        performTest(ADMIN_HOME_PATH, null, 1, ADMIN_LOGIN_PATH, 1);
        performTest(ADMIN_HOME_PATH, 1, null, ADMIN_HOME_PATH, 0);
        performTest("/admin/any", 1, null, "/admin/any", 0);
    }

    private void performTest(String path, Integer adminId, Integer userId, String expectedPath,
                             int expectedDispetcherCallTimes) throws ServletException, IOException {
        setUpMocks(path, adminId, userId);
        filter.doFilter(request, response, chain);
        dispatcherCallCounter+=expectedDispetcherCallTimes;
        verify(requestDispatcher, times(dispatcherCallCounter)).forward(any(), any());
        if (expectedDispetcherCallTimes > 0) {
            assertEquals(
                    String.format(FORMAT, path, adminId, userId, expectedPath),
                    expectedPath, dispatcherArgCaptor.getValue());
        }
    }


    private void setUpMocks(String uri, Integer adminId, Integer userId) {
        when(request.getRequestURI()).thenReturn(uri);
        when(session.getAttribute(ADMIN_ID)).thenReturn(adminId);
        when(session.getAttribute(USER_ID)).thenReturn(userId);
    }

}