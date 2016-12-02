package coffee_machine.dao;

import coffee_machine.model.entity.item.Drink;

import java.util.List;
import java.util.Set;

public interface DrinkDao extends GenericDao<Drink> {

	List<Drink> getAllFromList(List<Drink> items);

	void updateQuantityAllInList(List<Drink> items);

	List<Drink> getAllByIds(Set<Integer> itemIds);

	void updateQuantity(Drink drink);
}