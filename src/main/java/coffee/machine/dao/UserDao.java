package coffee.machine.dao;

import coffee.machine.model.entity.User;

import java.util.List;

/**
 * This class represents user entity DAO functionality
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface UserDao extends GenericDao<User> {

	/**
	 * Searches for saved user and returns it
	 * 
	 * @param login
	 *            user's email field
	 * @return user entity
	 */
	User getUserByLogin(String login);

	/**
	 * @return List of users, which have no admin privileges.
	 */
	List<User> getAllNonAdmin();
}
