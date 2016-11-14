package coffee_machine.dao;

import java.util.List;

import coffee_machine.model.entity.user.User;

public interface UserDao extends GenericDao<User> {

	@Override
	int insert(User t);

	@Override
	void update(User t);

	@Override
	List<User> getAll();

	@Override
	User getById(int id);

	@Override
	void deleteById(int id);

	User getUserByLogin(String login);
}
