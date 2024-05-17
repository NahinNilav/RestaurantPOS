package org.example.restaurant_management_system.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    // DO NOT EDIT ANYTHING
    // TABLE EMPLOYEE HARDCODED FOR LOGIN SIGNUP. RESOLVE THIS
    // INCLUDE JAR IN LIBRARIES
    // Import java.sql.Connection for JDBC database connections
    // You're using Class.forName("com.mysql.jdbc.Driver") to load the MySQL driver
    // However, in recent versions of MySQL Connector/J, the driver class has
    // changed to com.mysql.cj.jdbc.Driver

    /*
    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3307/restaurant", "root", "");
            return connect;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
*/


    /*
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private static final String URL = "jdbc:mysql://localhost:3307/restaurant";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection connectDB() {
        try {
            Class.forName(DRIVER_CLASS);
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            return connection;
        }

        catch (SQLException  | ClassNotFoundException e) {
            System.out.println("Database class object related exception");
            e.printStackTrace();

            return null;
        }
    }

     */

    private static final String URL = "jdbc:mysql://localhost:3307/restaurant";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private static Connection connection;

    // Private constructor to prevent instantiation from outside
    private Database() {
        // Initialize the connection
        try {
            Class.forName(DRIVER_CLASS);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }

        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Static method to get the singleton instance
    public static synchronized Connection connectDB() {
        if (connection == null) {
            new Database();
        }

        return connection;
    }
}