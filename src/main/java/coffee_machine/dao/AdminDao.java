package coffee_machine.dao;

import coffee_machine.model.entity.user.Admin;

public interface AdminDao extends GenericDao<Admin> {

	Admin getAdminByLogin(String login);
}
