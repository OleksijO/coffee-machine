package coffee.machine.model.entity.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Creates new instance on Item hierarchy by ItemType
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class ItemFactory {
    private Map<ItemType, Class> supportedClasses = new HashMap<ItemType, Class>() {{
        put(ItemType.ADDON, Item.class);
        put(ItemType.DRINK, Drink.class);
    }};

    private static class InstanceHolder {
        private static final ItemFactory instance = new ItemFactory();
    }

    public static ItemFactory getInstance() {
        return InstanceHolder.instance;
    }

    public Item getNewInstanceOfType(ItemType type) {
        Objects.requireNonNull(type);
        Class clazz = supportedClasses.get(type);
        Objects.requireNonNull(clazz);
        try {
            return (Item) clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
