package coffee.machine.service;

import coffee.machine.model.entity.user.User;

import java.util.List;

/**
 * This class represents user service
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public interface UserService {

    /**
     * @param id user's id, whose entity will be returned
     * @return user entity with specified id
     */
    User getById(int id);

    /**
     * @param login user's email (login), whose entity will be returned
     * @return user entity with specified email (login)
     */
    User getUserByLogin(String login);

    /**
      * @return list of user entity with field admin==false
     */
    List<User> getAllNonAdminUsers();

    /**
     * Creates account, sets it to user and saves user entity to database
     *
     * @param user user entity to be saved in data base
     */
    void createNewUser(User user);
}
