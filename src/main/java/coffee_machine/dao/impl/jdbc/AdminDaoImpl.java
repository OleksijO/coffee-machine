package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.AdminDao;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.i18n.message.key.error.DaoErrorKey;
import coffee_machine.model.entity.user.Admin;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDaoImpl extends AbstractUserDao<Admin> implements AdminDao {
	private static final Logger logger = Logger.getLogger(AdminDaoImpl.class);

	private static final String WHERE_ABSTRACT_USER_LOGIN = " WHERE abstract_user.email = ?";
	private static final String WHERE_ABSTRACT_USER_ID = " WHERE abstract_user.id = ?";

	private static final String SELECT_ALL_SQL = String.format(SELECT_ALL_FROM_ABSTRACT_USER_SQL, ", admins.enabled")
			+ " INNER JOIN admins ON abstract_user.id = admins.admin_id ";
	private static final String UPDATE_SQL = UPDATE_ABSTRACT_USER_SQL + "UPDATE admins SET enabled = ? WHERE admin_id = ?;";
	private static final String INSERT_SQL = "INSERT INTO admins (admin_id, enabled) VALUES (?, ?);";
	private static final String DELETE_SQL = DELETE_ABSTRACT_USER_SQL + "DELETE FROM admins WHERE admin_id = ?; ";

	private static final String FIELD_LOGIN = "email";
	private static final String FIELD_PASSWORD = "password";
	private static final String FIELD_FULL_NAME = "full_name";
	private static final String FIELD_ENABLED = "enabled";

	private Connection connection;

	public AdminDaoImpl(Connection connection) {
		super(logger);
		this.connection = connection;
	}

	@Override
	public Admin insert(Admin admin) {
		if (admin == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_CREATE_EMPTY);
		}
		if (admin.getId() != 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_CREATE_ALREADY_SAVED);
		}

		try (PreparedStatement statementForAbstractUser = connection.prepareStatement(INSERT_ABSTRACT_USER_SQL,
				Statement.RETURN_GENERATED_KEYS);
				PreparedStatement statementForAdmin = connection.prepareStatement(INSERT_SQL)) {

			statementForAbstractUser.setString(1, admin.getEmail());
			statementForAbstractUser.setString(2, admin.getPassword());
			statementForAbstractUser.setString(3, admin.getFullName());

			int abstractUserId = executeInsertStatement(statementForAbstractUser);
			admin.setId(abstractUserId);
			statementForAdmin.setInt(1, abstractUserId);
			statementForAdmin.setBoolean(2, admin.isEnabled());
			statementForAdmin.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_INSERTING, admin, e);
		}
		return admin;
	}

	@Override
	public void update(Admin admin) {
		if (admin == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_EMPTY);
		}
		if (admin.getId() == 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_UNSAVED);
		}
		try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL);) {

			statement.setString(1, admin.getEmail());
			statement.setString(2, admin.getPassword());
			statement.setString(3, admin.getFullName());
			statement.setInt(4, admin.getId());
			statement.setBoolean(5, admin.isEnabled());
			statement.setInt(6, admin.getId());

			statement.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_UPDATING, admin, e);
		}
	}

	@Override
	public List<Admin> getAll() {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {

			return parseResultSet(resultSet);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_ALL, e);
		}
		throw new InternalError(); // STUB for compiler
	}

	private List<Admin> parseResultSet(ResultSet resultSet) throws SQLException {
		List<Admin> adminList = new ArrayList<>();
		while (resultSet.next()) {
			Admin admin = new Admin();
			admin.setId(resultSet.getInt(FIELD_ID));
			admin.setFullName(resultSet.getString(FIELD_FULL_NAME));
			admin.setEmail(resultSet.getString(FIELD_LOGIN));
			admin.setPassword(resultSet.getString(FIELD_PASSWORD));
			admin.setEnabled(resultSet.getBoolean(FIELD_ENABLED));

			adminList.add(admin);
		}
		return adminList;
	}

	@Override
	public Admin getById(int id) {
		try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_ABSTRACT_USER_ID)) {

			statement.setInt(1, id);
			List<Admin> adminList = parseResultSet(statement.executeQuery());
			checkSingleResult(adminList);

			return adminList == null || adminList.isEmpty() ? null : adminList.get(0);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_BY_ID, e);
		}
		throw new InternalError(); // STUB for compiler
	}

	@Override
	public void deleteById(int id) {
		Admin admin = getById(id);
		if (admin == null) {
			return;
		}
		try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

			statement.setInt(1, id);
			statement.setInt(2, id);
			statement.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_DELETING_BY_ID, admin, e);
		}
	}

	@Override
	public Admin getAdminByLogin(String login) {
		try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_ABSTRACT_USER_LOGIN)) {

			statement.setString(1, login);
			List<Admin> adminList = parseResultSet(statement.executeQuery());
			checkSingleResult(adminList);

			return adminList == null || adminList.isEmpty() ? null : adminList.get(0);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_BY_LOGIN, login, e);
		}
		throw new InternalError(); // STUB for compiler
	}

}
