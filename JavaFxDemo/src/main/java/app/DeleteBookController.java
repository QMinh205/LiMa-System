package app;

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

public class DeleteBookController extends BaseController{
    @FXML
    private Button returnButton;

    @FXML
    private TextField bookIdField;

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
    private void onReturnButtonClicked() {
        loadScene("Request.fxml", "Request", returnButton);
    }

    @FXML
    private void onConfirmButtonClicked() {
        String bookId = bookIdField.getText();
        String confirmationText = confirmationField.getText();

        if (bookId.isEmpty() || confirmationText.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        if (!confirmationText.equalsIgnoreCase("DELETE")) {
            statusLabel.setText("Confirmation text must be 'DELETE'.");
            return;
        }

        deleteBookFromDatabase(bookId);
    }

    private void deleteBookFromDatabase(String bookId) {
        String deleteQuery = "DELETE FROM books WHERE book_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setString(1, bookId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                statusLabel.setText("Book successfully deleted.");
                statusLabel.setStyle("-fx-text-fill: green;");
            } else {
                statusLabel.setText("No book found with the given ID.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("An error occurred while deleting the book.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
