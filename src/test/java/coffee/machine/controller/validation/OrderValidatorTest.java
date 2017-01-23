package coffee.machine.controller.validation;

import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import coffee.machine.model.entity.product.ProductType;
import org.junit.Test;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.*;
import static org.junit.Assert.assertEquals;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class OrderValidatorTest {
    private Validator<Order> validator = new OrderValidator();

    @Test(expected = NullPointerException.class)
    public void testValidateThrowsNullPointerIfLoginDataIsNull()  {
        validator.validate(null);
    }

    @Test
    public void testValidateReturnsNotificationWithSpecifiedObjectClassIfLoginDataIsCorrect()  {
        Order order = getCorrectOrder();
        Notification notification = validator.validate(order);
        assertEquals(Order.class, notification.getValidationObjectClass());
    }

    @Test
    public void testValidateReturnsNotificationWithSpecifiedObjectClassIfLoginDataIsIncorrect()  {
        Order order = getEmptyOrder();
        Notification notification = validator.validate(order);
        assertEquals(Order.class, notification.getValidationObjectClass());
    }

    @Test
    public void testValidateReturnsNotificationHasOneMessageKeyIfReceiptIsEmpty()  {
        Order order = getEmptyOrder();
        Notification notification = validator.validate(order);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationHasCorrectMessageKeyIfReceiptIsEmpty()  {
        Order order = getEmptyOrder();
        Notification notification = validator.validate(order);
        assertEquals(ERROR_PREPARE_ORDER_NOTHING_TO_BUY, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationHasLogMessageIfReceiptIsEmpty()  {
        Order order = getEmptyOrder();
        Notification notification = validator.validate(order);
        assertEquals(1, notification.getLogMessages().size());
    }

    @Test
    public void testValidateReturnsNotificationHasOneMessageKeyIfReceiptHasDrinkNegativeQuantity()  {
        Order order = getCorrectOrder();
        order.getDrinks().get(1).setQuantity(-5);
        Notification notification = validator.validate(order);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationHasOneMessageKeyIfReceiptHasSeveralDrinkNegativeQuantity()  {
        Order order = getCorrectOrder();
        order.getDrinks().get(1).setQuantity(-5);
        order.getDrinks().get(0).setQuantity(-5);
        Notification notification = validator.validate(order);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationHasCorrectMessageKeyIfReceiptHasDrinkNegativeQuantity()  {
        Order order = getCorrectOrder();
        order.getDrinks().get(1).setQuantity(-5);
        Notification notification = validator.validate(order);
        assertEquals(QUANTITY_SHOULD_BE_NON_NEGATIVE, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationHasLogMessageIfReceiptHasDrinkNegativeQuantity()  {
        Order order = getCorrectOrder();
        order.getDrinks().get(1).setQuantity(-5);
        Notification notification = validator.validate(order);
        assertEquals(1, notification.getLogMessages().size());
    }

    @Test
    public void testValidateReturnsNotificationHasOneMessageKeyIfReceiptHasAddonNegativeQuantity()  {
        Order order = getCorrectOrder();
        order.getDrinks().get(1).getAddons().get(1).setQuantity(-5);
        Notification notification = validator.validate(order);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }
    @Test
    public void testValidateReturnsNotificationHasOneMessageKeyIfReceiptHasSeveralAddonNegativeQuantity()  {
        Order order = getCorrectOrder();
        order.getDrinks().get(1).getAddons().get(1).setQuantity(-5);
        order.getDrinks().get(0).getAddons().get(0).setQuantity(-5);
        Notification notification = validator.validate(order);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationHasCorrectMessageKeyIfReceiptHasAddonNegativeQuantity()  {
        Order order = getCorrectOrder();
        order.getDrinks().get(1).getAddons().get(1).setQuantity(-5);
        Notification notification = validator.validate(order);
        assertEquals(QUANTITY_SHOULD_BE_NON_NEGATIVE, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationHasLogMessageIfReceiptHasAddonNegativeQuantity()  {
        Order order = getCorrectOrder();
        order.getDrinks().get(1).getAddons().get(1).setQuantity(-5);
        Notification notification = validator.validate(order);
        assertEquals(1, notification.getLogMessages().size());
    }

    @Test
    public void testValidateReturnsNotificationWithOneMessageKeyIfReceiptHasIncorrectUserId()  {
        Order order = new Order.Builder().setUserId(0).build();
        order.addDrink(new Drink.Builder().setId(1).setQuantity(1).build());
        Notification notification = validator.validate(order);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationWithCorrectMessageKeyIfReceiptHasIncorrectUserId()  {
        Order order = new Order.Builder().setUserId(-1).build();
        order.addDrink(new Drink.Builder().setId(1).setQuantity(1).build());
        Notification notification = validator.validate(order);
        assertEquals(ERROR_INCORRECT_USER_ID, notification.getErrorMessageKeys().get(0));
    }


    private Order getEmptyOrder() {
        return new Order.Builder().setUserId(1).build();
    }

    private Order getCorrectOrder() {
        Order order = new Order.Builder()
                .setUserId(1)
                .build();
        order.addDrink(new Drink.Builder().setId(1).setQuantity(1).build());
        order.addDrink(new Drink.Builder().setId(2).setQuantity(2).build());
        order.addDrink(new Drink.Builder().setId(3).setQuantity(0).build());
        order.addDrink(new Drink.Builder().setId(4).setQuantity(3).build());
        order.getDrinks().get(0).addAddon(new Product.Builder(ProductType.ADDON).setId(10).setQuantity(1).build());
        order.getDrinks().get(0).addAddon(new Product.Builder(ProductType.ADDON).setId(20).setQuantity(0).build());
        order.getDrinks().get(1).addAddon(new Product.Builder(ProductType.ADDON).setId(10).setQuantity(0).build());
        order.getDrinks().get(1).addAddon(new Product.Builder(ProductType.ADDON).setId(20).setQuantity(0).build());
        order.getDrinks().get(2).addAddon(new Product.Builder(ProductType.ADDON).setId(10).setQuantity(1).build());
        order.getDrinks().get(2).addAddon(new Product.Builder(ProductType.ADDON).setId(20).setQuantity(2).build());
        order.getDrinks().get(2).addAddon(new Product.Builder(ProductType.ADDON).setId(10).setQuantity(1).build());
        order.getDrinks().get(2).addAddon(new Product.Builder(ProductType.ADDON).setId(20).setQuantity(2).build());
        return order;
    }

}