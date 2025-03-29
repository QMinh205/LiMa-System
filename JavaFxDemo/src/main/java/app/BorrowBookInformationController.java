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
import java.util.ArrayList;
import java.util.List;

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
    private JFXButton sortByOverdueButton;

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

        if (bookId.isEmpty() && userId.isEmpty() && borrowId.isEmpty()) {
            System.out.println("Vui lòng nhập ít nhất một trường tìm kiếm!");
            return;
        }

        // Tìm kiếm với tất cả các trường đã được nhập
        searchBorrowedBooksBy(bookId, userId, borrowId);
    }

    private void searchBorrowedBooksBy(String bookId, String userId, String borrowId) {
        booksListView.getItems().clear(); // Xóa danh sách cũ

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Tạo câu truy vấn động dựa trên trường tìm kiếm
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM borrowed_books WHERE 1=1"); //dễ xây
            List<String> parameters = new ArrayList<>();

            if (!bookId.isEmpty()) {
                queryBuilder.append(" AND book_id LIKE ?");
                parameters.add("%" + bookId + "%");
            }
            if (!userId.isEmpty()) {
                queryBuilder.append(" AND member_id LIKE ?");
                parameters.add("%" + userId + "%");
            }
            if (!borrowId.isEmpty()) {
                queryBuilder.append(" AND borrow_id LIKE ?");
                parameters.add("%" + borrowId + "%");
            }

            String query = queryBuilder.toString();
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.size(); i++) {
                statement.setString(i + 1, parameters.get(i));
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Lấy dữ liệu từ ResultSet
                String memberId = resultSet.getString("member_id");
                String retrievedBookId = resultSet.getString("book_id");
                String retrievedBorrowId = resultSet.getString("borrow_id");
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
                Label borrowIdLabel = createStyledLabel(retrievedBorrowId, 100);
                Label bookIdLabel = createStyledLabel(retrievedBookId, 100);
                Label userIdLabel = createStyledLabel(memberId, 100);
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
        //ButtonSoundUtil.addClickSound(searchBookIdButton);
        //ButtonSoundUtil.addClickSound(searchBorrowIdButton);
    }
    @FXML
    private void onReturnButtonClicked() {
        loadScene("Information.fxml", "Information", returnButton);
    }

    private boolean overdueSortAsc = true; // Track toggle state (true = overdue first)

    @FXML
    private void onSortByOverdueClicked() {
        booksListView.getItems().sort((hbox1, hbox2) -> {
            Label overdueLabel1 = (Label) hbox1.getChildren().get(6); // Assuming overdue status is the 7th column
            Label overdueLabel2 = (Label) hbox2.getChildren().get(6);

            // Get the overdue status ("Yes" or "No")
            boolean isOverdue1 = "Yes".equals(overdueLabel1.getText());
            boolean isOverdue2 = "Yes".equals(overdueLabel2.getText());

            // Compare based on toggle state
            if (overdueSortAsc) {
                return Boolean.compare(isOverdue2, isOverdue1); // Overdue first
            } else {
                return Boolean.compare(isOverdue1, isOverdue2); // Non-overdue first
            }
        });
        overdueSortAsc = !overdueSortAsc;
    }

}
