package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.AdminDao;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.user.Admin;
import coffee_machine.service.AdminService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public class AdminServiceImpl extends AbstractService implements AdminService {
    private static final Logger logger = Logger.getLogger(AdminServiceImpl.class);

    private static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    public AdminServiceImpl() {
        super(logger);
    }

    private static class InstanceHolder {
        private static AdminService instance = new AdminServiceImpl();
    }

    public static AdminService getInstance() {
        return InstanceHolder.instance;
    }

    public Admin create(Admin admin) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            AdminDao adminDao = daoFactory.getAdminDao(connection);
            try {
                connection.beginTransaction();
                adminDao.insert(admin);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndWrapException(e);
            }
            connection.commitTransaction();
        }
        return admin;
    }

    public void update(Admin admin) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            AdminDao adminDao = daoFactory.getAdminDao(connection);
            try {
                connection.beginTransaction();
                adminDao.update(admin);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndWrapException( e);
            }
            connection.commitTransaction();
        }
    }

    public List<Admin> getAll() {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            List<Admin> admins = null;
            AdminDao adminDao = daoFactory.getAdminDao(connection);
            try {
                connection.beginTransaction();
                admins = adminDao.getAll();
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndWrapException(e);
            }
            connection.commitTransaction();
            return (admins == null) ? new ArrayList<>() : admins;
        }
    }

    public Admin getById(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            Admin admin = null;
            AdminDao adminDao = daoFactory.getAdminDao(connection);
            try {
                connection.beginTransaction();
                admin = adminDao.getById(id);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndWrapException(e);
            }
            connection.commitTransaction();
            return admin;
        }

    }

    public void delete(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AdminDao adminDao = daoFactory.getAdminDao(connection);
            try {
                connection.beginTransaction();
                adminDao.deleteById(id);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndWrapException(e);
            }
            connection.commitTransaction();
        }
    }

    @Override
    public Admin getAdminByLogin(String login) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            Admin admin = null;
            AdminDao adminDao = daoFactory.getAdminDao(connection);
            try {
                connection.beginTransaction();
                admin = adminDao.getAdminByLogin(login);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndWrapException( e);
            }
            connection.commitTransaction();
            return admin;
        }
    }
}
