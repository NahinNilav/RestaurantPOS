package org.example.restaurant_management_system.Controllers;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.restaurant_management_system.Model.Data;
import org.example.restaurant_management_system.Model.Database;


public class AuthenController implements Initializable {

    @FXML
    public AnchorPane si_loginForm;

    @FXML
    public TextField si_username;

    @FXML
    public PasswordField si_password;

    @FXML
    public Button si_loginBtn;

    @FXML
    public Hyperlink si_forgotPass;

    @FXML
    public AnchorPane su_signupForm;

    @FXML
    public TextField su_username;

    @FXML
    public PasswordField su_password;

    @FXML
    public ComboBox<?> su_question;

    @FXML
    public ComboBox<?> selectuser;

    @FXML
    public TextField su_answer;

    @FXML
    public Button su_signupBtn;

    @FXML
    public TextField fp_username;

    @FXML
    public AnchorPane fp_questionForm;

    @FXML
    public Button fp_proceedBtn;

    @FXML
    public ComboBox<?> fp_question;

    @FXML
    public TextField fp_answer;

    @FXML
    public Button fp_back;

    @FXML
    public AnchorPane np_newPassForm;

    @FXML
    public PasswordField np_newPassword;

    @FXML
    public PasswordField np_confirmPassword;

    @FXML
    public Button np_changePassBtn;

    @FXML
    public Button np_back;

    @FXML
    public AnchorPane side_form;

    @FXML
    public Button side_CreateBtn;

    @FXML
    public Button side_alreadyHave;

    public Connection connect;
    public PreparedStatement prepare;
    public ResultSet result;

    public Alert alert;




    public void loginBtn() {
        if (si_username.getText().isEmpty() || si_password.getText().isEmpty() ||
                selectuser.getSelectionModel().getSelectedItem() == null) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please enter Username/Password and select a user type.");
            alert.showAndWait();
        }

        else {
            String selectData = "SELECT username, password, type FROM users WHERE username = ? and password = ?";
            connect = (Connection) Database.connectDB();

            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, si_username.getText());
                prepare.setString(2, si_password.getText());

                result = prepare.executeQuery();

                if (result.next()) {
                    String role = result.getString("type");
                    String selectedRole = selectuser.getSelectionModel().getSelectedItem().toString();

                    if (role.equals(selectedRole)) {
                        // Successful login, set the username
                        Data.username = si_username.getText();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Logged In!");
                        alert.showAndWait();

                        // Load the appropriate main form based on role
                        String fxmlFile = (role.equals("Admin")) ? "/view/Admin.fxml" : "/view/Customer.fxml";
                        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

                        Stage stage = new Stage();
                        Scene scene = new Scene(root);

                        stage.setTitle("Restaurant Management System");
                        stage.setMinWidth(1100);
                        stage.setMinHeight(600);

                        stage.setScene(scene);
                        stage.show();

                        // Close
                        si_loginBtn.getScene().getWindow().hide();
                    }

                    else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("User type does not match. Please select the correct user type.");
                        alert.showAndWait();
                    }
                }

                else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username/Password.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private String[] clientType = {"Admin", "Customer"};

    public void clientTypeList()
    {
        List<String> listQ = new ArrayList<>();

        for (String data : clientType) {
            listQ.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listQ);
        selectuser.setItems(listData);

    }



    public void registerButton() {
        if (su_username.getText().isEmpty() || su_password.getText().isEmpty()
                || su_question.getSelectionModel().getSelectedItem() == null) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String regData = "INSERT INTO users (username, password, type, date) "
                    + "VALUES(?,?,?,?)";
            connect = (Connection) Database.connectDB();

            try {
                // RECORD PRESENT
                String checkUsername = "SELECT username FROM users WHERE username = '"
                        + su_username.getText() + "'";

                prepare = connect.prepareStatement(checkUsername);
                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(su_username.getText() + " is already taken");
                    alert.showAndWait();
                } else if (su_password.getText().length() < 8) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Password, at least 8 characters are needed");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(regData);
                    prepare.setString(1, su_username.getText());
                    prepare.setString(2, su_password.getText());
                    prepare.setString(3, (String) su_question.getSelectionModel().getSelectedItem());

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(4, String.valueOf(sqlDate));
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully registered Account!");
                    alert.showAndWait();

                    su_username.setText("");
                    su_password.setText("");
                    su_question.getSelectionModel().clearSelection();

                    TranslateTransition slider = new TranslateTransition();
                    slider.setNode(side_form);
                    slider.setToX(0);
                    slider.setDuration(Duration.seconds(.5));

                    slider.setOnFinished((ActionEvent e) -> {
                        side_alreadyHave.setVisible(false);
                        side_CreateBtn.setVisible(true);
                    });

                    slider.play();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private String[] userTypeList = {"Employee", "Customer"};

    public void userTypeListMethod() {
        List<String> listQ = new ArrayList<>();

        for (String data : userTypeList) {
            listQ.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listQ);
        su_question.setItems(listData);
    }

    public void switchForgotPass() {
        fp_questionForm.setVisible(true);
        si_loginForm.setVisible(false);

        forgotPassQuestionList();
    }

    public void proceedBtn() {

        if (fp_username.getText().isEmpty() || fp_question.getSelectionModel().getSelectedItem() == null
                || fp_answer.getText().isEmpty()) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else {

            String selectData = "SELECT username, question, answer FROM employee WHERE username = ? AND question = ? AND answer = ?";
            connect = (Connection) Database.connectDB();

            try {

                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, fp_username.getText());
                prepare.setString(2, (String) fp_question.getSelectionModel().getSelectedItem());
                prepare.setString(3, fp_answer.getText());

                result = prepare.executeQuery();

                if (result.next()) {
                    np_newPassForm.setVisible(true);
                    fp_questionForm.setVisible(false);
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Information");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void changePassBtn() {

        if (np_newPassword.getText().isEmpty() || np_confirmPassword.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {

            if (np_newPassword.getText().equals(np_confirmPassword.getText())) {
                String getDate = "SELECT date FROM employee WHERE username = '"
                        + fp_username.getText() + "'";

                connect = (Connection) Database.connectDB();

                try {

                    prepare = connect.prepareStatement(getDate);
                    result = prepare.executeQuery();

                    String date = "";
                    if (result.next()) {
                        date = result.getString("date");
                    }

                    String updatePass = "UPDATE employee SET password = '"
                            + np_newPassword.getText() + "', question = '"
                            + fp_question.getSelectionModel().getSelectedItem() + "', answer = '"
                            + fp_answer.getText() + "', date = '"
                            + date + "' WHERE username = '"
                            + fp_username.getText() + "'";

                    prepare = connect.prepareStatement(updatePass);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully changed Password!");
                    alert.showAndWait();

                    si_loginForm.setVisible(true);
                    np_newPassForm.setVisible(false);

                    // TO CLEAR FIELDS
                    np_confirmPassword.setText("");
                    np_newPassword.setText("");
                    fp_question.getSelectionModel().clearSelection();
                    fp_answer.setText("");
                    fp_username.setText("");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Not match");
                alert.showAndWait();
            }
        }
    }

    public void forgotPassQuestionList() {

        List<String> listQ = new ArrayList<>();

        for (String data : userTypeList) {
            listQ.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listQ);
        fp_question.setItems(listData);

    }

    public void backToLoginForm(){
        si_loginForm.setVisible(true);
        fp_questionForm.setVisible(false);
    }

    public void backToQuestionForm(){
        fp_questionForm.setVisible(true);
        np_newPassForm.setVisible(false);
    }

    public void switchForm(ActionEvent event) {

        TranslateTransition slider = new TranslateTransition();

        if (event.getSource() == side_CreateBtn) {
            slider.setNode(side_form);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.001));

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(true);
                side_CreateBtn.setVisible(false);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);

                userTypeListMethod();
            });

            slider.play();
        } else if (event.getSource() == side_alreadyHave) {
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.001));

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(false);
                side_CreateBtn.setVisible(true);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
                //clientTypeList();

            });

            slider.play();

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clientTypeList();

    }

}