package app;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class BaseController {

    protected void loadScene(String fxmlFile, String title, JFXButton button) {
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
}
