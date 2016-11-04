package coffee_machine.model.goods;

import java.util.Map;

public class Drink extends AbstractGoods {
	Map<Addon, Integer> addons;

	@Override
	public long getPrice() {
		long totalPrice=price;
		for (Addon addon : addons.keySet()) {
			if (addons.get(addon) > 0) {
				totalPrice += addon.getPrice() * addons.get(addon);
			}
		}
		return totalPrice;
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

}
