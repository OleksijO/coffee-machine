package coffee.machine.controller;

import coffee.machine.controller.command.UnsupportedPathCommand;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static coffee.machine.view.config.Paths.HOME_PATH;
import static coffee.machine.view.config.Paths.REDIRECTED;
import static org.mockito.Mockito.*;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class FrontControllerTest {
    private static final String DEPLOY_PATH = "/deploy_path";

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

    private FrontController controller;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        controller = new FrontController();
        controller.setCommandHolder(commandHolder);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(1);
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);
        when(request.getContextPath()).thenReturn(DEPLOY_PATH);

    }

    @Test
    public void testDoGetDoNothingIfCommandReturnedRedirected() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("get");
        when(command.execute(request, response)).thenReturn(REDIRECTED);
        when(commandHolder.findCommand(any())).thenReturn(command);
        controller.doGet(request, response);
        verify(response, times(0)).sendRedirect(any());
        verify(requestDispatcher, times(0)).forward(request, response);
    }

    @Test
    public void testDoPostDoNothingIfCommandReturnedRedirected() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("get");
        when(command.execute(request, response)).thenReturn(REDIRECTED);
        when(commandHolder.findCommand(any())).thenReturn(command);
        controller.doPost(request, response);
        verify(response, times(0)).sendRedirect(any());
        verify(requestDispatcher, times(0)).forward(request, response);
    }

    @Test
    public void testDoGetRedirectsToHomePathIfUriNotSupported() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("get");
        when(commandHolder.findCommand(any())).thenReturn(new UnsupportedPathCommand());
        controller.doGet(request, response);
        verify(response, times(1)).sendRedirect(DEPLOY_PATH+HOME_PATH);
        verify(requestDispatcher, times(0)).forward(request, response);
    }

    @Test
    public void testDoPostRedirectsToHomePathIfUriNotSupported() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("post");
        when(commandHolder.findCommand(any())).thenReturn(new UnsupportedPathCommand());
        controller.doPost(request, response);
        verify(response, times(1)).sendRedirect(DEPLOY_PATH+HOME_PATH);
        verify(requestDispatcher, times(0)).forward(request, response);
    }

    @Test
    public void testDoGetForwardsToReturnedByCommandPath() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("get");
        when(commandHolder.findCommand("GET:path")).thenReturn(command);
        when(command.execute(request, response)).thenReturn("pagePost");
        controller.doGet(request, response);
        verify(request).getRequestDispatcher("pagePost");
    }

    @Test
    public void testDoPostForwardsToReturnedByCommandPath()throws IOException, ServletException  {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("post");
        when(commandHolder.findCommand("POST:path")).thenReturn(command);
        when(command.execute(request, response)).thenReturn("pagePost");
        controller.doPost(request, response);
        verify(request).getRequestDispatcher("pagePost");
    }

    @Test
    public void testDoGetRetrievesGetCommandsFromHolderIfRequestMethodIsGet() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("get");
        when(commandHolder.findCommand("GET:path")).thenReturn(command);
        when(command.execute(request, response)).thenReturn("pageGet");
        controller.doGet(request, response);
        verify(commandHolder, times(1)).findCommand("GET:path");
        verify(commandHolder, times(0)).findCommand("POST:path");
    }

    @Test
    public void testDoPostRetrievesPostCommandsFromHolderIfRequestMethodIsPost() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("post");
        when(commandHolder.findCommand("POST:path")).thenReturn(command);
        when(command.execute(request, response)).thenReturn("pagePost");
        controller.doPost(request, response);
        verify(commandHolder, times(0)).findCommand("GET:path");
        verify(commandHolder, times(1)).findCommand("POST:path");
    }

}