package org.example.PDFGenerator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserDB {
    private final static String DRIVER = "postgresql";
    private final static String HOST = "localhost";
    private final static int PORT = 30001;
    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";
    private final static String DATABASE_NAME = "customerdb";

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
