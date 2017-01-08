package coffee.machine.model.entity.item;

import coffee.machine.config.CoffeeMachineConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents Drink entity. NOTE: Drink = Item + List<Item>
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class Drink extends Item {
    private List<Item> addons = new ArrayList<>();

    public Drink() {
    }

    private Drink(Drink drink) {
        super(drink);
        this.addAddons(drink.getAddonsCopy());
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
     * @return Total price of base drink and sum of prices of all addons in it
     */
    public long getTotalPrice() {
        return price +
                addons.stream()
                        .mapToLong(addon -> addon.getPrice() * addon.getQuantity())
                        .sum();
    }

    /**
     * @return Real amount of price in human friendly format with fractional digits
     */
    public double getRealTotalPrice() {
        return CoffeeMachineConfig.DB_MONEY_COEFF * getTotalPrice();
    }

    public List<Item> getAddons() {
        return addons;
    }

    public void addAddons(List<Item> addons) {
        Objects.requireNonNull(addons);
        this.addons.addAll(addons);
    }

    public void addAddon(Item addon) {
        this.addons.add(addon);
    }

    public void setAddons(List<Item> addons) {
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

    private List<Item> getAddonsCopy() {
        List<Item> baseAddons = new ArrayList<>(addons.size());
        for (Item addon : addons) {
            Item addonCopy = new Item(addon);

            baseAddons.add(addonCopy);
        }
        return baseAddons;
    }

    public static class Builder {
        private Item.Builder itemBuilder;
        private List<Item> addons = new ArrayList<>();


        public Builder() {
            this.itemBuilder = new Item.Builder(ItemType.DRINK);
        }

        public Builder setId(int id) {
            itemBuilder.setId(id);
            return this;
        }

        public Builder setName(String name) {
            itemBuilder.setName(name);
            return this;
        }

        public Builder setPrice(long price) {
            itemBuilder.setPrice(price);
            return this;
        }

        public Builder setQuantity(int quantity) {
            itemBuilder.setQuantity(quantity);
            return this;
        }

        public Builder addAddons(List<Item> addons) {
            this.addons.addAll(addons);
            return this;
        }

        public Drink build() {
            Drink drink = (Drink) itemBuilder.build();
            drink.addAddons(addons);
            return drink;
        }

    }


}
