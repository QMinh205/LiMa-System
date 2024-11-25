package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Library extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Library.class.getResource("Request.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 750);
        stage.initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("TamThaiTu");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}