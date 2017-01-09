package coffee.machine.dao;

import java.util.List;
import java.util.Optional;

/**
 * This class represents common DAO functionality
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface GenericDao<T> {

	/**
	 * Inserts object into database
	 * 
	 * @param obj
	 *            instance to insert
	 * @return object with updated field Id
	 */
	T insert(T obj);

	/**
	 * Updates correspondent to object rows in database
	 * 
	 * @param obj
	 *            instance to update
	 */
	void update(T obj);

	/**
	 * @return collection of all instances, saved in db.
	 */
	List<T> getAll();

	/**
	 * Searches for saved instance by id
	 * 
	 * @param id
	 *            instance's field id
	 * @return Optional of entity with specified Id
	 */
	Optional<T> getById(int id);

	/**
	 * Searches for saved instance by id and removes it from BD
	 * 
	 * @param id
	 *            instance's field id
	 */
	void deleteById(int id);
}
