package coffee_machine.dao.impl.jdbc;

import java.sql.Connection;

import coffee_machine.dao.CoffeeMachineDao;
import coffee_machine.model.entity.CoffeeMachine;

public class CoffeeMachineDaoImpl implements CoffeeMachineDao {

	private Connection connection;

	CoffeeMachineDaoImpl(Connection connection) {
		this.connection = connection;
	}

	@Override
	public CoffeeMachine get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(CoffeeMachine coffeeMachine) {
		// TODO Auto-generated method stub

	}

}
