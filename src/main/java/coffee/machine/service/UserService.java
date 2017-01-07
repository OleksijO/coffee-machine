package coffee.machine.service;

import coffee.machine.model.entity.LoginData;
import coffee.machine.model.entity.User;

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
     * @param loginData user's email (login), whose entity will be returned
     * @return user entity with specified email (login)
     */
    User getUserByLoginData(LoginData loginData);

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
