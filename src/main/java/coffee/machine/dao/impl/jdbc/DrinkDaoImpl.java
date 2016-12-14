package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.DrinkDao;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.model.entity.item.ItemFactory;
import coffee.machine.model.entity.item.ItemType;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static coffee.machine.dao.impl.jdbc.ItemDaoImpl.*;

/**
 * This class is the implementation of Drink entity DAO
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class DrinkDaoImpl extends AbstractDao<Drink> implements DrinkDao {
    private static final Logger logger = Logger.getLogger(DrinkDaoImpl.class);
    private static final String SELECT_ALL_DRINKS_WITH_ADDONS_FORMAT = "" +
            " SELECT id, name, price, quantity, type, drink_id AS parent_id " +
            " FROM item " +
            " LEFT JOIN drink_addons ON item.id = drink_addons.addon_id " +
            " %s " +
            " ORDER BY type DESC, id ASC";
    private static final String WHERE_ID_OR_DRINK_ID = " WHERE drink_id = ? OR item.id = ? ";
    static final String DB_ERROR_WHILE_INSERTING_ADDONS = "Database error while inserting addons of drink: ";
    private static final String INSERT_ADDON_SQL = "INSERT INTO drink_addons (drink_id, addon_id) VALUES (?,?); ";
    private static final String DELETE_ADDON_FROM_SET_SQL = "DELETE FROM drink_addons WHERE drink_id = ?; ";

    private static final String FIELD_PARENT_ID = "parent_id";

    private final Connection connection;
    private ItemDaoImpl itemDao;

    DrinkDaoImpl(Connection connection) {
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
        try (PreparedStatement statement =
                     connection.prepareStatement(String.format(SELECT_ALL_DRINKS_WITH_ADDONS_FORMAT, ""));
             ResultSet resultSet = statement.executeQuery()) {
            return parseResultSet(resultSet);

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_GETTING_ALL, e);
        }
        throw new InternalError(); // STUB for compiler
    }

    private List<Drink> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Drink> drinkList = new ArrayList<>();
        while (resultSet.next()) {
            ItemType type = ItemType.valueOf(resultSet.getString(FIELD_TYPE));
            Item item = ItemFactory.getInstance().getNewInstanceOfType(type);
            item.setType(type);
            item.setId(resultSet.getInt(FIELD_ID));
            item.setName(resultSet.getString(FIELD_NAME));
            item.setPrice(resultSet.getLong(FIELD_PRICE));
            item.setQuantity(resultSet.getInt(FIELD_QUANTITY));
            if (type == ItemType.DRINK) {
                ((Drink) item).setAddons(new TreeSet<>());
                drinkList.add((Drink) item);
            } else {
                Drink drink = getDrinkFromListById(drinkList, resultSet.getInt(FIELD_PARENT_ID));
                drink.getAddons().add(item);
            }
        }
        return drinkList;
    }

    private Drink getDrinkFromListById(List<Drink> drinks, int drinkId) {
        Drink drink;
        Optional<Drink> orderOptional = drinks.stream().filter(d -> d.getId() == drinkId).findFirst();
        if (orderOptional.isPresent()) {
            drink = orderOptional.get();
        } else {
            throw new IllegalStateException();
        }
        return drink;
    }

    @Override
    public Drink getById(int id) {
        try (PreparedStatement statement =
                     connection.prepareStatement(
                             String.format(SELECT_ALL_DRINKS_WITH_ADDONS_FORMAT, WHERE_ID_OR_DRINK_ID) + FOR_UPDATE)) {

            statement.setInt(1, id);
            statement.setInt(2, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                List<Drink> drinks = parseResultSet(resultSet);
                checkSingleResult(drinks);
                return drinks == null || drinks.isEmpty() ? null : drinks.get(0);
            }
        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_GETTING_BY_ID, e);
        }
        throw new InternalError(); // STUB for compiler

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
