package app;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class UserInformationController extends BaseController {

    @FXML
    private JFXButton returnButton;

    @FXML
    private JFXButton searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXButton sortByDobButton;

    @FXML
    private ListView<HBox> memberListView;

    public void initialize() {
        // thêm âm thanh click cho các nút
        ButtonSoundUtil.addClickSound(returnButton);
        ButtonSoundUtil.addClickSound(searchButton);
    }

    @FXML
    private void onReturnButtonClicked() {
        loadScene("Information.fxml", "Information", returnButton);
    }

    @FXML
    private void onSearchButtonClicked() {
        String searchId = searchTextField.getText().trim();
        if (!searchId.isEmpty()) {
            searchMemberById(searchId); // Gọi phương thức tìm kiếm theo ID
        }
    }

    private void searchMemberById(String searchTerm) {
        memberListView.getItems().clear(); // xóa các hàng cũ trước khi hiển thị dữ liệu mới

        boolean noMembers = true;

        // kết nối và truy vấn cơ sở dữ liệu
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM members WHERE member_id LIKE ? OR userName LIKE ? OR fullName LIKE ? OR email LIKE ? OR phoneNumber LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            String searchPattern = "%" + searchTerm + "%"; // Tìm kiếm chứa chuỗi
            statement.setString(1, searchPattern); // Tìm theo member_id
            statement.setString(2, searchPattern); // Tìm theo userName
            statement.setString(3, searchPattern); // Tìm theo fullName
            statement.setString(4, searchPattern);
            statement.setString(5, searchPattern);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                noMembers = false;
                // lấy dữ liệu từ ResultSet
                String id = String.valueOf(resultSet.getInt("member_id"));
                String fullName = resultSet.getString("fullName");
                String userName = resultSet.getString("userName");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phoneNumber");
                String safeCode = resultSet.getString("safeCode");
                String dob = resultSet.getDate("dateOfBirth").toString();

                // tạo các Label cho từng cột
                Label idLabel = createStyledLabel(id, 50);
                Label fullNameLabel = createStyledLabel(fullName, 195);
                Label userNameLabel = createStyledLabel(userName, 195);
                Label passwordLabel = createStyledLabel(password, 195);
                Label emailLabel = createStyledLabel(email, 205);
                Label phoneLabel = createStyledLabel(phone, 195);
                Label safeCodeLabel = createStyledLabel(safeCode, 185);
                Label dobLabel = createStyledLabel(dob, 175);

                // tạo hbox chứa các label
                HBox row = new HBox(idLabel, fullNameLabel, userNameLabel, passwordLabel, emailLabel, phoneLabel, safeCodeLabel, dobLabel);
                row.setSpacing(2); // khoảng cách giữa các cột

                // căn chỉnh hbox vào center
                row.setAlignment(Pos.CENTER);

                // Thêm hbox vào listView
                memberListView.getItems().add(row);
            }

            if (noMembers) {
                showNoResultsAlert();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isAscendingOrder = true;

    @FXML
    private void onSortByDobButtonClicked() {
        ObservableList<HBox> rows = memberListView.getItems();

        FXCollections.sort(rows, (row1, row2) -> {
            Label dobLabel1 = (Label) row1.getChildren().get(7);
            Label dobLabel2 = (Label) row2.getChildren().get(7);

            String dob1 = dobLabel1.getText();
            String dob2 = dobLabel2.getText();

            return isAscendingOrder ? dob1.compareTo(dob2) : dob2.compareTo(dob1);
        });

        memberListView.setItems(rows);
        isAscendingOrder = !isAscendingOrder; // Toggle order
    }

    private void showNoResultsAlert() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("No Results");
        alert.setHeaderText(null);
        alert.setContentText("No members found matching the search criteria.");
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
}
