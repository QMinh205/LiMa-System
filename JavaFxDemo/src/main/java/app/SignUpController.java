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


public class SignUpController {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmpassword;

    @FXML
    private TextField email;

    @FXML
    private Button signUpConfirmButton;

    @FXML
    private Button signUpReturnButton;

    @FXML
    private TextField safeCode;

    @FXML
    protected void onSignUpConfirmButton() {
        String user = username.getText();
        String pass = password.getText();
        String confirmPass = confirmpassword.getText();
        String emailAddress = email.getText();
        String safecode = safeCode.getText();

        // check điền full ô
        if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || emailAddress.isEmpty() || safecode.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        // check nhập lại password
        if (!pass.equals(confirmPass)) {
            showAlert("Error", "Passwords do not match!");
            return;
        }

        // đăng ký vào database
        if (registerUser(user, pass, emailAddress, safecode)) {
            showAlert("Success", "Registration successful!");
        } else {
            showAlert("Error", "Registration failed! Please try again.");
        }
    }

    @FXML
    protected void onSignUpReturnButton() {
        try {
            // về màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) signUpReturnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not load the login screen.");
        }
    }

    private boolean registerUser(String username, String password, String email, String safecode) {
        String url = "jdbc:mysql://localhost:3306/user_db";
        String dbUser = "root";
        String dbPassword = "Phong416ct5x2";

        try (Connection connection = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String sql = "INSERT INTO users (username, password, email, safecode) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, safecode);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; //true nếu có ít nhất 1 hàng được thêm vào
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // nếu có lỗi xảy ra, trả về false
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

