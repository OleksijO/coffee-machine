package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.UserDao;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.user.User;
import coffee_machine.service.UserService;
import coffee_machine.service.exception.ServiceException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    private static UserServiceImpl instance;
    private static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    public static UserService getInstance() {
        UserServiceImpl localInstance = instance;
        if (instance == null) {
            synchronized (UserServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserServiceImpl();
                }
            }
        }
        return localInstance;
    }


    @Override
    public User create(User user) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.getUserDao(connection);
            try {
                connection.beginTransaction();
                userDao.insert(user);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
        }
        return user;
    }

    private void logErrorAndThrowWrapperServiceException(String message, Throwable e) {
        logger.error(message, e);
        throw new ServiceException(message, e);
    }


    @Override
    public void update(User user) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            UserDao userDao = daoFactory.getUserDao(connection);
            try {
                connection.beginTransaction();
                userDao.update(user);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
        }
    }

    @Override
    public List<User> getAll() {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            List<User> users = null;
            UserDao userDao = daoFactory.getUserDao(connection);
            try {
                connection.beginTransaction();
                users = userDao.getAll();
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
            return (users == null) ? new ArrayList<>() : users;
        }
    }

    @Override
    public User getById(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            User user = null;
            UserDao userDao = daoFactory.getUserDao(connection);
            try {
                connection.beginTransaction();
                user = userDao.getById(id);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
            return user;
        }

    }

    @Override
    public void delete(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao userDao = daoFactory.getUserDao(connection);
            try {
                connection.beginTransaction();
                userDao.deleteById(id);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
        }
    }

    @Override
    public User getUserByLogin(String login) {
        return null;
    }
}
