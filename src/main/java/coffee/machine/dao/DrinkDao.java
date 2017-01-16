package coffee.machine.dao;

import coffee.machine.model.entity.product.Drink;

import java.util.List;
import java.util.Set;

/**
 * This class represents drink entity DAO functionality
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface DrinkDao extends GenericDao<Drink> {

	/**
	 * @param drinks
	 *            Drink list to be retrieved from database in actual state
	 * @return actual list of drinks, listed in argument
	 */
	List<Drink> getAllFromList(List<Drink> drinks);

	/**
	 * Updates quantity of all drinks in the database
	 * 
	 * @param drinks
	 *            Entities, which quantities have to be updated.
	 */
	void updateQuantityAllInList(List<Drink> drinks);

	/**
	 * @param drinkIds
	 *            list of ids, which entities has to be retrieved from the database
	 * @return actual list of drinks, which id's specified in argument
	 */
	List<Drink> getAllByIds(Set<Integer> drinkIds);

	/**
	 * Updates quantity of drink in the database
	 *
	 * @param drink
	 *            Entity, which quantity has to be updated.
	 */
	void updateQuantity(Drink drink);
}