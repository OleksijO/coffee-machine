package coffee_machine.model.entity.user;

public class Admin extends AbstractUser {
	boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "Admin [id=" + id + ", login=" + login + ", fullName=" + fullName + ", enabled=" + enabled + "]";
	}

}
