package game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;



/**
 * Non-Implemented
 */
public class GameController {

    private Stage primaryStage;
    private StartMeUp gameEngine;
    private File saveFile;

    /**
     * Setter for the primaryStage field
     * @param   primaryStage    the Stage that the scene is on
     */
    public void startNew (Stage primaryStage) {
        this.primaryStage = primaryStage;
        InputStream in = Load.loadDefaultSaveFile();
        System.out.println(in);
        initializeGame(in);
        System.out.println("Hi");
        setEventFilter();
        System.out.println("Hi");
    }

    public void startOld (Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            saveFile = Load.fileSelect(primaryStage);
            initializeGame(new FileInputStream(saveFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private GridPane gameGrid;
    @FXML
    private MenuItem saveGame;
    @FXML
    private MenuItem loadGame;
    @FXML
    private MenuItem exit;
    @FXML
    private MenuItem undo;
    @FXML
    private RadioMenuItem musicToggle;
    @FXML
    private RadioMenuItem debugToggle;
    @FXML
    private MenuItem resetLevel;
    @FXML
    private MenuItem aboutGame;

    @FXML
    void SaveGame(ActionEvent event) {
        System.out.println("Save");
    }

    @FXML
    void LoadGame(ActionEvent event) {
        try {
            saveFile = Load.fileSelect(primaryStage);
            initializeGame(new FileInputStream(saveFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ExitGame(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void ToggleDebug(ActionEvent event) {
        gameEngine.toggleDebug();
        reloadGrid();
    }

    @FXML
    void ToggleMusic(ActionEvent event) {

    }

    @FXML
    void ShowAbout(ActionEvent event) {
        String title = "About This Game";
        String message = "Enjoy the Game!\n";

        newDialog(title, message, null);
    }

    /**
     * Initializes a new gameEngine with {@link StartMeUp}
     * using a .<!-- -->skb file as input.
     * Once the engine is initialized the grid is reloaded.
     * @param   input   The game file to initialize
     */
    public void initializeGame(InputStream input) {
        gameEngine = new StartMeUp(input, true);
        reloadGrid();
    }

    /**
     * Creates an event to detect a key press.
     * Upon key press {@link StartMeUp#handleKey} is called.
     */
    public void setEventFilter() {
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            gameEngine.handleKey(event.getCode());
            reloadGrid();
        });
    }

    /**
     * Checks if the game is completed, updates each of the positions on the grid.
     * Uses {@link #showVictoryMessage()}
     */
    private void reloadGrid() {
        if (gameEngine.isGameComplete()) {
            showVictoryMessage();
            return;
        }

        Level currentLevel = gameEngine.getCurrentLevel();
        Level.LevelIterator levelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getCurrentPosition());
        }
        gameGrid.autosize();
        primaryStage.sizeToScene();
    }

    /**
     * Displays a dialog box with the number of moves to complete the game.
     */
    public void showVictoryMessage() {
        String dialogTitle = "Game Over!";
        String dialogMessage = "You completed " + gameEngine.getMapSetName() + " in " + gameEngine.getMovesCount() + " moves!";
        MotionBlur mb = new MotionBlur(2, 3);

        newDialog(dialogTitle, dialogMessage, mb);
    }

    /**
     * Creates a dialog box using the information from {@link  #showVictoryMessage()}
     * @param   dialogTitle         The title for the dialog box
     * @param   dialogMessage       The message in the dialog box
     * @param   dialogMessageEffect The effect on the dialog box
     */
    public void newDialog(String dialogTitle, String dialogMessage, Effect dialogMessageEffect) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setResizable(false);
        dialog.setTitle(dialogTitle);

        Text text1 = new Text(dialogMessage);
        text1.setTextAlignment(TextAlignment.CENTER);
        text1.setFont(javafx.scene.text.Font.font(14));

        if (dialogMessageEffect != null) {
            text1.setEffect(dialogMessageEffect);
        }

        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);
        dialogVbox.getChildren().add(text1);

        Scene dialogScene = new Scene(dialogVbox, 350, 150);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Creates a new {@link  #showVictoryMessage()} at a position, with game object as the parameter.
     * @param   gameObject  The character at that position, defines whether it is a wall, air etc.
     * @param   location    The position the game object needs to be added in.
     */
    public void addObjectToGrid(GameObject gameObject, Point location) {
        GraphicObject graphicObject = new GraphicObject(gameObject);
        gameGrid.add(graphicObject, location.y, location.x);
    }


}
