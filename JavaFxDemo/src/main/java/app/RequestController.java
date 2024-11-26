package app;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestController extends BaseController {

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton requestButton;

    @FXML
    private JFXButton informationButton;

    @FXML
    private JFXButton favouriteButton;

    @FXML
    private JFXButton gameButton;

    @FXML
    private JFXButton settingButton;

    @FXML
    private JFXButton deleteButton;

    @FXML
    private JFXButton fixButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label userLabel;

    @FXML
    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(homeButton);
        ButtonSoundUtil.addClickSound(requestButton);
        ButtonSoundUtil.addClickSound(informationButton);
        ButtonSoundUtil.addClickSound(favouriteButton);
        ButtonSoundUtil.addClickSound(gameButton);
        ButtonSoundUtil.addClickSound(settingButton);
        ButtonSoundUtil.addClickSound(deleteButton);
        ButtonSoundUtil.addClickSound(fixButton);
        ButtonSoundUtil.addClickSound(addButton);

        updateUserInfo(userLabel);
        // Use the setupButton method from BaseController
        setupButton(homeButton, "User-Home.fxml", "Home");
        setupButton(requestButton, "Request.fxml", "Request");
        setupButton(informationButton, "Information.fxml", "Information");
        setupButton(favouriteButton, "Favourite.fxml", "Favourite");
        setupButton(settingButton, "Setting.fxml", "Setting");

        setupGameButton(gameButton);
    }

    @FXML
    private void onFixButtonClicked() {
        try {
            // Load the UserHome.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fixRequest.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) fixButton.getScene().getWindow();
            stage.setScene(new Scene(root)); // Adjust the window size here
            stage.setTitle("Fix Request");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeleteButtonClicked() {
        try {
            // Load the UserHome.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("deleteRequest.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) deleteButton.getScene().getWindow();
            stage.setScene(new Scene(root)); // Adjust the window size here
            stage.setTitle("Delete Request");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddButtonClicked() {
        try {
            // Load the UserHome.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBook.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1500, 750)); // Adjust the window size here
            stage.setTitle("Add Book");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
