package coffee_machine.dao;

import coffee_machine.model.entity.user.User;

import java.util.List;

public interface UserDao extends GenericDao<User> {

	@Override
	User insert(User user);

	@Override
	void update(User user);

	@Override
	List<User> getAll();

	@Override
	User getById(int id);

	@Override
	void deleteById(int id);

	User getUserByLogin(String login);
}
