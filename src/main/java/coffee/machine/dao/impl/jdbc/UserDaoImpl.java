package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.UserDao;
import coffee.machine.dao.exception.DaoException;
import coffee.machine.model.entity.Account;
import coffee.machine.model.entity.user.User;
import coffee.machine.model.entity.user.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static coffee.machine.model.entity.user.UserRole.ADMIN;

/**
 * This class is the implementation of User entity DAO
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class UserDaoImpl extends AbstractDao<User> implements UserDao {
    private static final String LOG_MESSAGE_DB_ERROR_WHILE_GETTING_BY_LOGIN =
            "Database error while getting user by login";
    private static final String LOG_MESSAGE_DB_ERROR_WHILE_GETTING_ALL_BY_ROLE =
            LOG_MESSAGE_DB_ERROR_WHILE_GETTING_ALL + " for role ";

    private static final String SELECT_ALL_SQL =
            "SELECT users.id, email, password, full_name, account_id, amount, role FROM users " +
                    "LEFT JOIN account ON users.account_id = account.id ";
    private static final String UPDATE_SQL =
            "UPDATE users SET email = ?, password = ?, full_name = ?, account_id = ?, role = ? WHERE id = ? ";
    private static final String INSERT_SQL =
            "INSERT INTO users (email, password, full_name, account_id, role) VALUES (?, ?, ?, ?, ?) ";
    private static final String TABLE = "users";
    private static final String WHERE_USER_EMAIL = " WHERE users.email = ? ";
    private static final String WHERE_USER_ID = " WHERE users.id = ? ";
    private static final String WHERE_USER_ROLE = " WHERE users.role = ? ";
    private static final String ORDER_BY_EMAIL = " ORDER BY email ";

    private static final String FIELD_LOGIN = "email";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_FULL_NAME = "full_name";
    private static final String FIELD_ACCOUNT_ID = "account_id";
    private static final String FIELD_ROLE = "role";
    private static final String FIELD_ACCOUNT_AMOUNT = "amount";

    UserDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public User insert(User user) {
        checkForNull(user);
        checkIsUnsaved(user);
        int accountId;
        if (user.getRole() == ADMIN) {
            accountId = 0;
        } else {
            Account account = user.getAccount();
            checkForNull(account);
            checkIsSaved(account);
            accountId = account.getId();
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFullName());
            statement.setInt(4, accountId);
            statement.setString(5, user.getRole().toString());

            int userId = executeInsertStatement(statement);
            user.setId(userId);

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_INSERTING + user.toString());
        }
        return user;
    }

    @Override
    public void update(User user) {

        checkForNull(user);
        checkIsSaved(user);

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFullName());
            statement.setInt(4, (user.getAccount() == null) ? 0 : user.getAccount().getId());
            statement.setString(5, user.getRole().toString());
            statement.setInt(6, user.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_UPDATING + user.toString());
        }

    }

    @Override
    public List<User> getAll() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL + ORDER_BY_EMAIL)) {

            return parseResultSet(resultSet);

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_GETTING_ALL);
        }
    }

    private List<User> parseResultSet(ResultSet resultSet) throws SQLException {
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User.Builder()
                    .setId(resultSet.getInt(FIELD_ID))
                    .setEmail(resultSet.getString(FIELD_LOGIN))
                    .setPassword(resultSet.getString(FIELD_PASSWORD))
                    .setFullName(resultSet.getString(FIELD_FULL_NAME))
                    .setRole(UserRole.valueOf(resultSet.getString(FIELD_ROLE)))
                    .setAccount(new Account.Builder()
                            .setId(resultSet.getInt(FIELD_ACCOUNT_ID))
                            .setAmount(resultSet.getLong(FIELD_ACCOUNT_AMOUNT))
                            .build())
                    .build();
            userList.add(user);
        }
        return userList;
    }

    @Override
    public Optional<User> getById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_USER_ID)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> userList = parseResultSet(resultSet);
                checkSingleResult(userList);
                return userList.isEmpty()
                        ? Optional.empty()
                        : Optional.of(userList.get(0));
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_GETTING_BY_ID + id);
        }
    }

    @Override
    public void deleteById(int id) {

        super.deleteById(TABLE, id);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_USER_EMAIL)) {

            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> userList = parseResultSet(resultSet);
                checkSingleResult(userList);

                return userList.stream().findFirst();
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_GETTING_BY_LOGIN + login);
        }
    }

    @Override
    public List<User> getAllByRole(UserRole role) {
        try (PreparedStatement statement =
                     connection.prepareStatement(SELECT_ALL_SQL + WHERE_USER_ROLE + ORDER_BY_EMAIL)) {
            statement.setString(1, role.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                return parseResultSet(resultSet);
            }

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_GETTING_ALL_BY_ROLE + role);
        }
    }

}
