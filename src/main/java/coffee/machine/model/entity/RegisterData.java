package coffee.machine.model.entity;

/**
 * This class represents value object for transfer data of user register form and validation result
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class RegisterData extends LoginData {
    private String fullName;

    public RegisterData(String email, String password, String fullName) {
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
