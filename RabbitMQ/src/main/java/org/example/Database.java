package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final static String DRIVER = "postgresql";
    private final static String HOST = "localhost";
    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";

    public static Connection getConnection(int port, String databaseName) throws SQLException {
        try {
            return DriverManager.getConnection(getUrl(port, databaseName), USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUrl(int port, String databaseName) {
        return String.format(
                "jdbc:%s://%s:%d/%s",
                DRIVER,
                HOST,
                port,
                databaseName
        );
    }
}
