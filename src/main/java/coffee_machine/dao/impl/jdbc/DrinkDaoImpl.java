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
	public Drink insert(Drink drink) {
		return null;
	}

	@Override
	public void update(Drink drink) {

	}

	@Override
	public List<Drink> getAll() {
		return null;
	}

	@Override
	public Drink getById(int id) {
		return null;
	}

	@Override
	public void deleteById(int id) {

	}

	@Override
	public List<Drink> getAllFromList(List<Drink> baseDrinks) {
		return null;
	}

	@Override
	public void updateAllInList(List<Drink> baseDrinks) {

	}
}
