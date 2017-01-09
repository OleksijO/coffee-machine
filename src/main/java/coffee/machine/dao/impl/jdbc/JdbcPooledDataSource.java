package coffee.machine.dao.impl.jdbc;

import coffee.machine.dao.exception.DaoException;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ResourceBundle;

/**
 * This class defines jdbc connection pool for application
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class JdbcPooledDataSource {
    private static final String CAN_NOT_READ_PROPERTY_FOR_MIN_JDBC_CONNECTION_POOL_SIZE =
            "Can not read property for min jdbc connection pool size. Setting default = 5.";
    private static final Logger logger = Logger.getLogger(JdbcPooledDataSource.class);
    private static final String CAN_NOT_READ_PROPERTY_FOR_MAX_JDBC_CONNECTION_POOL_SIZE =
            "Can not read property for max jdbc connection pool size. Setting default = 20.";
    private static final String CAN_NOT_READ_PROPERTY_FOR_JDBC_CONNECTION_ACQUIRE_INCREMENT =
            "Can not read property for jdbc connection acquire increment. Setting default = 5.";

    private static final String DATABASE_BUNDLE = "database";
    private static final String JDBC_DRIVER = "jdbc.driver";
    private static final String JDBC_URL = "jdbc.url";
    private static final String JDBC_USER = "jdbc.user";
    private static final String JDBC_PASSWORD = "jdbc.password";
    private static final String JDBC_MAX_POOL_SIZE = "jdbc.max.pool.size";
    private static final String JDBC_MIN_POOL_SIZE = "jdbc.min.pool.size";
    private static final String JDBC_ACQUIRE_INCREMENT = "jdbc.acquire.increment";

    private static class InstanceHolder {
        private static final DataSource instance = initDataSource();
    }

    public static DataSource getInstance() {
        return JdbcPooledDataSource.InstanceHolder.instance;
    }

    private static DataSource initDataSource() {
        ResourceBundle jdbcProperties = ResourceBundle.getBundle(DATABASE_BUNDLE);
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(jdbcProperties.getString(JDBC_DRIVER));
        } catch (PropertyVetoException e) {
            throw new DaoException(e);
        }
        cpds.setJdbcUrl(jdbcProperties.getString(JDBC_URL));
        cpds.setUser(jdbcProperties.getString(JDBC_USER));
        cpds.setPassword(jdbcProperties.getString(JDBC_PASSWORD));
        try {
            cpds.setMaxPoolSize(Integer.parseInt(jdbcProperties.getString(JDBC_MAX_POOL_SIZE)));
        } catch (NumberFormatException e) {
            logger.error(CAN_NOT_READ_PROPERTY_FOR_MAX_JDBC_CONNECTION_POOL_SIZE);
            cpds.setMaxPoolSize(20);
        }
        try {
            cpds.setMinPoolSize(Integer.parseInt(jdbcProperties.getString(JDBC_MIN_POOL_SIZE)));
        } catch (NumberFormatException e) {
            logger.error(CAN_NOT_READ_PROPERTY_FOR_MIN_JDBC_CONNECTION_POOL_SIZE);
            cpds.setMinPoolSize(5);
        }
        try {
            cpds.setAcquireIncrement(Integer.parseInt(jdbcProperties.getString(JDBC_ACQUIRE_INCREMENT)));
        } catch (NumberFormatException e) {
            logger.error(CAN_NOT_READ_PROPERTY_FOR_JDBC_CONNECTION_ACQUIRE_INCREMENT);
            cpds.setAcquireIncrement(5);
        }

        return cpds;
    }


}
