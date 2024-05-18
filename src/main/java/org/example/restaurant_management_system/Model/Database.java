package org.example.restaurant_management_system.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

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

    //  Singleton pattern
    private static final String url = "jdbc:mysql://localhost:3307/restaurant";
    private static final String username = "root";
    private static final String pwd = "";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private static Connection connection;

    private Database() {
        try {
            Class.forName(DRIVER_CLASS);
            connection = DriverManager.getConnection(url, username, pwd);
        }

        catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database connection problem occured");
            e.printStackTrace();
        }
    }


    public static synchronized Connection connectDB() {
        if (connection == null) {
            new Database();
        }

        return connection;
    }
}