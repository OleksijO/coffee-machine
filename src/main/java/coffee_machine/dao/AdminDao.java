package coffee_machine.dao;

import java.util.List;

import coffee_machine.model.entity.user.Admin;

public interface AdminDao extends GenericDao<Admin> {

	@Override
	int insert(Admin t);

	@Override
	void update(Admin t);

	@Override
	List<Admin> getAll();

	@Override
	Admin getById(int id);

	@Override
	void deleteById(int id);

	Admin getAdminByLogin(String login);
}
