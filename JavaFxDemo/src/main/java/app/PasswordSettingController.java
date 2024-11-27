package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import user.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordSettingController {

    @FXML
    private Button returnButton;
    @FXML
    private Button settingButton;
    @FXML
    private Button passwordSettingButton;
    @FXML
    private Button PrivacyPolicyButton;
    @FXML
    private Button saveButton;

    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField reEnterPasswordField;
    @FXML
    private PasswordField safeCodeField;

    User currentUser = UserSession.getInstance().getUser();

    @FXML
    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(returnButton);
        ButtonSoundUtil.addClickSound(settingButton);
        ButtonSoundUtil.addClickSound(PrivacyPolicyButton);
        ButtonSoundUtil.addClickSound(saveButton);
        // Populate the password fields with the current user information if required
        populateUserFields();
    }

    private void populateUserFields() {
        // Here, we assume the password fields are for updating the password,
        // so there's no need to populate them initially.
        // However, if you'd want to display a masked current password, it can be handled here.
    }

    @FXML
    private void onSaveButtonClicked() {
        // Get the input values from the fields
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String reEnteredPassword = reEnterPasswordField.getText();
        String enteredSafeCode = safeCodeField.getText();

        int loggedInUserId = currentUser.getUserId();

        // Retrieve the current password and safe code from the database
        String[] userData = getUserData(loggedInUserId);
        if (userData == null) {
            showAlert("Error", "User data not found.");
            return;
        }
        String correctCurrentPassword = userData[0]; // Retrieved from DB
        String correctSafeCode = userData[1]; // Retrieved from DB

        // Check if current password matches
        if (!currentPassword.equals(correctCurrentPassword)) {
            showAlert("Error", "The current password is incorrect.");
            return;
        }

        if (!enteredSafeCode.equals(correctSafeCode)) {
            showAlert("Error", "The safe code is incorrect.");
            return;
        }

        if (newPassword.isEmpty() || reEnteredPassword.isEmpty()) {
            showAlert("Error", "Please input new password.");
            return;
        }

        // Check if the new password and re-entered password match
        if (!newPassword.equals(reEnteredPassword)) {
            showAlert("Error", "The new password and re-entered password do not match.");
            return;
        }

        // If all checks pass, change the password in the database
        if (updatePassword(loggedInUserId, newPassword)) {
            showAlert("Success", "Your password has been changed successfully.");
            clearFields(); // Clear the fields after successful change
        } else {
            showAlert("Error", "Failed to change the password.");
        }
    }

    private String[] getUserData(int userId) {
        String query = "SELECT password, safeCode FROM users WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String password = resultSet.getString("password");
                String safeCode = resultSet.getString("safeCode");
                return new String[]{password, safeCode};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean updatePassword(int userId, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, newPassword);
            statement.setInt(2, userId);
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0; // Return true if the password was updated
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if the update failed
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        currentPasswordField.clear();
        newPasswordField.clear();
        reEnterPasswordField.clear();
        safeCodeField.clear();
    }

    @FXML
    private void onReturnButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("User-Home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSettingButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Setting.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) settingButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Setting");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
