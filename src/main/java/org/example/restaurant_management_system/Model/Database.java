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
