package coffee.machine.service;

import coffee.machine.model.entity.user.User;

/**
 * @author oleksij.onysymchuk@gmail.com 15.11.2016.
 */
public interface UserService {

    User getById(int id);

    User getUserByLogin(String login);

}
