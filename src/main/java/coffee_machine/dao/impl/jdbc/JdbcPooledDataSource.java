package coffee_machine.dao.impl.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * Created by oleksij.onysymchuk@gmail on 17.11.2016.
 */
public class JdbcPooledDataSource {
    private static DataSource dataSource = initDataSource();

    public static DataSource getDataSource() {
        return dataSource;
    }

    private static DataSource initDataSource() {

        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass("com.mysql.jdbc.Driver");
        } catch (PropertyVetoException e) {
            // TODO log
            throw new Error(e);
        }
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/coffee_machine");
        cpds.setUser("root");
        cpds.setPassword("root");
        cpds.setMaxPoolSize(20);
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        return cpds;
    }


}
