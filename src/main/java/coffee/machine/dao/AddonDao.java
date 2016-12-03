package coffee.machine.dao;

import java.util.List;
import java.util.Set;

import coffee.machine.model.entity.item.Item;

/**
 * This class represents addon entity DAO functionality
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface AddonDao extends GenericDao<Item> {

	/**
	 * Updates quantity of all addons in list in corresponding saved entities in
	 * database
	 * 
	 * @param addons
	 *            Entities, which quantities have to be updated.
	 */
	void updateQuantityAllInList(List<Item> addons);

	/**
	 * Updates quantity of corresponding saved entity in database
	 * 
	 * @param addon
	 *            Entities, which quantities have to be updated.
	 */
	void updateQuantity(Item addon);

	/**
	 * @param addons
	 *            Addon list to be retrieved from database in actual state
	 * @return actual list of addons, listed in argument
	 */
	List<Item> getAllFromList(List<Item> addons);

	/**
	 * @param itemIds
	 *            list of ids, which entities has to be retrieved from database
	 * @return
	 */
	List<Item> getAllByIds(Set<Integer> itemIds);
}