package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.UserDao;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.user.User;
import coffee_machine.service.UserService;
import org.apache.log4j.Logger;

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
