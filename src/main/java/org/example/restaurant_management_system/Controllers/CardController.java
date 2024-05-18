package org.example.restaurant_management_system.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.example.restaurant_management_system.Model.CuisineData;
import org.example.restaurant_management_system.Model.Data;
import org.example.restaurant_management_system.Model.Database;

public class CardController implements Initializable {

    @FXML
    private AnchorPane card_form;

    @FXML
    private Label prod_name;

    @FXML
    private Label prod_price;

    @FXML
    private ImageView prod_imageView;

    @FXML
    private Spinner<Integer> prod_spinner;

    @FXML
    private Button prod_addBtn;

    private CuisineData prodData;
    private Image image;

    private String prodID;
    private String type;
    private String prod_date;
    private String prod_image;

    private SpinnerValueFactory<Integer> spin;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    private int quantity;
    private double totalPrice;
    private double price;

    private static final int MIN_QUANTITY = 0;
    private static final int MAX_QUANTITY = 100;
    private static final String ERROR_TITLE = "Error Message";
    private static final String INFO_TITLE = "Information Message";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setQuantity();
    }

    public void setData(CuisineData prodData) {
        this.prodData = prodData;

        prod_image = prodData.getImage();
        prod_date = String.valueOf(prodData.getDate());
        type = prodData.getType();
        prodID = prodData.getCuisineId();
        prod_name.setText(prodData.getCuisineName());
        prod_price.setText("$" + String.valueOf(prodData.getPrice()));
        String path = "File:" + prodData.getImage();
        image = new Image(path, 190, 94, false, true);
        prod_imageView.setImage(image);
        price = prodData.getPrice();
    }

    private void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_QUANTITY, MAX_QUANTITY, MIN_QUANTITY);
        prod_spinner.setValueFactory(spin);
    }

    @FXML
    public void addBtn() {
        quantity = prod_spinner.getValue();

        if (quantity == 0 || !isProductAvailable()) {
            showAlert(Alert.AlertType.ERROR, ERROR_TITLE, "Something Wrong :3");
            return;
        }

        int checkStock = getStock(prodID);

        if (checkStock == 0) {
            updateProductStatus("Unavailable", 0);
        }

        if (checkStock < quantity) {
            showAlert(Alert.AlertType.ERROR, ERROR_TITLE, "Invalid. This product is Out of stock");
        } else {
            addProductToCustomer();
            updateProductStock(checkStock - quantity);
            showAlert(Alert.AlertType.INFORMATION, INFO_TITLE, "Successfully Added!");

            // Assuming mainFormController.menuGetTotal() is a method to update total
            mainFormController mForm = new mainFormController();
            mForm.menuGetTotal();
        }
    }

    private boolean isProductAvailable() {
        String status = "";
        String query = "SELECT status FROM product WHERE prod_id = ?";
        try (Connection connect = Database.connectDB();
             PreparedStatement prepare = connect.prepareStatement(query)) {

            prepare.setString(1, prodID);
            result = prepare.executeQuery();

            if (result.next()) {
                status = result.getString("status");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Available".equals(status);
    }

    private int getStock(String prodID) {
        int stock = 0;
        String query = "SELECT stock FROM product WHERE prod_id = ?";
        try (Connection connect = Database.connectDB();
             PreparedStatement prepare = connect.prepareStatement(query)) {

            prepare.setString(1, prodID);
            result = prepare.executeQuery();

            if (result.next()) {
                stock = result.getInt("stock");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stock;
    }

    private void updateProductStatus(String status, int stock) {
        String updateQuery = "UPDATE product SET prod_name = ?, type = ?, stock = ?, price = ?, status = ?, image = ?, date = ? WHERE prod_id = ?";
        try (Connection connect = Database.connectDB();
             PreparedStatement prepare = connect.prepareStatement(updateQuery)) {

            prepare.setString(1, prod_name.getText());
            prepare.setString(2, type);
            prepare.setInt(3, stock);
            prepare.setDouble(4, price);
            prepare.setString(5, status);
            prepare.setString(6, prod_image);
            prepare.setString(7, prod_date);
            prepare.setString(8, prodID);

            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addProductToCustomer() {
        String insertQuery = "INSERT INTO customer (customer_id, prod_id, prod_name, type, quantity, price, date, image, em_username) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connect = Database.connectDB();
             PreparedStatement prepare = connect.prepareStatement(insertQuery)) {

            prepare.setString(1, String.valueOf(Data.cID));
            prepare.setString(2, prodID);
            prepare.setString(3, prod_name.getText());
            prepare.setString(4, type);
            prepare.setInt(5, quantity);

            totalPrice = quantity * price;
            prepare.setDouble(6, totalPrice);

            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            prepare.setDate(7, sqlDate);

            prepare.setString(8, prod_image.replace("\\", "\\\\"));
            prepare.setString(9, Data.username);

            prepare.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProductStock(int updatedStock) {
        updateProductStatus("Available", updatedStock);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
