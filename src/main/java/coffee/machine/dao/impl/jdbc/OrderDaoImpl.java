package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.OrderDao;
import coffee.machine.dao.exception.DaoException;
import coffee.machine.model.entity.Order;
import coffee.machine.model.entity.product.Drink;
import coffee.machine.model.entity.product.Product;
import coffee.machine.model.entity.product.ProductType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * This class is the implementation of Order entity DAO
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class OrderDaoImpl extends AbstractDao<Order> implements OrderDao {
    private static final String SELECT_ALL_ORDERS_SQL =
            " SELECT orders.id, user_id, date_time, amount, " +
                    "  orders_drink.drink_id, product.name AS drink_name, orders_drink.quantity AS drink_quantity, " +
                    "   addon_id, addon_name, addon_quantity " +
                    "  FROM orders  " +
                    "  LEFT JOIN orders_drink  " +
                    "  ON orders.id=orders_drink.orders_id  " +
                    "  LEFT JOIN product  " +
                    "  ON orders_drink.drink_id = product.id " +
                    "  LEFT JOIN ( " +
                    "    SELECT " +
                    "     orders.id, " +
                    "     orders_drink.drink_id, " +
                    "     addon_id, " +
                    "     product.name          AS addon_name, " +
                    "     orders_addon.quantity AS addon_quantity " +
                    "    FROM orders " +
                    "     LEFT JOIN orders_drink " +
                    "      ON orders.id = orders_drink.orders_id " +
                    "     INNER JOIN orders_addon " +
                    "      ON orders_drink.id = orders_addon.orders_drink_id " +
                    "     LEFT JOIN product " +
                    "      ON orders_addon.addon_id = product.id " +
                    "    ) AS addons " +
                    " ON addons.drink_id = orders_drink.drink_id AND addons.id = orders_drink.orders_id ";
    private static final String INSERT_SQL =
            "INSERT INTO orders (user_id, date_time, amount) VALUES (?,?,?)";
    private static final String INSERT_ORDER_DRINK_SQL =
            "INSERT INTO orders_drink (orders_id, drink_id, quantity ) VALUES (?,?,?)";
    private static final String INSERT_ORDER_DRINK_ADDON_SQL =
            "INSERT INTO orders_addon (orders_drink_id, addon_id, quantity) VALUES (?,?,?)";
    private static final String TABLE = "orders";

    private static final String WHERE_USER_ID = " WHERE user_id = ?";
    private static final String ORDER_BY_DATE_TIME_DESC = " ORDER BY date_time DESC, drink_id ASC, addon_id ASC";
    private static final String SELECT_ALL_BY_USER_ID = SELECT_ALL_ORDERS_SQL + WHERE_USER_ID + ORDER_BY_DATE_TIME_DESC;
    private static final String WHERE_ID = " WHERE orders.id = ?";
    private static final String SELECT_BY_ID = SELECT_ALL_ORDERS_SQL + WHERE_ID;
    private static final String LIMIT_OFFSET_QUANTITY =
            "INNER JOIN (SELECT id FROM orders LIMIT ?,?) AS limitedOrders ON limitedOrders.id = orders.id ";
    private static final String SELECT_ALL_BY_USER_ID_WITH_LIMIT =
            SELECT_ALL_ORDERS_SQL + LIMIT_OFFSET_QUANTITY + WHERE_USER_ID + ORDER_BY_DATE_TIME_DESC;
    private static final String SELECT_COUNT_ORDERS = "SELECT COUNT(id) AS total_count FROM orders";

    private static final String FIELD_USER_ID = "user_id";
    private static final String FIELD_DATE_TIME = "date_time";
    private static final String FIELD_AMOUNT = "amount";
    private static final String FIELD_DRINK_ID = "drink_id";
    private static final String FIELD_ADDON_ID = "addon_id";
    private static final String FIELD_ADDON_QUANTITY = "addon_quantity";
    private static final String FIELD_ADDON_NAME = "addon_name";
    private static final String FIELD_DRINK_QUANTITY = "drink_quantity";
    private static final String FIELD_DRINK_NAME = "drink_name";
    private static final String FIELD_TOTAL_COUNT = "total_count";
    private static final String DATABASE_ERROR_WHILE_GETTING_ALL_BY_USER_ID =
            "Database error while getting all orders by user id = ";

    // state saving for method parseResultSet(ResultSet)
    private ResultSet resultSet;
    private boolean resultSetHasNext = true;
    private int currentOrderId;
    private int currentDrinkId;

    OrderDaoImpl(Connection connection) {
        super(connection);
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

        try (PreparedStatement statementDrink =
                     connection.prepareStatement(INSERT_ORDER_DRINK_SQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement statementAddon = connection.prepareStatement(INSERT_ORDER_DRINK_ADDON_SQL)) {
            for (Drink drink : drinks) {
                statementDrink.setInt(1, order.getId());
                statementDrink.setInt(2, drink.getId());
                statementDrink.setInt(3, drink.getQuantity());
                int ordersDrinkId = executeInsertStatement(statementDrink);
                insertOrderDrinkAddons(ordersDrinkId, drink, statementAddon);
            }
        }
    }

    private void insertOrderDrinkAddons(int ordersDrinkId, Drink drink, PreparedStatement statementAddon)
            throws SQLException {
        for (Product addon : drink.getAddons()) {
            if (addon.getQuantity() > 0) {
                statementAddon.setInt(1, ordersDrinkId);
                statementAddon.setInt(2, addon.getId());
                statementAddon.setInt(3, addon.getQuantity());
                statementAddon.executeUpdate();
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
                     connection.prepareStatement(SELECT_ALL_ORDERS_SQL + ORDER_BY_DATE_TIME_DESC)) {

            try (ResultSet resultSetOrder = statementOrder.executeQuery()) {

                return parseResultSets(resultSetOrder);

            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_GETTING_BY_ID);
        }
    }

    private List<Order> parseResultSets(ResultSet resultSet) throws SQLException {
        this.resultSet = resultSet;
        List<Order> orderList = new ArrayList<>();
        resultSetHasNext = resultSet.next();

        while (resultSetHasNext) {
            Order order = getOrderFromResultSet();
            currentOrderId = order.getId();
            getDrinksForCurrentOrderFromResultSet(resultSet)
                    .forEach(order::addDrink);
            orderList.add(order);
        }
        return orderList;
    }

    private Order getOrderFromResultSet() throws SQLException {
        return new Order.Builder()
                .setId(resultSet.getInt(FIELD_ID))
                .setUserId(resultSet.getInt(FIELD_USER_ID))
                .setDate(toDate(resultSet.getTimestamp(FIELD_DATE_TIME)))
                .setTotalCost(resultSet.getLong(FIELD_AMOUNT))
                .build();
    }

    private List<Drink> getDrinksForCurrentOrderFromResultSet(ResultSet resultSet) throws SQLException {
        List<Drink> drinks = new ArrayList<>();
        while (resultSetHasNext &&
                !isCurrentOrderIdChanged()) {
            Drink drink = getDrinkFromResultSet();
            currentDrinkId = drink.getId();
            List<Product> addons = getAddonsForCurrentDrinkFromResultSet(resultSet);
            drink.addAddons(addons);
            drinks.add(drink);
        }
        return drinks;
    }

    private boolean isCurrentOrderIdChanged() throws SQLException {
        return currentOrderId != resultSet.getInt(FIELD_ID);
    }

    private Drink getDrinkFromResultSet() throws SQLException {
        return new Drink.Builder()
                .setId(resultSet.getInt(FIELD_DRINK_ID))
                .setQuantity(resultSet.getInt(FIELD_DRINK_QUANTITY))
                .setName(resultSet.getString(FIELD_DRINK_NAME))
                .build();
    }

    private List<Product> getAddonsForCurrentDrinkFromResultSet(ResultSet resultSet) throws SQLException {
        List<Product> addons = new ArrayList<>();
        while (resultSetHasNext &&
                !isCurrentOrderIdChanged() &&
                !isCurrentDrinkIdChanged()) {

            int addonId = resultSet.getInt(FIELD_ADDON_ID);
            if (addonId != 0) {
                Product addon = getProductFromResultSet();
                addons.add(addon);
            }
            resultSetHasNext = resultSet.next();
        }
        return addons;
    }

    private boolean isCurrentDrinkIdChanged() throws SQLException {
        return currentDrinkId != resultSet.getInt(FIELD_DRINK_ID);
    }

    private Product getProductFromResultSet() throws SQLException {
        return new Product.Builder(ProductType.ADDON)
                .setId(resultSet.getInt(FIELD_ADDON_ID))
                .setQuantity(resultSet.getInt(FIELD_ADDON_QUANTITY))
                .setName(resultSet.getString(FIELD_ADDON_NAME))
                .build();
    }

    private Date toDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }

    @Override
    public Optional<Order> getById(int id) {
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_BY_ID)) {

            statement.setInt(1, id);
            try (ResultSet resultSetOrder = statement.executeQuery()) {

                List<Order> orderList = parseResultSets(resultSetOrder);
                checkSingleResult(orderList);

                return Optional.ofNullable(orderList.isEmpty() ? null : orderList.get(0));
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DB_ERROR_WHILE_GETTING_BY_ID + id);
        }
    }

    @Override
    public void deleteById(int id) {
        super.deleteById(TABLE, id);
    }

    @Override
    public List<Order> getAllByUserId(int userId) {
        try (PreparedStatement statementOrder =
                     connection.prepareStatement(SELECT_ALL_BY_USER_ID)) {

            statementOrder.setInt(1, userId);
            try (ResultSet resultSet = statementOrder.executeQuery()) {
                return parseResultSets(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DATABASE_ERROR_WHILE_GETTING_ALL_BY_USER_ID + userId);
        }
    }

    @Override
    public Orders getAllByUserId(int userId, int startFrom, int quantity) {
        Orders ordersResult = new Orders();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_COUNT_ORDERS);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                int count = resultSet.getInt(FIELD_TOTAL_COUNT);
                ordersResult.setTotalCount(count);
            }

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DATABASE_ERROR_WHILE_GETTING_ALL_BY_USER_ID + userId);
        }
        try (PreparedStatement statementOrder =
                     connection.prepareStatement(SELECT_ALL_BY_USER_ID_WITH_LIMIT)) {

            statementOrder.setInt(1, startFrom);
            statementOrder.setInt(2, quantity);
            statementOrder.setInt(3, userId);
            try (ResultSet resultSet = statementOrder.executeQuery()) {
                List<Order> orders = parseResultSets(resultSet);
                ordersResult.setOrders(orders);
            }

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(DATABASE_ERROR_WHILE_GETTING_ALL_BY_USER_ID + userId);
        }
        return ordersResult;
    }

}
