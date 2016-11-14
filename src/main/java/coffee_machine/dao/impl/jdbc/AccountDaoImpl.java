package coffee_machine.dao.impl.jdbc;

import java.sql.Connection;
import java.util.List;

import coffee_machine.dao.AccountDao;
import coffee_machine.model.entity.Account;

public class AccountDaoImpl implements AccountDao {

	private Connection connection;

	AccountDaoImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public int insert(Account t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(Account t) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Account> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub

	}

}
