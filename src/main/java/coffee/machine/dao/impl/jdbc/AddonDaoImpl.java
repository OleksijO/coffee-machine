package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.AddonDao;
import coffee.machine.model.entity.item.Item;
import coffee.machine.model.entity.item.ItemType;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class is the implementation of Addon entity DAO
 *
 * Mainly this is an adaptor for ItemDaoImpl, because Addon=Item, but extends it's functionality with some methods
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class AddonDaoImpl extends AbstractDao<Item> implements AddonDao {
    private ItemDaoImpl itemDao;

    AddonDaoImpl(Connection connection) {
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
