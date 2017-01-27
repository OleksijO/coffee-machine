package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.DrinkDao;
import coffee.machine.dao.exception.DaoException;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import coffee.machine.model.entity.product.ProductType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static coffee.machine.dao.impl.jdbc.ProductDaoHelper.*;

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
            " SELECT id, name, price, quantity, type, drink_id AS parent_id " +
            " FROM product " +
            " LEFT JOIN drink_addons ON product.id = drink_addons.addon_id " +
            " %s " +
            " ORDER BY type DESC, id ASC";
    private static final String WHERE_ID_OR_DRINK_ID = " WHERE drink_addons.drink_id = ? OR product.id = ? ";
    private static final String WHERE_ID_IN_LIST_OR_DRINK_ID_IN_LIST =
            " WHERE FIND_IN_SET(drink_addons.drink_id,?)>0 OR FIND_IN_SET(product.id,?)>0 ";
    private static final String INSERT_ADDON_SQL = "INSERT INTO drink_addons (drink_id, addon_id) VALUES (?,?); ";
    private static final String DELETE_ADDON_FROM_SET_SQL = "DELETE FROM drink_addons WHERE drink_id = ?; ";

    private static final String FIELD_PARENT_ID = "parent_id";

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
        insertAddonSet(drink);

        return drink;
    }

    private void insertAddonSet(Drink drink) {
        try (PreparedStatement statementForInsertAddonToSet = connection.prepareStatement(INSERT_ADDON_SQL)) {
            List<Product> addons = drink.getAddons();
            if (!addons.isEmpty()) {
                for (Product addon : addons) {
                    statementForInsertAddonToSet.setInt(1, drink.getId());
                    statementForInsertAddonToSet.setInt(2, addon.getId());
                    statementForInsertAddonToSet.executeUpdate();
                }
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
        insertAddonSet(drink);
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
        List<Drink> drinkList = new ArrayList<>();
        while (resultSet.next()) {
            Product product = getProductFromCurrentResultSet(resultSet);
            if (product.getType() == ProductType.DRINK) {
                drinkList.add((Drink) product);
            } else {
                Product addon = product;
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

    private Product getProductFromCurrentResultSet(ResultSet resultSet) throws SQLException {
        ProductType type = ProductType.valueOf(resultSet.getString(FIELD_TYPE));
        return new Product.Builder(type)
                .setId(resultSet.getInt(FIELD_ID))
                .setName(resultSet.getString(FIELD_NAME))
                .setPrice(resultSet.getLong(FIELD_PRICE))
                .setQuantity(resultSet.getInt(FIELD_QUANTITY))
                .build();
    }

    @Override
    public Optional<Drink> getById(int id) {
        try (PreparedStatement statement =
                     connection.prepareStatement(
                             String.format(SELECT_ALL_DRINKS_WITH_ADDONS_FORMAT, WHERE_ID_OR_DRINK_ID))) {

            statement.setInt(1, id);
            statement.setInt(2, id);

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
                String.format(SELECT_ALL_DRINKS_WITH_ADDONS_FORMAT,
                        WHERE_ID_IN_LIST_OR_DRINK_ID_IN_LIST))) {

            statement.setString(1, ids);
            statement.setString(2, ids);

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
