package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;

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
    private TextField fullName;

    @FXML
    private TextField phoneNumber;

    @FXML
    private DatePicker dateOfBirth;

    @FXML
    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(signUpConfirmButton);
        ButtonSoundUtil.addClickSound(signUpReturnButton);

    }

    @FXML
    protected void onSignUpConfirmButton() {
        String user = username.getText();
        String pass = password.getText();
        String confirmPass = confirmpassword.getText();
        String emailAddress = email.getText();
        String safecode = safeCode.getText();
        String fullNameText = fullName.getText();
        String phoneNumberText = phoneNumber.getText();
        LocalDate dob = dateOfBirth.getValue();

        // kiểm tra các trường có rỗng không
        if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || emailAddress.isEmpty() || safecode.isEmpty() || fullNameText.isEmpty() || phoneNumberText.isEmpty() || dob == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return;
        }

        // kiểm tra tuổi người dùng
        if (!isEligibleAge(dob)) {
            showAlert(Alert.AlertType.ERROR, "Error", "You must be at least 18 years old to register.");
            return;
        }

        // kiểm tra khớp mật khẩu
        if (!pass.equals(confirmPass)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
            return;
        }

        // đăng ký người dùng
        if (registerUser(user, pass, fullNameText, phoneNumberText, dob, emailAddress, safecode)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful!");
            clearAllFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Registration failed! Please try again.");
        }
    }

    private void clearAllFields() {
        username.clear();
        password.clear();
        confirmpassword.clear();
        email.clear();
        safeCode.clear();
        fullName.clear();
        phoneNumber.clear();
        dateOfBirth.setValue(null);
    }

    @FXML
    protected void onSignUpReturnButton() {
        try {
            // quay lại màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) signUpReturnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the login screen.");
        }
    }

    private boolean registerUser(String userName, String password, String fullName, String phoneNumber, LocalDate dateOfBirth, String email, String safecode) {
        Connection connection = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false); // Tắt auto commit

            // Kiểm tra username hoặc email đã tồn tại
            String checkUserQuery = "SELECT COUNT(*) FROM users WHERE userName = ? OR email = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkUserQuery)) {
                checkStmt.setString(1, userName);
                checkStmt.setString(2, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "The username or email is already taken.");
                    connection.rollback(); // Rollback nếu phát hiện lỗi
                    return false;
                }
            }

            // Thêm người dùng vào cơ sở dữ liệu
            String sql = "INSERT INTO users (fullName, userName, password, email, phoneNumber, safeCode, dateOfBirth) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, fullName);
                statement.setString(2, userName);
                statement.setString(3, password);
                statement.setString(4, email);
                statement.setString(5, phoneNumber);
                statement.setString(6, safecode);
                statement.setDate(7, Date.valueOf(dateOfBirth)); // Chuyển đổi LocalDate sang SQL Date

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    connection.commit(); // Commit nếu thành công
                    return true;
                } else {
                    connection.rollback(); // Rollback nếu không có dòng nào được thêm
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            showAlert(Alert.AlertType.ERROR, "Error", "Database error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isEligibleAge(LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(dob, currentDate).getYears();
        return age >= 18;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
