package com.example.javafxdemo;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SearchBarController {

    @FXML
    private StackPane rootPane;
    @FXML
    private JFXButton searchButton;

    @FXML
    private TextField bookTitleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publisherField;

    @FXML
    public void initialize() {

        CheckBox genre1 = new CheckBox("Fiction");
        CheckBox genre2 = new CheckBox("Non-Fiction");
        CheckBox genre3 = new CheckBox("Science");
        CheckBox genre4 = new CheckBox("History");
        CheckBox genre5 = new CheckBox("Biology");
        CheckBox genre6 = new CheckBox("Math");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(genre1, genre2, genre3, genre4, genre5, genre6);
        vbox.setStyle("-fx-background-color: white; -fx-padding: 10px;");

        vbox.setPrefWidth(300);
        vbox.setPrefHeight(200);

        // hiện dialog
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new javafx.scene.control.Label("Select Genres"));
        content.setBody(vbox);

        JFXDialog dialog = new JFXDialog();
        dialog.setDialogContainer(rootPane);
        dialog.setContent(content);

    }

    @FXML
    protected void onSearchButton() {
        try {
            // các field nhập ttin
            String title = bookTitleField.getText().replace(" ", "+");
            String author = authorField.getText().replace(" ", "+");
            String publisher = publisherField.getText().replace(" ", "+");

            StringBuilder queryBuilder = new StringBuilder();

            if (!title.isEmpty()) {
                queryBuilder.append("intitle:").append(title).append("+");
            }
            if (!author.isEmpty()) {
                queryBuilder.append("inauthor:").append(author).append("+");
            }
            if (!publisher.isEmpty()) {
                queryBuilder.append("inpublisher:").append(publisher).append("+");
            }

            if (queryBuilder.length() > 0 && queryBuilder.charAt(queryBuilder.length() - 1) == '+') {
                queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            }

            String query = queryBuilder.toString();

            if (!query.isEmpty()) {
                // chuyển đến result nếu ko empty
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchResult.fxml"));
                Parent root = loader.load();

                SearchResultController resultController = loader.getController();
                resultController.setSearchQuery(query);

                Stage stage = (Stage) searchButton.getScene().getWindow();
                stage.setScene(new Scene(root, 1500, 750));
                stage.setTitle("Search Results");
                stage.show();
            } else {
                System.out.println("No search available");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
