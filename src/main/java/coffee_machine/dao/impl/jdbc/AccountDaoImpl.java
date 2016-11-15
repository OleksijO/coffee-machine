package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.AccountDao;
import coffee_machine.model.entity.Account;

import java.sql.Connection;
import java.util.List;

public class AccountDaoImpl extends AbstractDao<Account> implements AccountDao {
	private static final String INSERT_SQL = "INSERT INTO account (id, amount) VALUES (?, ?);";
	private static final String UPDATE_SQL = "INSERT INTO account (id, amount) VALUES (?, ?);";
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
