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

    private boolean registerUser(String userName, String password, String email, String safecode) {
        String url = "jdbc:mysql://localhost:3306/new_dtb";
        String dbUser = "root";
        String dbPassword = "Phong416ct5x2";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, dbUser, dbPassword);
            // bắt đầu một giao dịch
            connection.setAutoCommit(false); // tắt tự động commit auto_increment

            // kiểm tra xem username và email có bị trùng không
            String checkUserQuery = "SELECT COUNT(*) FROM users WHERE userName = ? OR email = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkUserQuery)) {
                checkStmt.setString(1, userName);
                checkStmt.setString(2, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    showAlert("Error", "The username or email is already taken.");
                    connection.rollback(); // Rollback nếu có lỗi
                    return false;
                }
            }

            // sql để chèn người dùng vào cơ sở dữ liệu
            String sql = "INSERT INTO users (username, password, email, safecode) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userName);
                statement.setString(2, password);
                statement.setString(3, email);
                statement.setString(4, safecode);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    connection.commit(); // commit nếu chèn thành công
                    return true;
                } else {
                    connection.rollback(); // rollback nếu không có bản ghi nào được thêm
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                // nếu có lỗi xảy ra thì rollback giao dịch
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            // các lỗi chi tiết
            String errorMessage = "Database error: " + e.getMessage();
            if (e.getMessage().contains("Duplicate entry")) {
                showAlert("Error", "The username or email is already taken.");
            } else {
                showAlert("Error", errorMessage);
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                //trả lại đúng chế độ của commit sau khi commit xong
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

