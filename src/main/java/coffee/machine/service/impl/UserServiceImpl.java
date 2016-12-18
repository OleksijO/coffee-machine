package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AccountDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.UserDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.i18n.message.key.error.ServiceErrorKey;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.user.User;
import coffee.machine.service.UserService;
import coffee.machine.service.logging.ServiceErrorProcessing;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;

/**
 * This class is an implementation of UserService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserServiceImpl implements UserService, ServiceErrorProcessing {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    private static final String TRY_TO_REGISTER_USER_WITH_ALREADY_USED_EMAIL =
            "Try to register user with already used email: ";

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
    public User getById(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao userDao = daoFactory.getUserDao(connection);
            return userDao.getById(id);

        }
    }

    @Override
    public User getUserByLogin(String login) {
        Objects.requireNonNull(login);
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao adminDao = daoFactory.getUserDao(connection);
            return adminDao.getUserByLogin(login);
        }
    }

    @Override
    public List<User> getAllNonAdminUsers() {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao userDao = daoFactory.getUserDao(connection);
            return userDao.getAllNonAdmin();
        }
    }

    @Override
    public void createNewUser(User user) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            AccountDao accountDao = daoFactory.getAccountDao(connection);
            UserDao userDao = daoFactory.getUserDao(connection);
            connection.beginTransaction();
            if (userDao.getUserByLogin(user.getEmail()) != null) {
                logger.error(TRY_TO_REGISTER_USER_WITH_ALREADY_USED_EMAIL + user.getEmail());
                logErrorAndThrowNewServiceException(
                        logger, ServiceErrorKey.USER_WITH_SPECIFIED_EMAIL_ALREADY_REGISTERED);
            }
            Account newAccount = new Account();
            newAccount = accountDao.insert(newAccount);
            user.setAccount(newAccount);
            userDao.insert(user);
            connection.commitTransaction();
        }
    }

}
