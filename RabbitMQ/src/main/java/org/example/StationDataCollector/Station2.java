package org.example.StationDataCollector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Station2 {
    private final static String DRIVER = "postgresql";
    private final static String HOST = "localhost";
    private final static int PORT = 30012;
    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";
    private final static String DATABASE_NAME = "stationdb";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl());
    }

    public static String getUrl() {
        return String.format(
                "jdbc:%s://%s:%s/%s?user=%s&password=%s",
                DRIVER,
                HOST,
                PORT,
                DATABASE_NAME,
                USER,
                PASSWORD
        );
    }
}
