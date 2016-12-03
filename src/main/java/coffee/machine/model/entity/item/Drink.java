package coffee.machine.model.entity.item;

import coffee.machine.CoffeeMachineConfig;

import java.util.Set;
import java.util.TreeSet;
/**
 * This class represents Drink entity. NOTE: Drink = Item + List<Item>
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class Drink extends Item {
    Set<Item> addons;

    public Drink() {
    }

    private Drink(Drink drink) {
        super(drink);
        this.addons = drink.getAddonsCopy();
    }

    /**
     * @return Base drink: with quantity = 0 and set of available addons also with zero quantity.
     */
    public Drink getBaseDrink() {
        Drink baseDrink = new Drink(this);
        baseDrink.getAddons().forEach(addon -> addon.setQuantity(0));
        baseDrink.setQuantity(0);
        return baseDrink;
    }

    /**
     * @return Totla price of base drink and sum of prices of all addons in it
     */
    public long getTotalPrice() {
        long totalPrice = price;
        for (Item addon : addons) {
            if (addon.getQuantity() > 0) {
                totalPrice += addon.getPrice() * addon.getQuantity();
            }
        }
        return totalPrice;
    }

    /**
     * @return Real amount of price in human friendly format with fractional digits
     */
    public double getRealTotalPrice() {
        return CoffeeMachineConfig.DB_MONEY_COEFF * getTotalPrice();
    }

    public Set<Item> getAddons() {
        return addons;
    }

    public void setAddons(Set<Item> addons) {
        this.addons = addons;
    }

    @Override
    public String toString() {
        return "Drink [" + name + ", " + convertAddonsToString() +
                ", quantity=" + this.getQuantity() +
                ", price=" + this.getRealTotalPrice()

                + "]";
    }

    private String convertAddonsToString() {
        String addonList = "addons=[ ";
        for (Item addon : addons) {
            if (addon.getQuantity() > 0) {
                addonList += ", " + addon.getName() + "=" + addon.getQuantity();
            }
        }
        return addonList.replaceFirst(", ", "") + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((addons == null) ? 0 : addons.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Drink other = (Drink) obj;
        if (addons == null) {
            if (other.addons != null)
                return false;
        } else if (!addons.equals(other.addons))
            return false;
        return true;
    }

    private Set<Item> getAddonsCopy() {
        Set<Item> baseAddons = new TreeSet<>();
        for (Item addon : addons) {
            Item addonCopy = new Item(addon);

            baseAddons.add(addonCopy);
        }
        return baseAddons;
    }
}
