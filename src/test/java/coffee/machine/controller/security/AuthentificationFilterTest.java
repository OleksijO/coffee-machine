package coffee.machine.controller.security;

import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
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
        performTest(PagesPaths.HOME_PATH, null, null, PagesPaths.HOME_PATH, 0);
        performTest("/home/", null, null, PagesPaths.HOME_PATH, 0);
        performTest("/kdfvblkjsdfbjkv", null, null, PagesPaths.HOME_PATH, 0);

        performTest("/user/login", null, null, "/user/login", 0);
        performTest("/admin/login", null, null, "/admin/login", 0);

        performTest("/user/any", null, null, PagesPaths.USER_LOGIN_PATH, 1);
        performTest(PagesPaths.USER_HISTORY_PATH, null, null, PagesPaths.USER_LOGIN_PATH, 1);
        performTest(PagesPaths.USER_HISTORY_PATH, 1, null, PagesPaths.USER_LOGIN_PATH, 1);
        performTest(PagesPaths.USER_HISTORY_PATH, null, 1, PagesPaths.USER_HISTORY_PATH, 0);
        performTest("/user/any", null, 1, "/user/any", 0);

        performTest("/admin/any", null, null, PagesPaths.ADMIN_LOGIN_PATH, 1);
        performTest(PagesPaths.ADMIN_HOME_PATH, null, null, PagesPaths.ADMIN_LOGIN_PATH, 1);
        performTest(PagesPaths.ADMIN_HOME_PATH, null, 1, PagesPaths.ADMIN_LOGIN_PATH, 1);
        performTest(PagesPaths.ADMIN_HOME_PATH, 1, null, PagesPaths.ADMIN_HOME_PATH, 0);
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
        when(session.getAttribute(Attributes.ADMIN_ID)).thenReturn(adminId);
        when(session.getAttribute(Attributes.USER_ID)).thenReturn(userId);
    }

}