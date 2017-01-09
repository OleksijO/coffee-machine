package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.exception.DaoException;
import coffee.machine.model.entity.item.Item;
import coffee.machine.model.entity.item.ItemType;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is common DAO for Item hierarchy.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class ItemDaoHelper extends AbstractDao<Item> {
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
    private static final String WHERE_ITEM_ID_IN_LIST = " WHERE FIND_IN_SET(item.id,?)>0 ";

    private static final String DB_ERROR_WHILE_GETTING_ALL_BY_ID = "Database error while getting all items by id ";


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

        checkForNull(item);
        checkIsUnsaved(item);

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, item.getName());
            statement.setLong(2, item.getPrice());
            statement.setInt(3, item.getQuantity());
            statement.setString(4, item.getType().toString());

            int itemId = executeInsertStatement(statement);
            item.setId(itemId);
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_INSERTING + item.toString());
        }
        return item;
    }

    public void updateQuantity(Item item) {

        checkForNull(item);
        checkIsSaved(item);

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ITEM_QUANTITY_SQL)) {

            statement.setInt(1, item.getQuantity());
            statement.setInt(2, item.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_UPDATING + item.toString());
        }
    }

    @Override
    public void update(Item item) {

        checkForNull(item);
        checkIsSaved(item);

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, item.getName());
            statement.setLong(2, item.getPrice());
            statement.setInt(3, item.getQuantity());
            statement.setString(4, item.getType().toString());
            statement.setInt(5, item.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_UPDATING + item.toString());
        }
    }

    @Override
    public List<Item> getAll() {
        throw new UnsupportedOperationException();
    }


    private List<Item> parseResultSet(ResultSet resultSet) throws SQLException {
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
    public Optional<Item> getById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_ITEM_ID)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Item> itemList = parseResultSet(resultSet);
                checkSingleResult(itemList);
                return Optional.ofNullable(itemList.isEmpty() ? null : itemList.get(0));
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_GETTING_BY_ID + id);
        }
    }

    @Override
    public void deleteById(int id) {

        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setInt(1, id);
            statement.setInt(2, id);
            statement.setInt(3, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_DELETING_BY_ID + id);
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
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_GETTING_ALL);
        }
    }


    public List<Item> getAllByIds(Set<Integer> itemIds) {
        if (itemIds.isEmpty()) {
            return Collections.emptyList();
        }
        String ids = getStringListOf(itemIds);
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_ITEM_ID_IN_LIST)) {

            statement.setString(1, ids);

            try (ResultSet resultSet = statement.executeQuery()) {

                return parseResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_GETTING_ALL_BY_ID + itemIds);
        }
    }

    String getStringListOf(Set<Integer> itemIds) {
        return itemIds.stream().map(Object::toString).collect(Collectors.joining(","));
    }

}
