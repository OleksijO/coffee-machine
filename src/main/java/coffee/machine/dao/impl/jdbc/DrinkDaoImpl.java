package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.DrinkDao;
import coffee.machine.dao.exception.DaoException;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.model.entity.item.ItemType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static coffee.machine.dao.impl.jdbc.ItemDaoHelper.*;

/**
 * This class is the implementation of Drink entity DAO
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class DrinkDaoImpl extends AbstractDao<Drink> implements DrinkDao {
    private static final String SELECT_ALL_DRINKS_WITH_ADDONS_FORMAT = "" +
            " SELECT id, name, price, quantity, type, drink_id AS parent_id " +
            " FROM item " +
            " LEFT JOIN drink_addons ON item.id = drink_addons.addon_id " +
            " %s " +
            " ORDER BY type DESC, id ASC";
    private static final String WHERE_ID_OR_DRINK_ID = " WHERE drink_addons.drink_id = ? OR item.id = ? ";
    private static final String WHERE_ID_IN_LIST_OR_DRINK_ID_IN_LIST =
            " WHERE FIND_IN_SET(drink_addons.drink_id,?)>0 OR FIND_IN_SET(item.id,?)>0 ";
    static final String DB_ERROR_WHILE_INSERTING_ADDONS = "Database error while inserting addons of drink: ";
    private static final String INSERT_ADDON_SQL = "INSERT INTO drink_addons (drink_id, addon_id) VALUES (?,?); ";
    private static final String DELETE_ADDON_FROM_SET_SQL = "DELETE FROM drink_addons WHERE drink_id = ?; ";

    private static final String FIELD_PARENT_ID = "parent_id";
    private static final String DATABASE_ERROR_WHILE_DELETING_DRINK_ADDONS = "Database error wile deleting drink addons: ";
    private static final String DATABASE_ERROR_WHILE_GETTING_ALL_BY_ID = "Database error while getting all drinks by id ";

    private final Connection connection;
    private ItemDaoHelper itemDaoHelper;

    DrinkDaoImpl(Connection connection) {
        this.connection = connection;
        itemDaoHelper = new ItemDaoHelper(connection);
    }


    @Override
    public Drink insert(Drink drink) {
        checkForNull(drink);
        checkIsUnsaved(drink);

        itemDaoHelper.insert(drink);
        insertAddonSet(drink);

        return drink;
    }

    private void insertAddonSet(Drink drink) {
        try (PreparedStatement statementForInsertAddonToSet = connection.prepareStatement(INSERT_ADDON_SQL)) {
            List<Item> addons = drink.getAddons();
            if (!addons.isEmpty()) {
                for (Item addon : addons) {
                    statementForInsertAddonToSet.setInt(1, drink.getId());
                    statementForInsertAddonToSet.setInt(2, addon.getId());
                    statementForInsertAddonToSet.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_INSERTING_ADDONS + drink.toString());
        }
    }


    @Override
    public void updateQuantity(Drink drink) {
        itemDaoHelper.updateQuantity(drink);
    }

    @Override
    public void update(Drink drink) {
        itemDaoHelper.update(drink);
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
            throw new DaoException(e)
                    .addLogMessage(DATABASE_ERROR_WHILE_DELETING_DRINK_ADDONS + drink.toString());
        }
    }

    @Override
    public List<Drink> getAll() {
        try (PreparedStatement statement =
                     connection.prepareStatement(String.format(SELECT_ALL_DRINKS_WITH_ADDONS_FORMAT, ""));
             ResultSet resultSet = statement.executeQuery()) {

            return parseResultSet(resultSet);

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_GETTING_ALL);
        }
    }

    private List<Drink> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Drink> drinkList = new ArrayList<>();
        while (resultSet.next()) {
            Item item = getItemFromCurrentResultSet(resultSet);
            if (item.getType() == ItemType.DRINK) {
                drinkList.add((Drink) item);
            } else {
                Item addon = item;
                int drinkId = resultSet.getInt(FIELD_PARENT_ID);
                drinkList.stream()
                        .filter(drink -> drink.getId() == drinkId)
                        .findFirst()
                        .orElseThrow(IllegalStateException::new)
                        .addAddon(addon);
            }
        }
        return drinkList;
    }

    private Item getItemFromCurrentResultSet(ResultSet resultSet) throws SQLException {
        ItemType type = ItemType.valueOf(resultSet.getString(FIELD_TYPE));
        return new Item.Builder(type)
                .setId(resultSet.getInt(FIELD_ID))
                .setName(resultSet.getString(FIELD_NAME))
                .setPrice(resultSet.getLong(FIELD_PRICE))
                .setQuantity(resultSet.getInt(FIELD_QUANTITY))
                .build();
    }

    @Override
    public Drink getById(int id) {
        try (PreparedStatement statement =
                     connection.prepareStatement(
                             String.format(SELECT_ALL_DRINKS_WITH_ADDONS_FORMAT, WHERE_ID_OR_DRINK_ID))) {

            statement.setInt(1, id);
            statement.setInt(2, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                List<Drink> drinks = parseResultSet(resultSet);
                checkSingleResult(drinks);
                return drinks == null || drinks.isEmpty() ? null : drinks.get(0);
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_GETTING_BY_ID);
        }
    }

    @Override
    public void deleteById(int id) {
        Drink drink = getById(id);
        if (drink == null) {
            return;
        }
        deleteDrinkAddons(drink);
        itemDaoHelper.deleteById(id);
    }

    @Override
    public List<Drink> getAllFromList(List<Drink> drinksToGet) {
        Set<Integer> ids = drinksToGet.stream().map(Item::getId).collect(Collectors.toSet());
        return getAllByIds(ids);
    }

    @Override
    public List<Drink> getAllByIds(Set<Integer> itemIds) {
        checkForNull(itemIds);
        if (itemIds.isEmpty()) {
            return Collections.emptyList();
        }
        String ids = itemDaoHelper.getStringListOf(itemIds);
        try (PreparedStatement statement = connection.prepareStatement(
                String.format(SELECT_ALL_DRINKS_WITH_ADDONS_FORMAT,
                        WHERE_ID_IN_LIST_OR_DRINK_ID_IN_LIST))) {

            statement.setString(1, ids);
            statement.setString(2, ids);

            try (ResultSet resultSet = statement.executeQuery()) {

                return parseResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DATABASE_ERROR_WHILE_GETTING_ALL_BY_ID + itemIds.toString());
        }
    }

    @Override
    public void updateQuantityAllInList(List<Drink> items) {
        items.forEach(this::updateQuantity);
    }

}
