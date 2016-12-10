package coffee.machine.controller.impl.command.helper;

/**
 * Created by oleksij.onysymchuk@gmail on 10.12.2016.
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
