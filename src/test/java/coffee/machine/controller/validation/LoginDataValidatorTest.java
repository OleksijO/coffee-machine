package coffee.machine.controller.validation;

import coffee.machine.model.value.object.user.LoginData;
import org.junit.Test;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN;
import static org.junit.Assert.*;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class LoginDataValidatorTest {
    private Validator<LoginData> validator = new LoginDataValidator();

    @Test(expected = NullPointerException.class)
    public void testValidateThrowsNullPointerIfLoginDataIsNull()  {
        validator.validate(null);
    }

    @Test
    public void testValidateReturnsNotificationWithoutMessagesIfLoginDataIsCorrect()  {
        LoginData loginData = new LoginData("email@mail.com","password");
        Notification notification = validator.validate(loginData);
        assertFalse(notification.hasErrorMessages());
    }

    @Test
    public void testValidateReturnsNotificationWithMessagesIfLoginDataIsIncorrect()  {
        LoginData loginData = new LoginData("emailmail.com","");
        Notification notification = validator.validate(loginData);
        assertTrue(notification.hasErrorMessages());
    }

    @Test
    public void testValidateReturnsNotificationWithSpecifiedObjectClassIfLoginDataIsCorrect()  {
        LoginData loginData = new LoginData("email@mail.com","password");
        Notification notification = validator.validate(loginData);
        assertEquals(LoginData.class, notification.getValidationObjectClass());
    }

    @Test
    public void testValidateReturnsNotificationWithSpecifiedObjectClassIfLoginDataIsIncorrect()  {
        LoginData loginData = new LoginData("","");
        Notification notification = validator.validate(loginData);
        assertEquals(LoginData.class, notification.getValidationObjectClass());
    }

    @Test
    public void testValidateReturnsNotificationWithOneMessageKeyIfLoginDataHasIncorrectEmail()  {
        LoginData loginData = new LoginData("email@mail","password");
        Notification notification = validator.validate(loginData);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithCorrectMessageKeyIfLoginDataHasIncorrectEmail()  {
        LoginData loginData = new LoginData("email@mail","password");
        Notification notification = validator.validate(loginData);
        assertEquals(ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationWithOneMessageKeyIfLoginDataHasIncorrectPassword()  {
        LoginData loginData = new LoginData("email@mail.com","&*&^%%^%");
        Notification notification = validator.validate(loginData);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithCorrectMessageKeyIfLoginDataHasIncorrectPassword()  {
        LoginData loginData = new LoginData("email@mail.com","&*&^%%^%");
        Notification notification = validator.validate(loginData);
        assertEquals(ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationWithTwoMessageKeysIfLoginDataHasIncorrectEmailAndPassword()  {
        LoginData loginData = new LoginData("email","");
        Notification notification = validator.validate(loginData);
        assertEquals(2, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithNoLogMessageIfLoginDataIsIncorrect()  {
        LoginData loginData = new LoginData("email@","pas swo rd");
        Notification notification = validator.validate(loginData);
        assertEquals(0, notification.getLogMessages().size());
    }

}