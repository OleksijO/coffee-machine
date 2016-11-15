package coffee_machine.dao;

import coffee_machine.model.entity.Account;

import java.util.List;

public interface AccountDao extends GenericDao<Account> {

	@Override
	Account insert(Account account);

	@Override
	void update(Account account);

	@Override
	List<Account> getAll();

	@Override
	Account getById(int id);

	@Override
	void deleteById(int id);

}