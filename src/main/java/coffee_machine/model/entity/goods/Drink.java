package coffee_machine.model.entity.goods;

import java.util.Set;
import java.util.TreeSet;

import static coffee_machine.view.Parameters.DB_MONEY_COEFF;

public class Drink extends AbstractGoods {
    Set<Addon> addons;

    public Drink() {
    }

    public Drink(Drink drink) {
        super(drink);
        this.addons = drink.getAddonsCopy();
    }

    public Drink getBaseDrink() {
        Drink baseDrink = new Drink(this);
        baseDrink.getAddons().forEach(addon -> addon.setQuantity(0));
        baseDrink.setQuantity(1);
        return baseDrink;
    }

    public long getTotalPrice() {
        long totalPrice = price;
        for (Addon addon : addons) {
            if (addon.getQuantity() > 0) {
                totalPrice += addon.getPrice() * addon.getQuantity();
            }
        }
        return totalPrice;
    }

    public double getRealTotalPrice() {
        return DB_MONEY_COEFF * getTotalPrice();
    }

    public Set<Addon> getAddons() {
        return addons;
    }

    public void setAddons(Set<Addon> addons) {
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
        for (Addon addon : addons) {
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

    private Set<Addon> getAddonsCopy() {
        Set<Addon> baseAddons = new TreeSet<>();
        for (Addon addon : addons) {
            Addon addonCopy = new Addon(addon);

            baseAddons.add(addonCopy);
        }
        return baseAddons;
    }
}
