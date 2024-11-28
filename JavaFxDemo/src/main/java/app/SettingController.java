package app;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import user.User;

import java.io.IOException;
import java.util.Optional;

public class SettingController {

    @FXML
    private Button returnButton;
    @FXML
    private Button passwordSettingButton;
    @FXML
    private Button PrivacyPolicyButton;
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
    public void initialize() {
        ButtonSoundUtil.addClickSound(returnButton);
        ButtonSoundUtil.addClickSound(passwordSettingButton);
        ButtonSoundUtil.addClickSound(returnButton);
        ButtonSoundUtil.addClickSound(PrivacyPolicyButton);
        ButtonSoundUtil.addClickSound(saveButton);

        //ButtonSoundUtil.addClickSound(PrivacyPolicyButton);
        //ButtonSoundUtil.addClickSound(logOutButton);
        // Update the label at the top using the BaseController's method
        //updateUserInfo(userLabel);

        // Populate fields with user data from UserSession
        populateUserFields();
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("User-Home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root)); // Adjust the window size here
            stage.setTitle("User Home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPasswordSettingButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("passwordSetting.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) passwordSettingButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Password Setting");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                Parent root = loader.load();

                // Get the current stage and set the new scene
                Stage stage = (Stage) returnButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // If the user cancels, do nothing
    }

}
