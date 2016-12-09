package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.AccountDao;
import coffee.machine.dao.UserDao;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.user.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the implementation of User entity DAO
 *
 * @author oleksij.onysymchuk@gmail.com
 */
public class UserDaoImpl extends AbstractDao<User> implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class);

    private static final String DB_ERROR_WHILE_GETTING_BY_LOGIN = "Database error while getting user by login";

    private static final String SELECT_ALL_SQL =
            "SELECT users.id, email, password, full_name, account_id, amount, is_admin FROM users " +
                    "LEFT JOIN account ON users.account_id = account.id ";
    private static final String UPDATE_SQL =
            "UPDATE users SET email = ?, password = ?, full_name = ?, account_id = ?, is_admin = ? WHERE id = ? ";
    private static final String INSERT_SQL =
            "INSERT INTO users (email, password, full_name, account_id, is_admin) VALUES (?, ?, ?, ?, ?) ";
    private static final String DELETE_SQL =
            "DELETE FROM users WHERE id = ? ";
    private static final String WHERE_USER_EMAIL = " WHERE users.email = ? ";
    private static final String WHERE_USER_ID = " WHERE users.id = ? ";
    private static final String WHERE_NOT_ADMIN = " WHERE users.is_admin = FALSE ";
    private static final String ORDER_BY_FULL_NAME = " ORDER BY full_name ";


    private static final String FIELD_LOGIN = "email";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_FULL_NAME = "full_name";
    private static final String FIELD_ACCOUNT_ID = "account_id";
    private static final String FIELD_IS_ADMIN = "is_admin";
    private static final String FIELD_ACCOUNT_AMOUNT = "amount";

    private final Connection connection;
    private final AccountDao accountDao;

    public UserDaoImpl(Connection connection, AccountDao accountDao) {
        this.connection = connection;
        this.accountDao = accountDao;
    }

    @Override
    public User insert(User user) {
        if (user == null) {
            logErrorAndThrowDaoException(logger, CAN_NOT_CREATE_EMPTY);
        }
        if (user.getId() != 0) {
            logErrorAndThrowDaoException(logger, CAN_NOT_CREATE_ALREADY_SAVED);
        }
        int accountId;
        if (user.isAdmin()) {
            accountId = 0;
        } else {
            if ((user.getAccount() != null) && (user.getAccount().getId() != 0)) {
                // if account was created before and set to user, f.e. in service layer
                accountId = user.getAccount().getId();
            } else {
                // if account was not created before
                accountId = accountDao.insert(user.getAccount()).getId();
            }
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFullName());
            statement.setInt(4, accountId);
            statement.setBoolean(5, user.isAdmin());

            int userId = executeInsertStatement(statement);
            user.setId(userId);

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_INSERTING, user, e);
        }
        return user;
    }

    @Override
    public void update(User user) {
        if (user == null) {
            logErrorAndThrowDaoException(logger, CAN_NOT_UPDATE_EMPTY);
        }
        if (user.getId() == 0) {
            logErrorAndThrowDaoException(logger, CAN_NOT_UPDATE_UNSAVED);
        }
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFullName());
            statement.setInt(4, (user.getAccount() == null) ? 0 : user.getAccount().getId());
            statement.setBoolean(5, user.isAdmin());
            statement.setInt(6, user.getId());
            System.out.println(UPDATE_SQL);
            statement.executeUpdate();

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_UPDATING, user, e);
        }

    }

    @Override
    public List<User> getAll() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL + ORDER_BY_FULL_NAME)) {

            return parseResultSet(resultSet);

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_GETTING_ALL, e);
        }
        throw new InternalError(); // STUB for compiler

    }

    private List<User> parseResultSet(ResultSet resultSet) throws SQLException {
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt(FIELD_ID));
            user.setFullName(resultSet.getString(FIELD_FULL_NAME));
            user.setEmail(resultSet.getString(FIELD_LOGIN));
            user.setPassword(resultSet.getString(FIELD_PASSWORD));
            Account account = new Account();
            if (!user.isAdmin()) {
                account.setId(resultSet.getInt(FIELD_ACCOUNT_ID));
                account.setAmount(resultSet.getLong(FIELD_ACCOUNT_AMOUNT));
            }
            user.setAccount(account);
            user.setAdmin(resultSet.getBoolean(FIELD_IS_ADMIN));
            userList.add(user);
        }
        return userList;
    }

    @Override
    public User getById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_USER_ID)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> userList = parseResultSet(resultSet);
                checkSingleResult(userList);
                return userList == null || userList.isEmpty() ? null : userList.get(0);
            }
        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_GETTING_BY_ID, e);
        }
        throw new InternalError(); // STUB for compiler

    }

    @Override
    public void deleteById(int id) {
        User user = getById(id);
        if (user == null) {
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_DELETING_BY_ID, user, e);
        }
        accountDao.deleteById(user.getAccount().getId());
    }

    @Override
    public User getUserByLogin(String login) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_USER_EMAIL)) {

            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> userList = parseResultSet(resultSet);
                checkSingleResult(userList);

                return userList == null || userList.isEmpty() ? null : userList.get(0);
            }
        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_GETTING_BY_LOGIN, login, e);
        }
        throw new InternalError(); // STUB for compiler

    }

    @Override
    public List<User> getAllNonAdmin() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL + WHERE_NOT_ADMIN + ORDER_BY_ID)) {

            return parseResultSet(resultSet);

        } catch (SQLException e) {
            logErrorAndThrowDaoException(logger, DB_ERROR_WHILE_GETTING_ALL, e);
        }
        throw new InternalError(); // STUB for compiler
    }

}
