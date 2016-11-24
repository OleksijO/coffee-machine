package data;

import coffee_machine.model.entity.user.Admin;

/**
 * Created by oleksij.onysymchuk@gmail on 24.11.2016.
 */
public enum Admins {
    A(1, "oleksij.onysymchuk@gmail.com", "630d82b1215e611e7aca66237834cd19", "Олексій Онисимчук", false),
    C(3, "admin@test.com", "630d82b1215e611e7aca66237834cd19", "Тестовий адміністратор", true);


    public Admin admin;

    Admins(int id, String email, String password, String fullName, boolean enabled) {
        admin = new Admin();
        admin.setId(id);
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setFullName(fullName);
        admin.setEnabled(enabled);
    }
}


