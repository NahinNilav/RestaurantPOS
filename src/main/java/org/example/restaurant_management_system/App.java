package org.example.restaurant_management_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
     //Stage primarystage = new Stage();
    @Override
    public void start(Stage stage) throws Exception {
        try {
            //this.primarystage = stage;
            FXMLLoader fxmlLoader = new  FXMLLoader(getClass().getResource("/view/Authentication.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Restaurant Management System");
            //AuthenticationController controller = fxmlLoader.getController();
            //stage.setScene(new Scene(root));
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        }
        catch (Exception e)
        {
            System.out.println(e);
        }


    }

    public static void main(String[] args) {
        launch();
    }

}