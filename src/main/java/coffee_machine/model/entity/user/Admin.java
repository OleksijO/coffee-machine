package coffee_machine.model.entity.user;

public class Admin extends AbstractUser {

	@Override
	public String toString() {
		return "Admin [id=" + id + ", login=" + login + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
	
}
