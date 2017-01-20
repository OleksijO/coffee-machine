package coffee.machine.model.entity.product;

import coffee.machine.config.CoffeeMachineConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents Drink entity. NOTE: Drink = Product + List<Product>
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class Drink extends Product {
    private static final String CAN_NOT_ADD_NOT_ADDON_TO_DRINKS_ADDONS_TRIED_TO_ADD_ENTITY =
            "Can not add not addon to drink's addons. Tried to add entity:";
    private List<Product> addons = new ArrayList<>();

    Drink() {
    }

    /**
     * @return Total price of base drink and sum of prices of all addons in it
     */
    public long getTotalPrice() {
        return quantity * (
                price +
                        addons.stream()
                                .mapToLong(Product::getTotalPrice)
                                .sum()
        );
    }

    /**
     * @return Real amount of price in human friendly format with fractional digits
     */
    public double getRealTotalPrice() {
        return CoffeeMachineConfig.DB_MONEY_COEFF * getTotalPrice();
    }

    public List<Product> getAddons() {
        return addons;
    }

    public void addAddons(List<Product> addons) {
        Objects.requireNonNull(addons);
        addons.forEach(this::addAddon);
    }

    public void addAddon(Product addon) {
        Objects.requireNonNull(addon);
        if (addon.getType() != ProductType.ADDON) {
            throw new IllegalArgumentException(CAN_NOT_ADD_NOT_ADDON_TO_DRINKS_ADDONS_TRIED_TO_ADD_ENTITY + addon);
        }
        this.addons.add(addon);
    }

    public void setAddons(List<Product> addons) {
        this.addons.clear();
        this.addAddons(addons);
    }

    @Override
    public String toString() {
        return "Drink{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addons=" + addons +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalPrice=" + getTotalPrice() +
                ", type=" + type +
                "} ";
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

    public static class Builder {
        private Product.Builder productBuilder;
        private List<Product> addons = new ArrayList<>();


        public Builder() {
            this.productBuilder = new Product.Builder(ProductType.DRINK);
        }

        public Builder setId(int id) {
            productBuilder.setId(id);
            return this;
        }

        public Builder setName(String name) {
            productBuilder.setName(name);
            return this;
        }

        public Builder setPrice(long price) {
            productBuilder.setPrice(price);
            return this;
        }

        public Builder setQuantity(int quantity) {
            productBuilder.setQuantity(quantity);
            return this;
        }

        public Builder addAddons(List<Product> addons) {
            this.addons.addAll(addons);
            return this;
        }

        public Drink build() {
            Drink drink = (Drink) productBuilder.build();
            drink.addAddons(addons);
            return drink;
        }

    }


}
