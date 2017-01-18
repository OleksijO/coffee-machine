package coffee.machine.controller.validation;

import coffee.machine.model.value.object.CreditsReceipt;

import java.util.Objects;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ERROR_UNKNOWN;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.ERROR_ADD_CREDITS_AMOUNT_IS_NEGATIVE;

/**
 * Created by oleksij.onysymchuk@gmail on 16.01.2017.
 */
public class CreditsReceiptValidator implements Validator<CreditsReceipt> {
    private static final String LOG_MESSAGE_FORMAT_AMOUNT_FOR_ADD_SHOULD_BE_GREATER_THAN_ZERO =
            "Amount to add should be greater than zero. UserId=%d, amount=%d.";
    private static final String LOG_MESSAGE_USER_ID_SHOULD_BE_GREATER_ZERO = "User's id=%d and should be greater zero";


    public Notification validate(CreditsReceipt receipt) {
        Objects.requireNonNull(receipt);
        Notification notification = new Notification(receipt.getClass());
        long amountToAdd = receipt.getAmount();
        int userId = receipt.getUserId();

        if (amountToAdd <= 0) {
            notification
                    .addMessageKey(ERROR_ADD_CREDITS_AMOUNT_IS_NEGATIVE)
                    .addLogMessage(
                            String.format(LOG_MESSAGE_FORMAT_AMOUNT_FOR_ADD_SHOULD_BE_GREATER_THAN_ZERO,
                                    userId, amountToAdd));
        }

        if (userId <= 0) {
            notification.addMessageKey(ERROR_UNKNOWN)
                    .addLogMessage(String.format(LOG_MESSAGE_USER_ID_SHOULD_BE_GREATER_ZERO, userId));
        }
        return notification;
    }
}
