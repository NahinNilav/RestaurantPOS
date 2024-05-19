package org.example.restaurant_management_system.Controllers;


import java.sql.Connection;
import java.util.Date;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.example.restaurant_management_system.Model.CuisineData;
import org.example.restaurant_management_system.Model.Data;
import org.example.restaurant_management_system.Model.Database;

public abstract class MenuController extends CustomerController {

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
    public Button menu_receiptBtn;


    public Alert alert;



    public ObservableList<CuisineData> cardListData = FXCollections.observableArrayList();



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

        // Get order id from selection
        getid = cd.getId();

    }

    public double totalP;

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

    public double amount;
    public double change;

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
                    alert.setContentText("Something wrong :3");
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
}