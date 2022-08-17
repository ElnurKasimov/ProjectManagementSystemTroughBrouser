package model.dbConnection;

import control.FrontController;
import lombok.Data;

import java.sql.*;

@Data
public class DBConnection {
    private static final DBConnection INSTANCE = new DBConnection();
    private Connection connection;
    private String connectionUrl;
    private String dbUser;
    private String dbPassword;

    private DBConnection() {
        try {
            connectionUrl = FrontController.prefs.getString(Prefs.DB_JDBC_CONNECTION_URL);
            dbUser = FrontController.prefs.getString(Prefs.DB_JDBC_USERNAME);
            dbPassword = FrontController.prefs.getString(Prefs.DB_JDBC_PASSWORD);
            Class.forName("com.mysql.cj.jdbc.Driver");
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

