package game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the SavePopupView.fxml
 */
public class SavePopupController {
    String fileName;

    @FXML
    private TextField saveNameInput;

    @FXML
    private Button saveButton;

    @FXML
    void saveButtonPressed(ActionEvent event) {
        fileName = saveNameInput.getText();
        GameController.m_saveName = fileName;
        Stage currentStage = (Stage) saveButton.getScene().getWindow();
        currentStage.close();
    }

}
