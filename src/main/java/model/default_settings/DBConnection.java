package model.default_settings;

import control.FrontController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final DBConnection INSTANCE = new DBConnection();
    private Connection connection;
    String connectionUrl;
    String dbUser;
    String dbPassword;

    private DBConnection() {
        try {

            System.out.println(FrontController.PROJECT_ROOT);
            Prefs prefs = new Prefs("F:/javacore5/javadev_module_6_PMS/");
            connectionUrl = prefs.getString(Prefs.DB_JDBC_CONNECTION_URL);
            dbUser = prefs.getString(Prefs.DB_JDBC_USERNAME);
            dbPassword = prefs.getString(Prefs.DB_JDBC_PASSWORD);
            System.out.println(" connection URL - " + connectionUrl);
            System.out.println(" db User  - " + dbUser);
            System.out.println("db password - " + dbPassword);

            connection = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static DBConnection getInstance() {
        return INSTANCE;
    }

    public int executeUpdate(String sql) {
        try(Statement st = connection.createStatement()) {
            return st.executeUpdate(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

