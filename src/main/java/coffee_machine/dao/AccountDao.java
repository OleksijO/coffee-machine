package coffee_machine.dao;

import coffee_machine.model.entity.Account;

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
	Account getByUserId(int userId);

}