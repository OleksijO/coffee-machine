package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.OrderDao;
import coffee.machine.dao.exception.DaoException;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.item.Drink;
import coffee.machine.model.entity.item.Item;
import coffee.machine.model.entity.item.ItemType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is the implementation of Order entity DAO
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class OrderDaoImpl extends AbstractDao<Order> implements OrderDao {
    private static final String SELECT_ALL_ORDERS_SQL =
            " SELECT orders.id, user_id, date_time, amount " +
                    " FROM orders ";
    private static final String SELECT_ALL_ORDER_DRINKS_SQL =
            " SELECT orders.id, " +
                    " orders_drink.drink_id, item.name AS item_name, orders_drink.quantity AS item_quantity " +
                    " FROM orders " +
                    " LEFT JOIN orders_drink " +
                    " ON orders.id=orders_drink.orders_id " +
                    " LEFT JOIN item " +
                    " ON orders_drink.drink_id = item.id ";
    private static final String SELECT_ALL_ORDER_DRINK_ADDONS_SQL =
            " SELECT orders.id, " +
                    " orders_drink.drink_id, " +
                    " addon_id, item.name AS item_name, orders_addon.quantity AS item_quantity " +
                    " FROM orders " +
                    " LEFT JOIN orders_drink " +
                    " ON orders.id=orders_drink.orders_id " +
                    " INNER JOIN orders_addon " +
                    " ON orders_drink.id=orders_addon.orders_drink_id " +
                    " LEFT JOIN item " +
                    " ON orders_addon.addon_id = item.id";
    private static final String INSERT_SQL =
            "INSERT INTO orders (user_id, date_time, amount) VALUES (?,?,?)";
    private static final String INSERT_ORDER_DRINK_SQL =
            "INSERT INTO orders_drink (orders_id, drink_id, quantity ) VALUES (?,?,?)";
    private static final String INSERT_ORDER_DRINK_ADDON_SQL =
            "INSERT INTO orders_addon (orders_drink_id, addon_id, quantity) VALUES (?,?,?)";
    private static final String DELETE_SQL = "DELETE FROM orders WHERE id=? ";

    private static final String WHERE_USER_ID = " WHERE user_id = ?";
    private static final String ORDER_BY_DATE_TIME = " ORDER BY date_time DESC ";
    private static final String WHERE_ID = " WHERE orders.id = ?";

    private static final String FIELD_USER_ID = "user_id";
    private static final String FIELD_DATE_TIME = "date_time";
    private static final String FIELD_AMOUNT = "amount";
    private static final String FIELD_DRINK_ID = "drink_id";
    private static final String FIELD_ADDON_ID = "addon_id";
    private static final String FIELD_ITEM_QUANTITY = "item_quantity";
    private static final String FIELD_ITEM_NAME = "item_name";
    public static final String DATABASE_ERROR_WHILE_GETTING_ALL_BY_USER_ID = "Database error while getting all by user id = ";

    private final Connection connection;

    OrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Order insert(Order order) {
        checkForNull(order);
        checkIsUnsaved(order);

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getUserId());
            statement.setTimestamp(2, toTimestamp(order.getDate()));
            statement.setLong(3, order.getTotalCost());
            int orderId = executeInsertStatement(statement);
            order.setId(orderId);
            insertOrderDrinks(order);

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_INSERTING + order);
        }
        return order;
    }

    private void insertOrderDrinks(Order order) throws SQLException {
        List<Drink> drinks = order.getDrinks();
        if ((drinks == null) || (drinks.isEmpty())) {
            return;
        }
        PreparedStatement statementDrink =
                connection.prepareStatement(INSERT_ORDER_DRINK_SQL, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement statementAddon = connection.prepareStatement(INSERT_ORDER_DRINK_ADDON_SQL);
        for (Drink drink : drinks) {
            statementDrink.setInt(1, order.getId());
            statementDrink.setInt(2, drink.getId());
            statementDrink.setInt(3, drink.getQuantity());
            int ordersDrinkId = executeInsertStatement(statementDrink);
            for (Item addon : drink.getAddons()) {
                if (addon.getQuantity() > 0) {
                    statementAddon.setInt(1, ordersDrinkId);
                    statementAddon.setInt(2, addon.getId());
                    statementAddon.setInt(3, addon.getQuantity());
                    statementAddon.executeUpdate();
                }
            }
        }
    }

    @Override
    public void update(Order order) {
        throw new UnsupportedOperationException();
    }

    private Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    @Override
    public List<Order> getAll() {
        try (PreparedStatement statementOrder =
                     connection.prepareStatement(SELECT_ALL_ORDERS_SQL + ORDER_BY_DATE_TIME);
             PreparedStatement statementDrink =
                     connection.prepareStatement(SELECT_ALL_ORDER_DRINKS_SQL);
             PreparedStatement statementAddon =
                     connection.prepareStatement(SELECT_ALL_ORDER_DRINK_ADDONS_SQL)) {

            try (ResultSet resultSetOrder = statementOrder.executeQuery();
                 ResultSet resultSetDrink = statementDrink.executeQuery();
                 ResultSet resultSetAddon = statementAddon.executeQuery()) {

                return parseResultSets(resultSetOrder, resultSetDrink, resultSetAddon);

            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_GETTING_BY_ID);
        }
    }

    private List<Order> parseResultSets(ResultSet rsOrder, ResultSet rsDrink, ResultSet rsAddon) throws SQLException {
        List<Order> orderList = new ArrayList<>();
        while (rsOrder.next()) {
            Order order = getOrderFromResultSet(rsOrder);
            orderList.add(order);
        }
        while (rsDrink.next()) {
            Order order = getOrderFromListById(orderList, rsDrink.getInt(FIELD_ID));
            Drink drink = getDrinkFromResultSet(rsDrink);
            order.addDrink(drink);
        }
        while (rsAddon.next()) {
            Order order = getOrderFromListById(orderList, rsAddon.getInt(FIELD_ID));
            Drink drink = getDrinkFromListById(order.getDrinks(), rsAddon.getInt(FIELD_DRINK_ID));
            Item addon = getItemFromResultSet(rsAddon);
            drink.getAddons().add(addon);
        }

        return orderList;
    }

    private Order getOrderFromResultSet(ResultSet rsOrder) throws SQLException {
        return new Order.Builder()
                .setId(rsOrder.getInt(FIELD_ID))
                .setUserId(rsOrder.getInt(FIELD_USER_ID))
                .setDate(toDate(rsOrder.getTimestamp(FIELD_DATE_TIME)))
                .setTotalCost(rsOrder.getLong(FIELD_AMOUNT))
                .build();
    }

    private Drink getDrinkFromResultSet(ResultSet rsDrink) throws SQLException {
        return new Drink.Builder()
                .setId(rsDrink.getInt(FIELD_DRINK_ID))
                .setQuantity(rsDrink.getInt(FIELD_ITEM_QUANTITY))
                .setName(rsDrink.getString(FIELD_ITEM_NAME))
                .build();
    }

    private Item getItemFromResultSet(ResultSet rsAddon) throws SQLException {
        return new Item.Builder(ItemType.ADDON)
                .setId(rsAddon.getInt(FIELD_ADDON_ID))
                .setQuantity(rsAddon.getInt(FIELD_ITEM_QUANTITY))
                .setName(rsAddon.getString(FIELD_ITEM_NAME))
                .build();
    }

    private Order getOrderFromListById(List<Order> orderList, int orderId) {
        return orderList.stream()
                .filter(o -> o.getId() == orderId)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private Drink getDrinkFromListById(List<Drink> drinks, int drinkId) {
        return drinks.stream()
                .filter(d -> d.getId() == drinkId)
                .findFirst()
                .orElseThrow(IllegalStateException::new);

    }

    private Date toDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }

    @Override
    public Order getById(int id) {
        try (PreparedStatement statementOrder =
                     connection.prepareStatement(SELECT_ALL_ORDERS_SQL + WHERE_ID);
             PreparedStatement statementDrink =
                     connection.prepareStatement(SELECT_ALL_ORDER_DRINKS_SQL + WHERE_ID);
             PreparedStatement statementAddon =
                     connection.prepareStatement(SELECT_ALL_ORDER_DRINK_ADDONS_SQL + WHERE_ID)) {

            statementOrder.setInt(1, id);
            statementDrink.setInt(1, id);
            statementAddon.setInt(1, id);
            try (ResultSet resultSetOrder = statementOrder.executeQuery();
                 ResultSet resultSetDrink = statementDrink.executeQuery();
                 ResultSet resultSetAddon = statementAddon.executeQuery()) {

                List<Order> orderList = parseResultSets(resultSetOrder, resultSetDrink, resultSetAddon);
                checkSingleResult(orderList);

                return orderList == null || orderList.isEmpty() ? null : orderList.get(0);
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
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_DELETING_BY_ID + id);
        }
    }

    @Override
    public List<Order> getAllByUserId(int userId) {
        try (PreparedStatement statementOrder =
                     connection.prepareStatement(SELECT_ALL_ORDERS_SQL + WHERE_USER_ID + ORDER_BY_DATE_TIME);
             PreparedStatement statementDrink =
                     connection.prepareStatement(SELECT_ALL_ORDER_DRINKS_SQL + WHERE_USER_ID);
             PreparedStatement statementAddon =
                     connection.prepareStatement(SELECT_ALL_ORDER_DRINK_ADDONS_SQL + WHERE_USER_ID)) {

            statementOrder.setInt(1, userId);
            statementDrink.setInt(1, userId);
            statementAddon.setInt(1, userId);
            try (ResultSet resultSetOrder = statementOrder.executeQuery();
                 ResultSet resultSetDrink = statementDrink.executeQuery();
                 ResultSet resultSetAddon = statementAddon.executeQuery()) {
                return parseResultSets(resultSetOrder, resultSetDrink, resultSetAddon);
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DATABASE_ERROR_WHILE_GETTING_ALL_BY_USER_ID +userId);
        }
    }
}
