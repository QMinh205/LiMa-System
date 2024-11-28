package app;

import com.jfoenix.controls.JFXButton;
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

public class InformationController extends BaseController {

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
    private Label userLabel;

    @FXML
    private JFXButton userButton;

    @FXML
    private JFXButton booksButton;

    @FXML
    private JFXButton borrowAndReturnButton;

    @FXML
    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(homeButton);
        ButtonSoundUtil.addClickSound(requestButton);
        ButtonSoundUtil.addClickSound(informationButton);
        ButtonSoundUtil.addClickSound(favouriteButton);
        ButtonSoundUtil.addClickSound(gameButton);
        ButtonSoundUtil.addClickSound(settingButton);
        ButtonSoundUtil.addClickSound(searchButton);
        ButtonSoundUtil.addClickSound(booksButton);
        ButtonSoundUtil.addClickSound(userButton);
        ButtonSoundUtil.addClickSound(borrowAndReturnButton);

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
    private void onUserButtonClicked() {
        loadScene("UserInformation.fxml", "Users Information", userButton);
    }

    @FXML
    private void onBooksButtonClicked() {
        loadScene("BookInformation.fxml", "Books Information", booksButton);
    }

    @FXML
    private void onBorrowButtonClicked() {
        loadScene("BorrowBookInformation.fxml", "Borrowed & Returned Information", borrowAndReturnButton);
    }
}
