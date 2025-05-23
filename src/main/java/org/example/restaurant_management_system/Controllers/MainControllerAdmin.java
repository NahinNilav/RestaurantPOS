package org.example.restaurant_management_system.Controllers;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.restaurant_management_system.Model.CuisineData;
import org.example.restaurant_management_system.Model.Data;
import org.example.restaurant_management_system.Model.Database;


public class MainControllerAdmin extends InventoryController implements Initializable {

    @FXML
    public AnchorPane main_form;

    @FXML
    public Label username;

    @FXML
    public Button dashboard_btn;

    @FXML
    public Button inventory_btn;

    @FXML
    public Button menu_btn;


    @FXML
    public Button menu_refresh_btn;

    @FXML
    public Button customers_btn;

    @FXML
    public Button logout_btn;

    @FXML
    public AnchorPane inventory_form;

    @FXML
    public AnchorPane menu_form;

    @FXML
    public ScrollPane menu_scrollPane;

    @FXML
    public GridPane menu_gridPane;


    @FXML
    public AnchorPane dashboard_form;

    @FXML
    public AnchorPane customers_form;



    @FXML
    public Label dashboard_NC;

    @FXML
    public Label dashboard_TI;

    @FXML
    public Label dashboard_TotalI;

    @FXML
    public Label dashboard_NSP;

    @FXML
    public AreaChart<?, ?> dashboard_incomeChart;

    @FXML
    public BarChart<?, ?> dashboard_CustomerChart;

    public Alert alert;

    public Connection connect;
    public PreparedStatement prepare;
    public Statement statement;
    public ResultSet result;

    public Image image;

    public ObservableList<CuisineData> cardListData = FXCollections.observableArrayList();

    public void dashboardDisplayNumCust() {

        String sql = "SELECT COUNT(id) FROM receipt";
        connect = (Connection) Database.connectDB();

        try {
            int nc = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                nc = result.getInt("COUNT(id)");
            }
            dashboard_NC.setText(String.valueOf(nc));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dashboardDisplayDailyInc() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "SELECT SUM(total) FROM receipt WHERE date = '"
                + sqlDate + "'";

        connect = (Connection) Database.connectDB();

        try {
            double ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getDouble("SUM(total)");
            }

            dashboard_TI.setText("Tk" + ti);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardTotalInc() {
        String sql = "SELECT SUM(total) FROM receipt";

        connect = (Connection) Database.connectDB();

        try {
            float ti = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                ti = result.getFloat("SUM(total)");
            }
            dashboard_TotalI.setText("Tk" + ti);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dashboardTotalSoldProducts() {
        String sql = "SELECT SUM(quantity) FROM customer";

        try {
            connect = Database.connectDB();
            int totalQuantity = 0;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                totalQuantity = result.getInt("SUM(quantity)");
            }
            dashboard_NSP.setText(String.valueOf(totalQuantity));

        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void dashboardIncomeChart() {
        dashboard_incomeChart.getData().clear();

        String sql = "SELECT date, SUM(total) FROM receipt GROUP BY date ORDER BY TIMESTAMP(date)";
        connect = (Connection) Database.connectDB();
        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getFloat(2)));
            }

            dashboard_incomeChart.getData().add(chart);

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardCustomerChart(){
        dashboard_CustomerChart.getData().clear();

        String sql = "SELECT date, COUNT(id) FROM receipt GROUP BY date ORDER BY TIMESTAMP(date)";
        connect = (Connection) Database.connectDB();
        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data<>(result.getString(1), result.getInt(2)));
            }

            dashboard_CustomerChart.getData().add(chart);

        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //public CustomerController custCont = new CustomerController();

    public void refreshMenu(ActionEvent event) {

        if (event.getSource() == menu_refresh_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(true);
            customers_form.setVisible(false);

            menuDisplayCard();
            menuDisplayTotal();
            menuShowOrderData();

            System.out.println("Menu refreshed");
        }

    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == dashboard_btn) {

            System.out.println("Dashboard button clicked.");
            dashboard_form.setVisible(true);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            customers_form.setVisible(false);

            dashboardDisplayNumCust();
            dashboardDisplayDailyInc();
            dashboardTotalInc();
            dashboardTotalSoldProducts();
            dashboardIncomeChart();
            dashboardCustomerChart();

        } else if (event.getSource() == inventory_btn) {

            System.out.println("Inventory button clicked.");
            dashboard_form.setVisible(false);
            inventory_form.setVisible(true);
            menu_form.setVisible(false);
            customers_form.setVisible(false);

            inventoryTypeList();
            inventoryStatusList();
            inventoryShowData();
        } else if (event.getSource() == menu_btn) {

            System.out.println("Menu button clicked.");
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(true);
            customers_form.setVisible(false);

            menuDisplayCard();
            menuDisplayTotal();
            menuShowOrderData();
        } else if (event.getSource() == customers_btn) {

            System.out.println("Customers button clicked.");
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            customers_form.setVisible(true);

            customersShowData();
        }

    }

    public void logout() {

        try {

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {

                logout_btn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("/view/Authentication.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("Restaurant Management System");

                stage.setScene(scene);
                stage.show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayUsername() {

        String user = Data.username;
        //String user = "Admin";
        user = user.substring(0, 1).toUpperCase() + user.substring(1);

        username.setText(user);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        displayUsername();

        dashboardDisplayNumCust();
        dashboardDisplayDailyInc();
        dashboardTotalInc();
        dashboardTotalSoldProducts();
        dashboardIncomeChart();
        dashboardCustomerChart();

        inventoryTypeList();
        inventoryStatusList();
        inventoryShowData();

        inventory_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                inventorySelectData();
            }
        });


        menuDisplayCard();
        menuGetOrder();
        menuDisplayTotal();
        menuShowOrderData();
        menu_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                menuSelectOrder();
            }
        });

        customersShowData();

    }

}