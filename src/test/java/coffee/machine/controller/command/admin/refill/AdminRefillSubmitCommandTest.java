package coffee.machine.controller.command.admin.refill;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.Command;
import coffee.machine.controller.command.helper.LoggingHelper;
import coffee.machine.model.entity.Account;
import coffee.machine.service.AccountService;
import coffee.machine.service.AddonService;
import coffee.machine.service.DrinkService;
import coffee.machine.service.RefillService;
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
import java.util.Optional;

import static coffee.machine.controller.command.admin.refill.ProductRefillTestData.*;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ADMIN_REFILL_NOTHING_TO_ADD;
import static coffee.machine.view.config.Attributes.*;
import static coffee.machine.view.config.Pages.ADMIN_REFILL_PAGE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author oleksij.onysymchuk@gmail.com
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
    private RefillService refillService;
    @Mock
    private DrinkService drinkService;
    @Mock
    private AddonService addonService;
    @Mock
    private AccountService accountService;


    private Command command;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        command = new AdminRefillSubmitCommand(
                refillService,
                drinkService,
                addonService,
                accountService
        );
        when(request.getSession()).thenReturn(session);
        when(request.getMethod()).thenReturn("post");
        when(accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)).thenReturn(Optional.of(new Account()));
    }

    @Test
    public void testExecuteReturnsCorrectPageIfNoError() throws IOException {
        setupRequestParams(REFILL_CORRECT_DATA);
        assertEquals(
                ADMIN_REFILL_PAGE,
                command.execute(request, response));
    }

    @Test
    public void testExecuteReturnsCorrectPageIfErrorOccurred() throws IOException {
        setupRequestParams(REFILL_CORRECT_DATA);
        doThrow(new ServiceException()
                .addMessageKey(ADMIN_REFILL_NOTHING_TO_ADD)
                .addLogMessage(""))
                .when(refillService).refill(any());
        assertEquals(
                ADMIN_REFILL_PAGE,
                command.execute(request, response));
    }

    @Test
    public void testExecuteDoesNotCallServiceIfFormIsEmpty() throws IOException {
        setupRequestParams(EMPTY_DATA);
        command.execute(request, response);
        verify(refillService, never()).refill(any());
    }

    @Test
    public void testExecuteDoesNotCallServiceIfFormHasNegativeQuantities() throws IOException {
        setupRequestParams(REFILL_DATA_WITH_NEGATIVE_QUANTITIES);
        command.execute(request, response);
        verify(refillService, never()).refill(any());
    }

    @Test
    public void testExecuteCallsServiceWithCorrectArgsIfFormIsFilledWithData() throws IOException {
        setupRequestParams(REFILL_CORRECT_DATA);
        when(request.getMethod()).thenReturn("get");
        when(session.getAttribute(USER_ID)).thenReturn(1);
        command.execute(request, response);
        verify(refillService).refill(REFILL_CORRECT_DATA.productsReceipt);
    }

    @Test
    public void testExecutePlacesErrorMessageToRequestIfErrorOccurred() throws IOException {
        setupRequestParams(REFILL_CORRECT_DATA);
        doThrow(new ServiceException()
                .addMessageKey(ADMIN_REFILL_NOTHING_TO_ADD)
                .addLogMessage(""))
                .when(refillService).refill(any());
        command.execute(request, response);
        verify(request).setAttribute(eq(ERROR_MESSAGE), any());
    }

    @Test
    public void testExecutePlacesErrorMessageToRequestIfValidationErrorOccurred() throws IOException {
        setupRequestParams(REFILL_DATA_WITH_NEGATIVE_QUANTITIES);
        doThrow(new ServiceException()
                .addMessageKey(ADMIN_REFILL_NOTHING_TO_ADD)
                .addLogMessage(""))
                .when(refillService).refill(any());
        command.execute(request, response);
        verify(request).setAttribute(eq(VALIDATION_ERRORS), any());
    }

    @Test
    public void testExecutePlacesUsualMessageToRequestIfNoError() throws IOException {
        setupRequestParams(REFILL_CORRECT_DATA);
        when(request.getMethod()).thenReturn("get");
        when(session.getAttribute(USER_ID)).thenReturn(1);
        command.execute(request, response);
        verify(request).setAttribute(eq(USUAL_MESSAGE), any());
    }

    private void setupRequestParams(ProductRefillTestData testData) {

        when(request.getParameterNames()).thenReturn(prepareParameterNames(testData));

        when(request.getParameter(anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return testData.requestParams.get(args[0]);
            }
        });
    }

    private Enumeration<String> prepareParameterNames(final ProductRefillTestData testData) {
        return new Enumeration<String>() {
            Iterator<String> iterator = testData.requestParams.keySet().iterator();

            @Override
            public boolean hasMoreElements() {
                boolean hasMoreElements = iterator.hasNext();
                if (!hasMoreElements) {
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