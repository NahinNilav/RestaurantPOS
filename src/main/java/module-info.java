module org.example.restaurant_management_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jasperreports;

    opens org.example.restaurant_management_system to javafx.fxml;
    opens org.example.restaurant_management_system.Model;
    opens org.example.restaurant_management_system.Controllers to javafx.fxml;
    exports org.example.restaurant_management_system;
    exports org.example.restaurant_management_system.Controllers;
}