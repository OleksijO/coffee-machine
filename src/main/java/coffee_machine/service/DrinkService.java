package coffee_machine.service;

import coffee_machine.model.entity.goods.Drink;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public interface DrinkService {

	List<Drink> getAll();

	void refill(Map<Integer, Integer> quantitiesById);

	List<Drink> getAllByIdSet(Set<Integer> integers);

    List<Drink> getAllBaseByIdSet(Set<Integer> drinkIds);

}
