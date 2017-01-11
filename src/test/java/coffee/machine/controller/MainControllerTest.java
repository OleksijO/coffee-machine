package coffee.machine.controller;

import coffee.machine.controller.command.UnsupportedPathCommand;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static coffee.machine.view.PagesPaths.HOME_PATH;
import static coffee.machine.view.PagesPaths.REDIRECTED;
import static org.mockito.Mockito.*;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class MainControllerTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    HttpSession session;
    @Mock
    RequestDispatcher requestDispatcher;
    @Mock
    CommandHolder commandHolder;
    @Mock
    Command command;
    @Captor
    ArgumentCaptor<String> dispatcherArgCaptor;

    private MainController controller;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        controller = new MainController();
        controller.commandHolder = commandHolder;
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(1);
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

    }

    @Test
    public void testProcessRequestDoNothingIfCommandReturnedRedirected() throws Exception {
        when(command.execute(request, response)).thenReturn(REDIRECTED);
        controller.processRequest(command, request, response);
        verify(response, times(0)).sendRedirect(any());
        verify(requestDispatcher, times(0)).forward(request, response);
    }

    @Test
    public void testProcessRequestRedirectsToHomePathIfUriNotSupported() throws Exception {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("get");
        when(commandHolder.get("path")).thenReturn(new UnsupportedPathCommand());
        controller.doGet(request, response);
        verify(response, times(1)).sendRedirect(HOME_PATH);
        verify(requestDispatcher, times(0)).forward(request, response);
    }

    @Test
    public void testProcessRequestForwardsToReturnedByCommandPath() throws Exception {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("get");
        when(commandHolder.post("path")).thenReturn(command);
        when(command.execute(request, response)).thenReturn("pagePost");
        controller.doPost(request, response);
        verify(request).getRequestDispatcher("pagePost");
    }

    @Test
    public void testDoGetRetrievesGetCommandsFromHolderIfRequestMethodIsGet() throws Exception {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("get");
        when(commandHolder.post("path")).thenReturn(command);
        when(command.execute(request, response)).thenReturn("pageGet");
        controller.doPost(request, response);
        verify(commandHolder, times(0)).get("path");
        verify(commandHolder, times(1)).post("path");
    }

    @Test
    public void testDoPostRetrievesPostCommandsFromHolderIfRequestMethodIsPost() throws Exception {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("get");
        when(commandHolder.post("path")).thenReturn(command);
        when(command.execute(request, response)).thenReturn("pagePost");
        controller.doPost(request, response);
        verify(commandHolder, times(0)).get("path");
        verify(commandHolder, times(1)).post("path");
    }

}