package coffee_machine.dao;

import java.util.List;

public interface GenericDao<T> {

	int insert(T t);

	void update(T t);

	List<T> getAll();

	/**
	 * Returns NULL if there is now entity with specified Id
	 */
	T getById(int id);

	void deleteById(int id);
}
