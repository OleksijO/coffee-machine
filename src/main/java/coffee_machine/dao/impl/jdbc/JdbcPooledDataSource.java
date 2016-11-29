package coffee_machine.dao.impl.jdbc;

import coffee_machine.dao.exception.DaoException;
import coffee_machine.dao.logging.DaoErrorProcessing;
import coffee_machine.i18n.message.key.error.DaoErrorKey;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ResourceBundle;

/**
 * Created by oleksij.onysymchuk@gmail on 17.11.2016.
 */
public class JdbcPooledDataSource implements DaoErrorProcessing {

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
           throw new DaoException(DaoErrorKey.DAO_ERROR, e);
        }
        cpds.setJdbcUrl(jdbcProperties.getString("jdbc.url"));
        cpds.setUser(jdbcProperties.getString("jdbc.user"));
        cpds.setPassword(jdbcProperties.getString("jdbc.password"));
        cpds.setMaxPoolSize(20);
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        return cpds;
    }


}
