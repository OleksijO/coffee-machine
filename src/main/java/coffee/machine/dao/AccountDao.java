package coffee.machine.dao;

import coffee.machine.model.entity.Account;

import java.util.Optional;

/**
 * This class represents account entity DAO functionality
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface AccountDao extends GenericDao<Account> {

	/**
	 * @param userId
	 *            user's id, which account is searched for
	 * @return Account entity
	 */
	Optional<Account> getByUserId(int userId);

}