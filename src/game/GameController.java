package game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

/**
 * Non-Implemented
 */
public class GameController {

    @FXML
    private MenuItem saveGame;

    @FXML
    private MenuItem loadGame;

    @FXML
    private MenuItem exit;

    @FXML
    private MenuItem undo;

    @FXML
    private MenuItem musicToggle;

    @FXML
    private MenuItem debugToggle;

    @FXML
    private MenuItem resetLevel;

    @FXML
    private MenuItem aboutGame;

    @FXML
    void SaveGame(ActionEvent event) {
        System.out.println("Save");
    }

}
