package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Handles the operations of the game by extending Application.
 * Manages the stages and initializing the game.
 */
public class Main extends Application {

    private Stage primaryStage;
    private StartMeUp gameEngine;
    private GridPane gameGrid;
    private File saveFile;
    private MenuBar MENU;

    /**
     * The main method, calls launch from the application class.
     * @param   args    Input on program start
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println("Done!");
    }

    /**
     * Defines the initial start conditions for the menus and the grid,
     * overrides the start method of application.
     * This method uses the {@link #loadDefaultSaveFile}
     * and {@link StartMeUp}
     * @param   primaryStage    The main game stage
     */
    @Override
    public void start(Stage primaryStage){
/*
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
*/


        this.primaryStage = primaryStage;

        MENU = new MenuBar();

        MenuItem menuItemSaveGame = new MenuItem("Save Game");
        menuItemSaveGame.setDisable(true);
        menuItemSaveGame.setOnAction(actionEvent -> saveGame());
        MenuItem menuItemLoadGame = new MenuItem("Load Game");
        menuItemLoadGame.setOnAction(actionEvent -> loadGame());
        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(actionEvent -> closeGame());
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(menuItemSaveGame, menuItemLoadGame,
                new SeparatorMenuItem(), menuItemExit);

        MenuItem menuItemUndo = new MenuItem("Undo");
        menuItemUndo.setDisable(true);
        menuItemUndo.setOnAction(actionEvent -> undo());
        RadioMenuItem radioMenuItemMusic = new RadioMenuItem("Toggle Music");
        radioMenuItemMusic.setOnAction(actionEvent -> toggleMusic());
        RadioMenuItem radioMenuItemDebug = new RadioMenuItem("Toggle Debug");
        radioMenuItemDebug.setOnAction(actionEvent -> toggleDebug());
        MenuItem menuItemResetLevel = new MenuItem("Reset Level");
        Menu menuLevel = new Menu("Level");
        menuLevel.setOnAction(actionEvent -> resetLevel());
        menuLevel.getItems().addAll(menuItemUndo, radioMenuItemMusic, radioMenuItemDebug,
                new SeparatorMenuItem(), menuItemResetLevel);

        MenuItem menuItemGame = new MenuItem("About This Game");
        Menu menuAbout = new Menu("About");
        menuAbout.setOnAction(actionEvent -> showAbout());
        menuAbout.getItems().addAll(menuItemGame);
        MENU.getMenus().addAll(menuFile, menuLevel, menuAbout);
        gameGrid = new GridPane();
        GridPane root = new GridPane();
        root.add(MENU, 0, 0);
        root.add(gameGrid, 0, 1);
        primaryStage.setTitle(StartMeUp.GAME_NAME);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        loadDefaultSaveFile(primaryStage);
    }

    /**
     * Loads the default SampleGame.skb and initializes the game.
     * @param   primaryStage    The main game stage
     */
    void loadDefaultSaveFile(Stage primaryStage) {
        this.primaryStage = primaryStage;
        System.out.println("Hi");
        InputStream in = getClass().getClassLoader().getResourceAsStream("resources/SampleGame.skb");
        System.out.println(in);
        initializeGame(in);
        System.out.println("Hi");
        setEventFilter();
        System.out.println("Hi");
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
     * Opens file search with filters for a .<!-- -->skb extension,
     * loads chosen file using {@link #initializeGame}
     * @throws FileNotFoundException    Throws an exception which is caught by the {@link #loadGame()}
     */
    public void loadGameFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        saveFile = fileChooser.showOpenDialog(primaryStage);

        if (saveFile != null) {
            if (StartMeUp.isDebugActive()) {
                StartMeUp.logger.info("Loading save file: " + saveFile.getName());
            }
            initializeGame(new FileInputStream(saveFile));
        }
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

    /**
     * Ends the program when the exit menu option is chosen.
     */
    public void closeGame() {
        System.exit(0);
    }

    /**
     * Saves the game when the save menu option is clicked.
     */
    public void saveGame() {
    }

    /**
     * Runs the {@link  #loadGameFile()} method upon load menu option being selected.
     */
    public void loadGame() {
        try {
            loadGameFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Undoes the last move the player made
     */
    public void undo() {
        closeGame();
    }

    /**
     * Resets the level to the starting state
     */
    public void resetLevel() {
    }

    /**
     *Displays a dialog box that contains information about the game.
     */
    public void showAbout() {
        String title = "About This Game";
        String message = "Enjoy the Game!\n";

        newDialog(title, message, null);
    }

    /**
     * Allows the user to select whether of not they want music.
     */
    public void toggleMusic() {
        // TODO
    }

    /**
     * Enables debug mode which makes it easier to understand the games processes.
     */
    public void toggleDebug() {
        gameEngine.toggleDebug();
        reloadGrid();
    }


}
