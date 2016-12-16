package data.test.entity;

import coffee.machine.model.entity.Account;

/**
 * This enum represents data for tests, corresponding to sql populate script
 *
 * @author oleksij.onysymchuk@gmail.com
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

    public Account getCopy(){
        Account acc= new Account();
        acc.setId(account.getId());
        acc.setAmount(account.getAmount());
        return acc;
    }
}


