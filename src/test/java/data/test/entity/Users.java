package data.test.entity;

import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.User;

/**
 * This enum represents data for tests, corresponding to sql populate script
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public enum Users {
    A(1, "oleksij.onysymchuk@gmail.com", "495286b908f344a71f0895d3258f5e4a", "Олексій Онисимчук", Accounts.USER_A.getCopy(), false),
    B(2, "user@test.com", "495286b908f344a71f0895d3258f5e4a", "Тестовий користувач", Accounts.USER_B.getCopy(), false),
    C(3, "admin@test.com", "495286b908f344a71f0895d3258f5e4a", "Тестовий адміністратор", null, true);

    public final User user;

    Users(int id, String email, String password, String fullName, Account account, boolean admin) {
        user = new User.Builder()
                .setId(id)
                .setEmail(email)
                .setPassword(password)
                .setFullName(fullName)
                .setAccount(account)
                .setAdmin(admin)
                .build();
    }
}



