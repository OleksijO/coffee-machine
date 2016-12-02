package coffee_machine.service;

import coffee_machine.model.entity.user.User;

/**
 * @author oleksij.onysymchuk@gmail.com 15.11.2016.
 */
public interface UserService {

    User getById(int id);

    User getUserByLogin(String login);

}
