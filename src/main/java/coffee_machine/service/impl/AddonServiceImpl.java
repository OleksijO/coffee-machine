package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.AddonDao;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.goods.Addon;
import coffee_machine.service.AddonService;
import coffee_machine.service.exception.ServiceException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public class AddonServiceImpl implements AddonService {
    private static final Logger logger = Logger.getLogger(AddonServiceImpl.class);
    private static AddonService instance;
    private static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    public static AddonService getInstance() {
        AddonService localInstance = instance;
        if (instance == null) {
            synchronized (AddonServiceImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AddonServiceImpl();
                }
            }
        }
        return localInstance;
    }


    @Override
    public Addon create(Addon addon) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            AddonDao addonDao = daoFactory.getAddonDao(connection);
            try {
                connection.beginTransaction();
                addonDao.insert(addon);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
        }
        return addon;
    }

    private void logErrorAndThrowWrapperServiceException(String message, Throwable e) {
        logger.error(message, e);
        throw new ServiceException(message, e);
    }


    @Override
    public void update(Addon addon) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            AddonDao addonDao = daoFactory.getAddonDao(connection);
            try {
                connection.beginTransaction();
                addonDao.update(addon);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
        }
    }

    @Override
    public List<Addon> getAll() {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            List<Addon> addons = null;
            AddonDao addonDao = daoFactory.getAddonDao(connection);
            try {
                connection.beginTransaction();
                addons = addonDao.getAll();
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
            return (addons == null) ? new ArrayList<>() : addons;
        }
    }

    @Override
    public Addon getById(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            Addon addon = null;
            AddonDao addonDao = daoFactory.getAddonDao(connection);
            try {
                connection.beginTransaction();
                addon = addonDao.getById(id);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
            return addon;
        }

    }

    @Override
    public void delete(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AddonDao addonDao = daoFactory.getAddonDao(connection);
            try {
                connection.beginTransaction();
                addonDao.deleteById(id);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndThrowWrapperServiceException(e.getMessage(), e);
            }
            connection.commitTransaction();
        }
    }

}
