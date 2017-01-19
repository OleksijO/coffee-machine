package data.test.entity;

import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.user.User;
import coffee.machine.model.entity.user.UserRole;

import static coffee.machine.model.entity.user.UserRole.*;

/**
 * This enum represents data for tests, corresponding to sql populate script
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public enum UsersData {
    A(1, "oleksij.onysymchuk@gmail.com", "495286b908f344a71f0895d3258f5e4a", "Олексій Онисимчук", AccountsData.USER_A.getCopy(), USER),
    B(2, "user@test.com", "495286b908f344a71f0895d3258f5e4a", "Тестовий користувач", AccountsData.USER_B.getCopy(), USER),
    C(3, "admin@test.com", "495286b908f344a71f0895d3258f5e4a", "Тестовий адміністратор", null, ADMIN);

    public final User user;

    UsersData(int id, String email, String password, String fullName, Account account, UserRole role) {
        user = new User.Builder()
                .setId(id)
                .setEmail(email)
                .setPassword(password)
                .setFullName(fullName)
                .setAccount(account)
                .setRole(role)
                .build();
    }
}



