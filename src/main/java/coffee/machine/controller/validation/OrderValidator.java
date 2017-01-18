package coffee.machine.controller.validation;

import coffee.machine.model.entity.Order;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.*;

/**
 * Created by oleksij.onysymchuk@gmail on 16.01.2017.
 */
public class OrderValidator implements Validator<Order> {
    private static final String LOG_MESSAGE_QUANTITY_SHOULD_BE_NON_NEGATIVE = "Product quantity is negative. Order details: ";
    private static final String LOG_MESSAGE_YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY = "Order is empty. Order details: ";
    private static final String LOG_MESSAGE_USER_ID_SHOULD_BE_GREATER_ZERO = "User's id=%d and should be greater zero";

    public Notification validate(Order order) {
        Notification notification = new Notification(order.getClass());
        if (order.isEmpty()) {
            notification
                    .addMessageKey(ERROR_PREPARE_ORDER_NOTHING_TO_BUY)
                    .addLogMessage(LOG_MESSAGE_YOU_DID_NOT_SPECIFIED_DRINKS_TO_BUY + order);
        }
        if (order.hasNegativeQuantity()) {
            notification
                    .addMessageKey(QUANTITY_SHOULD_BE_NON_NEGATIVE)
                    .addLogMessage(LOG_MESSAGE_QUANTITY_SHOULD_BE_NON_NEGATIVE + order);
        }
        if (order.getUserId() <= 0) {

            notification
                    .addMessageKey(ERROR_INCORRECT_USER_ID)
                    .addLogMessage(String.format(LOG_MESSAGE_USER_ID_SHOULD_BE_GREATER_ZERO, order.getUserId()));

        }
        return notification;
    }
}
