package coffee.machine.model.entity.product;

import java.util.HashMap;
import java.util.Map;

import static coffee.machine.model.entity.product.ProductType.ADDON;
import static coffee.machine.model.entity.product.ProductType.DRINK;

/**
 * Creates new instance of Product hierarchy by ProductType
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class ProductFactory {
    private static final String UNSUPPORTED_PRODUCT_TYPE = "Unsupported product type = ";

    private Map<ProductType, ProductProducer> supportedProductProducers = new HashMap<>();

    private ProductFactory() {
        supportedProductProducers.put(ADDON, Product::new);
        supportedProductProducers.put(DRINK, Drink::new);
    }

    private static class InstanceHolder {
        private static final ProductFactory instance = new ProductFactory();
    }

    public static ProductFactory getInstance() {
        return InstanceHolder.instance;
    }

    Product getNewInstanceOfType(ProductType type) {
        Product product = supportedProductProducers.get(type).newInstance();
        if (product == null) {
            throw new IllegalArgumentException(UNSUPPORTED_PRODUCT_TYPE + type);
        }
        return product;
    }

    @FunctionalInterface
    private interface ProductProducer {
        Product newInstance();
    }
}
