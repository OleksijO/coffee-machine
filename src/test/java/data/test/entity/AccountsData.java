package data.test.entity;

import coffee.machine.model.entity.Account;

/**
 * This enum represents data for tests, corresponding to sql populate script
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public enum AccountsData {
    COFFEE_MACHINE(1, 0),
    USER_A(2, 9999999999L),
    USER_B(3, 999999);


    private final Account account;

    AccountsData(int id, long amount) {
        account = new Account.Builder()
                .setId(id)
                .setAmount(amount)
                .build();
    }

    public Account getCopy() {
        return new Account.Builder()
                .setId(account.getId())
                .setAmount(account.getAmount())
                .build();
    }
}


