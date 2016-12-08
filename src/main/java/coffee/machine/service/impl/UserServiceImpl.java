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
    static DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

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
            connection.beginTransaction();
            User user = userDao.getById(id);
            connection.commitTransaction();
            return user;

        }
    }

    @Override
    public User getUserByLogin(String login) {
        Objects.requireNonNull(login);
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao adminDao = daoFactory.getUserDao(connection);
            connection.beginTransaction();
            User user = adminDao.getUserByLogin(login);
            connection.commitTransaction();
            return user;
        }
    }

    @Override
    public List<User> getAllNonAdminUsers() {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao userDao = daoFactory.getUserDao(connection);
            connection.beginTransaction();
            List<User> users = userDao.getAllNonAdmin();
            connection.commitTransaction();
            return users;
        }
    }

    @Override
    public void createNewUser(User user) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            AccountDao accountDao = daoFactory.getAccountDao(connection);
            UserDao userDao = daoFactory.getUserDao(connection);
            connection.beginTransaction();
            if (userDao.getUserByLogin(user.getEmail()) != null) {
                logger.error("Try to register user with already used email: " + user.getEmail());
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
