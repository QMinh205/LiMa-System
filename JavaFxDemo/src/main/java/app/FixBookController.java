package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FixBookController {

    @FXML
    private Button returnButton;

    @FXML
    private TextField bookIdField, titleField, authorField, publisherField, descriptionField, imageUrlField,
            publishedDateField, pageCountField, categoriesField, averageRatingField, preViewLinkField; // Field to input book ID
    @FXML
    private Label oldTitleField, oldAuthorField, oldPublisherField, oldDescriptionField, oldImageUrlField,
            oldPublishedDateField, oldPageCountField, oldCategoriesField, oldAverageRatingField, oldPreViewLinkField; // Field to input book ID
    @FXML
    private Button okButton; // Button to fetch book data
    @FXML
    private Button confirmButton; // Button to update book data

    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(returnButton);

        // Handle OK button click to fetch book data
        okButton.setOnAction(event -> fetchBookData());

        // Handle Confirm button click to update book data
        confirmButton.setOnAction(event -> updateBookData());
    }

    private void fetchBookData() {
        String bookId = bookIdField.getText().trim();

        if (bookId.isEmpty()) {
            showAlert("Error", "Book ID cannot be empty.");
            return;
        }

        String query = "SELECT * FROM books WHERE book_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Populate fields with the book's data
                oldTitleField.setText(resultSet.getString("title"));
                oldAuthorField.setText(resultSet.getString("author"));
                oldPublisherField.setText(resultSet.getString("publisher"));
                oldDescriptionField.setText(resultSet.getString("description"));
                oldImageUrlField.setText(resultSet.getString("image_url"));
                oldPublishedDateField.setText(resultSet.getString("published_date"));
                oldPageCountField.setText(resultSet.getString("page_count"));
                oldCategoriesField.setText(resultSet.getString("categories"));
                oldAverageRatingField.setText(resultSet.getString("average_rating"));
                oldPreViewLinkField.setText(resultSet.getString("preview_link"));
            } else {
                showAlert("Not Found", "No book found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch book data.");
        }
    }

    private void updateBookData() {
        String bookId = bookIdField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String publisher = publisherField.getText().trim();
        String description = descriptionField.getText().trim();
        String imageUrl = imageUrlField.getText().trim();
        String publishedDate = publishedDateField.getText().trim();
        String pageCount = pageCountField.getText().trim();
        String categories = categoriesField.getText().trim();
        String averageRating = averageRatingField.getText().trim();
        String previewLink = preViewLinkField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || publisher.isEmpty()) {
            showAlert("Error", "Please fill out the first, second and third fields.");
            return;
        }

        int page;
        try {
            page = Integer.parseInt(pageCount);
        } catch (NumberFormatException e) {
            showAlert("Error", "Page Count must be a valid number.");
            return;
        }

        String query = "UPDATE books SET title = ?, author = ?, publisher = ?, description = ?, image_url = ?, " +
                "published_date = ?, page_count = ?, categories = ?, average_rating = ?, preview_link = ? WHERE book_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, publisher);
            statement.setString(4, description);
            statement.setString(5, imageUrl);
            statement.setString(6, publishedDate);
            statement.setString(7, pageCount);
            statement.setString(8, categories);
            statement.setString(9, averageRating);
            statement.setString(10, previewLink);
            statement.setString(11, bookId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Success", "Book data updated successfully.");
            } else {
                showAlert("Error", "Failed to update book data.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating the book data.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onReturnButtonClicked() {
        try {
            // Load request.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Request.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) returnButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
