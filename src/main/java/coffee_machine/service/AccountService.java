package coffee_machine.service;

import coffee_machine.model.entity.Account;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public interface AccountService {

    Account getById(int id);

    Account getByUserId(int userId);


}
