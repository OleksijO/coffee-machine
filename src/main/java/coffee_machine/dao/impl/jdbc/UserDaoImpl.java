package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.AccountDao;
import coffee_machine.dao.UserDao;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.model.entity.Account;
import coffee_machine.model.entity.user.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends AbstractUserDao<User> implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class);

    private static final String SELECT_ALL_SQL =
            String.format(SELECT_ALL_FROM_ABSTRACT_USER_SQL, ", users.account_id, account.amount") +
                    " LEFT JOIN users ON abstract_user.id = users.id " +
                    " LEFT JOIN account ON users.account_id = account.id ";
    private static final String UPDATE_SQL = UPDATE_ABSTRACT_USER_SQL +
            "UPDATE users SET account_id = ? WHERE id = ?;" +
            "UPDATE account SET amount = ? WHERE id = ?;";
    private static final String INSERT_USER_SQL = "INSERT INTO users (id, account_id) VALUES (?, ?);";
    private static final String DELETE_USER_SQL = DELETE_ABSTRACT_USER_SQL +
            "DELETE FROM users WHERE id = ?; ";

    private static final String FIELD_LOGIN = "email";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_FULL_NAME = "full_name";
    private static final String FIELD_ACCOUNT_ID = "account_id";
    private static final String FIELD_ACCOUNT_AMOUNT = "amount";

    private Connection connection;
    private AccountDao accountDao;

    public UserDaoImpl(Connection connection, AccountDao accountDao) {
        this.connection = connection;
        this.accountDao = accountDao;
    }

    @Override
    public User insert(User user) {
        if (user == null) {
            throw new DaoException("User to be created can not be null.");
        }
        if (user.getId() != 0) {
            throw new DaoException(ERROR_ID_MUST_BE_FROM_DBMS + "abstract_user");
        }

        int accountId = accountDao.insert(user.getAccount()).getId();

        try (PreparedStatement statementForAbstractUser =
                     connection.prepareStatement(INSERT_ABSTRACT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement statementForUser = connection.prepareStatement(INSERT_USER_SQL)) {

            statementForAbstractUser.setString(1, user.getEmail());
            statementForAbstractUser.setString(2, user.getPassword());
            statementForAbstractUser.setString(3, user.getFullName());

            int abstractUserId = statementForAbstractUser.executeUpdate();
            user.setId(abstractUserId);
            statementForUser.setInt(1, abstractUserId);
            statementForUser.setInt(2, accountId);
            statementForUser.executeUpdate();

        } catch (SQLException e) {
            logErrorAndThrowWrapperDaoException("Error while persisting user=" + user, e);
        }
        return user;
    }

    private void logErrorAndThrowWrapperDaoException(String message, Throwable e) {
        logger.error(message, e);
        throw new DaoException(message, e);
    }

    private void logErrorAndThrowNewDaoException(String message) {
        logger.error(message);
        throw new DaoException(message);
    }

    @Override
    public void update(User user) {
        if (user == null) {
            throw new DaoException("User to be updated can not be null.");
        }
        try (PreparedStatement statement =
                     connection.prepareStatement(UPDATE_SQL);) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFullName());
            statement.setInt(4, user.getId());
            statement.setInt(5, user.getAccount().getId());
            statement.setInt(6, user.getId());
            statement.setLong(7, user.getId());
            statement.setInt(8, user.getAccount().getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            logErrorAndThrowWrapperDaoException("Error while updating user=" + user, e);
        }

    }

    @Override
    public List<User> getAll() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {

            return parseResultSet(resultSet);

        } catch (SQLException e) {
            logErrorAndThrowWrapperDaoException("Error while getting all records from abstract_user, user, account", e);
        }
        throw new DaoException("Unexpected end of method");     // stub for compilation
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
            account.setId(resultSet.getInt(FIELD_ACCOUNT_ID));
            account.setAmount(resultSet.getLong(FIELD_ACCOUNT_AMOUNT));
            user.setAccount(account);
            userList.add(user);
        }
        return userList;
    }

    @Override
    public User getById(int id) {
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_ALL_SQL + " WHERE abstract_user.id = ?")) {

            statement.setInt(1, id);
            List<User> userList = parseResultSet(statement.executeQuery());
            checkSingleResult(userList);

            return userList == null || userList.isEmpty() ? null : userList.get(0);

        } catch (SQLException e) {
            logErrorAndThrowWrapperDaoException("Error while getting record from abstract_user, user, account", e);
        }
        throw new DaoException("Unexpected end of method");     // stub for compilation
    }

    private void checkSingleResult(List list) {
        if ((list != null) && (list.size() > 1)) {
            logErrorAndThrowNewDaoException("Unexpected multiple result set while requesting single record.");
        }
    }

    @Override
    public void deleteById(int id) {
        User user = getById(id);
        if (user == null) {
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_SQL)) {

            statement.setInt(1, id);
            statement.setInt(2, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            logErrorAndThrowWrapperDaoException("Error while deleting user" + user, e);
        }
        accountDao.deleteById(user.getAccount().getId());
    }

    @Override
    public User getUserByLogin(String login) {
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_ALL_SQL + " WHERE abstract_user.login = ?")) {

            statement.setString(1, login);
            List<User> userList = parseResultSet(statement.executeQuery());
            checkSingleResult(userList);

            return userList == null || userList.isEmpty() ? null : userList.get(0);

        } catch (SQLException e) {
            logErrorAndThrowWrapperDaoException("Error getting user by email=" + login, e);
        }
        throw new DaoException("Unexpected end of method");     // stub for compilation
    }

}
