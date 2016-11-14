package coffee_machine.dao;

public interface DaoFactory {

	AbstractConnection getConnection();

	UserDao getUserDao(AbstractConnection connection);

	AdminDao getAdminDao(AbstractConnection connection);

	DrinkDao getDrinkDao(AbstractConnection connection);

	AddonDao getAddonkDao(AbstractConnection connection);

	CoffeeMachineDao getCoffeeMachine(AbstractConnection connection);

}
