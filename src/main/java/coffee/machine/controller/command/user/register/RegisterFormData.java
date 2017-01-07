package coffee.machine.controller.command.user.register;

import coffee.machine.model.entity.LoginData;

/**
 * This class represents DTO for transfer data of user register form and validation result
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class RegisterFormData extends LoginData {
    private String fullName;

    public RegisterFormData(String email, String password, String fullName) {
        super(email, password);
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
