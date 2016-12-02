package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.AddonDao;
import coffee_machine.model.entity.item.Item;
import coffee_machine.model.entity.item.ItemType;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddonDaoImpl extends AbstractDao<Item> implements AddonDao {
    private static final Logger logger = Logger.getLogger(AddonDaoImpl.class);

    private ItemDaoImpl itemDao;

    public AddonDaoImpl(Connection connection) {
        itemDao = new ItemDaoImpl(connection);
    }


    @Override
    public Item insert(Item item) {
        return itemDao.insert(item);
    }

    @Override
    public void updateQuantity(Item item) {
        itemDao.updateQuantity(item);
    }

    @Override
    public List<Item> getAllFromList(List<Item> addons) {
        List<Item> updatedAddons = new ArrayList<>();
        addons.forEach(addon -> {
            if (addon != null) {
                Item item = getById(addon.getId());
                if (item != null) {
                    updatedAddons.add(item);
                }
            }
        });
        return updatedAddons;
    }

    @Override
    public List<Item> getAllByIds(Set<Integer> itemIds) {
        List<Item> addon = new ArrayList<>();
        itemIds.forEach(id -> {
            Item updatedDrink = getById(id);
            if (updatedDrink != null) {
                addon.add(updatedDrink);
            }
        });
        return addon;
    }

    @Override
    public void update(Item item) {
        itemDao.update(item);
    }

    @Override
    public List<Item> getAll() {
        return itemDao.getAll(ItemType.ADDON);
    }

    @Override
    public Item getById(int id) {
        return itemDao.getById(id);
    }

    @Override
    public void deleteById(int id) {
        itemDao.deleteById(id);
    }

    @Override
    public void updateQuantityAllInList(List<Item> items) {
        itemDao.updateQuantityAllInList(items);
    }

}
