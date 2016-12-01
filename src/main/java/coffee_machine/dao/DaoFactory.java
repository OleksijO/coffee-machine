package coffee_machine.dao;

public interface DaoFactory {

	AbstractConnection getConnection();

	UserDao getUserDao(AbstractConnection connection);

	DrinkDao getDrinkDao(AbstractConnection connection);

	AddonDao getAddonDao(AbstractConnection connection);

	AccountDao getAccountDao(AbstractConnection connection);

	HistoryRecordDao getHistoryRecordDao(AbstractConnection connection);

}
