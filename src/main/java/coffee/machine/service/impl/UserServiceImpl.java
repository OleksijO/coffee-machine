package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.UserDao;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.user.User;
import coffee.machine.service.UserService;

/**
 * This class is an implementation of UserService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserServiceImpl implements UserService {
    static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

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
            User user = userDao.getById(id);
            connection.commitTransaction();
            return user;

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
