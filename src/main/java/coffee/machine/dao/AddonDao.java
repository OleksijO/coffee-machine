package coffee.machine.dao;

import coffee.machine.model.entity.product.Product;

import java.util.List;
import java.util.Set;

/**
 * This class represents addon entity DAO functionality
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface AddonDao extends GenericDao<Product> {

	/**
	 * Updates quantity of all addons in list in corresponding saved entities in
	 * database
	 * 
	 * @param addons
	 *            Entities, which quantities have to be updated.
	 */
	void updateQuantityAllInList(List<Product> addons);

	/**
	 * Updates quantity of corresponding saved entity in database
	 * 
	 * @param addon
	 *            Entities, which quantities have to be updated.
	 */
	void updateQuantity(Product addon);

	/**
	 * @param addons
	 *            Addon list to be retrieved from database in actual state
	 * @return actual list of addons, listed in argument
	 */
	List<Product> getAllFromList(List<Product> addons);

	/**
	 * @param addonIds
	 *            list of ids, which entities has to be retrieved from database
	 * @return actual list of addons, which id's specified in argument
	 */
	List<Product> getAllByIds(Set<Integer> addonIds);
}