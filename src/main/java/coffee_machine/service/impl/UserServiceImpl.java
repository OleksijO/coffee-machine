package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.UserDao;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.user.User;
import coffee_machine.service.UserService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private static class InstanceHolder {
        private static final UserService instance = new UserServiceImpl();
    }

    public static UserService getInstance() {
        return InstanceHolder.instance;
    }


    public User create(User user) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao userDao = daoFactory.getUserDao(connection);
            connection.beginTransaction();
            userDao.insert(user);
            connection.commitTransaction();
            return user;

        }

    }

    public void update(User user) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao userDao = daoFactory.getUserDao(connection);
            connection.beginTransaction();
            userDao.update(user);
            connection.commitTransaction();

        }
    }

    public List<User> getAll() {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao userDao = daoFactory.getUserDao(connection);
            connection.beginTransaction();
            List<User> users = userDao.getAll();
            connection.commitTransaction();
            return (users == null) ? new ArrayList<>() : users;

        }
    }

    @Override
    public User getById(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao userDao = daoFactory.getUserDao(connection);
            User user = userDao.getById(id);
            connection.commitTransaction();
            return user;

        }
    }

    public void delete(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao userDao = daoFactory.getUserDao(connection);
            connection.beginTransaction();
            userDao.deleteById(id);
            connection.commitTransaction();

        }
    }

    @Override
    public User getUserByLogin(String login) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            UserDao adminDao = daoFactory.getUserDao(connection);
            connection.beginTransaction();
            User user = adminDao.getUserByLogin(login);
            connection.commitTransaction();
            return user;

        }
    }
}
