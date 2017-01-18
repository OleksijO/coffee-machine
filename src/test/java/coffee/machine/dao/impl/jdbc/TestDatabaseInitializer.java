package coffee.machine.dao.impl.jdbc;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
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
public class TestDatabaseInitializer {
    public static final String DDL_POPULATE_SQL = "DDL+POPULATE.sql";
    public static final String JDBC_DRIVER = "jdbc.driver";
    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USER = "jdbc.user";
    public static final String JDBC_PASSWORD = "jdbc.password";

    private String ddlPopulate;

    public TestDatabaseInitializer() {
        this(DDL_POPULATE_SQL);
    }

    public TestDatabaseInitializer(String ddlPopulateSql) {
        this.ddlPopulate = ddlPopulateSql;
    }

    public void initTestJdbcDB() throws Exception {
        ResourceBundle jdbcProperties = ResourceBundle.getBundle("database");

        File script = new File(
                this.getClass()
                        .getClassLoader()
                        .getResource(ddlPopulate)
                        .getFile());
        if (script==null) {
            throw new IllegalStateException();
        }
        String multiQuery = FileUtils.readFileToString(script, "utf-8");

        System.out.println("=============================================");
        System.out.println("            Running SQL scripts...");
        System.out.println("=============================================");

        Class.forName(jdbcProperties.getString(JDBC_DRIVER));
        try (Connection con = DriverManager.getConnection(
                jdbcProperties.getString(JDBC_URL),
                jdbcProperties.getString(JDBC_USER),
                jdbcProperties.getString(JDBC_PASSWORD))) {

            con.createStatement().execute(multiQuery);
        }

        System.out.println("=============================================");
        System.out.println("            Running SQL scripts...DONE");
        System.out.println("=============================================");
    }
}


