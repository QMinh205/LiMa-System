import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class RequestControllerTest extends ApplicationTest {

    private Button homeButton;
    private Button requestButton;
    private Button informationButton;
    private Button issueButton;
    private Button gameButton;
    private Button settingButton;
    private Button deleteButton;
    private Button fixButton;
    private Button addButton;
    private Button banButton;
    private Label userLabel;

    @Override
    public void start(Stage stage) throws Exception {
        File fxmlFile = new File("E:/Bibi/Code/java/Oop/oop_btl/LiMa-System/JavaFxDemo/src/main/resources/app/Request.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Request");
        stage.show();

        // Lookup buttons and label
        homeButton = lookup("#homeButton").query();
        requestButton = lookup("#requestButton").query();
        informationButton = lookup("#informationButton").query();
        issueButton = lookup("#issueButton").query();
        gameButton = lookup("#gameButton").query();
        settingButton = lookup("#settingButton").query();
        deleteButton = lookup("#deleteButton").query();
        fixButton = lookup("#fixButton").query();
        addButton = lookup("#addButton").query();
        banButton = lookup("#banButton").query();
        userLabel = lookup("#userLabel").query();
    }

    @BeforeEach
    void setUp() {
        // Clear any previous data or selections before each test
    }

    @Test
    void testButtonsAreInitialized() {
        // Verify all buttons are loaded and not null
        assertNotNull(homeButton, "Home button should be initialized.");
        assertNotNull(requestButton, "Request button should be initialized.");
        assertNotNull(informationButton, "Information button should be initialized.");
        assertNotNull(issueButton, "Issue button should be initialized.");
        assertNotNull(gameButton, "Game button should be initialized.");
        assertNotNull(settingButton, "Setting button should be initialized.");
        assertNotNull(deleteButton, "Delete button should be initialized.");
        assertNotNull(fixButton, "Fix button should be initialized.");
        assertNotNull(addButton, "Add button should be initialized.");
        assertNotNull(banButton, "Ban button should be initialized.");
    }

    /*@Test
    void testOnFixButtonClicked() {
        // Simulate clicking the Delete button
        clickOn(fixButton);

        // Verify the scene's title after clicking the button
        Stage stage = (Stage) fixButton.getScene().getWindow();
        assertNotNull(stage, "Stage should not be null after clicking the Fix button");

        // Assert that the title has changed as expected
        assertEquals("Fix Request", stage.getTitle(), "The title should change to 'Fix Request'");
    }


    @Test
    void testOnDeleteButtonClicked() {
        // Simulate clicking the Delete button
        clickOn(deleteButton);

        // Verify the scene's title after clicking the button
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        assertNotNull(stage, "Stage should not be null after clicking the Delete button");
        assertEquals("Delete Request", stage.getTitle(), "The title should change to 'Delete Request'");
    }

    @Test
    void testOnAddButtonClicked() {
        // Simulate clicking the Add button
        clickOn(addButton);

        // Verify the scene's title after clicking the button
        Stage stage = (Stage) addButton.getScene().getWindow();
        assertNotNull(stage, "Stage should not be null after clicking the Add button");
        assertEquals("Add Book", stage.getTitle(), "The title should change to 'Add Book'");
    }

    @Test
    void testOnBanButtonClicked() {
        // Simulate clicking the Ban button
        clickOn(banButton);

        // Get the stage after button click
        Stage stage = (Stage) banButton.getScene().getWindow();
        assertNotNull(stage, "Stage should not be null after clicking the Ban button");
        assertEquals("Ban User Request", stage.getTitle(), "The title should change to 'Ban User Request'");
    }

     */

    @Test
    void testUserLabelIsUpdated() {
        // Verify user label is correctly initialized
        assertNotNull(userLabel, "User label should be initialized.");
        assertFalse(userLabel.getText().isEmpty(), "User label should not be empty.");
    }

    /*
    @Test
    void testGameButtonClicked() {
        // Simulate clicking the Game button
        clickOn(gameButton);

        // Verify the scene's title after clicking the Game button
        Stage stage = (Stage) gameButton.getScene().getWindow();
        assertNotNull(stage, "Stage should not be null after clicking the Game button");
        assertTrue(stage.getTitle().contains("Game"), "The title should indicate game navigation.");
    }

     */
}
