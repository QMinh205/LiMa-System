package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class BookDetailController {

    @FXML
    private Hyperlink previewLink;
    @FXML
    private ImageView bookImageView;
    @FXML
    private Label bookIdLabel, bookTitleLabel, bookAuthorLabel, bookPublisherLabel, bookPublishedDateLabel, bookPageCountLabel,
            bookCategoriesLabel, bookAverageRatingLabel, bookDescriptionLabel;
    @FXML
    private TextFlow bookDescriptionFlow;
    @FXML
    private Button backButton;

    private static final int DESCRIPTION_PREVIEW_LENGTH = 200;

    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(backButton);

    }

    public void setBookDetails(Book book) {
        bookIdLabel.setText("Book ID: " + book.getBookId());
        bookTitleLabel.setText(book.getTitle());
        bookAuthorLabel.setText(book.getAuthor());
        bookPublisherLabel.setText(book.getPublisher());
        bookPublishedDateLabel.setText("Published Date: " + book.getPublishedDate());
        bookPageCountLabel.setText("Pages: " + book.getPageCount());
        bookCategoriesLabel.setText("Categories: " + book.getCategories());
        bookAverageRatingLabel.setText("Rating: " + book.getAverageRating());
        //bookDescriptionLabel.setText(book.getDescription());

        // Handle book image
        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            try {
                bookImageView.setImage(new Image(book.getImageUrl()));
            } catch (Exception e) {
                e.printStackTrace();
                bookImageView.setImage(loadUnavailableImage());
            }
        } else {
            bookImageView.setImage(loadUnavailableImage());
        }

        if (book.getPreviewLink() != null && !book.getPreviewLink().isEmpty()) {
            previewLink.setText("Preview");
            previewLink.setOnAction(e -> {
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(book.getPreviewLink()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } else {
            previewLink.setText("No Preview Available");
            previewLink.setDisable(true);
        }

        setBookDescriptionWithPreview(book.getDescription());
    }


    private void setBookDescriptionWithPreview(String description) {
        bookDescriptionFlow.getChildren().clear(); // Clear any previous content

        if (description.length() > DESCRIPTION_PREVIEW_LENGTH) {
            // Truncated description
            String previewText = description.substring(0, DESCRIPTION_PREVIEW_LENGTH) + "... ";

            // Create Text nodes
            Text preview = new Text(previewText);
            Text seeMore = new Text("See more");

            // Style "See more" as a hyperlink
            seeMore.setFill(Color.BLUE);
            seeMore.setUnderline(true);

            // Add click event to "See more"
            seeMore.setOnMouseClicked(event -> showFullDescription(description));

            // Add nodes to TextFlow
            bookDescriptionFlow.getChildren().addAll(preview, seeMore);
        } else {
            // Description is short enough to display fully
            bookDescriptionFlow.getChildren().add(new Text(description));
        }
    }

    private void showFullDescription(String description) {
        bookDescriptionFlow.getChildren().clear(); // Clear preview
        Text fullDescription = new Text(description);

        // Add the full description to TextFlow
        bookDescriptionFlow.getChildren().add(fullDescription);
    }



    // Load the unavailable image
    private Image loadUnavailableImage() {
        try {
            // Convert the file path to URI and load the image
            File file = new File("E:/Bibi/Code/java/Oop/oop btl/Library-Management-System/JavaFxDemo/assets/unavailable.jpg");
            return new Image(file.toURI().toString());  // Convert file path to URI and load as Image
        } catch (Exception e) {
            e.printStackTrace();  // Log any errors loading the fallback image
            return null;  // If the fallback image cannot be loaded, return null
        }
    }

    @FXML
    private void onBackButtonClicked() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene cachedScene = SearchResultController.getSearchResultScene();
        if (cachedScene != null) {
            stage.setScene(cachedScene);
        } else {
            System.out.println("No cached SearchResult scene found. Loading a new one...");
            // Optional: Load a new SearchResult scene if caching fails
            try {
                Parent root = FXMLLoader.load(getClass().getResource("SearchResult.fxml"));
                Scene newScene = new Scene(root, 1500, 750);
                stage.setScene(newScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
