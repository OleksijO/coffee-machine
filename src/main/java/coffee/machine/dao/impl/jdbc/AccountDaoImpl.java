package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.AccountDao;
import coffee.machine.dao.exception.DaoException;
import coffee.machine.model.entity.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is the implementation of Account entity DAO
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class AccountDaoImpl extends AbstractDao<Account> implements AccountDao {
    private static final String SELECT_ALL_SQL = "SELECT id, amount FROM account";
    private static final String SELECT_BY_USER_ID_SQL = "SELECT account.id, amount FROM account "
            + "INNER JOIN users ON users.account_id = account.id WHERE users.id = ?";
    private static final String WHERE_ID = " WHERE id = ?";
    private static final String INSERT_SQL = "INSERT INTO account (amount) VALUES (?);";
    private static final String UPDATE_SQL = "UPDATE account SET amount=? WHERE id=?;";
    private static final String TABLE = "account";

    private static final String FIELD_ID = "id";
    private static final String FIELD_AMOUNT = "amount";

    AccountDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Account insert(Account account) {
        checkForNull(account);
        checkIsUnsaved(account);

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, account.getAmount());

            int accountId = executeInsertStatement(statement);

            account.setId(accountId);

        } catch (SQLException e) {
            throw new DaoException(e).addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_INSERTING);
        }
        return account;
    }

    @Override
    public void update(Account account) {
        checkForNull(account);
        checkIsSaved(account);

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setLong(1, account.getAmount());
            statement.setInt(2, account.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_UPDATING);
        }
    }

    @Override
    public List<Account> getAll() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {

            return parseResultSet(resultSet);

        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_GETTING_ALL);
        }
    }

    private List<Account> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Account> accountList = new ArrayList<>();
        while (resultSet.next()) {
            Account account = new Account.Builder()
                    .setId(resultSet.getInt(FIELD_ID))
                    .setAmount(resultSet.getLong(FIELD_AMOUNT))
                    .build();
            accountList.add(account);
        }
        return accountList;
    }

    @Override
    public Optional<Account> getById(int id) {
        return Optional.ofNullable(getAccountByQueryAndIntParameter(SELECT_ALL_SQL + WHERE_ID, id));
    }

    private Account getAccountByQueryAndIntParameter(String sql, int id) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Account> accountList = parseResultSet(resultSet);
                checkSingleResult(accountList);

                return accountList.isEmpty() ? null : accountList.get(0);
            }
        } catch (SQLException e) {
            throw new DaoException(e)
                    .addLogMessage(LOG_MESSAGE_DB_ERROR_WHILE_GETTING_BY_ID);
        }
    }

    @Override
    public Optional<Account> getByUserId(int userId) {
        return Optional.ofNullable(getAccountByQueryAndIntParameter(SELECT_BY_USER_ID_SQL, userId));

    }

    @Override
    public void deleteById(int id) {

        super.deleteById(TABLE, id);
    }

}
