package org.example.restaurant_management_system.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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




public class CuisineCardController implements Initializable {

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

    public void setDataToCards(CuisineData prodData) {
        this.prodData = prodData;

        prod_image = prodData.getImage();
        prod_date = String.valueOf(prodData.getDate());
        type = prodData.getType();
        prodID = prodData.getCuisineId();
        prod_name.setText(prodData.getCuisineName());
        prod_price.setText("Tk " + String.valueOf(prodData.getPrice()));
        String path = "File:" + prodData.getImage();
        image = new Image(path, 190, 94, false, true);
        prod_imageView.setImage(image);
        price = prodData.getPrice();

    }
    private int quantity;

    public void setQuantity() {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        prod_spinner.setValueFactory(spin);
    }

    private double totalPrice;
    private double price;

    private ActionEvent event;

    public void addBtn() {

        MainControllerAdmin mForm = new MainControllerAdmin();
        mForm.customerID();
        //CustomerController.customerID();


        quantity = prod_spinner.getValue();
        String check = "";
        String checkAvailable = "SELECT status FROM product WHERE prod_id = '"
                + prodID + "'";

        connect = Database.connectDB();

        try {
            int checkStck = 0;
            String checkStock = "SELECT stock FROM product WHERE prod_id = '"
                    + prodID + "'";

            prepare = connect.prepareStatement(checkStock);
            result = prepare.executeQuery();

            if (result.next()) {
                checkStck = result.getInt("stock");
            }

            if(checkStck == 0){

                String updateStock = "UPDATE product SET prod_name = '"
                        + prod_name.getText() + "', type = '"
                        + type + "', stock = 0, price = " + price
                        + ", status = 'Unavailable', image = '"
                        + prod_image + "', date = '"
                        + prod_date + "' WHERE prod_id = '"
                        + prodID + "'";
                prepare = connect.prepareStatement(updateStock);
                prepare.executeUpdate();

            }

            prepare = connect.prepareStatement(checkAvailable);
            result = prepare.executeQuery();

            if (result.next()) {
                check = result.getString("status");
            }

            if (!check.equals("Available") || quantity == 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Something Wrong :3");
                alert.showAndWait();
            } else {

                if (checkStck < quantity) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid. This product is Out of stock");
                    alert.showAndWait();
                } else {
                    prod_image = prod_image.replace("\\", "\\\\");

                    String insertData = "INSERT INTO customer "
                            + "(customer_id, prod_id, prod_name, type, quantity, price, date, image, em_username) "
                            + "VALUES(?,?,?,?,?,?,?,?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, String.valueOf(Data.cID));
                    prepare.setString(2, prodID);
                    prepare.setString(3, prod_name.getText());
                    prepare.setString(4, type);
                    prepare.setString(5, String.valueOf(quantity));

                    totalPrice = (quantity * price);
                    prepare.setString(6, String.valueOf(totalPrice));

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(7, String.valueOf(sqlDate));

                    prepare.setString(8, prod_image);
                    prepare.setString(9, Data.username);

                    prepare.executeUpdate();

                    int upStock = checkStck - quantity;



                    System.out.println("Date: " + prod_date);
                    System.out.println("Image: " + prod_image);

                    String updateStock = "UPDATE product SET prod_name = '"
                            + prod_name.getText() + "', type = '"
                            + type + "', stock = " + upStock + ", price = " + price
                            + ", status = '"
                            + check + "', image = '"
                            + prod_image + "', date = '"
                            + prod_date + "' WHERE prod_id = '"
                            + prodID + "'";

                    prepare = connect.prepareStatement(updateStock);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    //mForm.menuShowOrderData();
                    mForm.menuGetTotal();
                    //mForm.switchForm(event);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setQuantity();

    }

}