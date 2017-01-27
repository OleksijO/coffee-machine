package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.exception.DaoException;
import coffee.machine.model.entity.product.Product;
import coffee.machine.model.entity.product.ProductType;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is common DAO for Product hierarchy.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class ProductDaoHelper extends AbstractDao<Product> {
    static final String FIELD_NAME = "name";
    static final String FIELD_PRICE = "price";
    static final String FIELD_QUANTITY = "quantity";
    static final String FIELD_TYPE = "type";

    private static final String LOG_MESSAGE_DB_ERROR_WHILE_GETTING_ALL_BY_ID = "Database error while getting all products by id ";

    private static final String SELECT_ALL_SQL =
            "SELECT product.id, name, price, quantity, type FROM product ";
    private static final String UPDATE_SQL =
            "UPDATE product SET name = ?, price = ?, quantity = ?, type = ? WHERE id = ?; ";
    private static final String UPDATE_PRODUCT_QUANTITY_SQL =
            "UPDATE product SET quantity = ? WHERE id = ?; ";
    private static final String INSERT_SQL =
            "INSERT INTO product (name, price, quantity, type) VALUES (?, ?, ?, ?); ";
    private static final String DELETE_SQL =
            "DELETE FROM drink_addons WHERE drink_id = ? OR addon_id = ?;" +
                    "DELETE FROM product WHERE id = ?; ";
    private static final String WHERE_PRODUCT_ID = " WHERE product.id = ?";
    private static final String WHERE_PRODUCT_IS = " WHERE type = '%s'";
    private static final String WHERE_PRODUCT_ID_IN_LIST = " WHERE FIND_IN_SET(product.id,?)>0 ";

    ProductDaoHelper(Connection connection) {
        super(connection);
    }

    @Override
    public Product insert(Product product) {

        checkForNull(product);
        checkIsUnsaved(product);

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, product.getName());
            statement.setLong(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setString(4, product.getType().toString());

            int productId = executeInsertStatement(statement);
            product.setId(productId);
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_INSERTING + product.toString());
        }
        return product;
    }

    void updateQuantity(Product product) {

        checkForNull(product);
        checkIsSaved(product);

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_QUANTITY_SQL)) {

            statement.setInt(1, product.getQuantity());
            statement.setInt(2, product.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_UPDATING + product.toString());
        }
    }

    @Override
    public void update(Product product) {

        checkForNull(product);
        checkIsSaved(product);

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, product.getName());
            statement.setLong(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setString(4, product.getType().toString());
            statement.setInt(5, product.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_UPDATING + product.toString());
        }
    }

    @Override
    public List<Product> getAll() {
        throw new UnsupportedOperationException();
    }

    private List<Product> parseResultSet(ResultSet resultSet) throws SQLException {

        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {

            ProductType type = ProductType.valueOf(resultSet.getString(FIELD_TYPE));
            Product product = new Product.Builder(type)
                    .setId(resultSet.getInt(FIELD_ID))
                    .setName(resultSet.getString(FIELD_NAME))
                    .setPrice(resultSet.getLong(FIELD_PRICE))
                    .setQuantity(resultSet.getInt(FIELD_QUANTITY))
                    .build();
            products.add(product);
        }
        return products;
    }


    public Optional<Product> getById(int id) {

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_PRODUCT_ID)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Product> products = parseResultSet(resultSet);
                checkSingleResult(products);
                return Optional.ofNullable(products.isEmpty() ? null : products.get(0));
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_GETTING_BY_ID + id);
        }
    }


    public void deleteById(int id) {

        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setInt(1, id);
            statement.setInt(2, id);
            statement.setInt(3, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_DELETING_BY_ID + id);
        }
    }

    List<Product> getAll(ProductType type) {

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     SELECT_ALL_SQL + String.format(WHERE_PRODUCT_IS, type) + ORDER_BY_ID)) {

            return parseResultSet(resultSet);

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_GETTING_ALL);
        }
    }


    List<Product> getAllByIds(Set<Integer> productIds) {

        if (productIds.isEmpty()) {
            return Collections.emptyList();
        }
        String ids = getStringListOf(productIds);
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_PRODUCT_ID_IN_LIST)) {

            statement.setString(1, ids);

            try (ResultSet resultSet = statement.executeQuery()) {

                return parseResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_GETTING_ALL_BY_ID + productIds);
        }
    }

    String getStringListOf(Set<Integer> productIds) {

        return productIds.stream()
                .map(i -> Integer.toString(i))
                .collect(Collectors.joining(","));
    }


    void updateQuantityAllInList(List<? extends Product> products) {
        products.forEach(this::updateQuantity);
    }
}
