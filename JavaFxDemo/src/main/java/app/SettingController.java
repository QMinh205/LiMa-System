package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingController {

    @FXML
    private Button returnButton;

    @FXML
    private void onReturnButtonClicked() {
        try {
            // Load the UserHome.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("User-Home.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root)); // Adjust the window size here
            stage.setTitle("User Home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
