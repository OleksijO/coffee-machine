package coffee_machine.model.entity;

import java.util.Map;

import coffee_machine.model.entity.goods.Addon;
import coffee_machine.model.entity.goods.Drink;

public class CoffeeMachine {
	private Map<Drink, Integer> baseDrinks;
	private Map<Addon, Integer> addons;
	private Account account;

	public boolean tryPrepareDrinks(Map<Drink, Integer> drinksToPrepare) {
		for (Drink drinkToPrepare : drinksToPrepare.keySet()) {
			Drink baseDrink = drinkToPrepare.getBaseDrink();
			int drinkToPrepareCount = drinksToPrepare.get(baseDrink);
			int drinkAvailability = baseDrinks.get(baseDrink) - drinkToPrepareCount;
			if (drinkAvailability < 0) {
				return false;
			}
			baseDrinks.put(baseDrink, drinkAvailability);
			for (Addon addon : drinkToPrepare.getAddons().keySet()) {
				int addonAvailability = addons.get(addon) - drinkToPrepare.getAddons().get(addon) * drinkToPrepareCount;
				if (addonAvailability < 0) {
					return false;
				}
				addons.put(addon, addonAvailability);
			}
		}
		return true;
	}

	public void addBaseDrinks(Map<Drink, Integer> drinks) {
		drinks.keySet().forEach(drink -> {
			if (!baseDrinks.containsKey(drink)) {
				baseDrinks.put(drink, 0);
			}
			baseDrinks.put(drink, baseDrinks.get(drink) + drinks.get(drink));
		});
	}

	public void addBaseaddons(Map<Addon, Integer> addonsToAdd) {
		addonsToAdd.keySet().forEach(addon -> {
			if (!addons.containsKey(addon)) {
				addons.put(addon, 0);
			}
			addons.put(addon, addons.get(addon) + addonsToAdd.get(addon));
		});
	}

	public Map<Drink, Integer> getBaseDrinks() {
		return baseDrinks;
	}

	public void setBaseDrinks(Map<Drink, Integer> baseDrinks) {
		this.baseDrinks = baseDrinks;
	}

	public Map<Addon, Integer> getAddons() {
		return addons;
	}

	public void setAddons(Map<Addon, Integer> addons) {
		this.addons = addons;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
