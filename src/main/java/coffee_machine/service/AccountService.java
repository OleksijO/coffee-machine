package coffee_machine.service;

import coffee_machine.model.entity.Account;

/**
 * @author oleksij.onysymchuk@gmail.com 15.11.2016.
 */
public interface AccountService {

    Account getById(int id);

    Account getByUserId(int userId);


}
