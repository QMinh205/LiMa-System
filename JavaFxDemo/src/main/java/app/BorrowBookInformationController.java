package app;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BorrowBookInformationController {
    @FXML
    private JFXButton returnButton;

    @FXML
    private JFXButton searchUserIdButton;

    @FXML
    private JFXButton searchBookIdButton;

    @FXML
    private JFXButton searchBorowIdButton;

    public void initialize() {
        ButtonSoundUtil.addClickSound(returnButton);
        ButtonSoundUtil.addClickSound(searchUserIdButton);
        ButtonSoundUtil.addClickSound(searchBookIdButton);
        ButtonSoundUtil.addClickSound(searchBorowIdButton);
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
