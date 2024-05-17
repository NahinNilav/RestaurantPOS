package org.example.restaurant_management_system.Controllers;
//import com.almasb.fxgl.net.Connection;
import javafx.scene.control.Alert;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.sql.PreparedStatement;


public class AuthenticationController  {
        @FXML
        public Button login_btn;

        @FXML
        public PasswordField login_pass;

        @FXML
        public Button login_signup_btn;

        @FXML
        public TextField login_username;


        @FXML
        public TextField email;

        @FXML
        public TextField first_name;

        @FXML
        public TextField last_name;

        @FXML
        public PasswordField register_Confirm_pass;

        @FXML
        public Button register_login_btn;

        @FXML
        public PasswordField register_pass;

        @FXML
        public TextField register_username;

        @FXML
        public Button sign_up_btn;

//        @Override
//        public void initialize(URL url, ResourceBundle resourceBundle) {
//                // Pre-fill the TextField with an email address
//                login_username.setText("username");
//        }


        private Connection connect;
        private PreparedStatement prepare;
        private ResultSet result;



        Stage stage1 = new Stage();
        Stage stage2 = new Stage();


//        public void handleEvent(ActionEvent event) throws IOException {
//                Stage dialogStage;
//                Node node = (Node) event.getSource();
//                dialogStage = (Stage) node.getScene().getWindow();
//                dialogStage.close();
//
//                if (event.getSource() == login_signup_btn) {
//                        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/registerview.fxml")));
//                        dialogStage.setScene(scene);
//                } else if (event.getSource() == login_btn) {
//                        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/Loginview.fxml")));
//                        dialogStage.setScene(scene);
//                }
//
//                dialogStage.show();
//        }


        public void handleEvent(ActionEvent event) throws IOException {
                if(event.getSource() == login_signup_btn)
                {

                        Stage dialogStage;
                        Node node = (Node) event.getSource();
                        dialogStage = (Stage) node.getScene().getWindow();
                        dialogStage.close();
                        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/registerview.fxml")));
                        dialogStage.setScene(scene);
                        dialogStage.show();


                }


        }

        public void loginswitch(ActionEvent event) throws IOException {



                        Stage dialogStage;
                        Node node = (Node) event.getSource();
                        dialogStage = (Stage) node.getScene().getWindow();
                        dialogStage.close();
                        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/Loginview.fxml")));
                        dialogStage.setScene(scene);
                        dialogStage.show();



        }

        public void transitiontomainForm(ActionEvent event) throws IOException {
                Stage dialogStage;
                Node node = (Node) event.getSource();
                dialogStage = (Stage) node.getScene().getWindow();
                dialogStage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/view/mainform2.fxml")));
                dialogStage.setScene(scene);
                dialogStage.show();

        }



}