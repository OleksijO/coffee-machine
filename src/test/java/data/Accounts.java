package data;

import coffee_machine.model.entity.Account;

/**
 * Created by oleksij.onysymchuk@gmail on 24.11.2016.
 */
public enum Accounts {
    COFFEE_MACHINE(1,0),
    USER_A(2,9999999999L),
    USER_B(3,999999);


    public final Account account;

    Accounts(int id, long amount) {
        account=new Account();
        account.setId(id);
        account.setAmount(amount);
    }
}


