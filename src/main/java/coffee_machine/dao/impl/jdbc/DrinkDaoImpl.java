package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.DrinkDao;
import coffee_machine.model.entity.goods.Drink;

import java.sql.Connection;
import java.util.List;

public class DrinkDaoImpl extends AbstractDao<Drink> implements DrinkDao {

	private Connection connection;

	DrinkDaoImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Drink insert(Drink t) {
		// TODO Auto-generated method stub
		return t;
	}

	@Override
	public void update(Drink t) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Drink> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Drink getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(int id) {
		// TODO Auto-generated method stub

	}

}
