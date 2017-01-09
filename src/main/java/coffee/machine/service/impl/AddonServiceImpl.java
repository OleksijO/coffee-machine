package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.AddonService;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an implementation of AddonService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AddonServiceImpl implements AddonService {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private AddonServiceImpl() {
    }

    private static class InstanceHolder {
        private static final AddonService instance = new AddonServiceImpl();
    }

    public static AddonService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public List<Item> getAll() {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AddonDao addonDao = daoFactory.getAddonDao(connection);
            List<Item> addons = addonDao.getAll();
            return (addons == null) ? new ArrayList<>() : addons;

        }
    }

    public void setDaoFactory(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
