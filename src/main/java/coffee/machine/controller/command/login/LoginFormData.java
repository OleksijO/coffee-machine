package coffee.machine.controller.command.login;

/**
 * This class represents DTO for transfer data of login form and validation result
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class LoginFormData {
    private String email;
    private String password;
    private boolean valid = false;

    public LoginFormData(String email, String password) {
        this.email = email;
        this.password = password;
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

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
