package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.GenericDao;
import coffee_machine.dao.exception.DaoException;
import coffee_machine.i18n.SupportedLocale;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

abstract class AbstractDao<T> implements GenericDao<T> {
	static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("i18n.messages",
			SupportedLocale.EN.getLocale());

	static final String FIELD_ID = "id";

	private Logger logger;
	private String errorMessageEntityPrefix;

	public AbstractDao(Logger logger, String errorMessageEntityPrefix) {
		this.logger = logger;
		this.errorMessageEntityPrefix = errorMessageEntityPrefix;
	}

	protected void logErrorAndThrowDaoException(String message, Throwable e) {
		String messageKey = errorMessageEntityPrefix + message;
		logger.error(RESOURCE_BUNDLE.getString(messageKey), e);
		throw new DaoException(messageKey, e);
	}

	protected void logErrorAndThrowDaoException(String message, Object entity, Throwable e) {
		String messageKey = errorMessageEntityPrefix + message;
		logger.error(RESOURCE_BUNDLE.getString(messageKey) + "\t" + entity.toString(), e);
		throw new DaoException(messageKey, e);
	}

	protected void logErrorAndThrowNewDaoException(String message) {
		String messageKey = errorMessageEntityPrefix + message;
		logger.error(RESOURCE_BUNDLE.getString(messageKey));
		throw new DaoException(messageKey);
	}

	protected void logErrorAndThrowNewDaoException(String message, Object entity) {
		String messageKey = errorMessageEntityPrefix + message;
		logger.error(RESOURCE_BUNDLE.getString(messageKey) + "\t" + entity.toString());
		throw new DaoException(messageKey);
	}

	protected void checkSingleResult(List list) {
		if ((list != null) && (list.size() > 1)) {
			logErrorAndThrowNewDaoException("Unexpected multiple result set while requesting single record.");
		}
	}

	protected int executeInsertStatement(PreparedStatement statement) throws SQLException {
		statement.executeUpdate();
		ResultSet keys = statement.getGeneratedKeys();
		keys.next();
		return keys.getInt(1);
	}

}
