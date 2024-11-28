package app;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BanUserController {

    @FXML
    private JFXButton returnButton;

    @FXML
    private TextField memberIdField;

    @FXML
    private TextField confirmationField;

    @FXML
    private Button confirmButton;

    @FXML
    private Label statusLabel;

    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(confirmButton);
        ButtonSoundUtil.addClickSound(returnButton);

    }

    @FXML
    public void onReturnButtonClicked() {
        try {
            // Load the UserHome.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Request.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root)); // Adjust the window size here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onConfirmButtonClicked() {
        String memberId = memberIdField.getText();
        String confirmationText = confirmationField.getText();

        if (memberId.isEmpty() || confirmationText.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        if (!confirmationText.equalsIgnoreCase("DELETE")) {
            statusLabel.setText("Confirmation text must be 'DELETE'.");
            return;
        }

        deleteMemberFromDatabase(memberId);
    }

    public void deleteMemberFromDatabase(String memberId) {
        String deleteQuery = "DELETE FROM members WHERE member_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setString(1, memberId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                statusLabel.setText("Member successfully deleted.");
                statusLabel.setStyle("-fx-text-fill: green;");
            } else {
                statusLabel.setText("No member found with the given ID.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("An error occurred while deleting member.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

}
