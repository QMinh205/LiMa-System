package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PasswordSettingController {

    @FXML
    private Button returnButton;
    @FXML
    private Button settingButton;
    @FXML
    private Button passwordSettingButton;
    @FXML
    private Button PrivacyPolicyButton;

    @FXML
    private void onReturnButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("User-Home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSettingButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Setting.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) settingButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Setting");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
