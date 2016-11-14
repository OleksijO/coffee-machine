package coffee_machine.dao;

import java.util.List;

import coffee_machine.model.entity.Account;

public interface AccountDao extends GenericDao<Account> {

	@Override
	int insert(Account t);

	@Override
	void update(Account t);

	@Override
	List<Account> getAll();

	@Override
	Account getById(int id);

	@Override
	void deleteById(int id);

}