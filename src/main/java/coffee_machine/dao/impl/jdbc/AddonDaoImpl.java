package coffee_machine.dao.impl.jdbc;

import java.sql.Connection;
import java.util.List;

import coffee_machine.dao.AddonDao;
import coffee_machine.model.entity.goods.Addon;

public class AddonDaoImpl extends AbstractDao<Addon> implements AddonDao {

	private Connection connection;

	AddonDaoImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public int insert(Addon t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(Addon t) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Addon> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Addon getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub

	}

}
