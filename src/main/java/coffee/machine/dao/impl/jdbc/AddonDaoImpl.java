package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.AddonDao;
import coffee.machine.model.entity.item.Item;
import coffee.machine.model.entity.item.ItemType;

import java.sql.Connection;
import java.util.*;

/**
 * This class is the implementation of Addon entity DAO
 *
 * Mainly this is an adaptor for ItemDaoHelper, because Addon=Item, but extends it's functionality with some methods
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class AddonDaoImpl extends AbstractDao<Item> implements AddonDao {
    private ItemDaoHelper itemDaoHelper;

    AddonDaoImpl(Connection connection) {
        itemDaoHelper = new ItemDaoHelper(connection);
    }


    @Override
    public Item insert(Item item) {
        return itemDaoHelper.insert(item);
    }

    @Override
    public void updateQuantity(Item item) {
        itemDaoHelper.updateQuantity(item);
    }

    @Override
    public List<Item> getAllFromList(List<Item> addonsToGet) {
        List<Item> updatedAddons = new ArrayList<>();
        addonsToGet.forEach(addon -> {
            if (addon != null) {
                Item updatedAddon = getById(addon.getId());
                if (updatedAddon != null) {
                    updatedAddons.add(updatedAddon);
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
        itemDaoHelper.update(item);
    }

    @Override
    public List<Item> getAll() {
        return itemDaoHelper.getAll(ItemType.ADDON);
    }

    @Override
    public Item getById(int id) {
        return itemDaoHelper.getById(id);
    }

    @Override
    public void deleteById(int id) {
        itemDaoHelper.deleteById(id);
    }

    @Override
    public void updateQuantityAllInList(List<Item> items) {
        itemDaoHelper.updateQuantityAllInList(items);
    }

}
