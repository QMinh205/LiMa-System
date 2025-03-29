package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class BookInformationController {

    @FXML
    private JFXButton returnButton;

    @FXML
    private JFXButton searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXButton sortByRatingButton;

    @FXML
    private ListView<HBox> booksListView;

    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(returnButton);
        ButtonSoundUtil.addClickSound(searchButton);
    }

    @FXML
    private void onReturnButtonClicked() {
        try {
            // Load the UserHome.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Information.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Information");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSearchButtonClicked() {
        String searchId = searchTextField.getText().trim();
        if (!searchId.isEmpty()) {
            searchBookById(searchId); // Gọi phương thức tìm kiếm theo ID
        }
    }

    private void searchBookById(String searchTerm) {
        booksListView.getItems().clear(); // xóa các hàng cũ trước khi hiển thị dữ liệu mới

        boolean noResults = true;

        // kết nối và truy vấn cơ sở dữ liệu
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM books WHERE book_id LIKE ? OR title LIKE ? OR author LIKE ? OR publisher LIKE ? OR categories LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            String searchPattern = "%" + searchTerm + "%"; // Tìm kiếm chứa chuỗi
            statement.setString(1, searchPattern); // Tìm theo member_id
            statement.setString(2, searchPattern); // Tìm theo userName
            statement.setString(3, searchPattern); // Tìm theo fullName
            statement.setString(4, searchPattern);
            statement.setString(5, searchPattern);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                noResults = false;
                // lấy dữ liệu từ ResultSet
                String id = resultSet.getString("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                String publishedDate = resultSet.getString("published_date");
                String pageCount = resultSet.getString("page_count");
                String categories = resultSet.getString("categories");
                String rating = resultSet.getString("average_rating");

                // tạo các Label cho từng cột
                Label idLabel = createStyledLabel(id, 70);
                Label titleLabel = createStyledLabel(title, 195);
                Label authorLabel = createStyledLabel(author, 195);
                Label publisherLabel = createStyledLabel(publisher, 195);
                Label publishedDateLabel = createStyledLabel(publishedDate, 195);
                Label pageCountLabel = createStyledLabel(pageCount, 175);
                Label categoriesLabel = createStyledLabel(categories, 215);
                Label ratingLabel= createStyledLabel(rating, 195);

                // tạo hbox chứa các label
                HBox row = new HBox(idLabel,titleLabel,authorLabel,publisherLabel,publishedDateLabel,pageCountLabel,categoriesLabel,ratingLabel);
                row.setSpacing(2); // khoảng cách giữa các cột

                // căn chỉnh hbox vào center
                row.setAlignment(Pos.CENTER);

                // Thêm hbox vào listView
                booksListView.getItems().add(row);
            }

            if (noResults) {
                showNoResultsAlert();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showNoResultsAlert() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("No Results");
        alert.setHeaderText(null);
        alert.setContentText("No books found matching the search criteria.");
        alert.showAndWait();
    }

    // phương thức tạo label đã định dạng
    private Label createStyledLabel(String text, double width) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 15)); // cỡ chữ lớn và in đậm
        label.setTextFill(Color.BLACK); // màu đen
        label.setPrefWidth(width); // đặt chiều rộng cho label
        label.setAlignment(Pos.CENTER_LEFT); // căn lề trái
        return label;
    }

    private boolean isDescendingOrder = true;

    @FXML
    private void onSortByRatingButtonClicked() {
        // Check if there are items in the ListView
        if (booksListView.getItems().isEmpty()) {
            System.out.println("No books to sort.");
            return;
        }

        // Sort the books by rating
        booksListView.getItems().sort((hbox1, hbox2) -> {
            Label ratingLabel1 = (Label) hbox1.getChildren().get(7);
            Label ratingLabel2 = (Label) hbox2.getChildren().get(7);

            String ratingText1 = ratingLabel1.getText();
            String ratingText2 = ratingLabel2.getText();

            try {
                double rating1 = ratingText1.isEmpty() ? 0 : Double.parseDouble(ratingText1);
                double rating2 = ratingText2.isEmpty() ? 0 : Double.parseDouble(ratingText2);

                // Sort in descending order
                return isDescendingOrder ? Double.compare(rating2, rating1) : Double.compare(rating1, rating2);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        });
        isDescendingOrder = !isDescendingOrder;
    }
}
