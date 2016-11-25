package integration.dao;

import com.ibatis.common.jdbc.ScriptRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by oleksij.onysymchuk@gmail on 25.11.2016.
 */
public class TestDataBaseInitializer {
    public void initTestJdbcDB() throws ClassNotFoundException, SQLException, InterruptedException, IOException {
        ResourceBundle jdbcProperties = ResourceBundle.getBundle("database");


        InputStream ddlSQL = this.getClass().getClassLoader()
                .getResourceAsStream("DDL.sql");
        InputStream populateSQL = this.getClass().getClassLoader()
                .getResourceAsStream("populate.sql");

        // Create MySql Connection
        Class.forName(jdbcProperties.getString("jdbc.driver"));
        Connection con = DriverManager.getConnection(
                jdbcProperties.getString("jdbc.url"),
                jdbcProperties.getString("jdbc.user"),
                jdbcProperties.getString("jdbc.password"));

        System.out.println("=============================================");
        System.out.println("            Running SQL scripts");
        System.out.println("=============================================");

        ScriptRunner scriptRunner = new ScriptRunner(con, false, false);

        Reader reader = new BufferedReader(
                new InputStreamReader(ddlSQL));
        scriptRunner.runScript(reader);

        reader = new BufferedReader(
                new InputStreamReader(populateSQL));
        scriptRunner.runScript(reader);
    }
}


