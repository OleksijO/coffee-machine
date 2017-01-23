package coffee.machine.controller.command.user.purchase;

import coffee.machine.controller.Command;
import coffee.machine.controller.command.helper.LoggingHelper;
import coffee.machine.model.entity.Order;
import coffee.machine.service.OrderPreparationService;
import coffee.machine.service.exception.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

import static coffee.machine.controller.command.user.purchase.PurchaseDrinksTestData.*;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_PREPARE_ORDER_NOTHING_TO_BUY;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.ERROR_PREPARE_ORDER_PRODUCT_NO_LONGER_AVAILABLE;
import static coffee.machine.view.config.Attributes.*;
import static coffee.machine.view.config.Pages.USER_PURCHASE_PAGE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author oleksij.onysymchuk@gmail.com
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
    public void testExecuteReturnsCorrectPageIfReceiptIsEmpty() throws IOException {
        setupRequestParams(EMPTY_DATA);
        when(orderService.prepareOrder(any())).thenReturn(new Order.Builder().build());
        assertEquals(
                USER_PURCHASE_PAGE,
                command.execute(request, response));
    }

    @Test
    public void testExecuteReturnsCorrectPageIfReceiptHasNegativeQuantities() throws IOException {
        setupRequestParams(PURCHASE_DATA_WITH_NEGATIVE_QUANTITIES);
        when(orderService.prepareOrder(any())).thenReturn(new Order.Builder().build());
        assertEquals(
                USER_PURCHASE_PAGE,
                command.execute(request, response));
    }

    @Test
    public void testExecuteReturnsCorrectPageIfErrorOccurred() throws IOException {
        setupRequestParams(PURCHASE_CORRECT_DATA);
        when(session.getAttribute(USER_ID)).thenReturn(1);
        doThrow(new ServiceException()
                .addMessageKey(ERROR_PREPARE_ORDER_NOTHING_TO_BUY)
                .addLogMessage(""))
                .when(orderService).prepareOrder(any());
        assertEquals(
                USER_PURCHASE_PAGE,
                command.execute(request, response));
    }

    @Test
    public void testExecuteDoesNotCallServiceIfReceiptIsEmpty() throws IOException  {
        setupRequestParams(EMPTY_DATA);
        when(session.getAttribute(USER_ID)).thenReturn(1);
        command.execute(request, response);
        verify(orderService, never()).prepareOrder(EMPTY_DATA.order);
    }

    @Test
    public void testExecuteDoesNotCallServiceIfReceiptHasNegativeQuantities() throws IOException {
        setupRequestParams(PURCHASE_DATA_WITH_NEGATIVE_QUANTITIES);
        when(session.getAttribute(USER_ID)).thenReturn(1);
        command.execute(request, response);
        verify(orderService, never()).prepareOrder(EMPTY_DATA.order);
    }

    @Test
    public void testExecuteCallsServiceWithCorrectArgsIfReceiptIsFilled() throws IOException {
        setupRequestParams(PURCHASE_CORRECT_DATA);
        when(request.getMethod()).thenReturn("get");
        when(session.getAttribute(USER_ID)).thenReturn(1);
        when(orderService.prepareOrder(any())).thenReturn(new Order.Builder().build());
        command.execute(request, response);
        verify(orderService).prepareOrder(PURCHASE_CORRECT_DATA.order);
    }

    @Test
    public void testExecutePlacesErrorMessageToRequestIfErrorOccurred() throws IOException {
        setupRequestParams(PURCHASE_CORRECT_DATA);
        when(session.getAttribute(USER_ID)).thenReturn(1);
        doThrow(new ServiceException()
                .addMessageKey(ERROR_PREPARE_ORDER_PRODUCT_NO_LONGER_AVAILABLE)
                .addLogMessage(""))
                .when(orderService).prepareOrder(any());
        command.execute(request, response);
        verify(request).setAttribute(eq(ERROR_MESSAGE), any());
    }

    @Test
    public void testExecutePlacesUsualMessageToRequestIfNoError() throws IOException {
        setupRequestParams(PURCHASE_CORRECT_DATA);
        when(request.getMethod()).thenReturn("get");
        when(session.getAttribute(USER_ID)).thenReturn(1);
        when(orderService.prepareOrder(any())).thenReturn(new Order.Builder().build());
        command.execute(request, response);
        verify(request).setAttribute(eq(USUAL_MESSAGE), any());
    }

    private void setupRequestParams(PurchaseDrinksTestData testData) {

        when(request.getParameterNames()).thenReturn(prepareParameterNames(testData));

        when(request.getParameter(anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return testData.requestParams.get(args[0]);
            }
        });
    }

    private Enumeration<String> prepareParameterNames(PurchaseDrinksTestData testData) {
        return new Enumeration<String>() {
            Iterator<String> iterator = testData.requestParams.keySet().iterator();

            @Override
            public boolean hasMoreElements() {
                boolean hasMoreElements = iterator.hasNext();
                if (!hasMoreElements){
                    iterator = testData.requestParams.keySet().iterator();
                    return false;
                }
                return true;
            }

            @Override
            public String nextElement() {
                return iterator.next();
            }
        };
    }

}