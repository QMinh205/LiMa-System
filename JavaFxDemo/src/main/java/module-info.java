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
    requires java.desktop;
    requires javafx.media;

    opens app to javafx.fxml;
    exports app;
}