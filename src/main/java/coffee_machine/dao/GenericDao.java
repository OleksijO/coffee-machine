package coffee_machine.dao;

import java.util.List;

public interface GenericDao<T> {

	T insert(T obj);

	void update(T obj);

	List<T> getAll();

	/**
	 * Returns NULL if there is no entity with specified Id
	 */
	T getById(int id);

	void deleteById(int id);
}
