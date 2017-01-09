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

    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void encryptPassword() {
        password = PasswordEncryptor.encryptPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "email='" + email + '\'' +
                ", password " + ((password != null) ? "is entered" : "is null") +
                '}';
    }
}
