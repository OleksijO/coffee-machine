package coffee.machine.controller.command.admin.refill;

import coffee.machine.controller.Command;
import coffee.machine.controller.command.helper.LoggingHelper;
import coffee.machine.service.CoffeeMachineRefillService;
import coffee.machine.service.exception.ServiceException;
import coffee.machine.view.PagesPaths;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Iterator;

import static coffee.machine.controller.command.admin.refill.ItemRefillTestData.EMPTY_DATA;
import static coffee.machine.controller.command.admin.refill.ItemRefillTestData.REFILL_FULL_DATA;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.ADMIN_REFILL_NOTHING_TO_ADD;
import static coffee.machine.view.Attributes.ADMIN_ID;
import static coffee.machine.view.Attributes.ERROR_MESSAGE;
import static coffee.machine.view.Attributes.USUAL_MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by oleksij.onysymchuk@gmail on 10.01.2017.
 */
public class AdminRefillSubmitCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private LoggingHelper loggingHelper;
    @Mock
    private CoffeeMachineRefillService refillService;

    private Command command = new AdminRefillSubmitCommand();


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ((AdminRefillSubmitCommand) command).setCoffeeMachineRefillService(refillService);
        when(request.getSession()).thenReturn(session);
        when(request.getMethod()).thenReturn("post");
    }

    @Test
    public void testExecuteNormallyReturnPage() throws Exception {
        setupRequestParams(EMPTY_DATA);
        assertEquals(
                PagesPaths.ADMIN_REFILL_PAGE,
                command.execute(request, response));
    }

    @Test
    public void testExecuteAfterErrorReturnPage() throws Exception {
        setupRequestParams(EMPTY_DATA);
        doThrow(new ServiceException()
                .addMessageKey(ADMIN_REFILL_NOTHING_TO_ADD)
                .addLogMessage(""))
                .when(refillService).refill(any());
        assertEquals(
                PagesPaths.ADMIN_REFILL_PAGE,
                command.execute(request, response));
    }

    @Test
    public void testExecuteEmptyForm() throws Exception {
        setupRequestParams(EMPTY_DATA);
        command.execute(request, response);
        verify(refillService).refill(EMPTY_DATA.itemReceipt);
    }

    @Test
    public void testExecuteFilledForm() throws Exception {
        setupRequestParams(REFILL_FULL_DATA);
        when(request.getMethod()).thenReturn("get");
        when(session.getAttribute(ADMIN_ID)).thenReturn(1);
        command.execute(request, response);
        verify(refillService).refill(REFILL_FULL_DATA.itemReceipt);
    }

    @Test
    public void testExecuteAfterErrorPlacedErrorMessageToRequest() throws Exception {
        setupRequestParams(EMPTY_DATA);
        doThrow(new ServiceException()
                .addMessageKey(ADMIN_REFILL_NOTHING_TO_ADD)
                .addLogMessage(""))
                .when(refillService).refill(any());
        command.execute(request, response);
        verify(request).setAttribute(eq(ERROR_MESSAGE), any());
    }

    @Test
    public void testExecuteNormallyPlacedUsualMessageToRequest() throws Exception {
        setupRequestParams(REFILL_FULL_DATA);
        when(request.getMethod()).thenReturn("get");
        when(session.getAttribute(ADMIN_ID)).thenReturn(1);
        command.execute(request, response);
        verify(request).setAttribute(eq(USUAL_MESSAGE), any());
    }

    private void setupRequestParams(ItemRefillTestData testData) {

        when(request.getParameterNames()).thenReturn(getEnumeration(testData));

        when(request.getParameter(anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return testData.requestParams.get(args[0]);
            }
        });
    }

    private Enumeration<String> getEnumeration(final ItemRefillTestData testData) {
        return new Enumeration<String>() {
            Iterator<String> iterator = testData.requestParams.keySet().iterator();

            @Override
            public boolean hasMoreElements() {
                boolean hasMoreElements = iterator.hasNext();
                if (!hasMoreElements) {
                    iterator = testData.requestParams.keySet().iterator();
                    return false;
                }
                return hasMoreElements;
            }

            @Override
            public String nextElement() {
                return iterator.next();
            }
        };
    }

}