package coffee.machine.controller.command.admin.add.credit;

import coffee.machine.config.CoffeeMachineConfig;
import coffee.machine.controller.Command;
import coffee.machine.controller.command.helper.LoggingHelper;
import coffee.machine.model.entity.Account;
import coffee.machine.model.value.object.CreditsReceipt;
import coffee.machine.service.AccountService;
import coffee.machine.service.UserService;
import coffee.machine.service.exception.ServiceException;
import coffee.machine.view.config.Attributes;
import coffee.machine.view.config.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_ADD_CREDITS_AMOUNT_IS_NOT_POSITIVE;
import static coffee.machine.view.config.Attributes.*;
import static coffee.machine.view.config.Pages.ADMIN_ADD_CREDITS_PAGE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author oleksij.onysymchuk@gmail.com
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
    @Mock
    private UserService userService;

    private Command command = new AdminAddCreditSubmitCommand();


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ((AdminAddCreditSubmitCommand) command).setAccountService(accountService);
        ((AdminAddCreditSubmitCommand) command).setUserService(userService);
        when(request.getSession()).thenReturn(session);
        when(request.getMethod()).thenReturn("post");
        doNothing().when(accountService).addCredits(any());
    }

    @Test
    public void testExecuteReturnsCorrectPageIfNoError() throws IOException {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("2");
        when(session.getAttribute(Attributes.USER_ID)).thenReturn(1);

        assertEquals(ADMIN_ADD_CREDITS_PAGE, command.execute(request,response));
    }

    @Test
    public void testExecuteReturnsCorrectPageIfErrorOccurred() throws IOException {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("2");
        when(session.getAttribute(Attributes.USER_ID)).thenReturn(1);
        doThrow(new ServiceException().addMessageKey(ERROR_ADD_CREDITS_AMOUNT_IS_NOT_POSITIVE))
                .when(accountService).addCredits(any());

        assertEquals(ADMIN_ADD_CREDITS_PAGE, command.execute(request,response));
    }

    @Test
    public void testExecuteReturnsCorrectPageIfValidationErrorOccurred() throws IOException {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("-5");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("-5");
        when(session.getAttribute(Attributes.USER_ID)).thenReturn(1);
        assertEquals(ADMIN_ADD_CREDITS_PAGE, command.execute(request,response));
    }

    @Test
    public void testExecutePlacesErrorMessageToRequestIfErrorOccurred() throws IOException {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("2");
        when(session.getAttribute(Attributes.USER_ID)).thenReturn(1);
        doThrow(new ServiceException().addMessageKey(ERROR_ADD_CREDITS_AMOUNT_IS_NOT_POSITIVE))
                .when(accountService).addCredits(any());
        command.execute(request,response);
        verify(request).setAttribute(eq(ERROR_MESSAGE), any());
    }

    @Test
    public void testExecutePlacesMessageToRequestIfNoError() throws IOException  {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("2");
        when(session.getAttribute(Attributes.USER_ID)).thenReturn(1);
        command.execute(request,response);
        verify(request).setAttribute(eq(USUAL_MESSAGE), any());
    }

    @Test
    public void testExecuteDoesNotCallServiceIfFormHasNonPositiveAmount() throws IOException {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("0");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("1");
        when(session.getAttribute(Attributes.USER_ID)).thenReturn(1);
        when(accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)).thenReturn(Optional.of(new Account()));
        command.execute(request,response);
        CreditsReceipt testReceipt = new CreditsReceipt.Builder()
                .setAmount(0.0)
                .setUserId(1)
                .build();
        verify(accountService, never()).addCredits(testReceipt);
    }

    @Test
    public void testExecutePlacesValidationErrorMessageToRequestIfFormHasNonPositiveAmount() throws IOException {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("0");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("1");
        when(session.getAttribute(Attributes.USER_ID)).thenReturn(1);
        when(accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)).thenReturn(Optional.of(new Account()));
        command.execute(request,response);
        verify(request).setAttribute(eq(VALIDATION_ERRORS), any() );
    }

    @Test
    public void testExecuteDoesNotCallServiceIfUserIdIsNonPositive() throws IOException {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2.50");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("0");
        when(session.getAttribute(Attributes.USER_ID)).thenReturn(1);
        command.execute(request,response);
        CreditsReceipt testReceipt = new CreditsReceipt.Builder()
                .setAmount(2.50)
                .setUserId(0)
                .build();
        verify(accountService, never()).addCredits(testReceipt);
    }

    @Test
    public void testExecutePlacesValidationErrorMessageServiceIfUserIdIsNonPositive() throws IOException {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2.50");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("0");
        when(session.getAttribute(Attributes.USER_ID)).thenReturn(1);
        command.execute(request,response);
        verify(request).setAttribute(eq(VALIDATION_ERRORS), any() );
    }

    @Test
    public void testExecuteCallsServiceWithCorrectArgsIfFormIsFilled() throws IOException {
        when(request.getParameter(Parameters.CREDITS_TO_ADD)).thenReturn("2.50");
        when(request.getParameter(Parameters.USER_ID)).thenReturn("5");
        when(session.getAttribute(Attributes.USER_ID)).thenReturn(1);
        when(accountService.getById(CoffeeMachineConfig.ACCOUNT_ID)).thenReturn(Optional.of(new Account()));
        command.execute(request,response);
        CreditsReceipt testReceipt = new CreditsReceipt.Builder()
                .setAmount(2.50)
                .setUserId(5)
                .build();
        verify(accountService).addCredits(testReceipt);
    }

}