package org.example.restaurant_management_system.Controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.restaurant_management_system.Model.CuisineData;
import org.example.restaurant_management_system.Model.CustomerData;
import org.example.restaurant_management_system.Model.Data;
import org.example.restaurant_management_system.Model.Database;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

// Code not cleaned intentionally because of time scarcity
public class MainControllerCustomer implements Initializable {

    @FXML
    public AnchorPane main_form;

    @FXML
    public Label username;

    @FXML
    public Button menu_btn;

    @FXML
    public Button menu_refresh_btn;

    @FXML
    public Button logout_btn;

    @FXML
    public AnchorPane inventory_form;

    @FXML
    public TableView<CuisineData> inventory_tableView;

    @FXML
    public TableColumn<CuisineData, String> inventory_col_productID;

    @FXML
    public TableColumn<CuisineData, String> inventory_col_productName;

    @FXML
    public TableColumn<CuisineData, String> inventory_col_type;

    @FXML
    public TableColumn<CuisineData, String> inventory_col_stock;

    @FXML
    public TableColumn<CuisineData, String> inventory_col_price;

    @FXML
    public TableColumn<CuisineData, String> inventory_col_status;

    @FXML
    public TableColumn<CuisineData, String> inventory_col_date;

    @FXML
    public ImageView inventory_imageView;

    @FXML
    public Button inventory_importBtn;

    @FXML
    public Button inventory_addBtn;

    @FXML
    public Button inventory_updateBtn;

    @FXML
    public Button inventory_clearBtn;

    @FXML
    public Button inventory_deleteBtn;

    @FXML
    public TextField inventory_productID;

    @FXML
    public TextField inventory_productName;

    @FXML
    public TextField inventory_stock;

    @FXML
    public TextField inventory_price;

    @FXML
    public ComboBox<?> inventory_status;

    @FXML
    public ComboBox<?> inventory_type;

    @FXML
    public AnchorPane menu_form;

    @FXML
    public ScrollPane menu_scrollPane;

    @FXML
    public GridPane menu_gridPane;

    @FXML
    public TableView<CuisineData> menu_tableView;

    @FXML
    public TableColumn<CuisineData, String> menu_col_productName;

    @FXML
    public TableColumn<CuisineData, String> menu_col_quantity;

    @FXML
    public TableColumn<CuisineData, String> menu_col_price;

    @FXML
    public Label menu_total;

    @FXML
    public TextField menu_amount;

    @FXML
    public Button menu_amount_enter_btn;

    @FXML
    public Label menu_change;

    @FXML
    public Button menu_payBtn;

    @FXML
    public Button menu_removeBtn;



    @FXML
    public AnchorPane dashboard_form;

    @FXML
    public AnchorPane customers_form;

    @FXML
    public TableView<CustomerData> customers_tableView;

    @FXML
    public TableColumn<CustomerData, String> customers_col_customerID;

    @FXML
    public TableColumn<CustomerData, String> customers_col_total;

    @FXML
    public TableColumn<CustomerData, String> customers_col_date;

    @FXML
    public TableColumn<CustomerData, String> customers_col_cashier;

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



    public ObservableList<CuisineData> inventoryDataList() {

        ObservableList<CuisineData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM product";

        connect = (Connection) Database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            CuisineData prodData;

            while (result.next()) {

                prodData = new CuisineData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(prodData);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }


    private ObservableList<CuisineData> inventoryListData;

    public void inventoryShowData() {
        inventoryListData = inventoryDataList();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("cuisineId"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("cuisineName"));
        inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_tableView.setItems(inventoryListData);

    }

    public void inventorySelectData() {

        CuisineData prodData = inventory_tableView.getSelectionModel().getSelectedItem();
        int num = inventory_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        inventory_productID.setText(prodData.getCuisineId());
        inventory_productName.setText(prodData.getCuisineName());
        inventory_stock.setText(String.valueOf(prodData.getStock()));
        inventory_price.setText(String.valueOf(prodData.getPrice()));

        Data.path = prodData.getImage();

        String path = "File:" + prodData.getImage();
        Data.date = String.valueOf(prodData.getDate());
        Data.id = prodData.getId();

        image = new Image(path, 120, 127, false, true);
        inventory_imageView.setImage(image);
    }

    private String[] typeList = {"Meals", "Drinks"};

    public void inventoryTypeList() {

        List<String> typeL = new ArrayList<>();

        for (String data : typeList) {
            typeL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(typeL);
        inventory_type.setItems(listData);
    }

    private String[] statusList = {"Available", "Unavailable"};

    public void inventoryStatusList() {

        List<String> statusL = new ArrayList<>();

        for (String data : statusList) {
            statusL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(statusL);
        inventory_status.setItems(listData);

    }

    public ObservableList<CuisineData> menuGetData() {

        String sql = "SELECT * FROM product";

        ObservableList<CuisineData> listData = FXCollections.observableArrayList();
        connect = (Connection) Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            CuisineData prod;

            while (result.next()) {
                prod = new CuisineData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    public void menuDisplayCard() {

        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menu_gridPane.getChildren().clear();
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();

        for (int q = 0; q < cardListData.size(); q++) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/view/CuisineCard.fxml"));
                AnchorPane pane = load.load();
                CuisineCardController cardC = load.getController();
                cardC.setDataToCards(cardListData.get(q));

                if (column == 2) {
                    column = 0;
                    row += 1;
                }

                menu_gridPane.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(10));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<CuisineData> menuGetOrder() {
        customerID();
        ObservableList<CuisineData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM customer WHERE customer_id = " + cID;

        connect = (Connection) Database.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            CuisineData prod;

            while (result.next()) {
                prod = new CuisineData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"));
                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private ObservableList<CuisineData> menuOrderListData;

    public void menuShowOrderData() {
        menuOrderListData = menuGetOrder();

        menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("cuisineName"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        menu_tableView.setItems(menuOrderListData);
    }
    private int getid;

    public void menuSelectOrder() {

        CuisineData cd = menu_tableView.getSelectionModel().getSelectedItem();
        int num = menu_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        getid = cd.getId();

    }

    private double totalP;

    public void menuGetTotal() {
        customerID();
        String total = "SELECT SUM(price) FROM customer WHERE customer_id = " + cID;

        connect = (Connection) Database.connectDB();

        try {

            prepare = connect.prepareStatement(total);
            result = prepare.executeQuery();

            if (result.next()) {
                totalP = result.getDouble("SUM(price)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void menuDisplayTotal() {
        menuGetTotal();
        menu_total.setText("Tk" + totalP);
    }

    private double amount;
    private double change;

    public void menuAmount() {
        menuGetTotal();

        if (menu_amount.getText().isEmpty() || totalP == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid :3");
            alert.showAndWait();
        } else {
            amount = Double.parseDouble(menu_amount.getText());
            System.out.println("Amount text: " + amount); // Debugging output
            if (amount < totalP) {
                menu_amount.setText("");
            } else {
                change = (amount - totalP);
                menu_change.setText("Tk" + change);
            }
        }
    }

    public void menuPayBtn() {

        if (totalP == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose your order first!");
            alert.showAndWait();
        } else {
            menuGetTotal();
            String insertPay = "INSERT INTO receipt (customer_id, total, date, em_username) "
                    + "VALUES(?,?,?,?)";

            connect = (Connection) Database.connectDB();

            try {

                if (amount == 0) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Oops! Something has gone wrong");
                    alert.showAndWait();
                } else {
                    alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if (option.get().equals(ButtonType.OK)) {
                        customerID();
                        menuGetTotal();
                        prepare = connect.prepareStatement(insertPay);
                        prepare.setString(1, String.valueOf(cID));
                        prepare.setString(2, String.valueOf(totalP));

                        Date date = new Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        prepare.setString(3, String.valueOf(sqlDate));

                        //prepare.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                        prepare.setString(4, Data.username);

                        prepare.executeUpdate();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successful.");
                        alert.showAndWait();

                        menuShowOrderData();
                        menuRestart();

                    } else {
                        alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Cancelled.");
                        alert.showAndWait();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void menuRemoveBtn() {

        if (getid == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select the order you want to remove");
            alert.showAndWait();
        }

        else {
            String deleteData = "DELETE FROM customer WHERE id = " + getid;
            connect = (Connection) Database.connectDB();

            try {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this order?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                    menuRestart();
                }

                menuShowOrderData();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void menuRestart() {
        totalP = 0;
        change = 0;
        amount = 0;
        menu_total.setText("Tk 0.0");
        menu_amount.setText("");
        menu_change.setText("Tk 0.0");
    }

    private int cID;



    public void customerID() {

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

    public ObservableList<CustomerData> customersDataList() {

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

    private ObservableList<CustomerData> customersListData;

    public void customersShowData() {
        customersListData = customersDataList();

        customers_col_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customers_col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        customers_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        customers_col_cashier.setCellValueFactory(new PropertyValueFactory<>("emUsername"));

        customers_tableView.setItems(customersListData);
    }



    //public CustomerController custCont = new CustomerController();




    public void logout() {

        try {

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
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


    public void refreshMenu(ActionEvent event) {
        if (event.getSource() == menu_refresh_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(true);
            customers_form.setVisible(false);

            menuDisplayCard();
            menuDisplayTotal();
            menuShowOrderData();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        displayUsername();

//        dashboardDisplayTotalCustomers();
//        dashboardDisplayTI();
//        dashboardTotalI();
//        dashboardTotalSoldProducts();
//        dashboardIncomeChart();
//        dashboardCustomerChart();

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