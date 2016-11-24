package coffee_machine.dao;

import java.util.List;

import coffee_machine.model.entity.goods.Drink;

public interface DrinkDao extends GenericDao<Drink> {

	List<Drink> getAllFromList(List<Drink> baseDrinks);

	void updateAllInList(List<Drink> baseDrinks);

	List<Drink> getAllByIds(List<Integer> drinkIds);

	void updateQuantity(Drink drink);
}