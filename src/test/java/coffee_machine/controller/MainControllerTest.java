package coffee_machine.controller;

import coffee_machine.exception.ApplicationException;
import org.junit.Assert;
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
import java.util.List;

import static coffee_machine.view.PagesPaths.HOME_PATH;
import static coffee_machine.view.PagesPaths.REDIRECTED;
import static org.mockito.Mockito.*;

/**
 * Created by oleksij.onysymchuk@gmail on 29.11.2016.
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
        when(request.getRequestDispatcher(dispatcherArgCaptor.capture())).thenReturn(requestDispatcher);

    }

    @Test
    public void processRequest() throws Exception {
        when(command.execute(request, response)).thenReturn(REDIRECTED);
        controller.processRequest(command, request, response);
        verify(response, times(0)).sendRedirect(any());
        verify(requestDispatcher, times(0)).forward(request, response);
    }

    @Test
    public void processRequestNoSuchPage() throws Exception {
        controller.processRequest(null, request, response);
        verify(response, times(1)).sendRedirect(HOME_PATH);
        verify(requestDispatcher, times(0)).forward(request, response);
    }

    @Test
    public void processRequestRuntimeException() throws Exception {
        when(command.execute(request, response)).thenThrow(new RuntimeException("messageKey"));
        controller.processRequest(command, request, response);
        verify(request, times(1)).setAttribute(any(), any());
        verify(response, times(0)).sendRedirect(any());
        verify(requestDispatcher, times(0)).forward(request, response);
    }

    @Test
    public void processRequestApplicationException() throws Exception {
        when(command.execute(request, response)).thenThrow(new ApplicationException("error.unknown"));
        controller.processRequest(command, request, response);
        verify(request, times(2)).setAttribute(any(), any());
        verify(response, times(0)).sendRedirect(any());
        verify(requestDispatcher, times(0)).forward(request, response);
    }

    @Test
    public void doGet() throws Exception {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("get");
        when(commandHolder.get("path")).thenReturn(command);
        when(command.execute(request, response)).thenReturn("pageGet");
        controller.doGet(request, response);
        verify(commandHolder, times(1)).get("path");
        verify(commandHolder, times(0)).post("path");
        verify(command, times(1)).execute(request, response);
        List<String> args = dispatcherArgCaptor.getAllValues();
        Assert.assertEquals("pageGet", args.get(args.size() - 1));

    }

    @Test
    public void doPost() throws Exception {
        when(request.getRequestURI()).thenReturn("path");
        when(request.getMethod()).thenReturn("get");
        when(commandHolder.post("path")).thenReturn(command);
        when(command.execute(request, response)).thenReturn("pagePost");
        controller.doPost(request, response);
        verify(commandHolder, times(0)).get("path");
        verify(commandHolder, times(1)).post("path");
        verify(command, times(1)).execute(request, response);
        List<String> args = dispatcherArgCaptor.getAllValues();
        Assert.assertEquals("pagePost", args.get(args.size() - 1));
    }

}