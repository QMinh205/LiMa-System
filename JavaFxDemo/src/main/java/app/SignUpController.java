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

        // kiểm tra email
        if (!emailAddress.contains("@")) {
           showAlert(Alert.AlertType.ERROR, "Error", "Email address must have @!");
           return;
        }

        // kiểm tra tuổi người dùng
        if (!isEligibleAge(dob)) {
            showAlert(Alert.AlertType.ERROR, "Error", "You must be at least 18 years old to register.");
            return;
        }

        // ktra sđt
        if (!isNumeric(phoneNumberText)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Phone number must be numeric!");
            return;
        }

        // ktra độ dài mk
        if (pass.length() < 5 || confirmPass.length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password must be at least 5 characters long!");
            return;
        }

        // kiểm tra khớp mật khẩu
        if (!pass.equals(confirmPass)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
            return;
        }

        // safecode có cả chữ và số
        if (!safecode.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Safe code must contain both letters and numbers!");
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

    private boolean isNumeric(String str) {
        return str.matches("\\d+"); //d là digit, + là nhiều số
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

            // Kiểm tra username đã tồn tại
            String checkUsernameQuery = "SELECT COUNT(*) FROM users WHERE userName = ?";
            try (PreparedStatement checkUsernameStmt = connection.prepareStatement(checkUsernameQuery)) {
                checkUsernameStmt.setString(1, userName);
                ResultSet rsUsername = checkUsernameStmt.executeQuery();
                if (rsUsername.next() && rsUsername.getInt(1) > 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "The username is already taken.");
                    connection.rollback(); // Rollback nếu phát hiện lỗi
                    return false;
                }
            }

            // Kiểm tra email đã tồn tại
            String checkEmailQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
            try (PreparedStatement checkEmailStmt = connection.prepareStatement(checkEmailQuery)) {
                checkEmailStmt.setString(1, email);
                ResultSet rsEmail = checkEmailStmt.executeQuery();
                if (rsEmail.next() && rsEmail.getInt(1) > 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "The email is already taken.");
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
