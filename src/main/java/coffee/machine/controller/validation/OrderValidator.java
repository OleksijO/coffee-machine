package coffee.machine.controller.validation;

import coffee.machine.model.entity.Order;

import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.QUANTITY_SHOULD_BE_NON_NEGATIVE;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY;

/**
 * Created by oleksij.onysymchuk@gmail on 16.01.2017.
 */
public class OrderValidator implements Validator<Order> {
    String LOG_MESSAGE_QUANTITY_SHOULD_BE_NON_NEGATIVE = "Product quantity is negative. Order details: ";
    String LOG_MESSAGE_YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY = "Order is empty. Order details: ";

    public Notification validate(Order order) {
        Notification notification = new Notification(order.getClass());
        if (order.isEmpty()) {
            notification
                    .addMessageKey(YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY)
                    .addLogMessage(LOG_MESSAGE_YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY +order);
        }
        if (order.hasNegativeQuantity()) {
            notification
                    .addMessageKey(QUANTITY_SHOULD_BE_NON_NEGATIVE)
                    .addLogMessage(LOG_MESSAGE_QUANTITY_SHOULD_BE_NON_NEGATIVE + order);
        }
        return notification;
    }
}
