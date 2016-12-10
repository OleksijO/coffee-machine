package coffee.machine.controller.impl.command.helper;

/**
 * Created by oleksij.onysymchuk@gmail on 10.12.2016.
 */
public class RegisterFormData extends LoginFormData {
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
