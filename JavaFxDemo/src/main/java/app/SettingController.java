package app;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import user.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public class SettingController extends BaseController {

    @FXML
    private Button returnButton;
    @FXML
    private Button passwordSettingButton;
    @FXML
    private Button saveButton;

    @FXML
    private TextField fullnameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField dobField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private Label profileLabel;

    @FXML
    private JFXButton logOutButton;

    @FXML
    private Button changeImageButton;

    @FXML
    private ImageView profileImageView;

    private static Image profileImage;

    public static Image getProfileImage() {
        return profileImage;
    }

    public static void setProfileImage(Image image) {
        profileImage = image;
    }

    @FXML
    public void initialize() {
        if (profileImage != null) {
            profileImageView.setImage(profileImage);
        }

        ButtonSoundUtil.addClickSound(returnButton);
        ButtonSoundUtil.addClickSound(passwordSettingButton);
        ButtonSoundUtil.addClickSound(returnButton);
        ButtonSoundUtil.addClickSound(saveButton);

        //ButtonSoundUtil.addClickSound(PrivacyPolicyButton);
        //ButtonSoundUtil.addClickSound(logOutButton);
        // Update the label at the top using the BaseController's method
        //updateUserInfo(userLabel);

        // Populate fields with user data from UserSession
        populateUserFields();
    }

    @FXML
    private void onChangeImageButtonClicked() {
        // Create a file chooser to select an image file
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif")
        );

        // Open file chooser and let the user select a file
        File selectedFile = fileChooser.showOpenDialog(changeImageButton.getScene().getWindow());

        // If a file is selected, update the ImageView with the chosen image
        if (selectedFile != null) {
            try {
                // Create a new Image object with the selected file
                profileImage = new Image(new FileInputStream(selectedFile));
                // Set the new image in the ImageView
                profileImageView.setImage(profileImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateUserFields() {
        User currentUser = UserSession.getInstance().getUser();

        // Check if a user is logged in
        if (currentUser != null) {
            profileLabel.setText(currentUser.getUserName());
            fullnameField.setText(currentUser.getFullName()); // Assuming `getFullname()` exists in your User class
            usernameField.setText(currentUser.getUserName());
            dobField.setText(currentUser.getDateOfBirth()); // Assuming `getDateOfBirth()` exists in your User class
            emailField.setText(currentUser.getEmail()); // Assuming `getEmail()` exists in your User class
            phoneField.setText(currentUser.getPhoneNumber()); // Assuming `getPhone()` exists in your User class
        } else {
            // Handle case where no user is logged in
            profileLabel.setText("");
            fullnameField.setText("");
            usernameField.setText("");
            dobField.setText("");
            emailField.setText("");
            phoneField.setText("");
        }
    }

    @FXML
    private void onReturnButtonClicked() {
        loadScene("User-Home.fxml", "User Home", returnButton);
    }

    @FXML
    private void onPasswordSettingButtonClicked() {
        loadScene("passwordSetting.fxml","Password Setting", passwordSettingButton);
    }

    @FXML
    private void onLogoutButtonClicked() {
        // Create a confirmation dialog
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Logout Confirmation");
        confirmationDialog.setHeaderText("Are you sure you want to log out?");
        confirmationDialog.setContentText("Press OK to confirm or Cancel to stay logged in.");

        // Customize the dialog buttons
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationDialog.getButtonTypes().setAll(okButton, cancelButton);

        // Show the dialog and capture the user's choice
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            // User confirmed logout, navigate to the login screen
            loadScene("login.fxml", "Login", logOutButton);
        }
        // If the user cancels, do nothing
    }
}
