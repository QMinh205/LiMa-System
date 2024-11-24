package app;

import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserHome extends BaseController {

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

    @FXML
    public void initialize() {
        // Use the setupButton method from BaseController
        setupButton(homeButton, "User-Home.fxml", "Home");
        setupButton(requestButton, "Request.fxml", "Request");
        setupButton(informationButton, "Information.fxml", "Information");
        setupButton(favouriteButton, "Favourite.fxml", "Favourite");
        setupButton(settingButton, "Setting.fxml", "Setting");
        setupButton(searchButton, "SearchBar.fxml", "SearchBar");

        setupGameButton(gameButton);
    }
}
