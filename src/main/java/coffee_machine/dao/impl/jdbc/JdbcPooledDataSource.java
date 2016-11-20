package coffee_machine.dao.impl.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.ResourceBundle;

/**
 * Created by oleksij.onysymchuk@gmail on 17.11.2016.
 */
public class JdbcPooledDataSource {
    private static DataSource dataSource = initDataSource();

    public static DataSource getDataSource() {
        return dataSource;
    }

    private static DataSource initDataSource() {
        ResourceBundle jdbcProperties = ResourceBundle.getBundle("database");
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass(jdbcProperties.getString("jdbc.driver"));
        } catch (PropertyVetoException e) {
            // TODO log
            throw new Error(e);
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
