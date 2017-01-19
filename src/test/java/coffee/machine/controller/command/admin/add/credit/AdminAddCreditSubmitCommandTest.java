package coffee.machine.controller.command.admin.add.credit;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.Command;
import coffee.machine.controller.command.helper.LoggingHelper;
import coffee.machine.model.entity.Account;
import coffee.machine.model.value.object.CreditsReceipt;
import coffee.machine.service.AccountService;
import coffee.machine.service.exception.ServiceException;
import coffee.machine.view.Attributes;
import coffee.machine.view.PagesPaths;
import coffee.machine.view.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_ADD_CREDITS_AMOUNT_IS_NOT_POSITIVE;
import static coffee.machine.view.Attributes.ERROR_MESSAGE;
import static coffee.machine.view.Attributes.USUAL_MESSAGE;
import static coffee.machine.view.Attributes.VALIDATION_ERRORS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by oleksij.onysymchuk@gmail on 10.01.2017.
 */
public class AdminAddCreditSubmitCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private LoggingHelper loggingHelper;
    @Mock
    private AccountService accountService;

    private Command command = new AdminAddCreditSubmitCommand();


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ((AdminAddCreditSubmitCommand) command).setAccountService(accountService);
        when(request.getSession()).thenReturn(session);
        when(request.getMethod()).thenReturn("post");
    }

    @Test
    public void testExecuteReturnsCorrectPageIfNoError() throws Exception {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("2");
        when(session.getAttribute(Attributes.ADMIN_ID)).thenReturn(1);

        assertEquals(PagesPaths.ADMIN_ADD_CREDITS_PAGE, command.execute(request,response));
    }

    @Test
    public void testExecuteReturnsCorrectPageIfErrorOccurred() throws Exception {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("2");
        when(session.getAttribute(Attributes.ADMIN_ID)).thenReturn(1);
        doThrow(new ServiceException().addMessageKey(ERROR_ADD_CREDITS_AMOUNT_IS_NOT_POSITIVE))
                .when(accountService).addCredits(any());

        assertEquals(PagesPaths.ADMIN_ADD_CREDITS_PAGE, command.execute(request,response));
    }

    @Test
    public void testExecuteReturnsCorrectPageIfValidationErrorOccurred() throws Exception {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("-5");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("-5");
        when(session.getAttribute(Attributes.ADMIN_ID)).thenReturn(1);
        assertEquals(PagesPaths.ADMIN_ADD_CREDITS_PAGE, command.execute(request,response));
    }

    @Test
    public void testExecutePlacesErrorMessageToRequestIfErrorOccurred() throws Exception {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("2");
        when(session.getAttribute(Attributes.ADMIN_ID)).thenReturn(1);
        doThrow(new ServiceException().addMessageKey(ERROR_ADD_CREDITS_AMOUNT_IS_NOT_POSITIVE))
                .when(accountService).addCredits(any());
        command.execute(request,response);
        verify(request).setAttribute(eq(ERROR_MESSAGE), any());
    }

    @Test
    public void testExecutePlacesMessageToRequestIfNoError() throws Exception {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("2");
        when(session.getAttribute(Attributes.ADMIN_ID)).thenReturn(1);
        command.execute(request,response);
        verify(request).setAttribute(eq(USUAL_MESSAGE), any());
    }

    @Test
    public void testExecuteDoesNotCallServiceIfFormHasNonPositiveAmount() throws Exception {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("0");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("1");
        when(session.getAttribute(Attributes.ADMIN_ID)).thenReturn(1);
        when(accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)).thenReturn(Optional.of(new Account()));
        command.execute(request,response);
        CreditsReceipt testReceipt = new CreditsReceipt.Builder()
                .setAmount(0.0)
                .setUserId(1)
                .build();
        verify(accountService, never()).addCredits(testReceipt);
    }

    @Test
    public void testExecutePlacesValidationErrorMessageToRequestIfFormHasNonPositiveAmount() throws Exception {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("0");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("1");
        when(session.getAttribute(Attributes.ADMIN_ID)).thenReturn(1);
        when(accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)).thenReturn(Optional.of(new Account()));
        command.execute(request,response);
        verify(request).setAttribute(eq(VALIDATION_ERRORS), any() );
    }

    @Test
    public void testExecuteDoesNotCallServiceIfUserIdIsNonPositive() throws Exception {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2.50");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("0");
        when(session.getAttribute(Attributes.ADMIN_ID)).thenReturn(1);
        command.execute(request,response);
        CreditsReceipt testReceipt = new CreditsReceipt.Builder()
                .setAmount(2.50)
                .setUserId(0)
                .build();
        verify(accountService, never()).addCredits(testReceipt);
    }

    @Test
    public void testExecutePlacesValidationErrorMessageServiceIfUserIdIsNonPositive() throws Exception {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2.50");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("0");
        when(session.getAttribute(Attributes.ADMIN_ID)).thenReturn(1);
        command.execute(request,response);
        verify(request).setAttribute(eq(VALIDATION_ERRORS), any() );
    }

    @Test
    public void testExecuteCallsServiceWithCorrectArgsIfFormIsFilled() throws Exception {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2.50");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("5");
        when(session.getAttribute(Attributes.ADMIN_ID)).thenReturn(1);
        when(accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)).thenReturn(Optional.of(new Account()));
        command.execute(request,response);
        CreditsReceipt testReceipt = new CreditsReceipt.Builder()
                .setAmount(2.50)
                .setUserId(5)
                .build();
        verify(accountService).addCredits(testReceipt);
    }

}