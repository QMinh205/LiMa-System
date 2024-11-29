package app;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class BorrowBookInformationController extends BaseController {
    @FXML
    private JFXButton returnButton;

    @FXML
    private JFXButton searchUserIdButton;

    @FXML
    private JFXButton searchBookIdButton;

    @FXML
    private JFXButton searchBorrowIdButton;

    @FXML
    private TextField bookIdField;
    @FXML
    private TextField userIdField;
    @FXML
    private TextField borrowIdField;
    @FXML
    private ListView<HBox> booksListView;

    @FXML
    private void onSearchButtonClicked() {
        String bookId = bookIdField.getText().trim();
        String userId = userIdField.getText().trim();
        String borrowId = borrowIdField.getText().trim();

        if (!bookId.isEmpty()) {
            searchBorrowedBooksBy("book_id", bookId);
        } else if (!userId.isEmpty()) {
            searchBorrowedBooksBy("member_id", userId);
        } else if (!borrowId.isEmpty()) {
            searchBorrowedBooksBy("borrow_id", borrowId);
        } else {
            // Hiển thị thông báo nếu không có trường nào được điền
            System.out.println("Vui lòng nhập ít nhất một trường tìm kiếm!");
        }
    }

    private void searchBorrowedBooksBy(String fieldName, String searchValue) {
        booksListView.getItems().clear(); // Xóa danh sách cũ

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Tạo câu truy vấn động dựa trên trường tìm kiếm
            String query = "SELECT * FROM borrowed_books WHERE " + fieldName + " LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchValue + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Lấy dữ liệu từ ResultSet
                String borrowId = resultSet.getString("member_id");
                String bookId = resultSet.getString("book_id");
                String userId = resultSet.getString("borrow_id");
                String bookTitle = resultSet.getString("book_title");
                String borrowDate = resultSet.getString("borrow_date");
                String returnDate = resultSet.getString("return_date");

                boolean overdue = false;
                try {
                    LocalDate returnDateParsed = LocalDate.parse(returnDate); // Assuming `return_date` is stored in ISO format (YYYY-MM-DD)
                    overdue = returnDateParsed.isBefore(LocalDate.now()); // Compare with today's date
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid date format in database: " + returnDate);
                }

                // Tạo các Label đã định dạng
                Label borrowIdLabel = createStyledLabel(borrowId, 100);
                Label bookIdLabel = createStyledLabel(bookId, 100);
                Label userIdLabel = createStyledLabel(userId, 100);
                Label bookTitleLabel = createStyledLabel(bookTitle, 240);
                Label borrowDateLabel = createStyledLabel(borrowDate, 210);
                Label returnDateLabel = createStyledLabel(returnDate, 210);
                Label overdueLabel = createStyledLabel(overdue ? "Yes" : "No", 50);

                // Tạo HBox chứa các Label
                HBox row = new HBox(borrowIdLabel, bookIdLabel, userIdLabel, bookTitleLabel, borrowDateLabel, returnDateLabel, overdueLabel);
                row.setSpacing(5);
                row.setAlignment(Pos.CENTER);

                // Thêm HBox vào ListView
                booksListView.getItems().add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức tạo Label định dạng
    private Label createStyledLabel(String text, double width) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 15)); // Cỡ chữ lớn và in đậm
        label.setTextFill(Color.BLACK); // Màu chữ
        label.setPrefWidth(width); // Đặt chiều rộng cho Label
        label.setAlignment(Pos.CENTER_LEFT); // Căn lề trái
        return label;
    }

    public void initialize() {
        ButtonSoundUtil.addClickSound(returnButton);
        ButtonSoundUtil.addClickSound(searchUserIdButton);
        ButtonSoundUtil.addClickSound(searchBookIdButton);
        ButtonSoundUtil.addClickSound(searchBorrowIdButton);
    }
    @FXML
    private void onReturnButtonClicked() {
        loadScene("Information.fxml", "Information", returnButton);
    }
}
