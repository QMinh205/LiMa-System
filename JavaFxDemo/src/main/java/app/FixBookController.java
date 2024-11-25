package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class FixBookController {

    @FXML
    private Button returnButton;

    @FXML
    private void onReturnButtonClicked() {
        try {
            // Load request.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Request.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) returnButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
