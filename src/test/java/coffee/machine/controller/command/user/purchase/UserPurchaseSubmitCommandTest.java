package coffee.machine.controller.command.user.purchase;

import coffee.machine.controller.Command;
import coffee.machine.controller.command.helper.LoggingHelper;
import coffee.machine.model.entity.Order;
import coffee.machine.service.OrderPreparationService;
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

import static coffee.machine.controller.command.user.purchase.PurchaseDrinksTestData.EMPTY_DATA;
import static coffee.machine.controller.command.user.purchase.PurchaseDrinksTestData.PURCHASE_FULL_DATA;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_PREPARE_ORDER_NOTHING_TO_BUY;
import static coffee.machine.view.Attributes.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by oleksij.onysymchuk@gmail on 10.01.2017.
 */
public class UserPurchaseSubmitCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private LoggingHelper loggingHelper;
    @Mock
    private OrderPreparationService orderService;

    private Command command = new UserPurchaseSubmitCommand();


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ((UserPurchaseSubmitCommand) command).setCoffeeMachineOrderService(orderService);
        when(request.getSession()).thenReturn(session);
        when(request.getMethod()).thenReturn("post");
    }

    @Test
    public void testExecuteReturnsCorrectPageIfReceiptIsEmpty() throws Exception {
        setupRequestParams(EMPTY_DATA);
        when(orderService.prepareOrder(any())).thenReturn(new Order.Builder().build());
        assertEquals(
                PagesPaths.USER_PURCHASE_PAGE,
                command.execute(request, response));
    }

    @Test
    public void testExecuteReturnsCorrectPageIfErrorOccurred() throws Exception {
        setupRequestParams(EMPTY_DATA);
        when(session.getAttribute(USER_ID)).thenReturn(1);
        doThrow(new ServiceException()
                .addMessageKey(ERROR_PREPARE_ORDER_NOTHING_TO_BUY)
                .addLogMessage(""))
                .when(orderService).prepareOrder(any());
        assertEquals(
                PagesPaths.USER_PURCHASE_PAGE,
                command.execute(request, response));
    }

    @Test
    public void testExecuteCallsServiceWithCorrectArgsIfReceiptIsEmpty() throws Exception {
        setupRequestParams(EMPTY_DATA);
        when(session.getAttribute(USER_ID)).thenReturn(1);
        doThrow(new ServiceException()
                .addMessageKey(ERROR_PREPARE_ORDER_NOTHING_TO_BUY)
                .addLogMessage(""))
                .when(orderService).prepareOrder(any());
        command.execute(request, response);
        verify(orderService).prepareOrder(EMPTY_DATA.order);
    }

    @Test
    public void testExecuteCallsServiceWithCorrectArgsIfReceiptIsFilled() throws Exception {
        setupRequestParams(PURCHASE_FULL_DATA);
        when(request.getMethod()).thenReturn("get");
        when(session.getAttribute(USER_ID)).thenReturn(1);
        when(orderService.prepareOrder(any())).thenReturn(new Order.Builder().build());
        command.execute(request, response);
        verify(orderService).prepareOrder(PURCHASE_FULL_DATA.order);
    }

    @Test
    public void testExecutePlacesErrorMessageToRequestIfErrorOccurred() throws Exception {
        setupRequestParams(EMPTY_DATA);
        when(session.getAttribute(USER_ID)).thenReturn(1);
        doThrow(new ServiceException()
                .addMessageKey(ERROR_PREPARE_ORDER_NOTHING_TO_BUY)
                .addLogMessage(""))
                .when(orderService).prepareOrder(any());
        command.execute(request, response);
        verify(request).setAttribute(eq(ERROR_MESSAGE), any());
    }

    @Test
    public void testExecutePlacesUsualMessageToRequestIfNoError() throws Exception {
        setupRequestParams(PURCHASE_FULL_DATA);
        when(request.getMethod()).thenReturn("get");
        when(session.getAttribute(USER_ID)).thenReturn(1);
        when(orderService.prepareOrder(any())).thenReturn(new Order.Builder().build());
        command.execute(request, response);
        verify(request).setAttribute(eq(USUAL_MESSAGE), any());
    }

    private void setupRequestParams(PurchaseDrinksTestData testData) {

        when(request.getParameterNames()).thenReturn(getEnumeration(testData));

        when(request.getParameter(anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return testData.requestParams.get(args[0]);
            }
        });
    }

    private Enumeration<String> getEnumeration(PurchaseDrinksTestData testData) {
        return new Enumeration<String>() {
            Iterator<String> iterator = testData.requestParams.keySet().iterator();

            @Override
            public boolean hasMoreElements() {
                boolean hasMoreElements = iterator.hasNext();
                if (!hasMoreElements){
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