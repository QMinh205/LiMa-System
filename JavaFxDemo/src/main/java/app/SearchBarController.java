package app;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SearchBarController {

    //@FXML
    //private ImageView tagImage;  // ImageView for the tag
    @FXML
    private StackPane rootPane; // StackPane as container for JFXDialog
    @FXML
    private JFXButton searchButton;

    @FXML
    private TextField bookTitleField; // Define the book title field
    @FXML
    private TextField authorField; // Define the author field
    @FXML
    private TextField publisherField; // Define the publisher field
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(searchButton);
        ButtonSoundUtil.addClickSound(backButton);
        //if (tagImage == null) {
        //    System.out.println("Error: tagImage is null");
        //    return;
        //}

        // Create CheckBoxes for genres
        CheckBox genre1 = new CheckBox("Fiction");
        CheckBox genre2 = new CheckBox("Non-Fiction");
        CheckBox genre3 = new CheckBox("Science");
        CheckBox genre4 = new CheckBox("History");
        CheckBox genre5 = new CheckBox("Biology");
        CheckBox genre6 = new CheckBox("Math");

        // Add CheckBoxes to VBox
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(genre1, genre2, genre3, genre4, genre5, genre6);
        vbox.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        vbox.setPrefWidth(300);
        vbox.setPrefHeight(200);

        // Set up JFXDialog
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new javafx.scene.control.Label("Select Genres"));
        content.setBody(vbox);

        JFXDialog dialog = new JFXDialog();
        dialog.setDialogContainer(rootPane);
        dialog.setContent(content);

        // Show dialog when tagImage is clicked
        //tagImage.setOnMouseClicked(event -> dialog.show());
    }

    @FXML
    protected void onSearchButton() {
        try {
            // Get the search input from the user
            String title = bookTitleField.getText() != null ? bookTitleField.getText().trim() : null;
            String author = authorField.getText() != null ? authorField.getText().trim() : null;
            String publisher = publisherField.getText() != null ? publisherField.getText().trim() : null;

            // Ensure at least one search field is provided
            if ((title == null || title.isEmpty()) &&
                    (author == null || author.isEmpty()) &&
                    (publisher == null || publisher.isEmpty())) {
                System.out.println("No search criteria provided");
                return;
            }

            // Load SearchResult.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchResult.fxml"));
            Parent root = loader.load();

            // Get the controller of SearchResult.fxml
            SearchResultController resultController = loader.getController();

            // Pass the search parameters to the controller
            resultController.setSearchQuery(title, author, publisher);

            // Show the new scene
            Stage stage = (Stage) searchButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1500, 750));
            stage.setTitle("Search Results");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackButtonClicked() {
        try {
            // Load the UserHome.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("User-Home.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1500, 750)); // Adjust the window size here
            stage.setTitle("User Home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
