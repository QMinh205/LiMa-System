import app.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;

import java.sql.Connection;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Enable Mockito extensions for JUnit
class AddBookControllerTest {

    @Mock
    private TextField titleField;
    @Mock
    private TextField authorField;
    @Mock
    private TextField publisherField;
    @Mock
    private TextField descriptionField;
    @Mock
    private TextField publishedDateField;
    @Mock
    private TextField pageCountField;
    @Mock
    private TextField categoriesField;
    @Mock
    private TextField ratingField;
    @Mock
    private TextField imageUrlField;
    @Mock
    private TextField previewLinkField;
    @Mock
    private Button cancelButton;
    @Mock
    private Button returnButton;

    @Mock
    private DatabaseConnection databaseConnection;

    @InjectMocks
    private AddBookController addBookController;

    @BeforeEach
    void setUp() {
        // Initialize the controller and mock the buttons and fields
        addBookController.initialize();
    }

    @Test
    void testHandleAddBookActionWithEmptyFields() {
        // Set up the mock fields to return empty values
        when(titleField.getText()).thenReturn("");
        when(authorField.getText()).thenReturn("");
        when(publisherField.getText()).thenReturn("");

        // Call the method under test
        addBookController.handleAddBookAction();

        // Verify that the alert is shown
        // You can mock showAlert method and verify it was called
        verify(addBookController, times(1)).showAlert(eq(Alert.AlertType.ERROR), eq("Validation Error"), eq("Title, Author, and Publisher are required."));
    }

    @Test
    void testHandleAddBookActionWithValidFields() throws Exception {
        // Set up valid inputs
        when(titleField.getText()).thenReturn("Test Book");
        when(authorField.getText()).thenReturn("Test Author");
        when(publisherField.getText()).thenReturn("Test Publisher");
        when(descriptionField.getText()).thenReturn("Description");
        when(publishedDateField.getText()).thenReturn("2024-11-27");
        when(pageCountField.getText()).thenReturn("300");
        when(categoriesField.getText()).thenReturn("Fiction");
        when(ratingField.getText()).thenReturn("4.5");
        when(imageUrlField.getText()).thenReturn("image_url");
        when(previewLinkField.getText()).thenReturn("preview_link");

        // Mock the database connection behavior
        Connection mockConnection = mock(Connection.class);
        when(DatabaseConnection.getConnection()).thenReturn(mockConnection);

        // Call the method under test
        addBookController.handleAddBookAction();

        // Verify that saveBookToDatabase is called once
        verify(addBookController, times(1)).saveBookToDatabase(
                "Test Book", "Test Author", "Test Publisher", "Description", "image_url",
                "2024-11-27", "300", "Fiction", "4.5", "preview_link");

        // Verify the alert for successful insertion (if your mock database query succeeds)
        verify(addBookController, times(1)).showAlert(Alert.AlertType.INFORMATION, "Success", "The book has been added successfully.");
    }

    @Test
    void testHandleCancelAction() {
        // Mock the cancel button and stage
        Stage mockStage = mock(Stage.class);
        when(cancelButton.getScene()).thenReturn(mock(Scene.class));
        when(cancelButton.getScene().getWindow()).thenReturn(mockStage);

        // Call the method under test
        addBookController.handleCancelAction();

        // Verify that the stage is closed
        verify(mockStage, times(1)).close();
    }

    @Test
    void testHandleReturnToRequestAction() throws Exception {
        // Mock the FXMLLoader and Scene
        FXMLLoader mockLoader = mock(FXMLLoader.class);
        Parent mockRoot = mock(Parent.class);
        when(mockLoader.load()).thenReturn(mockRoot);

        // Call the method under test
        addBookController.handleReturnToRequestAction();

        // Verify that a new scene is set
        verify(mockLoader, times(1)).load();
    }

}
