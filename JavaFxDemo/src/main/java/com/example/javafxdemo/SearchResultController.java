package com.example.javafxdemo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

public class SearchResultController {

    @FXML
    private TextField searchField; // chỗ search
    @FXML
    private ListView<String> resultsListView; // listview hien ket qua
    @FXML
    private VBox resultsVBox;

    private static final String API_KEY = ""; // api key

    @FXML
    public void initialize() {
    }

    public void setSearchQuery(String query) {
        if (query != null && !query.isEmpty()) {
            fetchBooks(query);
        }
    }

    @FXML
    private void onSearchKeyTyped(KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            String query = searchField.getText().trim();
            System.out.println("Search query: " + query);
            if (!query.isEmpty()) {
                fetchBooks(query);
            }
        }
    }

    private void fetchBooks(String query) {
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=" + API_KEY;
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
                System.out.println("No books found for the query: " + query);
                return;
            }

            resultsVBox.getChildren().clear();

            for (int i = 0; i < items.size(); i++) {
                JsonObject book = items.get(i).getAsJsonObject();
                JsonObject volumeInfo = book.getAsJsonObject("volumeInfo");

                String title = volumeInfo.get("title").getAsString();
                String author = volumeInfo.has("authors") ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : "Unknown Author";
                String publisher = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : "Unknown Publisher";
                String imageUrl = null;

                // kiểm tra xem có ảnh ko
                if (volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")) {
                    imageUrl = volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString();
                }

                HBox bookInfoBox = new HBox(10);
                VBox textBox = new VBox();
                textBox.getChildren().addAll(
                        new Label("Title: " + title),
                        new Label("Author: " + author),
                        new Label("Publisher: " + publisher)
                );

                if (imageUrl != null) {
                    ImageView imageView = new ImageView(imageUrl);
                    imageView.setFitWidth(700);  // chiều rộng ảnh
                    imageView.setFitHeight(140);  // chiều cao ảnh

                    bookInfoBox.getChildren().addAll(imageView, textBox);
                } else {
                    bookInfoBox.getChildren().add(textBox);
                }

                resultsVBox.getChildren().add(bookInfoBox);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    @FXML
    private void onBackButtonClicked() {
        try {
            // quay lại searchBar
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
