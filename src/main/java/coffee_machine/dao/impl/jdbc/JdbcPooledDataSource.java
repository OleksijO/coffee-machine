package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.exception.DaoException;
import coffee_machine.dao.logging.DaoErrorProcessing;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ResourceBundle;

/**
 * Created by oleksij.onysymchuk@gmail on 17.11.2016.
 */
class JdbcPooledDataSource implements DaoErrorProcessing {
    private static final String CAN_NOT_READ_PROPERTY_FOR_MIN_JDBC_CONNECTION_POOL_SIZE =
            "Can not read property for min jdbc connection pool size. Setting default = 5.";
    private static final Logger logger = Logger.getLogger(JdbcPooledDataSource.class);
    private static final String CAN_NOT_READ_PROPERTY_FOR_MAX_JDBC_CONNECTION_POOL_SIZE =
            "Can not read property for max jdbc connection pool size. Setting default = 20.";
    private static final String CAN_NOT_READ_PROPERTY_FOR_JDBC_CONNECTION_ACQUIRE_INCREMENT =
            "Can not read property for jdbc connection acquire increment. Setting default = 5.";

    private static class InstanceHolder {
        private static final DataSource instance = initDataSource();
    }

    public static DataSource getInstance() {
        return JdbcPooledDataSource.InstanceHolder.instance;
    }

    private static DataSource initDataSource() {
        ResourceBundle jdbcProperties = ResourceBundle.getBundle("database");
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(jdbcProperties.getString("jdbc.driver"));
        } catch (PropertyVetoException e) {
            logger.error(e);
            throw new DaoException();
        }
        cpds.setJdbcUrl(jdbcProperties.getString("jdbc.url"));
        cpds.setUser(jdbcProperties.getString("jdbc.user"));
        cpds.setPassword(jdbcProperties.getString("jdbc.password"));
        try {
            cpds.setMaxPoolSize(Integer.parseInt(jdbcProperties.getString("jdbc.max.pool.size")));
        } catch (NumberFormatException e) {
            logger.error(CAN_NOT_READ_PROPERTY_FOR_MAX_JDBC_CONNECTION_POOL_SIZE);
            cpds.setMaxPoolSize(20);
        }
        try {
            cpds.setMinPoolSize(Integer.parseInt(jdbcProperties.getString("jdbc.min.pool.size")));
        } catch (NumberFormatException e) {
            logger.error(CAN_NOT_READ_PROPERTY_FOR_MIN_JDBC_CONNECTION_POOL_SIZE);
            cpds.setMinPoolSize(5);
        }
        try {
            cpds.setAcquireIncrement(Integer.parseInt(jdbcProperties.getString("jdbc.acquire.increment")));
        } catch (NumberFormatException e) {
            logger.error(CAN_NOT_READ_PROPERTY_FOR_JDBC_CONNECTION_ACQUIRE_INCREMENT);
            cpds.setAcquireIncrement(5);
        }

        return cpds;
    }


}
