package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import user.User;

import java.io.IOException;

public class SettingController {

    @FXML
    private Button returnButton;
    @FXML
    private Button passwordSettingButton;
    @FXML
    private Button PrivacyPolicyButton;

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
    public void initialize() {
        // Update the label at the top using the BaseController's method
        //updateUserInfo(userLabel);

        // Populate fields with user data from UserSession
        populateUserFields();
    }

    private void populateUserFields() {
        User currentUser = UserSession.getInstance().getUser();

        // Check if a user is logged in
        if (currentUser != null) {
            fullnameField.setText(currentUser.getFullName()); // Assuming `getFullname()` exists in your User class
            usernameField.setText(currentUser.getUserName());
            dobField.setText(currentUser.getDateOfBirth()); // Assuming `getDateOfBirth()` exists in your User class
            emailField.setText(currentUser.getEmail()); // Assuming `getEmail()` exists in your User class
            phoneField.setText(currentUser.getPhoneNumber()); // Assuming `getPhone()` exists in your User class
        } else {
            // Handle case where no user is logged in
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
}
