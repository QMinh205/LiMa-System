package app;

import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import dao.BookDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import user.User;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class UserHome extends BaseController {

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton requestButton;

    @FXML
    private JFXButton informationButton;

    @FXML
    private JFXButton favouriteButton;

    @FXML
    private JFXButton gameButton;

    @FXML
    private JFXButton settingButton;

    @FXML
    private JFXButton searchButton;

    @FXML
    private Label userLabel;

    private List<Book> books;

    @FXML
    public void initialize() {
        updateUserInfo(userLabel);
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(homeButton);
        ButtonSoundUtil.addClickSound(requestButton);
        ButtonSoundUtil.addClickSound(informationButton);
        ButtonSoundUtil.addClickSound(favouriteButton);
        ButtonSoundUtil.addClickSound(gameButton);
        ButtonSoundUtil.addClickSound(settingButton);
        ButtonSoundUtil.addClickSound(searchButton);

        // Use the setupButton method from BaseController
        setupButton(homeButton, "User-Home.fxml", "Home");
        setupButton(requestButton, "Request.fxml", "Request");
        setupButton(informationButton, "Information.fxml", "Information");
        setupButton(favouriteButton, "Favourite.fxml", "Favourite");
        setupButton(settingButton, "Setting.fxml", "Setting");
        setupButton(searchButton, "SearchBar.fxml", "SearchBar");

        setupGameButton(gameButton);

        BookDAO bookDAO = new BookDAO();
        books = bookDAO.fetchBooks();
    }



    @FXML
    private void onBookClicked(ActionEvent event) {
        // Get the source of the event (JFXButton)
        JFXButton clickedButton = (JFXButton) event.getSource();

        // Retrieve the ImageView from the button's graphic property
        ImageView clickedImageView = (ImageView) clickedButton.getGraphic();
        if (clickedImageView == null || clickedImageView.getImage() == null) {
            System.out.println("No image associated with this button.");
            return;
        }

        // Find the book associated with the clicked image
        Book selectedBook = getBookByImage(clickedImageView.getImage());
        if (selectedBook != null) {
            openBookDetailView(selectedBook);
        } else {
            System.out.println("No book associated with this image.");
        }
    }


    private void openBookDetailView(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookDetail.fxml"));
            Parent root = loader.load();

            // Pass data to BookDetailController
            BookDetailController controller = loader.getController();
            controller.setBookDetails(book);

            // Open the book detail view in a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Book Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Book getBookByImage(Image clickedImage) {
        if (clickedImage == null || books == null) {
            return null;
        }

        // Convert clicked image to a BufferedImage
        BufferedImage clickedBufferedImage = toBufferedImage(clickedImage);

        for (Book book : books) {
            if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                try {
                    // Load the image from URL or local path
                    Image bookImage = new Image(book.getImageUrl());
                    BufferedImage bookBufferedImage = toBufferedImage(bookImage);

                    // Compare the image content
                    if (areImagesEqual(clickedBufferedImage, bookBufferedImage)) {
                        return book; // Return the matching book
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null; // No match found
    }

    // Convert Image to BufferedImage
    private BufferedImage toBufferedImage(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        if (width <= 0 || height <= 0) {
            System.out.println("Error: Image is not loaded correctly or has invalid dimensions.");
            return null; // Return null or handle the error as appropriate
        }

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Get PixelReader from the Image
        PixelReader pixelReader = image.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the pixel color from the PixelReader
                javafx.scene.paint.Color fxColor = pixelReader.getColor(x, y);

                // Convert the JavaFX Color to ARGB format (Java AWT format)
                int argb = (int) (fxColor.getOpacity() * 255) << 24 | (int) (fxColor.getRed() * 255) << 16
                        | (int) (fxColor.getGreen() * 255) << 8 | (int) (fxColor.getBlue() * 255);

                // Set pixel in the BufferedImage
                bufferedImage.setRGB(x, y, argb);
            }
        }

        return bufferedImage;
    }

    // Compare two BufferedImages by checking pixel equality
    private boolean areImagesEqual(BufferedImage img1, BufferedImage img2) {
        if (img1 == null || img2 == null) {
            System.out.println("One or both images are null. Cannot compare.");
            return false;
        }
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}
