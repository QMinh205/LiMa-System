module com.example.javafxdemo {
    requires javafx.fxml;

    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires com.jfoenix;
    requires com.google.api.client;
    requires com.google.gson;
    requires javafx.controls;

    opens com.example.javafxdemo to javafx.fxml;
    exports com.example.javafxdemo;
}