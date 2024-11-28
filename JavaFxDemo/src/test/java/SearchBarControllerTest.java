import app.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class SearchBarControllerTest extends ApplicationTest {

    private TextField bookTitleField;
    private TextField authorField;
    private TextField publisherField;
    private Button searchButton;
    private Button backButton;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/SearchBar.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // Access FXML elements
        bookTitleField = lookup("#bookTitleField").query();
        authorField = lookup("#authorField").query();
        publisherField = lookup("#publisherField").query();
        searchButton = lookup("#searchButton").query();
        backButton = lookup("#backButton").query();
    }

    @BeforeEach
    void setUp() {
        // Clear all input fields before each test
        FxRobot robot = new FxRobot();
        robot.clickOn(bookTitleField).eraseText(bookTitleField.getText().length());
        robot.clickOn(authorField).eraseText(authorField.getText().length());
        robot.clickOn(publisherField).eraseText(publisherField.getText().length());
    }

    @Test
    void testSearchButtonNoInput() {
        // Click the search button without entering any input
        clickOn(searchButton);

        // Verify that the console prints the correct error message
        // (You can use log capturing libraries to verify log outputs)
        // Here, we'll ensure no new window is opened
        assertEquals("No search criteria provided", getConsoleOutput());
    }

    @Test
    void testSearchButtonWithValidInput() {
        // Enter valid input into the search fields
        clickOn(bookTitleField).write("Test Book");
        clickOn(authorField).write("Test Author");
        clickOn(publisherField).write("Test Publisher");

        // Click the search button
        clickOn(searchButton);

        // Verify that the scene switches to SearchResult.fxml
        assertTrue(((Stage) searchButton.getScene().getWindow()).getTitle().contains("Search Results"));
    }

    @Test
    void testBackButton() {
        // Click the back button
        clickOn(backButton);

        // Verify that the scene switches to User-Home.fxml
        assertTrue(((Stage) backButton.getScene().getWindow()).getTitle().contains("User Home"));
    }

    // Helper method to capture console output (requires overriding System.out)
    private String getConsoleOutput() {
        // Implement logic to capture console output for testing purposes
        return ""; // Placeholder
    }
}
