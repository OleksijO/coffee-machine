package coffee_machine.dao.impl.jdbc;

import java.sql.Connection;
import java.util.List;

import coffee_machine.dao.UserDao;
import coffee_machine.model.entity.user.User;

public class UserDaoImpl implements UserDao {

	private Connection connection;

	UserDaoImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public int insert(User t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(User t) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public User getUserByLogin(String login) {
		// TODO Auto-generated method stub
		return null;
	}

}
