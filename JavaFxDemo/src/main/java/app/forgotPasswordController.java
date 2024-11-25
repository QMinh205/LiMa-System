package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class forgotPasswordController {
    @FXML
    private Button passwordRecoveryReturnButton;

    @FXML
    private Button applyPasswordRecoveryButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField safecodeField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    protected void onPasswordRecoveryReturnButton() {
        try {
            // Quay lại màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) passwordRecoveryReturnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onApplyPasswordRecoveryButton() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String safecode = safecodeField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // kiểm tra các trường có được điền đầy đủ ko
        if (username.isEmpty() || email.isEmpty() || safecode.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return;
        }

        // kiểm tra mật khẩu nhập lại có khớp ko
        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
            return;
        }

        // kiểm tra safecode và cập nhật mật khẩu
        if (validateSafecode(username, email, safecode)) {
            if (updatePassword(username, newPassword)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update password!");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid username, email, or safecode!");
        }
    }

    private boolean validateSafecode(String username, String email, String safecode) {
        String url = "jdbc:mysql://localhost:3306/new_dtb";
        String dbUser = "root";
        String dbPassword = "Phong416ct5x2";
        String query = "SELECT COUNT(*) FROM users WHERE userName = ? AND email = ? AND safeCode = ?";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, safecode);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // true nếu tìm thấy >0 kết quả
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // false nếu không hợp lệ
    }

    private boolean updatePassword(String username, String newPassword) {
        String url = "jdbc:mysql://localhost:3306/new_dtb";
        String dbUser = "root";
        String dbPassword = "Phong416ct5x2";
        String query = "UPDATE users SET password = ? WHERE userName = ?";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newPassword);
            statement.setString(2, username);

            return statement.executeUpdate() > 0; // true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // false nếu có lỗi
    }

    private void clearFields() {
        usernameField.clear();
        emailField.clear();
        safecodeField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
