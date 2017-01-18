package coffee.machine.controller.validation;

import coffee.machine.model.value.object.user.RegisterData;
import org.junit.Test;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.*;
import static org.junit.Assert.*;

/**
 * Created by oleksij.onysymchuk@gmail on 18.01.2017.
 */
public class RegisterDataValidatorTest {
    private Validator<RegisterData> validator = new RegisterDataValidator();

    @Test(expected = NullPointerException.class)
    public void testValidateThrowsNullPointerIfRegisterDataIsNull() throws Exception {
        validator.validate(null);
    }

    @Test
    public void testValidateReturnsNotificationWithoutMessagesIfRegisterDataIsCorrect() throws Exception {
        RegisterData loginData = new RegisterData("email@mail.com","password","My Full Name");
        Notification notification = validator.validate(loginData);
        assertFalse(notification.hasErrorMessages());
    }

    @Test
    public void testValidateReturnsNotificationWithMessagesIfRegisterDataIsIncorrect() throws Exception {
        RegisterData loginData = new RegisterData("emailmail.com","","12312321");
        Notification notification = validator.validate(loginData);
        assertTrue(notification.hasErrorMessages());
    }

    @Test
    public void testValidateReturnsNotificationWithSpecifiedObjectClassIfRegisterDataIsCorrect() throws Exception {
        RegisterData loginData = new RegisterData("email@mail.com","password","My Full Name");
        Notification notification = validator.validate(loginData);
        assertEquals(RegisterData.class, notification.getValidationObjectClass());
    }

    @Test
    public void testValidateReturnsNotificationWithSpecifiedObjectClassIfRegisterDataIsIncorrect() throws Exception {
        RegisterData loginData = new RegisterData("","","");
        Notification notification = validator.validate(loginData);
        assertEquals(RegisterData.class, notification.getValidationObjectClass());
    }

    @Test
    public void testValidateReturnsNotificationWithOneMessageKeyIfRegisterDataHasIncorrectEmail() throws Exception {
        RegisterData loginData = new RegisterData("email@mail","password","My Full Name");
        Notification notification = validator.validate(loginData);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithCorrectMessageKeyIfRegisterDataHasIncorrectEmail() throws Exception {
        RegisterData loginData = new RegisterData("email@mail","password","My Full Name");
        Notification notification = validator.validate(loginData);
        assertEquals(ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationWithOneMessageKeyIfRegisterDataHasIncorrectPassword() throws Exception {
        RegisterData loginData = new RegisterData("email@mail.com","&*&^%%^%","My Full Name");
        Notification notification = validator.validate(loginData);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithCorrectMessageKeyIfRegisterDataHasIncorrectPassword() throws Exception {
        RegisterData loginData = new RegisterData("email@mail.com","&*&^%%^%","My Full Name");
        Notification notification = validator.validate(loginData);
        assertEquals(ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationWithOneMessageKeyIfRegisterDataHasIncorrectFullName() throws Exception {
        RegisterData loginData = new RegisterData("email@mail.com","password","");
        Notification notification = validator.validate(loginData);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithCorrectMessageKeyIfRegisterDataHasIncorrectFullName() throws Exception {
        RegisterData loginData = new RegisterData("email@mail.com","password","");
        Notification notification = validator.validate(loginData);
        assertEquals(ERROR_REGISTER_FULL_NAME_DO_NOT_MATCH_PATTERN, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationWithTwoMessageKeysIfRegisterDataHasIncorrectEmailAndPassword() throws Exception {
        RegisterData loginData = new RegisterData("email","","My Full Name");
        Notification notification = validator.validate(loginData);
        assertEquals(2, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithTwoMessageKeysIfRegisterDataHasAllIncorrectData() throws Exception {
        RegisterData loginData = new RegisterData("","","");
        Notification notification = validator.validate(loginData);
        assertEquals(3, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithNoLogMessageIfRegisterDataIsIncorrect() throws Exception {
        RegisterData loginData = new RegisterData("email@","pas swo rd","");
        Notification notification = validator.validate(loginData);
        assertEquals(0, notification.getLogMessages().size());
    }


}