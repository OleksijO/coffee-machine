package coffee.machine.service;


import coffee.machine.model.entity.item.Drink;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents drink service
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface DrinkService {

	List<Drink> getAll();

	/**
	 * Adds specified quantity for every drink with specified id
	 *
	 * @param quantitiesById Map of pairs (drinkId, quantityToAdd)
	 */
	void refill(Map<Integer, Integer> quantitiesById);

	List<Drink> getAllByIdSet(Set<Integer> integers);

    List<Drink> getAllBaseByIdSet(Set<Integer> drinkIds);

}
