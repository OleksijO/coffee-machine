package coffee.machine.model.entity;

import coffee.machine.model.security.PasswordEncryptor;

/**
 * This class represents DTO for transfer data of login form and validation result
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

    public void encryptPassword(){
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

 }
