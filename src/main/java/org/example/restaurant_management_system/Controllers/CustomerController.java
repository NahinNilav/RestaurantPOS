package org.example.restaurant_management_system.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.restaurant_management_system.Model.CustomerData;
import org.example.restaurant_management_system.Model.Data;
import org.example.restaurant_management_system.Model.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomerController {

    public static Connection connect;
    public static PreparedStatement prepare;
    public Statement statement;
    public static ResultSet result;
    @FXML
    public static TableView<CustomerData> customers_tableView;

    @FXML
    public static TableColumn<CustomerData, String> customers_col_customerID;

    @FXML
    public static TableColumn<CustomerData, String> customers_col_total;

    @FXML
    public static TableColumn<CustomerData, String> customers_col_date;

    @FXML
    public static TableColumn<CustomerData, String> customers_col_cashier;


    private static int cID;

    public static void customerID() {

        String sql = "SELECT MAX(customer_id) FROM customer";
        connect = (Connection) Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                cID = result.getInt("MAX(customer_id)");
            }

            String checkCID = "SELECT MAX(customer_id) FROM receipt";
            prepare = connect.prepareStatement(checkCID);
            result = prepare.executeQuery();
            int checkID = 0;
            if (result.next()) {
                checkID = result.getInt("MAX(customer_id)");
            }

            if (cID == 0) {
                cID += 1;
            } else if (cID == checkID) {
                cID += 1;
            }

            Data.cID = cID;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<CustomerData> customersDataList() {

        ObservableList<CustomerData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM receipt";
        connect = (Connection) Database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            CustomerData cData;

            while (result.next()) {
                cData = new CustomerData(result.getInt("id"),
                        result.getInt("customer_id"),
                        result.getDouble("total"),
                        result.getDate("date"),
                        result.getString("em_username"));

                listData.add(cData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private static ObservableList<CustomerData> customersListData;

    public static void customersShowData() {
        customersListData = customersDataList();

        customers_col_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customers_col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        customers_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customers_col_cashier.setCellValueFactory(new PropertyValueFactory<>("emUsername"));

        customers_tableView.setItems(customersListData);
    }

}
