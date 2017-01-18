package coffee.machine.controller.validation;

import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import coffee.machine.model.entity.product.ProductType;
import coffee.machine.model.value.object.ProductsReceipt;
import org.junit.Test;

import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.ADMIN_REFILL_NOTHING_TO_ADD;
import static coffee.machine.controller.i18n.message.key.error.ControllerErrorMessageKey.QUANTITY_SHOULD_BE_NON_NEGATIVE;
import static org.junit.Assert.assertEquals;

/**
 * Created by oleksij.onysymchuk@gmail on 18.01.2017.
 */
public class ProductsReceiptValidatorTest {
    private Validator<ProductsReceipt> validator = new ProductsReceiptValidator();

    @Test(expected = NullPointerException.class)
    public void testValidateThrowsNullPointerIfLoginDataIsNull() throws Exception {
        validator.validate(null);
    }

    @Test
    public void testValidateReturnsNotificationWithSpecifiedObjectClassIfLoginDataIsCorrect() throws Exception {
        ProductsReceipt receipt = getCorrectProductsReceipt();
        Notification notification = validator.validate(receipt);
        assertEquals(ProductsReceipt.class, notification.getValidationObjectClass());
    }

    @Test
    public void testValidateReturnsNotificationWithSpecifiedObjectClassIfLoginDataIsIncorrect() throws Exception {
        ProductsReceipt receipt = getEmptyProductsReceipt();
        Notification notification = validator.validate(receipt);
        assertEquals(ProductsReceipt.class, notification.getValidationObjectClass());
    }

    @Test
    public void testValidateReturnsNotificationHasOneMessageKeyIfReceiptIsEmpty() throws Exception {
        ProductsReceipt receipt = getEmptyProductsReceipt();
        Notification notification = validator.validate(receipt);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationHasCorrectMessageKeyIfReceiptIsEmpty() throws Exception {
        ProductsReceipt receipt = getEmptyProductsReceipt();
        Notification notification = validator.validate(receipt);
        assertEquals(ADMIN_REFILL_NOTHING_TO_ADD, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationHasLogMessageIfReceiptIsEmpty() throws Exception {
        ProductsReceipt receipt = getEmptyProductsReceipt();
        Notification notification = validator.validate(receipt);
        assertEquals(1, notification.getLogMessages().size());
    }

    @Test
    public void testValidateReturnsNotificationHasOneMessageKeyIfReceiptHasDrinkNegativeQuantity() throws Exception {
        ProductsReceipt receipt = getCorrectProductsReceipt();
        receipt.getDrinks().get(1).setQuantity(-5);
        Notification notification = validator.validate(receipt);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationHasCorrectMessageKeyIfReceiptHasDrinkNegativeQuantity() throws Exception {
        ProductsReceipt receipt = getCorrectProductsReceipt();
        receipt.getDrinks().get(1).setQuantity(-5);
        Notification notification = validator.validate(receipt);
        assertEquals(QUANTITY_SHOULD_BE_NON_NEGATIVE, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationHasLogMessageIfReceiptHasDrinkNegativeQuantity() throws Exception {
        ProductsReceipt receipt = getCorrectProductsReceipt();
        receipt.getDrinks().get(1).setQuantity(-5);
        Notification notification = validator.validate(receipt);
        assertEquals(1, notification.getLogMessages().size());
    }

    @Test
    public void testValidateReturnsNotificationHasOneMessageKeyIfReceiptHasAddonNegativeQuantity() throws Exception {
        ProductsReceipt receipt = getCorrectProductsReceipt();
        receipt.getAddons().get(1).setQuantity(-5);
        Notification notification = validator.validate(receipt);
        assertEquals(1, notification.getErrorMessageKeys().size());
    }

    @Test
    public void testValidateReturnsNotificationHasCorrectMessageKeyIfReceiptHasAddonNegativeQuantity() throws Exception {
        ProductsReceipt receipt = getCorrectProductsReceipt();
        receipt.getAddons().get(1).setQuantity(-5);
        Notification notification = validator.validate(receipt);
        assertEquals(QUANTITY_SHOULD_BE_NON_NEGATIVE, notification.getErrorMessageKeys().get(0));
    }

    @Test
    public void testValidateReturnsNotificationHasLogMessageIfReceiptHasAddonNegativeQuantity() throws Exception {
        ProductsReceipt receipt = getCorrectProductsReceipt();
        receipt.getAddons().get(1).setQuantity(-5);
        Notification notification = validator.validate(receipt);
        assertEquals(1, notification.getLogMessages().size());
    }


    private ProductsReceipt getEmptyProductsReceipt() {
        return new ProductsReceipt();
    }

    private ProductsReceipt getCorrectProductsReceipt() {
        ProductsReceipt receipt = new ProductsReceipt();
        receipt.addDrink(new Drink.Builder().setId(1).setQuantity(1).build());
        receipt.addDrink(new Drink.Builder().setId(2).setQuantity(2).build());
        receipt.addDrink(new Drink.Builder().setId(3).setQuantity(0).build());
        receipt.addDrink(new Drink.Builder().setId(4).setQuantity(3).build());
        receipt.addAddon(new Product.Builder(ProductType.ADDON).setId(10).setQuantity(1).build());
        receipt.addAddon(new Product.Builder(ProductType.ADDON).setId(20).setQuantity(2).build());
        receipt.addAddon(new Product.Builder(ProductType.ADDON).setId(21).setQuantity(0).build());
        receipt.addAddon(new Product.Builder(ProductType.ADDON).setId(30).setQuantity(3).build());
        return receipt;
    }


}