package coffee.machine.service;


import coffee.machine.model.entity.item.Drink;

import java.util.List;
import java.util.Set;

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

	/**
	 *
	 * @param drinkIds List of ids of drinks to return
	 * @return list of all drinks with specified ids with zero quantities and zero addon quantities.
	 */
	List<Drink> getAllBaseByIdSet(Set<Integer> drinkIds);

}
