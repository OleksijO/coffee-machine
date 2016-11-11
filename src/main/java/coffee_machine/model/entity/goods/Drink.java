package coffee_machine.model.entity.goods;

import java.util.HashMap;
import java.util.Map;

public class Drink extends AbstractGoods {
	Map<Addon, Integer> addons;

	public Drink getBaseDrink() {
		Drink baseDrink = new Drink();
		baseDrink.setName(name);
		baseDrink.setPrice(price);
		Map<Addon, Integer> addonSet = new HashMap<>();
		for (Addon addon : addons.keySet()) {
			addonSet.put(addon, 0);
		}
		return baseDrink;

	}

	@Override
	public long getPrice() {
		long totalPrice = price;
		for (Addon addon : addons.keySet()) {
			if (addons.get(addon) > 0) {
				totalPrice += addon.getPrice() * addons.get(addon);
			}
		}
		return totalPrice;
	}

	public Map<Addon, Integer> getAddons() {
		return addons;
	}

	public void setAddons(Map<Addon, Integer> addons) {
		this.addons = addons;
	}

	@Override
	public String toString() {
		return "Drink [id=" + id + ", name=" + name + convertAddonsToString() + ", price=" + this.getPrice() + "]";
	}

	private String convertAddonsToString() {
		String addonList = "addons=[ ";
		for (Addon addon : addons.keySet()) {
			if (addons.get(addon) > 0) {
				addonList += ", " + addon.getName() + "=" + addons.get(addon);
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

}
