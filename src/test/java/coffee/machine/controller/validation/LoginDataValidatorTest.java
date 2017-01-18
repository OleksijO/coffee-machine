package coffee.machine.controller.validation;

import coffee.machine.model.value.object.user.LoginData;
import org.junit.Test;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN;
import static org.junit.Assert.*;

/**
 * Created by oleksij.onysymchuk@gmail on 18.01.2017.
 */
public class LoginDataValidatorTest {
    private Validator<LoginData> validator = new LoginDataValidator();

    @Test(expected = NullPointerException.class)
    public void testValidateThrowsNullPointerIfLoginDataIsNull() throws Exception {
        validator.validate(null);
    }

    @Test
    public void testValidateReturnsNotificationWithoutMessagesIfLoginDataIsCorrect() throws Exception {
        LoginData loginData = new LoginData("email@mail.com","password");
        Notification notification = validator.validate(loginData);
        assertFalse(notification.hasErrorMessages());
    }

    @Test
    public void testValidateReturnsNotificationWithMessagesIfLoginDataIsIncorrect() throws Exception {
        LoginData loginData = new LoginData("emailmail.com","");
        Notification notification = validator.validate(loginData);
        assertTrue(notification.hasErrorMessages());
    }

    @Test
    public void testValidateReturnsNotificationWithSpecifiedObjectClassIfLoginDataIsCorrect() throws Exception {
        LoginData loginData = new LoginData("email@mail.com","password");
        Notification notification = validator.validate(loginData);
        assertEquals(LoginData.class, notification.getValidationObjectClass());
    }

    @Test
    public void testValidateReturnsNotificationWithSpecifiedObjectClassIfLoginDataIsIncorrect() throws Exception {
        LoginData loginData = new LoginData("","");
        Notification notification = validator.validate(loginData);
        assertEquals(LoginData.class, notification.getValidationObjectClass());
    }

    @Test
    public void testValidateReturnsNotificationWithOneMessageKeyIfLoginDataHasIncorrectEmail() throws Exception {
        LoginData loginData = new LoginData("email@mail","password");
        Notification notification = validator.validate(loginData);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithCorrectMessageKeyIfLoginDataHasIncorrectEmail() throws Exception {
        LoginData loginData = new LoginData("email@mail","password");
        Notification notification = validator.validate(loginData);
        assertEquals(ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationWithOneMessageKeyIfLoginDataHasIncorrectPassword() throws Exception {
        LoginData loginData = new LoginData("email@mail.com","&*&^%%^%");
        Notification notification = validator.validate(loginData);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithCorrectMessageKeyIfLoginDataHasIncorrectPassword() throws Exception {
        LoginData loginData = new LoginData("email@mail.com","&*&^%%^%");
        Notification notification = validator.validate(loginData);
        assertEquals(ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationWithTwoMessageKeysIfLoginDataHasIncorrectEmailAndPassword() throws Exception {
        LoginData loginData = new LoginData("email","");
        Notification notification = validator.validate(loginData);
        assertEquals(2, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithNoLogMessageIfLoginDataIsIncorrect() throws Exception {
        LoginData loginData = new LoginData("email@","pas swo rd");
        Notification notification = validator.validate(loginData);
        assertEquals(0, notification.getLogMessages().size());
    }

}