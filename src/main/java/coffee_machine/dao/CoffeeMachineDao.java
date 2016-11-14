package coffee_machine.dao;

import coffee_machine.model.entity.CoffeeMachine;

public interface CoffeeMachineDao {

	CoffeeMachine get();

	void update(CoffeeMachine coffeeMachine);

}
