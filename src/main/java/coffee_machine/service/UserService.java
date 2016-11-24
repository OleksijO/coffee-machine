package coffee_machine.service;

import coffee_machine.model.entity.user.User;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public interface UserService {

    User getById(int id);

    User getUserByLogin(String login);

}
