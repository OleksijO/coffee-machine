package coffee.machine.dao.impl.jdbc;

import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.model.entity.item.ItemType;
import coffee.machine.dao.DrinkDao;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.List;
import java.util.Set;

/**
 * This class is the implementation of Drink entity DAO
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class DrinkDaoImpl extends AbstractDao<Drink> implements DrinkDao {
    private static final Logger logger = Logger.getLogger(DrinkDaoImpl.class);
    static final String DB_ERROR_WHILE_INSERTING_ADDONS = "Database error while inserting addons of drink: ";
    private static final String SELECT_ADDON_SET_SQL = "SELECT id, name, price, quantity, type " + "FROM item "
            + "INNER JOIN drink_addons ON addon_id=item.id " + "WHERE drink_id = ? ;";
    private static final String INSERT_ADDON_SQL = "INSERT INTO drink_addons (drink_id, addon_id) VALUES (?,?); ";
    private static final String DELETE_ADDON_FROM_SET_SQL = "DELETE FROM drink_addons WHERE drink_id = ?; ";

    private static final String DB_ERROR_WHILE_GETTING_ADDON_SET_OF_DRINK_WITH_ID =
            "Database error while getting addon set of drink with id=";

    private final Connection connection;
    private ItemDaoImpl itemDao;

    public DrinkDaoImpl(Connection connection) {
        this.connection = connection;
        itemDao = new ItemDaoImpl(connection);
    }


    @Override
    public Drink insert(Drink drink) {
        if (drink == null) {
            logErrorAndThrowDaoException(logger, CAN_NOT_CREATE_EMPTY);
        }
        if (drink.getId() != 0) {
            logErrorAndThrowDaoException(logger, CAN_NOT_CREATE_ALREADY_SAVED);
        }

        itemDao.insert(drink);
        insertAddonSet(drink); // Addons as Items should already exist in table Item.

        return drink;
    }

    private void insertAddonSet(Drink drink) {
        try (PreparedStatement statementForInsertAddonToSet = connection.prepareStatement(INSERT_ADDON_SQL)) {
            Set<Item> addons = drink.getAddons();
            if ((addons != null) && (!addons.isEmpty())) {
                for (Item addon : addons) {
                    statementForInsertAddonToSet.setInt(1, drink.getId());
                    statementForInsertAddonToSet.setInt(2, addon.getId());
                    statementForInsertAddonToSet.executeUpdate();
                }
            }
        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_INSERTING_ADDONS, drink, e);
        }
    }


    @Override
    public void updateQuantity(Drink drink) {
        itemDao.updateQuantity(drink);
    }

    @Override
    public void update(Drink drink) {
        itemDao.update(drink);
        updateDrinkAddons(drink);
    }

    private void updateDrinkAddons(Drink drink) {
        deleteDrinkAddons(drink);
        insertAddonSet(drink);
    }

    private void deleteDrinkAddons(Drink drink) {
        try (PreparedStatement statementForDeleteAddonsFromSet =
                     connection.prepareStatement(DELETE_ADDON_FROM_SET_SQL)) {

            statementForDeleteAddonsFromSet.setInt(1, drink.getId());
            statementForDeleteAddonsFromSet.executeUpdate();

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_UPDATING, drink, e);
        }
    }

    @Override
    public List<Drink> getAll() {

        List<Item> items = itemDao.getAll(ItemType.DRINK);
        List<Drink> drinks = new ArrayList<>();
        items.forEach(item -> {
            Drink drinkRef = (Drink) item;
            drinkRef.setAddons(getAddonSet(drinkRef.getId()));
            drinks.add(drinkRef);
        });
        return drinks;
    }

    private Set<Item> getAddonSet(int drinkId) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ADDON_SET_SQL)) {

            statement.setInt(1, drinkId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return new TreeSet<>(itemDao.parseResultSet(resultSet));
            }
        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_GETTING_ADDON_SET_OF_DRINK_WITH_ID, drinkId, e);
        }
        throw new InternalError(); // STUB for compiler
    }

    @Override
    public Drink getById(int id) {
        Drink drink = (Drink) itemDao.getById(id);
        if (drink != null) {
            drink.setAddons(getAddonSet(id));
        }
        return drink;

    }

    @Override
    public void deleteById(int id) {
        Drink drink = getById(id);
        if (drink == null) {
            return;
        }
        deleteDrinkAddons(drink);
        itemDao.deleteById(id);
    }

    @Override
    public List<Drink> getAllFromList(List<Drink> drinksToGet) {
        List<Drink> items = new ArrayList<>();
        drinksToGet.forEach(drink -> {
            if (drink != null) {
                Drink updatedDrink = getById(drink.getId());
                if (updatedDrink != null) {
                    items.add(updatedDrink);
                }
            }
        });
        return items;
    }

    @Override
    public List<Drink> getAllByIds(Set<Integer> itemIds) {
        List<Drink> drinks = new ArrayList<>();
        itemIds.forEach(id -> {
            Drink updatedDrink = getById(id);
            if (updatedDrink != null) {
                drinks.add(updatedDrink);
            }
        });
        return drinks;
    }

    @Override
    public void updateQuantityAllInList(List<Drink> items) {
        items.forEach(this::updateQuantity);
    }

}
