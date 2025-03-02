package com.mycompany.biker_portal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:derby://localhost:1527/bikerportal;create=true";
    private static final String USER = "daya";
    private static final String PASSWORD = "daya123";

    // Method to establish a new connection every time it's called
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("✅ Database connected successfully!");
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            return null;
        }
    }

    // Close connection after use
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔒 Database connection closed.");
            } catch (SQLException e) {
                System.err.println("❌ Failed to close connection: " + e.getMessage());
            }
        }
    }
}
