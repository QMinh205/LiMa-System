package com.example.javafxdemo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchResultController implements Initializable {

    @FXML
    private TextField searchField; // TextField for user search input
    @FXML
    private ListView<String> resultsListView; // ListView to display search results
    @FXML
    private VBox resultsVBox; // VBox to organize results

    private static final String API_KEY = ""; // Replace with your actual API key
    private ObservableList<Book> booksList = FXCollections.observableArrayList();
    private static Scene searchResultScene;

    public static void setSearchResultScene(Scene scene) {
        searchResultScene = scene;
    }

    public static Scene getSearchResultScene() {
        return searchResultScene;
    }

    @FXML
    public void initialize() {
        // Optional: set up listeners or any initial setup
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            JsonObject response = gson.fromJson(reader, JsonObject.class);

            JsonArray items = response.has("items") ? response.getAsJsonArray("items") : null;

            if (items == null || items.size() == 0) {
                System.out.println("No books found for the query.");
                return;
            }

            resultsVBox.getChildren().clear();  // Clear previous results
            booksList.clear();  // Clear previous search results

            for (int i = 0; i < items.size(); i++) {
                JsonObject book = items.get(i).getAsJsonObject();
                JsonObject volumeInfo = book.getAsJsonObject("volumeInfo");

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

                Book bookObject = new Book(
                        bookTitle, bookAuthor, bookPublisher, description, imageUrl,
                        publishedDate, pageCount, categories, averageRating, previewLink
                );
                booksList.add(bookObject);  // Add book to the list

                // Create the UI components for displaying the book
                HBox bookInfoBox = new HBox(10);
                VBox textBox = new VBox();
                textBox.getChildren().addAll(
                        new Label("Title: " + bookTitle),
                        new Label("Author: " + bookAuthor),
                        new Label("Publisher: " + bookPublisher)
                );

                // Handle the image
                ImageView imageView = imageUrl != null
                        ? new ImageView(new Image(imageUrl))
                        : new ImageView(new Image(new File("E:/Bibi/Code/java/Oop/oop btl/" +
                        "Library-Management-System/JavaFxDemo/assets/unavailable.jpg").toURI().toString()));

                imageView.setFitWidth(160);
                imageView.setFitHeight(240);

                bookInfoBox.getChildren().addAll(imageView, textBox);

                // Add event handler to show book details
                bookInfoBox.setOnMouseClicked(event -> showBookDetails(bookObject));

                resultsVBox.getChildren().add(bookInfoBox); // Add book to results VBox
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
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
        try {
            // Go back to SearchBar.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchBar.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) resultsListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Search Books");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
