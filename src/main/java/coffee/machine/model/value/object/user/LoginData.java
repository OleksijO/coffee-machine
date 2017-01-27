package coffee.machine.model.value.object.user;

import coffee.machine.model.security.PasswordEncryptor;

/**
 * This class represents value object for transfer data of login form and validation result
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LoginData {
    private String email;
    private String password;

    private boolean passwordEncrypted;

    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void encryptPassword() {
        if (!passwordEncrypted) {
            password = PasswordEncryptor.encryptPassword(password);
            passwordEncrypted = true;
        }
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "email='" + email + '\'' +
                ", password " + ((password != null) ? "is entered" : "is null") +
                '}';
    }
}
