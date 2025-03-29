package app;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ResourceBundle;

public class SearchResultController extends BaseController implements Initializable {

    @FXML
    private VBox resultsVBox; // VBox to organize results
    @FXML
    private Button backButton;

    private static final String API_KEY = "AIzaSyB5k130KmLYxiVXscKGpiBaBNV-v1FSQWs"; // Replace with your actual API key
    private static Scene searchResultScene;

    public static void setSearchResultScene(Scene scene) {
        searchResultScene = scene;
    }

    public static Scene getSearchResultScene() {
        return searchResultScene;
    }

    @FXML
    public void initialize() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(backButton);
        // Save the current scene to the static variable
        Platform.runLater(() -> {
            Stage stage = (Stage) resultsVBox.getScene().getWindow();
            setSearchResultScene(stage.getScene());
        });
    }


    public void setSearchQuery(String title, String author, String publisher) {
        if ((title != null && !title.isEmpty()) || (author != null && !author.isEmpty()) || (publisher != null && !publisher.isEmpty())) {
            fetchBooks(title, author, publisher); // Pass the criteria to fetchBooks
        }
    }

    private String buildQueryUrl(String title, String author, String publisher) {
        StringBuilder query = new StringBuilder("https://www.googleapis.com/books/v1/volumes?q=");

        boolean hasQuery = false;

        if (title != null && !title.isEmpty()) {
            query.append("intitle:").append(title.replace(" ", "+"));
            hasQuery = true;
        }

        if (author != null && !author.isEmpty()) {
            if (hasQuery) query.append("+");
            query.append("inauthor:").append(author.replace(" ", "+"));
            hasQuery = true;
        }

        if (publisher != null && !publisher.isEmpty()) {
            if (hasQuery) query.append("+");
            query.append("inpublisher:").append(publisher.replace(" ", "+"));
        }

        // Append API key
        query.append("&key=").append(API_KEY);

        return query.toString();
    }



    private void fetchBooks(String title, String author, String publisher) {
        String urlString = buildQueryUrl(title, author, publisher);
        HttpURLConnection connection = null;
        try {
            ObservableList<Book> apiBooks = FXCollections.observableArrayList();
            ObservableList<Book> dbBooks = fetchBooksFromDatabase(title, author, publisher);
            ObservableList<Book> combinedBooks = FXCollections.observableArrayList();
            combinedBooks.addAll(dbBooks);

            if (dbBooks.isEmpty()) {
                System.out.println("No books found in the database.");
            }

            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            JsonObject response = gson.fromJson(reader, JsonObject.class);

            JsonArray items = response.has("items") ? response.getAsJsonArray("items") : null;

            if (items != null) {
                for (JsonElement element : items) {
                    JsonObject bookJson = element.getAsJsonObject();
                    Book book = parseBookFromJson(bookJson);
                    if (book != null) {
                        apiBooks.add(book);
                    }
                }
            }

            combinedBooks.addAll(apiBooks);

            if (combinedBooks.isEmpty()) {
                displayNoResultsUI();
                return;
            }

            updateResultsUI(combinedBooks);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    private void displayNoResultsUI() {
        resultsVBox.getChildren().clear();
        System.out.println("No books found for the query.");

        Button addBookButton = new Button("Not found your book? Add book");
        addBookButton.setStyle("-fx-font-size: 16px; -fx-padding: 10;");
        ButtonSoundUtil.addClickSound(addBookButton);
        addBookButton.setOnAction(event -> onAddBookButtonClicked());
        resultsVBox.getChildren().add(addBookButton);
    }

    private void updateResultsUI(ObservableList<Book> combinedBooks) {
        Platform.runLater(() -> {
            resultsVBox.getChildren().clear();

            for (Book book : combinedBooks) {
                HBox bookInfoBox = new HBox(10);
                VBox textBox = new VBox();

                // Tạo các label với màu trắng và in đậm
                Label titleLabel = new Label("Title: " + book.getTitle());
                titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

                Label authorLabel = new Label("Author: " + book.getAuthor());
                authorLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

                Label publisherLabel = new Label("Publisher: " + book.getPublisher());
                publisherLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

                textBox.getChildren().addAll(titleLabel, authorLabel, publisherLabel);

                // Check if the image URL is valid
                String imageUrl = book.getImageUrl();
                ImageView imageView;
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    try {
                        imageView = new ImageView(new Image(imageUrl));
                    } catch (IllegalArgumentException e) {
                        // Fallback to a placeholder if the URL is invalid
                        imageView = new ImageView(new Image(new File("E:/Bibi/Code/java/Oop/oop_btl/" +
                                "Library-Management-System/JavaFxDemo/assets/unavailable.jpg").toURI().toString()));
                    }
                } else {
                    // Fallback to a placeholder if the URL is null or empty
                    imageView = new ImageView(new Image(new File("E:/Bibi/Code/java/Oop/oop_btl/" +
                            "Library-Management-System/JavaFxDemo/assets/unavailable.jpg").toURI().toString()));
                }

                imageView.setFitWidth(160);
                imageView.setFitHeight(240);

                bookInfoBox.getChildren().addAll(imageView, textBox);
                bookInfoBox.setOnMouseClicked(event -> showBookDetails(book));
                resultsVBox.getChildren().add(bookInfoBox);
            }

            // Optionally add a button to handle no results found
            Button addBookButton = new Button("Not found your book? Add book");
            addBookButton.setStyle("-fx-font-size: 16px; -fx-padding: 10;");
            ButtonSoundUtil.addClickSound(addBookButton);
            addBookButton.setOnAction(event -> onAddBookButtonClicked());
            resultsVBox.getChildren().add(addBookButton);
        });
    }


    private Book parseBookFromJson(JsonObject bookJson) {
        try {
            String bookID = bookJson.has("id") ? bookJson.get("id").getAsString() : "Unknown ID";
            JsonObject volumeInfo = bookJson.getAsJsonObject("volumeInfo");
            String bookTitle = volumeInfo.get("title").getAsString();
            String bookAuthor = volumeInfo.has("authors") ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : "Unknown Author";
            String bookPublisher = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : "Unknown Publisher";
            String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No description available.";
            String publishedDate = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString() : "Unknown";
            int pageCount = volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").getAsInt() : 0;
            String categories = volumeInfo.has("categories") ? volumeInfo.getAsJsonArray("categories").get(0).getAsString() : "Uncategorized";
            String averageRating = volumeInfo.has("averageRating") ? volumeInfo.get("averageRating").getAsString() : "No rating";
            String previewLink = volumeInfo.has("previewLink") ? volumeInfo.get("previewLink").getAsString() : "No preview available";
            String imageUrl = volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")
                    ? volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString()
                    : null;

            return new Book(bookID, bookTitle, bookAuthor, bookPublisher, description, imageUrl, publishedDate, pageCount, categories, averageRating, previewLink);
        } catch (Exception e) {
            System.out.println("Error parsing book JSON: " + e.getMessage());
            return null;
        }
    }

    private ObservableList<Book> fetchBooksFromDatabase(String title, String author, String publisher) {
        ObservableList<Book> dbBooks = FXCollections.observableArrayList();

        // Normalize input
        title = (title == null || title.isEmpty()) ? "" : title;
        author = (author == null || author.isEmpty()) ? "" : author;
        publisher = (publisher == null || publisher.isEmpty()) ? "" : publisher;

        String sql = "SELECT * FROM books WHERE (title LIKE ? OR ? = '') AND (author LIKE ? OR ? = '') AND (publisher LIKE ? OR ? = '')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + title + "%");
            pstmt.setString(2, title);
            pstmt.setString(3, "%" + author + "%");
            pstmt.setString(4, author);
            pstmt.setString(5, "%" + publisher + "%");
            pstmt.setString(6, publisher);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                            rs.getString("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getString("description"),
                            rs.getString("image_url"),
                            rs.getString("published_date"),
                            rs.getInt("page_count"),
                            rs.getString("categories"),
                            rs.getString("average_rating"),
                            rs.getString("preview_link")
                    );
                    dbBooks.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dbBooks;
    }



    private void onAddBookButtonClicked() {
        System.out.println("Redirecting to Add Book Form...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBook.fxml"));
            Parent root = loader.load();

            // Open the Add Book form in a new window
            Stage stage = new Stage();
            stage.setTitle("Add Book");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showBookDetails(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookDetail.fxml"));
            Parent root = loader.load();

            BookDetailController controller = loader.getController();
            controller.setBookDetails(book);

            Stage stage = (Stage) resultsVBox.getScene().getWindow();
            Scene scene = new Scene(root, 1500, 750);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackButtonClicked() {
        loadScene("SearchBar.fxml", "Search Bar", backButton);
    }
}
