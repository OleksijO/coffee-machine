package coffee.machine.controller.validation;

import coffee.machine.model.value.object.CreditsReceipt;

import java.util.Objects;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_ADD_CREDITS_AMOUNT_IS_NOT_POSITIVE;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_INCORRECT_USER_ID;

/**
 * This class represents validator for value object CreditsReceipt.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class CreditsReceiptValidator implements Validator<CreditsReceipt> {
    private static final String LOG_MESSAGE_FORMAT_AMOUNT_FOR_ADD_SHOULD_BE_GREATER_THAN_ZERO =
            "Amount to add should be greater than zero. UserId=%d, amount=%d.";
    private static final String LOG_MESSAGE_USER_ID_SHOULD_BE_GREATER_ZERO = "User's id=%d and should be greater zero";

    @Override
    public Notification validate(CreditsReceipt receipt) {
        Objects.requireNonNull(receipt);
        Notification notification = new Notification(receipt.getClass());
        long amountToAdd = receipt.getAmount();
        int userId = receipt.getUserId();

        if (amountToAdd <= 0) {
            notification
                    .addMessageKey(ERROR_ADD_CREDITS_AMOUNT_IS_NOT_POSITIVE)
                    .addLogMessage(
                            String.format(LOG_MESSAGE_FORMAT_AMOUNT_FOR_ADD_SHOULD_BE_GREATER_THAN_ZERO,
                                    userId, amountToAdd));
        }

        if (userId <= 0) {
            notification
                    .addMessageKey(ERROR_INCORRECT_USER_ID)
                    .addLogMessage(String.format(LOG_MESSAGE_USER_ID_SHOULD_BE_GREATER_ZERO, userId));
        }
        return notification;
    }
}
