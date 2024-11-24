package app;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private Label wrongId;

    @FXML
    private ImageView img1;

    @FXML
    private Button btnNext;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private PasswordField passwordField; // chỗ nhập mật khẩu

    @FXML
    private Button registerButton;

    @FXML
    private Button forgotPasswordButton;

    @FXML
    private TextField usernameTxt;

    @FXML
    private TextField passwordTxt;

    @FXML
    private CheckBox showPasswordCheckBox; // checkbox để hiện mật khẩu

    @FXML
    private BorderPane rootPane; //  biến này để tham chiếu đến BorderPane


    // kết nối tới cơ sở dữ liệu
    private static final String DB_URL = "jdbc:mysql://localhost:3306/user_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "bisql69";

    @FXML
    protected void onLoginConfirmButton() {
        // lấy username và password
        String userName = usernameTxt.getText();
        String password = passwordField.isVisible() ? passwordField.getText() : passwordTxt.getText();

        // kiểm tra đăng nhập
        if (isValidLogin(userName, password)) {
            try {
                // nếu thành công thì chuyển màn
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/app/User-Home.fxml"));
                Parent root = fxmlLoader.load();

                // lấy stage hiện tại
                Stage currentStage = (Stage) btnLogin.getScene().getWindow();

                // hiển thị giao diện mới
                Scene scene = new Scene(root);
                currentStage.setScene(scene);
                currentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                wrongId.setText("Failed to load the next scene");
            }
        } else {
            wrongId.setText("Invalid credentials!");
        }
    }

    // hàm xác thực đăng nhập
    private boolean isValidLogin(String userName, String password) {
        String query = "SELECT * FROM users WHERE userName = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();  // nếu tìm thấy thông tin đúng, trả về true
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void initialize() {
        // chuyển sang màn hình đăng ký
        registerButton.setOnAction(event -> {
            try {
                // load fxml cho signUp
                FXMLLoader loader = new FXMLLoader(getClass().getResource("signUp.fxml"));
                Parent root = loader.load();
                Scene registerScene = new Scene(root);
                Stage stage = (Stage) registerButton.getScene().getWindow();
                stage.setScene(registerScene);
                stage.setTitle("Register");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        forgotPasswordButton.setOnAction(event -> {
            try {
                // load fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("forgotPassword.fxml"));
                Parent root = loader.load();
                // tạo một scene mới với root từ file fxml của quên mk
                Scene forgotPasswordScene = new Scene(root);
                Stage stage = (Stage) forgotPasswordButton.getScene().getWindow();
                stage.setScene(forgotPasswordScene);
                stage.setTitle("forgotPassword");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // tạo textfield mới để hiển thị mật khẩu
        passwordTxt = new TextField();
        passwordTxt.setLayoutX(passwordField.getLayoutX());
        passwordTxt.setLayoutY(passwordField.getLayoutY());
        passwordTxt.setPrefHeight(passwordField.getPrefHeight());
        passwordTxt.setPrefWidth(passwordField.getPrefWidth());
        passwordTxt.setVisible(false);

        AnchorPane parent = (AnchorPane) passwordField.getParent();

        // thêm textField vào cùng vị trí với PasswordField
        parent.getChildren().add(passwordTxt);

        // xử lý sự kiện cho checkbox
        showPasswordCheckBox.setOnAction(e -> {
            if (showPasswordCheckBox.isSelected()) {
                // khi chọn checkbox, hiện txt
                passwordTxt.setText(passwordField.getText());
                passwordField.setVisible(false);
                passwordTxt.setVisible(true);
                passwordTxt.getStyleClass().addAll(passwordField.getStyleClass());
                passwordTxt.setStyle(passwordField.getStyle());
            } else {
                // khi bỏ chọn, ẩn txt
                passwordField.setText(passwordTxt.getText());
                passwordTxt.setVisible(false);
                passwordField.setVisible(true);
            }
        });
    }
}
