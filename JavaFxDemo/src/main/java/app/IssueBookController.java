package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class IssueBookController extends BaseController {
    @FXML
    private JFXButton confirmButton;

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
    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(homeButton);
        ButtonSoundUtil.addClickSound(requestButton);
        ButtonSoundUtil.addClickSound(informationButton);
        ButtonSoundUtil.addClickSound(favouriteButton);
        ButtonSoundUtil.addClickSound(gameButton);
        ButtonSoundUtil.addClickSound(settingButton);
        ButtonSoundUtil.addClickSound(confirmButton);

        // Use the setupButton method from BaseController
        setupButton(homeButton, "User-Home.fxml", "Home");
        setupButton(requestButton, "Request.fxml", "Request");
        setupButton(informationButton, "Information.fxml", "Information");
        setupButton(favouriteButton, "IssueBook.fxml", "Issue Book");
        setupButton(settingButton, "Setting.fxml", "Setting");

        setupGameButton(gameButton);
    }

}
