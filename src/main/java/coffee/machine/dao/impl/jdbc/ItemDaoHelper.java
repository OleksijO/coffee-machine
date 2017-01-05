package coffee.machine.dao.impl.jdbc;

import coffee.machine.model.entity.item.Item;
import coffee.machine.model.entity.item.ItemType;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is common DAO for Item hierarchy.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class ItemDaoHelper extends AbstractDao<Item> {
    private static final Logger logger = Logger.getLogger(ItemDaoHelper.class);

    private static final String SELECT_ALL_SQL =
            "SELECT item.id, name, price, quantity, type FROM item ";
    private static final String UPDATE_SQL =
            "UPDATE item SET name = ?, price = ?, quantity = ?, type = ? WHERE id = ?; ";
    private static final String UPDATE_ITEM_QUANTITY_SQL =
            "UPDATE item SET quantity = ? WHERE id = ?; ";
    private static final String INSERT_SQL =
            "INSERT INTO item (name, price, quantity, type) VALUES (?, ?, ?, ?); ";
    private static final String DELETE_SQL =
            "DELETE FROM drink_addons WHERE drink_id = ? OR addon_id = ?;" +
                    "DELETE FROM item WHERE id = ?; ";
    private static final String WHERE_ITEM_ID = " WHERE item.id = ?";
    private static final String WHERE_ITEM_IS = " WHERE type = '%s'";

    static final String FIELD_NAME = "name";
    static final String FIELD_PRICE = "price";
    static final String FIELD_QUANTITY = "quantity";
    static final String FIELD_TYPE = "type";

    private final Connection connection;

    ItemDaoHelper(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Item insert(Item item) {
        if (item == null) {
            logErrorAndThrowDaoException(logger, CAN_NOT_CREATE_EMPTY);
        }
        if (item.getId() != 0) {
            logErrorAndThrowDaoException(logger, CAN_NOT_CREATE_ALREADY_SAVED);
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, item.getName());
            statement.setLong(2, item.getPrice());
            statement.setInt(3, item.getQuantity());
            statement.setString(4, item.getType().toString());

            int itemId = executeInsertStatement(statement);
            item.setId(itemId);
        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_INSERTING, item, e);
        }
        return item;
    }

    public void updateQuantity(Item item) {
        if (item == null) {
            logErrorAndThrowDaoException(logger, CAN_NOT_UPDATE_EMPTY);
        }
        if (item.getId() == 0) {
            logErrorAndThrowDaoException(logger, CAN_NOT_UPDATE_UNSAVED);
        }
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ITEM_QUANTITY_SQL)) {

            statement.setInt(1, item.getQuantity());
            statement.setInt(2, item.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_UPDATING, item, e);
        }
    }

    @Override
    public void update(Item item) {
        if (item == null) {
            logErrorAndThrowDaoException(logger, CAN_NOT_UPDATE_EMPTY);
        }
        if (item.getId() == 0) {
            logErrorAndThrowDaoException(logger, CAN_NOT_UPDATE_UNSAVED);
        }

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, item.getName());
            statement.setLong(2, item.getPrice());
            statement.setInt(3, item.getQuantity());
            statement.setString(4, item.getType().toString());
            statement.setInt(5, item.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_UPDATING, item, e);
        }
    }

    @Override
    public List<Item> getAll() {
        throw new UnsupportedOperationException();
    }


    List<Item> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Item> itemList = new ArrayList<>();
        while (resultSet.next()) {

            ItemType type = ItemType.valueOf(resultSet.getString(FIELD_TYPE));
            Item item = new Item.Builder(type)
                    .setId(resultSet.getInt(FIELD_ID))
                    .setName(resultSet.getString(FIELD_NAME))
                    .setPrice(resultSet.getLong(FIELD_PRICE))
                    .setQuantity(resultSet.getInt(FIELD_QUANTITY))
                    .build();
            itemList.add(item);
        }
        return itemList;
    }

    @Override
    public Item getById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_ITEM_ID)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Item> itemList = parseResultSet(resultSet);
                checkSingleResult(itemList);
                return itemList == null || itemList.isEmpty() ? null : itemList.get(0);
            }
        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_GETTING_BY_ID, e);
        }
        throw new InternalError(); // STUB for compiler
    }

    @Override
    public void deleteById(int id) {
        Item item = getById(id);
        if (item == null) {
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setInt(1, id);
            statement.setInt(2, id);
            statement.setInt(3, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_DELETING_BY_ID, item, e);
        }
    }


    public void updateQuantityAllInList(List<Item> items) {
        items.forEach(this::updateQuantity);
    }

    public List<Item> getAll(ItemType type) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     SELECT_ALL_SQL + String.format(WHERE_ITEM_IS, type) + ORDER_BY_ID)) {

            return parseResultSet(resultSet);

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_GETTING_ALL, e);
        }
        throw new InternalError(); // STUB for compiler
    }
}
