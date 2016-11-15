package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.AddonDao;
import coffee_machine.model.entity.goods.Addon;

import java.sql.Connection;
import java.util.List;

public class AddonDaoImpl extends AbstractDao<Addon> implements AddonDao {

	private Connection connection;

	AddonDaoImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Addon insert(Addon addon) {
		return null;
	}

	@Override
	public void update(Addon addon) {

	}

	@Override
	public List<Addon> getAll() {
		return null;
	}

	@Override
	public Addon getById(int id) {
		return null;
	}

	@Override
	public void deleteById(int id) {

	}

	@Override
	public List<Addon> getAllFromList(List<Addon> addons) {
		return null;
	}

	@Override
	public void updateAllInList(List<Addon> addons) {

	}
}
