package coffee.machine.controller.validation;

import coffee.machine.model.value.object.CreditsReceipt;
import org.junit.Test;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_ADD_CREDITS_AMOUNT_IS_NOT_POSITIVE;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_INCORRECT_USER_ID;
import static org.junit.Assert.*;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class CreditsReceiptValidatorTest {
    private Validator<CreditsReceipt> validator = new CreditsReceiptValidator();

    @Test(expected = NullPointerException.class)
    public void testValidateThrowsNullPointerIfReceiptIsNull()  {
        validator.validate(null);
    }

    @Test
    public void testValidateReturnsNotificationWithoutMessagesIfReceiptIsCorrect()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(1).setAmount(1).build();
        Notification notification = validator.validate(receipt);
        assertFalse(notification.hasErrorMessages());
    }

    @Test
    public void testValidateReturnsNotificationWithMessagesIfReceiptIsIncorrect()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(-2).setAmount(1).build();
        Notification notification = validator.validate(receipt);
        assertTrue(notification.hasErrorMessages());
    }

    @Test
    public void testValidateReturnsNotificationWithSpecifiedObjectClassIfReceiptIsCorrect()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(1).setAmount(1).build();
        Notification notification = validator.validate(receipt);
        assertEquals(CreditsReceipt.class, notification.getValidationObjectClass());
    }

    @Test
    public void testValidateReturnsNotificationWithSpecifiedObjectClassIfReceiptIsIncorrect()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(-5).setAmount(-5).build();
        Notification notification = validator.validate(receipt);
        assertEquals(CreditsReceipt.class, notification.getValidationObjectClass());
    }

    @Test
    public void testValidateReturnsNotificationWithOneMessageKeyIfReceiptHasIncorrectUserId()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(-5).setAmount(5).build();
        Notification notification = validator.validate(receipt);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithCorrectMessageKeyIfReceiptHasIncorrectUserId()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(-5).setAmount(5).build();
        Notification notification = validator.validate(receipt);
        assertEquals(ERROR_INCORRECT_USER_ID, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationWithOneMessageKeyIfReceiptHasZeroAmount()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(4).setAmount(0).build();
        Notification notification = validator.validate(receipt);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithСщккусеMessageKeyIfReceiptHasZeroAmount()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(4).setAmount(0).build();
        Notification notification = validator.validate(receipt);
        assertEquals(ERROR_ADD_CREDITS_AMOUNT_IS_NOT_POSITIVE, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationWithOneMessageKeyIfReceiptHasNegativeAmount()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(4).setAmount(-5).build();
        Notification notification = validator.validate(receipt);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithCorrectMessageKeyIfReceiptHasNegativeAmount()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(4).setAmount(-5).build();
        Notification notification = validator.validate(receipt);
        assertEquals(ERROR_ADD_CREDITS_AMOUNT_IS_NOT_POSITIVE, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationWithTwoMessageKeysIfReceiptHasIncorrectAmountAndUserId()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(-5).setAmount(-5).build();
        Notification notification = validator.validate(receipt);
        assertEquals(2, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithOneLogMessageIfReceiptHasIncorrectUserId()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(-5).setAmount(5).build();
        Notification notification = validator.validate(receipt);
        assertEquals(1, notification.getLogMessages().size());
    }

    @Test
    public void testValidateReturnsNotificationWithOneLogMessageIfReceiptHasZeroAmount()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(4).setAmount(0).build();
        Notification notification = validator.validate(receipt);
        assertEquals(1, notification.getLogMessages().size());
    }

    @Test
    public void testValidateReturnsNotificationWithOneLogMessageIfReceiptHasNegativeAmount()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(4).setAmount(0).build();
        Notification notification = validator.validate(receipt);
        assertEquals(1, notification.getLogMessages().size());
    }

    @Test
    public void testValidateReturnsNotificationWithTwoLogMessagesIfReceiptHasIncorrectAmountAndUserId()  {
        CreditsReceipt receipt = new CreditsReceipt.Builder().setUserId(-5).setAmount(-5).build();
        Notification notification = validator.validate(receipt);
        assertEquals(2, notification.getLogMessages().size());
    }

}