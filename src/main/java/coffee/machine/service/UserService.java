package coffee.machine.service;

import coffee.machine.model.entity.user.User;

import java.util.List;

/**
 * This class represents user service
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface UserService {

    User getById(int id);

    User getUserByLogin(String login);

    List<User> getAllNonAdminUsers();
}
