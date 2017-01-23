package coffee.machine.controller.validation;

import coffee.machine.model.value.object.ProductsReceipt;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ADMIN_REFILL_NOTHING_TO_ADD;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.QUANTITY_SHOULD_BE_NON_NEGATIVE;

/**
 * This class represents validator for value object ProductsReceipt.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class ProductsReceiptValidator implements Validator<ProductsReceipt> {
    private static final String LOG_MESSAGE_ADMIN_REFILL_NOTHING_TO_ADD = "Nothing to add. Details: ";
    private static final String LOG_MESSAGE_QUANTITY_SHOULD_BE_NON_NEGATIVE = "Product quantity is negative. Details: ";

    @Override
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
