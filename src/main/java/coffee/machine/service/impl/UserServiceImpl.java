package coffee.machine.service.impl;

import coffee.machine.service.RegExp;
import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AccountDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.UserDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.Account;
import coffee.machine.model.value.object.user.LoginData;
import coffee.machine.model.value.object.user.RegisterData;
import coffee.machine.model.entity.User;
import coffee.machine.model.security.PasswordEncryptor;
import coffee.machine.service.UserService;
import coffee.machine.service.exception.ServiceException;
import coffee.machine.service.logging.ServiceErrorProcessing;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import static coffee.machine.i18n.message.key.error.CommandErrorKey.ERROR_REGISTER_FULL_NAME_DO_NOT_MATCH_PATTERN;
import static coffee.machine.i18n.message.key.error.ServiceErrorKey.*;

/**
 * This class is an implementation of UserService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserServiceImpl implements UserService, ServiceErrorProcessing {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    private static final String TRY_TO_REGISTER_USER_WITH_ALREADY_USED_EMAIL =
            "Try to register user with already used email: ";
    private static final String TRY_FAILED_WRONG_EMAIL_OR_PASSWORD =
            "LOGIN TRY FAILED: no such combination of email and password. Entered e-mail: ";

    private static final Pattern PATTERN_EMAIL = Pattern.compile(RegExp.REGEXP_EMAIL);
    private static final Pattern PATTERN_PASSWORD = Pattern.compile(RegExp.REGEXP_PASSWORD);
    private static final Pattern PATTERN_FULL_NAME = Pattern.compile(RegExp.REGEXP_FULL_NAME);

    DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private UserServiceImpl() {
    }

    private static class InstanceHolder {
        private static final UserService instance = new UserServiceImpl();
    }

    public static UserService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public Optional<User> getById(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao userDao = daoFactory.getUserDao(connection);
            return userDao.getById(id);

        }
    }

    @Override
    public User getUserByLoginData(LoginData loginData) {

        checkLoginData(loginData);
        loginData.encryptPassword();

        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao adminDao = daoFactory.getUserDao(connection);
            return adminDao.getUserByLogin(loginData.getEmail())
                    .filter(user -> user.getPassword().equals(loginData.getPassword()))
                    .orElseThrow(() ->
                            new ServiceException(ERROR_LOGIN_NO_SUCH_COMBINATION)
                                    .addLogMessage(TRY_FAILED_WRONG_EMAIL_OR_PASSWORD + loginData.getEmail()));

        }
    }

    private void checkLoginData(LoginData loginData) {
        Objects.requireNonNull(loginData);
        if (!isLoginValid(loginData.getEmail())) {
            throw new ServiceException(ERROR_LOGIN_EMAIL_DO_NOT_MATCH_PATTERN);
        }
        if (!isPasswordValid(loginData.getPassword())) {
            throw new ServiceException(ERROR_LOGIN_PASSWORD_DO_NOT_MATCH_PATTERN);
        }
    }

    private boolean isPasswordValid(String password) {
        return checkToPattern(PATTERN_PASSWORD, password);
    }

    private boolean isLoginValid(String email) {
        return checkToPattern(PATTERN_EMAIL, email);
    }

    private boolean checkToPattern(Pattern pattern, String stringToCheck) {
        return (stringToCheck != null) && (pattern.matcher(stringToCheck).matches());
    }

    @Override
    public List<User> getAllNonAdminUsers() {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao userDao = daoFactory.getUserDao(connection);
            return userDao.getAllNonAdmin();
        }
    }

    @Override
    public User createNewUser(RegisterData registerData) {
        checkRegisterData(registerData);
        registerData.encryptPassword();
        User user = getUserFromRegisterData(registerData);

        try (AbstractConnection connection = daoFactory.getConnection()) {
            AccountDao accountDao = daoFactory.getAccountDao(connection);
            UserDao userDao = daoFactory.getUserDao(connection);

            connection.beginTransaction();
            checkIfUserAlreadyExists(user.getEmail(), userDao);
            user = createNewUser(user, accountDao, userDao);
            connection.commitTransaction();
        }
        return user;
    }

    private void checkRegisterData(RegisterData registerData) {
        checkLoginData(registerData);
        if (!isFullNameValid(registerData.getFullName())) {
            throw new ServiceException(ERROR_REGISTER_FULL_NAME_DO_NOT_MATCH_PATTERN);
        }
    }

    private boolean isFullNameValid(String fullName) {
        return checkToPattern(PATTERN_FULL_NAME, fullName);
    }

    private User getUserFromRegisterData(RegisterData formData) {

        return new User.Builder()
                .setAdmin(false)
                .setFullName(formData.getFullName())
                .setEmail(formData.getEmail())
                .setPassword(PasswordEncryptor.encryptPassword(formData.getPassword()))
                .build();
    }

    private void checkIfUserAlreadyExists(String email, UserDao userDao) {
        if (userDao.getUserByLogin(email).isPresent()) {
            throw new ServiceException(USER_WITH_SPECIFIED_EMAIL_ALREADY_REGISTERED)
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
