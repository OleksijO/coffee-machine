package coffee_machine.model.user;

import coffee_machine.model.Account;

public class User extends AbstractUser{
	private Account account;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [accountId=" + account.getId() + ", id=" + id + ", login=" + login + ", firstName=" + firstName
				+ ", lastName=" + lastName + "]";
	}

	
	
}
