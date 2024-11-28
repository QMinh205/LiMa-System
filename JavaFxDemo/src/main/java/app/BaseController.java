package app;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import user.User;

import java.io.IOException;

public abstract class BaseController {

    protected void loadScene(String fxmlFile, String title, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void setupButton(JFXButton button, String fxmlFile, String title) {
        button.setOnAction(event -> loadScene(fxmlFile, title, button));
    }

    protected void setupGameButton(JFXButton gameButton) {
        gameButton.setOnAction(event -> {
            try {
                Stage stage = (Stage) gameButton.getScene().getWindow();
                app.Game game = new app.Game(stage);
                stage.setScene(game.createIntroScene());
                stage.setTitle("Game");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected void updateUserInfo(Label userLabel) {
        // Retrieve the current user from UserSession
        User currentUser = UserSession.getInstance().getUser();

        // Update the label if the user exists in the session
        if (currentUser != null) {
            userLabel.setText("Name: " + currentUser.getUserName() + " - ID: " + currentUser.getUserId());
        }
    }
}
