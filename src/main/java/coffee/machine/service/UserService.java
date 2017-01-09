package coffee.machine.service;

import coffee.machine.model.value.object.user.LoginData;
import coffee.machine.model.value.object.user.RegisterData;
import coffee.machine.model.entity.User;

import java.util.List;
import java.util.Optional;

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
    Optional<User> getById(int id);

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
     * @param registerData register data of user to be created and saved in data base
     */
    User createNewUser(RegisterData registerData);
}
