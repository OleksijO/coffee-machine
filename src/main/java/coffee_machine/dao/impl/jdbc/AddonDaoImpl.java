package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.AddonDao;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.i18n.message.key.error.DaoErrorKey;
import coffee_machine.model.entity.goods.Addon;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddonDaoImpl extends AbstractGoodsDao<Addon> implements AddonDao {

	private static final Logger logger = Logger.getLogger(AddonDaoImpl.class);

	private static final String WHERE_GOODS_ID = " WHERE abstract_goods.id = ?";

	private static final String SELECT_ALL_SQL = String.format(SELECT_ALL_FROM_GOODS_SQL, "")
			+ " INNER JOIN addon ON abstract_goods.id = addon.id ";
	private static final String UPDATE_SQL = UPDATE_GOODS_SQL + "";
	private static final String INSERT_SQL = "INSERT INTO addon (id) VALUES (?);";
	private static final String DELETE_SQL = DELETE_GOODS_SQL + "";

	private static final String FIELD_NAME = "name";
	private static final String FIELD_PRICE = "price";
	private static final String FIELD_QUANTITY = "quantity";

	private final Connection connection;

	public AddonDaoImpl(Connection connection) {
		super(logger);
		this.connection = connection;
	}

	@Override
	public Addon insert(Addon addon) {
		if (addon == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_CREATE_EMPTY);
		}
		if (addon.getId() != 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_CREATE_ALREADY_SAVED);
		}

		try (PreparedStatement statementForGoods = connection.prepareStatement(INSERT_GOODS_SQL,
				Statement.RETURN_GENERATED_KEYS);
				PreparedStatement statementForAddon = connection.prepareStatement(INSERT_SQL)) {

			statementForGoods.setString(1, addon.getName());
			statementForGoods.setLong(2, addon.getPrice());
			statementForGoods.setInt(3, addon.getQuantity());

			int goodsId = executeInsertStatement(statementForGoods);
			addon.setId(goodsId);
			statementForAddon.setInt(1, goodsId);

			statementForAddon.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_INSERTING, addon, e);
		}
		return addon;
	}

	@Override
	public void update(Addon addon) {
		if (addon == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_EMPTY);
		}
		if (addon.getId() == 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_UNSAVED);
		}
		try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL);) {

			statement.setString(1, addon.getName());
			statement.setLong(2, addon.getPrice());
			statement.setInt(3, addon.getQuantity());
			statement.setInt(4, addon.getId());

			statement.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_UPDATING, addon, e);
		}

	}

	@Override
	public List<Addon> getAll() {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {

			return parseResultSet(resultSet);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_ALL, e);
		}
		throw new InternalError(); // STUB for compiler

	}

	private List<Addon> parseResultSet(ResultSet resultSet) throws SQLException {
		List<Addon> addonList = new ArrayList<>();
		while (resultSet.next()) {
			Addon addon = new Addon();
			addon.setId(resultSet.getInt(FIELD_ID));
			addon.setName(resultSet.getString(FIELD_NAME));
			addon.setPrice(resultSet.getLong(FIELD_PRICE));
			addon.setQuantity(resultSet.getInt(FIELD_QUANTITY));
			addonList.add(addon);
		}
		return addonList;
	}

	@Override
	public Addon getById(int id) {
		try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL + WHERE_GOODS_ID)) {

			statement.setInt(1, id);
			List<Addon> addonList = parseResultSet(statement.executeQuery());
			checkSingleResult(addonList);

			return addonList == null || addonList.isEmpty() ? null : addonList.get(0);

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_GETTING_BY_ID, e);
		}
		throw new InternalError(); // STUB for compiler
	}

	@Override
	public void deleteById(int id) {
		Addon addon = getById(id);
		if (addon == null) {
			return;
		}
		try (PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

			statement.setInt(1, id);
            System.out.println(DELETE_SQL);
            statement.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_DELETING_BY_ID, addon, e);
		}
	}

	@Override
	public List<Addon> getAllFromList(List<Addon> addons) {
		List<Addon> updatedAddons = new ArrayList<>();
		addons.forEach(addon -> updatedAddons.add(getById(addon.getId())));
		return updatedAddons;
	}

	@Override
	public List<Addon> getAllByIds(List<Integer> addonIds) {
		List<Addon> addons = new ArrayList<>();
		addonIds.forEach(id -> addons.add(getById(id)));
		return addons;
	}

	@Override
	public void updateQuantityAllInList(List<Addon> addons) {
		addons.forEach(this::updateQuantity);
	}

	@Override
	public void updateQuantity(Addon addon) {
		if (addon == null) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_EMPTY);
		}
		if (addon.getId() == 0) {
			throw new DaoException(DaoErrorKey.CAN_NOT_UPDATE_UNSAVED);
		}
		try (PreparedStatement statement = connection.prepareStatement(UPDATE_GOODS_QUANTITY_SQL)) {

			statement.setInt(1, addon.getQuantity());
			statement.setInt(2, addon.getId());
			statement.executeUpdate();

		} catch (SQLException e) {
			logErrorAndThrowDaoException(DaoErrorKey.DB_ERROR_WHILE_UPDATING, addon, e);
		}
	}

}
