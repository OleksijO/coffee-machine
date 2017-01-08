package coffee.machine.service;

import coffee.machine.model.entity.Account;
import coffee.machine.model.value.object.CreditsReceipt;

import java.util.Optional;

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
     * @return account of user with specified id
     */
    Optional<Account> getByUserId(int userId);


    /**
     * Adds amount of credits to account of user specified in param.
     *
     * @param receipt Container for user and amount data
     */
    void addCredits(CreditsReceipt receipt);
}
