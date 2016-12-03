package coffee.machine.model.entity.item;

import static coffee.machine.CoffeeMachineConfig.DB_MONEY_COEFF;

/**
 * This class represents Addon entity. NOTE: Addon=Item.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class Item implements Comparable<Item> {
    protected int id;
    protected String name;
    protected long price;
    protected int quantity;
    protected ItemType type;

    public Item() {
    }

    protected Item(Item item) {
        id = item.getId();
        name = item.getName();
        price = item.getPrice();
        quantity = item.getQuantity();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public double getRealPrice() {
        return DB_MONEY_COEFF * price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    @Override
    public int compareTo(Item obj) {
        return Integer.compare(this.id, obj.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item that = (Item) o;

        if (id != that.id) return false;
        if (price != that.price) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (price ^ (price >>> 32));
        return result;
    }
}
