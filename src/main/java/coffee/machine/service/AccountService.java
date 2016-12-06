package coffee.machine.service;

import coffee.machine.model.entity.Account;

/**
 * This class represents account service
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface AccountService {

    Account getById(int id);

    Account getByUserId(int userId);


    void addToAccountByUserId(int userId, long amountToAdd);
}
