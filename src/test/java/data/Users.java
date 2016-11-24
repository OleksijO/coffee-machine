package data;

import coffee_machine.model.entity.Account;
import coffee_machine.model.entity.user.User;

import static data.Accounts.USER_A;
import static data.Accounts.USER_B;

/**
 * Created by oleksij.onysymchuk@gmail on 24.11.2016.
 */
public enum Users {
    A(1, "oleksij.onysymchuk@gmail.com", "630d82b1215e611e7aca66237834cd19", "Олексій Онисимчук", USER_A.account),
    B(2, "user@test.com", "630d82b1215e611e7aca66237834cd19", "Тестовий користувач", USER_B.account);

    public User user;

    Users(int id, String email, String password, String fullName, Account account) {
        user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setAccount(account);
    }
}



