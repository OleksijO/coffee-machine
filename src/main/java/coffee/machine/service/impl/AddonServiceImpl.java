package coffee.machine.service.impl;

import coffee.machine.dao.AbstractConnection;
import coffee.machine.dao.AddonDao;
import coffee.machine.dao.DaoFactory;
import coffee.machine.dao.impl.jdbc.DaoFactoryImpl;
import coffee.machine.model.entity.item.Item;
import coffee.machine.service.AddonService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is an implementation of AddonService
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class AddonServiceImpl implements AddonService {
    private static final Logger logger = Logger.getLogger(AddonServiceImpl.class);

    private static final String QUANTITIES_BY_ID_SHOULD_CONTAIN_ANY_DATA_GOT_OBJECT =
            "Quantities by id should contain any data. Got object: ";

    DaoFactory daoFactory = DaoFactoryImpl.getInstance();

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

    @Override
    public void refill(Map<Integer, Integer> quantitiesById) {
        if ((quantitiesById == null) || (quantitiesById.size() == 0)) {
            logger.error(
                    QUANTITIES_BY_ID_SHOULD_CONTAIN_ANY_DATA_GOT_OBJECT + quantitiesById);
            return;
        }
        try (AbstractConnection connection = daoFactory.getConnection()) {

            AddonDao addonDao = daoFactory.getAddonDao(connection);
            connection.beginSerializableTransaction();
            List<Item> addonsToUpdate = addonDao.getAllByIds(quantitiesById.keySet());
            addonsToUpdate.forEach(
                    addon -> addon.setQuantity(addon.getQuantity() + quantitiesById.get(addon.getId())));
            addonDao.updateQuantityAllInList(addonsToUpdate);
            connection.commitTransaction();
        }
    }
}
