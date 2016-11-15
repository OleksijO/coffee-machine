package coffee_machine.service;

import coffee_machine.model.entity.user.User;

import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public interface UserService {

    User create(User user);

    void update(User user);

    List<User> getAll();

    User getById(int id);

    void delete(int id);

    User getUserByLogin(String login);

}
