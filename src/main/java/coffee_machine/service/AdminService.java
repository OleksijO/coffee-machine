package coffee_machine.service;

import coffee_machine.model.entity.user.Admin;

/**
 * Created by oleksij.onysymchuk@gmail on 24.11.2016.
 */
public interface AdminService {

   Admin getAdminByLogin(String login);
}
