package coffee.machine.service;


import coffee.machine.model.entity.item.Drink;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author oleksij.onysymchuk@gmail.com 15.11.2016.
 */
public interface DrinkService {

	List<Drink> getAll();

	void refill(Map<Integer, Integer> quantitiesById);

	List<Drink> getAllByIdSet(Set<Integer> integers);

    List<Drink> getAllBaseByIdSet(Set<Integer> drinkIds);

}
