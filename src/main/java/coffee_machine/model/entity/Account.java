package coffee_machine.model.entity;

import static coffee_machine.controller.Parameters.DB_MONEY_COEFF;

public class Account {
	private int id;
	private long amount;

	public void withdrow(long amount) {
		this.amount -= amount;
	}

	public void add(long amount) {
		this.amount += amount;
	}

	public long getAmount() {
		return amount;
	}

	public double getRealAmount() {
		return DB_MONEY_COEFF*amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (amount ^ (amount >>> 32));
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (amount != other.amount)
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", amount=" + amount + "]";
	}

}
