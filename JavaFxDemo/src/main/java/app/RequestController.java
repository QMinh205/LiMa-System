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
    private JFXButton searchButton;

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
    private JFXButton banButton;

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
        ButtonSoundUtil.addClickSound(banButton);
        ButtonSoundUtil.addClickSound(searchButton);

        updateUserInfo(userLabel);
        // Use the setupButton method from BaseController
        setupButton(homeButton, "User-Home.fxml", "Home");
        setupButton(requestButton, "Request.fxml", "Request");
        setupButton(informationButton, "Information.fxml", "Information");
        setupButton(favouriteButton, "IssueBook.fxml", "Issue & Return Book");
        setupButton(settingButton, "Setting.fxml", "Setting");
        setupButton(searchButton, "SearchBar.fxml", "SearchBar");

        setupGameButton(gameButton);
    }

    @FXML
    private void onFixButtonClicked() {
        loadScene("fixRequest.fxml", "Fix Request", fixButton);
    }

    @FXML
    private void onDeleteButtonClicked() {
        loadScene("deleteRequest.fxml", "Delete Request", deleteButton);
    }

    @FXML
    private void onAddButtonClicked() {
        loadScene("AddBook.fxml", "Add Book", addButton);
    }

    @FXML
    private void onBanButtonClicked() {
        loadScene("BanUserRequest.fxml", "Ban User Request", banButton);
    }
}
