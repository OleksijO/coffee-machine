package coffee_machine.dao;

import coffee_machine.model.entity.user.Admin;

import java.util.List;

public interface AdminDao extends GenericDao<Admin> {

	@Override
	Admin insert(Admin admin);

	@Override
	void update(Admin admin);

	@Override
	List<Admin> getAll();

	@Override
	Admin getById(int id);

	@Override
	void deleteById(int id);

	Admin getAdminByLogin(String login);
}
