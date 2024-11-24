package app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBookController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField publishedDateField;
    @FXML
    private TextField pageCountField;
    @FXML
    private TextField categoriesField;
    @FXML
    private TextField ratingField;
    @FXML
    private TextField imageUrlField;
    @FXML
    private TextField previewLinkField;
    @FXML
    private Button cancelButton;

    // Method to handle Add Book action
    @FXML
    public void handleAddBookAction() {
        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        String description = descriptionField.getText();
        String imageUrl = imageUrlField.getText();
        String publishedDate = publishedDateField.getText();
        String pageCount = pageCountField.getText();
        String categories = categoriesField.getText();
        String rating = ratingField.getText();
        String previewLink = previewLinkField.getText();

        // Validate inputs
        if (title.isEmpty() || author.isEmpty() || publisher.isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "Title, Author, and Publisher are required.");
            return;
        }

        // Save to the database
        saveBookToDatabase(title, author, publisher, description, imageUrl, publishedDate, pageCount, categories, rating, previewLink);
    }

    // Method to handle Cancel action (close the form or go back)
    @FXML
    public void handleCancelAction() {
        // You can navigate to the previous screen or just close the form
        // For example:
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void saveBookToDatabase(String title, String author, String publisher, String description, String imageUrl,
                                    String publishedDate, String pageCount, String categories, String rating, String previewLink) {
        String url = "jdbc:mysql://localhost:3306/user_db";
        String user = "root";
        String password = "bisql69";

        String insertBookQuery = "INSERT INTO books (title, author, publisher, description, image_url, published_date, " +
                "page_count, categories, average_rating, preview_link) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(insertBookQuery)) {

            // Validate numeric fields
            int pageCountValue = pageCount.isEmpty() ? 0 : Integer.parseInt(pageCount);
            double ratingValue = rating.isEmpty() ? 0.0 : Double.parseDouble(rating);

            // Set parameters
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setString(3, publisher);
            statement.setString(4, description);
            statement.setString(5, imageUrl);
            statement.setString(6, publishedDate);
            statement.setInt(7, pageCountValue);
            statement.setString(8, categories);
            statement.setString(9, String.valueOf(ratingValue)); // Keep rating as string for now
            statement.setString(10, previewLink);

            // Execute the query
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(AlertType.INFORMATION, "Success", "The book has been added successfully.");
            } else {
                showAlert(AlertType.ERROR, "Insert Error", "No rows were inserted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Validation Error", "Page count and rating must be valid numbers.");
        }
    }


    // Helper method to show alert dialog
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
