package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.DrinkDao;
import coffee.machine.dao.exception.DaoException;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static coffee.machine.dao.impl.jdbc.ProductDaoHelper.*;
import static coffee.machine.model.entity.product.ProductType.ADDON;
import static coffee.machine.model.entity.product.ProductType.DRINK;

/**
 * This class is the implementation of Drink entity DAO
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class DrinkDaoImpl extends AbstractDao<Drink> implements DrinkDao {
    private static final String LOG_MESSAGE_DATABASE_ERROR_WHILE_DELETING_DRINK_ADDONS =
            "Database error wile deleting drink addons: ";
    private static final String LOG_MESSAGE_DATABASE_ERROR_WHILE_GETTING_ALL_BY_ID =
            "Database error while getting all drinks by id ";
    private static final String LOG_MESSAGE_DB_ERROR_WHILE_INSERTING_ADDONS =
            "Database error while inserting addons of drink: ";

    private static final String SELECT_ALL_DRINKS_WITH_ADDONS_FORMAT = "" +
            " SELECT  id, name, price, quantity, " +
            "         addons_id, addon_name, addon_price, addon_quantity " +
            " FROM product " +
            " LEFT JOIN drink_addons " +
            " ON product.id = drink_addons.drink_id " +
            " LEFT JOIN ( " +
            "              SELECT " +
            "                id    AS addons_id, " +
            "                name  AS addon_name, " +
            "                price AS addon_price, " +
            "                quantity addon_quantity " +
            "              FROM product " +
            "              WHERE type = '" + ADDON + "' " +
            "            ) AS addons " +
            " ON addons.addons_id = drink_addons.addon_id " +
            " WHERE type='" + DRINK + "' " +
            " %s " +
            " ORDER by id ASC, addons_id ASC ";
    private static final String AND_ID_IS = " AND id = ? ";
    private static final String AND_ID_IN_LIST =
            " AND FIND_IN_SET(id,?) > 0 ";
    private static final String INSERT_ADDON_SQL = "INSERT INTO drink_addons (drink_id, addon_id) VALUES (?,?); ";
    private static final String DELETE_ADDON_FROM_SET_SQL = "DELETE FROM drink_addons WHERE drink_id = ?; ";

    private static final String FIELD_DRINK_ID = FIELD_ID;
    private static final String FIELD_DRINK_NAME = FIELD_NAME;
    private static final String FIELD_DRINK_PRICE = FIELD_PRICE;
    private static final String FIELD_DRINK_QUANTITY = FIELD_QUANTITY;
    private static final String FIELD_ADDON_ID = "addons_" + FIELD_ID;
    private static final String FIELD_ADDON_NAME = "addon_" + FIELD_NAME;
    private static final String FIELD_ADDON_PRICE = "addon_" + FIELD_PRICE;
    private static final String FIELD_ADDON_QUANTITY = "addon_" + FIELD_QUANTITY;

    private ProductDaoHelper productDaoHelper;

    DrinkDaoImpl(Connection connection) {
        super(connection);
        productDaoHelper = new ProductDaoHelper(connection);
    }


    @Override
    public Drink insert(Drink drink) {
        checkForNull(drink);
        checkIsUnsaved(drink);

        productDaoHelper.insert(drink);
        insertAddonSetOfDrink(drink);

        return drink;
    }

    private void insertAddonSetOfDrink(Drink drink) {
        List<Product> addons = drink.getAddons();
        if (addons.isEmpty()) {
            return;
        }
        try (PreparedStatement statementForInsertAddonToSet = connection.prepareStatement(INSERT_ADDON_SQL)) {
            for (Product addon : addons) {
                statementForInsertAddonToSet.setInt(1, drink.getId());
                statementForInsertAddonToSet.setInt(2, addon.getId());
                statementForInsertAddonToSet.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_INSERTING_ADDONS + drink.toString());
        }
    }


    @Override
    public void updateQuantity(Drink drink) {
        productDaoHelper.updateQuantity(drink);
    }

    @Override
    public void update(Drink drink) {
        productDaoHelper.update(drink);
        updateDrinkAddons(drink);
    }

    private void updateDrinkAddons(Drink drink) {
        deleteDrinkAddons(drink);
        insertAddonSetOfDrink(drink);
    }

    private void deleteDrinkAddons(Drink drink) {
        try (PreparedStatement statementForDeleteAddonsFromSet =
                     connection.prepareStatement(DELETE_ADDON_FROM_SET_SQL)) {

            statementForDeleteAddonsFromSet.setInt(1, drink.getId());
            statementForDeleteAddonsFromSet.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DATABASE_ERROR_WHILE_DELETING_DRINK_ADDONS + drink.toString());
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
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_GETTING_ALL);
        }
    }

    private List<Drink> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Drink> drinks = new ArrayList<>();
        int currentDrinkId = -1;
        Drink drink = null;
        while (resultSet.next()) {
            if (isCurrentDrinkIdChanged(currentDrinkId, resultSet)) {
                drink = getDrinkFromResultSet(resultSet);
                drinks.add(drink);
                currentDrinkId = drink.getId();
            }
            addAddonFromResultSetToDrinkIfPresent(resultSet, drink);
        }
        return drinks;
    }

    private boolean isCurrentDrinkIdChanged(int currentDrinkId, ResultSet resultSet) throws SQLException {
        return currentDrinkId != resultSet.getInt(FIELD_DRINK_ID);
    }

    private Drink getDrinkFromResultSet(ResultSet resultSet) throws SQLException {
        return new Drink.Builder()
                .setId(resultSet.getInt(FIELD_DRINK_ID))
                .setName(resultSet.getString(FIELD_DRINK_NAME))
                .setPrice(resultSet.getLong(FIELD_DRINK_PRICE))
                .setQuantity(resultSet.getInt(FIELD_DRINK_QUANTITY))
                .build();
    }

    private void addAddonFromResultSetToDrinkIfPresent(ResultSet resultSet, Drink drink) throws SQLException {
        if (drink == null) {
            throw new IllegalStateException();
        }
        if (resultSet.getString(FIELD_ADDON_NAME) != null) {
            drink.addAddon(getAddonFromResultSet(resultSet));
        }
    }

    private Product getAddonFromResultSet(ResultSet resultSet) throws SQLException {
        return new Product.Builder()
                .setId(resultSet.getInt(FIELD_ADDON_ID))
                .setName(resultSet.getString(FIELD_ADDON_NAME))
                .setPrice(resultSet.getLong(FIELD_ADDON_PRICE))
                .setQuantity(resultSet.getInt(FIELD_ADDON_QUANTITY))
                .build();
    }

    @Override
    public Optional<Drink> getById(int id) {
        try (PreparedStatement statement =
                     connection.prepareStatement(
                             String.format(SELECT_ALL_DRINKS_WITH_ADDONS_FORMAT, AND_ID_IS))) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                List<Drink> drinks = parseResultSet(resultSet);
                checkSingleResult(drinks);
                return Optional.ofNullable(drinks.isEmpty() ? null : drinks.get(0));
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_GETTING_BY_ID);
        }
    }

    @Override
    public void deleteById(int id) {
        Drink drink = getById(id).orElse(null);
        if (drink == null) {
            return;
        }
        deleteDrinkAddons(drink);
        productDaoHelper.deleteById(id);
    }

    @Override
    public List<Drink> getAllFromList(List<Drink> drinksToGet) {
        Set<Integer> ids = drinksToGet.stream().map(Product::getId).collect(Collectors.toSet());
        return getAllByIds(ids);
    }

    @Override
    public List<Drink> getAllByIds(Set<Integer> productIds) {
        checkForNull(productIds);
        if (productIds.isEmpty()) {
            return Collections.emptyList();
        }
        String ids = productDaoHelper.getStringListOf(productIds);
        try (PreparedStatement statement = connection.prepareStatement(
                String.format(SELECT_ALL_DRINKS_WITH_ADDONS_FORMAT, AND_ID_IN_LIST))) {

            statement.setString(1, ids);

            try (ResultSet resultSet = statement.executeQuery()) {

                return parseResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DATABASE_ERROR_WHILE_GETTING_ALL_BY_ID + productIds.toString());
        }
    }

    @Override
    public void updateQuantityAllInList(List<Drink> products) {
        products.forEach(this::updateQuantity);
    }

}
