package integration.dao;

import com.ibatis.common.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

/**
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


        InputStream ddlSQL = this.getClass().getClassLoader()
                .getResourceAsStream(ddlPopulate);

        // Create MySql Connection
        Class.forName(jdbcProperties.getString(JDBC_DRIVER));
        Connection con = DriverManager.getConnection(
                jdbcProperties.getString(JDBC_URL),
                jdbcProperties.getString(JDBC_USER),
                jdbcProperties.getString(JDBC_PASSWORD));

        System.out.println("=============================================");
        System.out.println("            Running SQL scripts");
        System.out.println("=============================================");

        ScriptRunner scriptRunner = new ScriptRunner(con, false, false);

        Reader reader = new BufferedReader(
                new InputStreamReader(ddlSQL));
        scriptRunner.runScript(reader);

    }
}


