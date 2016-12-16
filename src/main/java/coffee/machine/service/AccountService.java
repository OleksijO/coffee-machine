package coffee.machine.service;

import coffee.machine.model.entity.Account;

/**
 * This class represents account service
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface AccountService {

    /**
     * @param id id of the account
     * @return account or null if there is no account with specified id
     */
    Account getById(int id);

    /**
     * @param userId  user's id whom account is to be found
     * @return account or null if there is no account with specified user's id
     */
    Account getByUserId(int userId);


    /**
     * Adds amount to account of user with specified id.
     *
     * @param userId user's id, whose account has to be updated
     * @param amountToAdd amount to be add to account of user with specified id
     */
    void addToAccountByUserId(int userId, long amountToAdd);
}
