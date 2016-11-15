package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.AdminDao;
import coffee_machine.model.entity.user.Admin;

import java.sql.Connection;
import java.util.List;

public class AdminDaoImpl extends AbstractUserDao<Admin> implements AdminDao {

	private Connection connection;

	AdminDaoImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public int insert(Admin t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(Admin t) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Admin> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Admin getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Admin getAdminByLogin(String login) {
		// TODO Auto-generated method stub
		return null;
	}

}
