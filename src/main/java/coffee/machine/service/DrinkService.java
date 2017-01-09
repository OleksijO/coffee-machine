package coffee.machine.service;


import coffee.machine.model.entity.item.Drink;

import java.util.List;

/**
 * This class represents drink service
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface DrinkService {

	/**
	 * @return list of all drinks with specified available quantity.
	 * Addons in sets also has quantity equals to available quantity
	 */
	List<Drink> getAll();

}
