package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class IssueBookController extends BaseController {

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
    private TextField memberIdField;

    @FXML
    private TextField bookIdField;

    @FXML
    private DatePicker borrowedDatePicker;

    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private JFXButton confirmButton;

    @FXML
    private TextField borrowIdField;

    @FXML
    private TextField confirmField;

    @FXML
    private JFXButton confirmButton1;

    @FXML
    private Label statusLabel;


    @FXML
    public void handleConfirmAction() {
        // Lấy dữ liệu từ giao diện
        String memberId = memberIdField.getText().trim();
        String bookId = bookIdField.getText().trim();
        LocalDate borrowedDate = borrowedDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        // Kiểm tra các trường nhập liệu
        if (memberId.isEmpty() || bookId.isEmpty() || borrowedDate == null || dueDate == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be filled!");
            return;
        }

        // Kiểm tra tính hợp lệ của ngày
        if (dueDate.isBefore(borrowedDate)) {
            showAlert(Alert.AlertType.ERROR, "Date Error", "Due date cannot be before borrowed date!");
            return;
        }

        if(dueDate.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Date Error", "Due date cannot be before current date!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Lấy tiêu đề sách từ bảng books
            String bookTitle = getBookTitleById(bookId, conn);
            int memberTitle = Integer.parseInt(memberId);
            String memberID = getMemberById(memberTitle, conn);
            if (bookTitle == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Book ID does not exist in the database!");
                return;
            }
            if (memberID == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Member ID does not exist in the database!");
                return;
            }

            // Chèn dữ liệu vào bảng borrowed_books
            String query = "INSERT INTO borrowed_books (book_id, member_id, book_title, borrow_date, return_date, overdue) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, bookId);
            preparedStatement.setString(2, memberId);
            preparedStatement.setString(3, bookTitle); // Thêm tiêu đề sách
            preparedStatement.setDate(4, java.sql.Date.valueOf(borrowedDate));
            preparedStatement.setDate(5, java.sql.Date.valueOf(dueDate));
            preparedStatement.setBoolean(6, false); // Mặc định là chưa quá hạn

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book issued successfully!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failure", "Failed to issue book. Please try again!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred: " + e.getMessage());
        }
    }

    // Hàm để lấy tiêu đề sách từ bảng books
    private String getBookTitleById(String bookId, Connection conn) throws SQLException {
        String query = "SELECT title FROM books WHERE book_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, bookId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("title");
        }
        return null; // Trả về null nếu không tìm thấy sách
    }

    private String getMemberById(int memberId, Connection conn) throws SQLException {
        String query = "SELECT member_id FROM members WHERE member_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, memberId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("member_id");
        }
        return null; // Trả về null nếu không tìm thấy member
    }

    private void clearFields() {
        memberIdField.clear();
        bookIdField.clear();
        confirmField.clear();
        borrowedDatePicker.setValue(null);
        dueDatePicker.setValue(null);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleReturnBookAction() {
        // Lấy borrow_id từ trường nhập liệu
        String borrowId = borrowIdField.getText().trim();

        // Kiểm tra xem borrow_id có hợp lệ không
        if (borrowId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid borrow ID!");
            return;
        }

        String confirmText = confirmField.getText();

        if (borrowId.isEmpty() || confirmText.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        if (!confirmText.equalsIgnoreCase("CONFIRM")) {
            statusLabel.setText("Confirmation text must be 'CONFIRM'.");
            return;
        }

        // Tiến hành xóa bản ghi trong borrowed_books tương ứng với borrow_id
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Kiểm tra xem borrow_id có tồn tại không
            String checkQuery = "SELECT * FROM borrowed_books WHERE borrow_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, borrowId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Not Found", "No record found with the given borrow ID.");
                return;
            }

            // Nếu tồn tại, xóa bản ghi
            String deleteQuery = "DELETE FROM borrowed_books WHERE borrow_id = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            deleteStmt.setString(1, borrowId);

            int rowsAffected = deleteStmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book return processed successfully!");
                borrowIdField.clear(); // Clear the field after successful return
            } else {
                showAlert(Alert.AlertType.ERROR, "Failure", "Failed to return the book. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(homeButton);
        ButtonSoundUtil.addClickSound(requestButton);
        ButtonSoundUtil.addClickSound(informationButton);
        ButtonSoundUtil.addClickSound(favouriteButton);
        ButtonSoundUtil.addClickSound(gameButton);
        ButtonSoundUtil.addClickSound(settingButton);
        ButtonSoundUtil.addClickSound(confirmButton);

        // transition
        setupButton(homeButton, "User-Home.fxml", "Home");
        setupButton(requestButton, "Request.fxml", "Request");
        setupButton(informationButton, "Information.fxml", "Information");
        setupButton(favouriteButton, "IssueBook.fxml", "Issue & Return Book");
        setupButton(settingButton, "Setting.fxml", "Setting");

        setupGameButton(gameButton);
    }

}
