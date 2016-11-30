package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.AdminDao;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.user.Admin;
import coffee_machine.service.AdminService;
import org.apache.log4j.Logger;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = Logger.getLogger(AdminServiceImpl.class);

    static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private static class InstanceHolder {
        private static final AdminService instance = new AdminServiceImpl();
    }

    public static AdminService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public Admin getAdminByLogin(String login) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AdminDao adminDao = daoFactory.getAdminDao(connection);
            connection.beginTransaction();
            Admin admin = adminDao.getAdminByLogin(login);
            connection.commitTransaction();
            return admin;

        }
    }
}
