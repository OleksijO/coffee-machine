package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.AddonDao;
import coffee_machine.dao.DaoFactory;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee_machine.model.entity.goods.Addon;
import coffee_machine.service.AddonService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by oleksij.onysymchuk@gmail on 15.11.2016.
 */
public class AddonServiceImpl extends AbstractService implements AddonService {
    private static final Logger logger = Logger.getLogger(AddonServiceImpl.class);

    private static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    public AddonServiceImpl() {
        super(logger);
    }

    private static class InstanceHolder {
        private static AddonService instance = new AddonServiceImpl();
    }

    public static AddonService getInstance() {
        return InstanceHolder.instance;
    }


    public Addon create(Addon addon) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            AddonDao addonDao = daoFactory.getAddonDao(connection);
            try {
                connection.beginTransaction();
                addonDao.insert(addon);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndWrapException(e);
            }
            connection.commitTransaction();
        }
        return addon;
    }

    public void update(Addon addon) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            AddonDao addonDao = daoFactory.getAddonDao(connection);
            try {
                connection.beginTransaction();
                addonDao.update(addon);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndWrapException(e);
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
                logErrorAndWrapException(e);
            }
            connection.commitTransaction();
            return (addons == null) ? new ArrayList<>() : addons;
        }
    }

    public Addon getById(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {
            Addon addon = null;
            AddonDao addonDao = daoFactory.getAddonDao(connection);
            try {
                connection.beginTransaction();
                addon = addonDao.getById(id);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndWrapException(e);
            }
            connection.commitTransaction();
            return addon;
        }

    }

    public void delete(int id) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AddonDao addonDao = daoFactory.getAddonDao(connection);
            try {
                connection.beginTransaction();
                addonDao.deleteById(id);
            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndWrapException(e);
            }
            connection.commitTransaction();
        }
    }

    @Override
    public void refill(Map<Integer, Integer> quantitiesById) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AddonDao addonDao = daoFactory.getAddonDao(connection);

            try {
                connection.beginTransaction();
                quantitiesById.keySet().forEach(id -> {
                    Addon addon = addonDao.getById(id);
                    addon.setQuantity(addon.getQuantity() + quantitiesById.get(id));
                    addonDao.update(addon);
                });

            } catch (DaoException e) {
                connection.rollbackTransaction();
                logErrorAndWrapException(e);
            }

            connection.commitTransaction();
        }
    }

}
