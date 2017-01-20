package coffee.machine.model.entity.product;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Creates new instance on Product hierarchy by ProductType
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class ProductFactory {
    private Map<ProductType, Class> supportedClasses = new HashMap<>();
    {
        supportedClasses.put(ProductType.ADDON, Product.class);
        supportedClasses.put(ProductType.DRINK, Drink.class);
    }

    private ProductFactory() {
    }

    private static class InstanceHolder {
        private static final ProductFactory instance = new ProductFactory();
    }

    public static ProductFactory getInstance() {
        return InstanceHolder.instance;
    }

    Product getNewInstanceOfType(ProductType type) {
        Class clazz = supportedClasses.get(type);
        Objects.requireNonNull(clazz);
        try {
            return (Product) clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
