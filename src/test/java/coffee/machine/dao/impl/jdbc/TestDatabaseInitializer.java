package coffee.machine.dao.impl.jdbc;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * This class represents functionality to put test database to initial state by running sql script,
 * which contains DDL commands and test data to be put into test database
 * <p>
 * For success perform of refilling database with test data it should be already created,
 * because method connects directly to database, specified in database.properties.
 *
 * @author oleksij.onysymchuk@gmail.com
 */
class TestDatabaseInitializer {
    private static final String DDL_POPULATE_SQL = "DDL+POPULATE.sql";
    private static final String JDBC_DRIVER = "jdbc.driver";
    private static final String JDBC_URL = "jdbc.url";
    private static final String JDBC_USER = "jdbc.user";
    private static final String JDBC_PASSWORD = "jdbc.password";

    private String ddlPopulate;

    TestDatabaseInitializer() {
        this(DDL_POPULATE_SQL);
    }

    private TestDatabaseInitializer(String ddlPopulateSql) {
        this.ddlPopulate = ddlPopulateSql;
    }

    void initTestJdbcDB() throws Exception {
        ResourceBundle jdbcProperties = ResourceBundle.getBundle("database");

        File script = new File(
                this.getClass()
                        .getClassLoader()
                        .getResource(ddlPopulate)
                        .getFile());

        String multiQuery = FileUtils.readFileToString(script, "utf-8");

        Class.forName(jdbcProperties.getString(JDBC_DRIVER));
        try (Connection con = DriverManager.getConnection(
                jdbcProperties.getString(JDBC_URL),
                jdbcProperties.getString(JDBC_USER),
                jdbcProperties.getString(JDBC_PASSWORD));
             Statement st = con.createStatement()) {

            st.execute(multiQuery);
        }
    }
}


