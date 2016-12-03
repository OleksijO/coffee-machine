package coffee.machine.dao;

import java.util.List;
import java.util.Set;

import coffee.machine.model.entity.item.Drink;

/**
 * This class represents drink entity DAO functionality
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface DrinkDao extends GenericDao<Drink> {

	List<Drink> getAllFromList(List<Drink> drinks);

	/**
	 * Updates quantity of all drinks in list in corresponding saved entities in
	 * database
	 * 
	 * @param drinks
	 *            Entities, which quantities have to be updated.
	 */
	void updateQuantityAllInList(List<Drink> drinks);

	List<Drink> getAllByIds(Set<Integer> drinkIds);

	void updateQuantity(Drink drink);
}