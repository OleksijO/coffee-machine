package data.entity;

import coffee_machine.model.entity.Account;
import coffee_machine.model.entity.user.User;

/**
 * Created by oleksij.onysymchuk@gmail on 24.11.2016.
 */
public enum Users {
    A(1, "oleksij.onysymchuk@gmail.com", "495286b908f344a71f0895d3258f5e4a", "Олексій Онисимчук", Accounts.USER_A.account, true),
    B(2, "user@test.com", "495286b908f344a71f0895d3258f5e4a", "Тестовий користувач", Accounts.USER_B.account, false),
    C(3, "admin@test.com", "495286b908f344a71f0895d3258f5e4a", "Тестовий адміністратор", null, true);

    public final User user;

    Users(int id, String email, String password, String fullName, Account account, boolean admin) {
        user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setAccount(account);
        user.setAdmin(admin);
    }
}



