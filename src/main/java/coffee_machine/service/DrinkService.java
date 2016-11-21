package coffee_machine.service;

import coffee_machine.model.entity.goods.Drink;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public interface DrinkService {

	Drink create(Drink drink);

	void update(Drink drink);

	List<Drink> getAll();

	List<Drink> getAllByIdSet(Set<Integer> ids);

	Drink getById(int id);

	void delete(int id);

	void refill(Map<Integer, Integer> quantitiesById);
}
