package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class BookInformationController {
    @FXML
    private JFXButton returnButton;

    @FXML
    private JFXButton searchButton;

    @FXML
    private TextField searchTextField;

    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(returnButton);
        ButtonSoundUtil.addClickSound(searchButton);
    }

    @FXML
    private void onReturnButtonClicked() {
        try {
            // Load the UserHome.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Information.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Information");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
