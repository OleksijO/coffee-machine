package coffee_machine.dao.impl.jdbc;

import coffee_machine.model.entity.item.Drink;
import coffee_machine.model.entity.item.Item;
import coffee_machine.model.entity.item.ItemType;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDaoImpl extends AbstractDao<Item> {
    private static final Logger logger = Logger.getLogger(ItemDaoImpl.class);

    private static final String SELECT_ALL_SQL =
            "SELECT item.id, name, price, quantity, type FROM item ";
    private static final String UPDATE_SQL =
            "UPDATE item SET name = ?, price = ?, quantity = ?, type = ? WHERE id = ?; ";
    private static final String UPDATE_ITEM_QUANTITY_SQL =
            "UPDATE item SET quantity = ? WHERE id = ?; ";
    private static final String INSERT_SQL =
            "INSERT INTO item (name, price, quantity, type) VALUES (?, ?, ?, ?); ";
    private static final String DELETE_SQL = "DELETE FROM item WHERE id = ?; ";
    private static final String WHERE_ITEM_ID = " WHERE item.id = ?";
    private static final String WHERE_ITEM_IS = " WHERE type = '%s'";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_QUANTITY = "quantity";
    private static final String FIELD_TYPE = "type";

    private final Connection connection;

    public ItemDaoImpl(Connection connection) {
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

        try (PreparedStatement statemement = connection.prepareStatement(INSERT_SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            statemement.setString(1, item.getName());
            statemement.setLong(2, item.getPrice());
            statemement.setInt(3, item.getQuantity());
            statemement.setString(4, item.getType().toString());

            int itemId = executeInsertStatement(statemement);
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
            Item item;
            ItemType type = ItemType.valueOf(resultSet.getString(FIELD_TYPE));
            if (type == ItemType.DRINK) {
                item = new Drink();
            } else {
                item = new Item();
            }
            item.setId(resultSet.getInt(FIELD_ID));
            item.setName(resultSet.getString(FIELD_NAME));
            item.setPrice(resultSet.getLong(FIELD_PRICE));
            item.setQuantity(resultSet.getInt(FIELD_QUANTITY));
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
            statement.executeUpdate();

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_DELETING_BY_ID, item, e);
        }
    }


    public void updateQuantityAllInList(List<Item> items) {
        items.forEach(this::updateQuantity);
    }

    public List<Item> getAll(ItemType type) {
        logger.info(SELECT_ALL_SQL + WHERE_ITEM_IS + type);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL + String.format(WHERE_ITEM_IS, type))) {

            return parseResultSet(resultSet);

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_GETTING_ALL, e);
        }
        throw new InternalError(); // STUB for compiler
    }
}
