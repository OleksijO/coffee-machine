package coffee.machine.controller.validation;

import coffee.machine.model.value.object.ProductsReceipt;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ADMIN_REFILL_NOTHING_TO_ADD;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.QUANTITY_SHOULD_BE_NON_NEGATIVE;

/**
 * Created by oleksij.onysymchuk@gmail on 16.01.2017.
 */
public class ProductsReceiptValidator implements Validator<ProductsReceipt> {
    private String LOG_MESSAGE_ADMIN_REFILL_NOTHING_TO_ADD = "Nothing to add. Details: ";
    private String LOG_MESSAGE_QUANTITY_SHOULD_BE_NON_NEGATIVE = "Product quantity is negative. Details: ";

    public Notification validate(ProductsReceipt receipt) {
        Notification notification = new Notification(receipt.getClass());
        if (receipt.isEmpty()) {
            notification
                    .addMessageKey(ADMIN_REFILL_NOTHING_TO_ADD)
                    .addLogMessage(LOG_MESSAGE_ADMIN_REFILL_NOTHING_TO_ADD + receipt);
        }
        if (receipt.hasNegativeQuantity()) {
            notification
                    .addMessageKey(QUANTITY_SHOULD_BE_NON_NEGATIVE)
                    .addLogMessage(LOG_MESSAGE_QUANTITY_SHOULD_BE_NON_NEGATIVE + receipt);
        }
        return notification;
    }

}
