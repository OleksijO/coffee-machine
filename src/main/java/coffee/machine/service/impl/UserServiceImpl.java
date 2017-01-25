package coffee.machine.service.impl;

import coffee.machine.dao.AccountDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.UserDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.user.User;
import coffee.machine.model.value.object.user.LoginData;
import coffee.machine.model.value.object.user.RegisterData;
import coffee.machine.service.UserService;
import coffee.machine.service.exception.ServiceException;
import coffee.machine.service.impl.wrapper.GenericService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static coffee.machine.model.entity.user.UserRole.USER;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.ERROR_LOGIN_NO_SUCH_COMBINATION;
import static coffee.machine.service.i18n.message.key.error.ServiceErrorMessageKey.ERROR_REGISTER_USER_WITH_SPECIFIED_EMAIL_ALREADY_REGISTERED;

/**
 * This class is an implementation of UserService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserServiceImpl extends GenericService implements UserService {
    private static final String TRY_TO_REGISTER_USER_WITH_ALREADY_USED_EMAIL =
            "Try to register user with already used email: ";
    private static final String Login_TRY_FAILED_WRONG_EMAIL_OR_PASSWORD =
            "LOGIN TRY FAILED: no such combination of email and password. Entered e-mail: ";


    private UserServiceImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    private static class InstanceHolder {
        private static final UserService instance = new UserServiceImpl(DaoFactoryImpl.getInstance());
    }

    public static UserService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public Optional<User> getById(int id) {

        return executeInNonTransactionalWrapper(daoManager ->
                daoManager
                        .getUserDao()
                        .getById(id)
        );
    }

    @Override
    public User getUserByLoginData(LoginData loginData) {

        Objects.requireNonNull(loginData);
        loginData.encryptPassword();
        return executeInNonTransactionalWrapper(daoManager ->
                daoManager
                        .getUserDao()
                        .getUserByLogin(loginData.getEmail())
                        .filter(user ->
                                user.getPassword().equals(loginData.getPassword()))
                        .orElseThrow(() ->
                                new ServiceException()
                                        .addMessageKey(ERROR_LOGIN_NO_SUCH_COMBINATION)
                                        .addLogMessage(Login_TRY_FAILED_WRONG_EMAIL_OR_PASSWORD + loginData.getEmail()))
        );

    }

    @Override
    public List<User> getAllNonAdminUsers() {

        return executeInNonTransactionalWrapper(daoManager ->
                daoManager.getUserDao()
                        .getAllByRole(USER)
        );
    }

    @Override
    public User createNewUser(RegisterData registerData) {
        Objects.requireNonNull(registerData);
        registerData.encryptPassword();
        User user = getUserFromRegisterData(registerData);

        return executeInTransactionalWrapper(
                daoManager -> {
                    AccountDao accountDao = daoManager.getAccountDao();
                    UserDao userDao = daoManager.getUserDao();
                    checkIfUserAlreadyExists(user.getEmail(), userDao);
                    return createNewUser(user, accountDao, userDao);
                }
        );
    }

    private User getUserFromRegisterData(RegisterData formData) {

        return new User.Builder()
                .setRole(USER)
                .setFullName(formData.getFullName())
                .setEmail(formData.getEmail())
                .setPassword(formData.getPassword())
                .build();
    }

    private void checkIfUserAlreadyExists(String email, UserDao userDao) {
        if (userDao.getUserByLogin(email).isPresent()) {
            throw new ServiceException()
                    .addMessageKey(ERROR_REGISTER_USER_WITH_SPECIFIED_EMAIL_ALREADY_REGISTERED)
                    .addLogMessage(TRY_TO_REGISTER_USER_WITH_ALREADY_USED_EMAIL + email);
        }
    }

    private User createNewUser(User user, AccountDao accountDao, UserDao userDao) {
        Account newAccount = new Account();
        newAccount = accountDao.insert(newAccount);
        user.setAccount(newAccount);
        return userDao.insert(user);
    }
}
