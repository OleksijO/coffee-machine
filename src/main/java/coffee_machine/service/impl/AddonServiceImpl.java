package coffee_machine.service.impl;

import coffee_machine.dao.AbstractConnection;
import coffee_machine.dao.AddonDao;
import coffee_machine.dao.DaoFactory;
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
public class AddonServiceImpl implements AddonService {
    private static final Logger logger = Logger.getLogger(AddonServiceImpl.class);

    static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    private static class InstanceHolder {
        private static final AddonService instance = new AddonServiceImpl();
    }

    public static AddonService getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public List<Addon> getAll() {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AddonDao addonDao = daoFactory.getAddonDao(connection);
            connection.beginTransaction();
            List<Addon> addons = addonDao.getAll();
            connection.commitTransaction();
            return (addons == null) ? new ArrayList<>() : addons;

        }
    }

    @Override
    public void refill(Map<Integer, Integer> quantitiesById) {
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AddonDao addonDao = daoFactory.getAddonDao(connection);
            connection.beginTransaction();
            quantitiesById.keySet().forEach(id -> {
                Addon addon = addonDao.getById(id);
                addon.setQuantity(addon.getQuantity() + quantitiesById.get(id));
                addonDao.update(addon);
            });
            connection.commitTransaction();

        }
    }

}
