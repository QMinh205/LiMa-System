package com.example.javafxdemo;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InformationController {
    @FXML
    private BorderPane mainBorderPane;

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
    private JFXButton searchButton;

    private final Map<String, String> buttonToFXMLMap = new HashMap<>();

    @FXML
    public void initialize() {
        setupHomeButton();
        setupRequestButton();
        setupInformationButton();
        setupFavouriteButton();
        setupGameButton();
        setupSettingButton();
        setupSearchButton();
    }

    private void setupSearchButton() {
        searchButton.setOnAction(event -> {
            loadScene("SearchBar.fxml", "SearchBar", searchButton);
        });
    }
    private void setupHomeButton() {
        homeButton.setOnAction(event -> {
            loadScene("Home.fxml", "Home", homeButton);
        });
    }

    private void setupRequestButton() {
        requestButton.setOnAction(event -> {
            loadScene("Request.fxml", "Request", requestButton);
        });
    }

    private void setupInformationButton() {
        informationButton.setOnAction(event -> {
            loadScene("Information.fxml", "Information", informationButton);
        });
    }

    private void setupFavouriteButton() {
        favouriteButton.setOnAction(event -> {
            loadScene("Favourite.fxml", "Favourite", favouriteButton);
        });
    }

    private void setupGameButton() {
        gameButton.setOnAction(event -> {
            loadScene("Game.fxml", "Game", gameButton);
        });
    }

    private void setupSettingButton() {
        settingButton.setOnAction(event -> {
            loadScene("Setting.fxml", "Setting", settingButton);
        });
    }

    private void loadScene(String fxmlFile, String title, JFXButton button) {
        try {
            // load fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // lấy stage hiện tại và đặt scene mới
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
