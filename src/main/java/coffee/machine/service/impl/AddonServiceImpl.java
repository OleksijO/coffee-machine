package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.AddonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is an implementation of AddonService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AddonServiceImpl implements AddonService {
    static DaoFactory daoFactory = DaoFactoryImpl.getInstance();

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
            connection.beginTransaction();
            List<Item> addons = addonDao.getAll();
            connection.commitTransaction();
            return (addons == null) ? new ArrayList<>() : addons;

        }
    }

    @Override
    public void refill(Map<Integer, Integer> quantitiesById) {
        if ((quantitiesById == null) || (quantitiesById.size() == 0)) {
            return;
        }
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AddonDao addonDao = daoFactory.getAddonDao(connection);
            connection.beginTransaction();
            List<Item> addonsToUpdate = addonDao.getAllByIds(quantitiesById.keySet());
            addonsToUpdate.forEach(
                    addon -> addon.setQuantity(addon.getQuantity() + quantitiesById.get(addon.getId())));
            addonDao.updateQuantityAllInList(addonsToUpdate);
            connection.commitTransaction();

        }
    }
}
