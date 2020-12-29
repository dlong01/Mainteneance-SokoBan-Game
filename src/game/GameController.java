package game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Controller for the GameView.fxml
 */
public class GameController {

    private static Stage m_primaryStage;
    private StartMeUp gameEngine;
    private File saveFile;
    private Image[] sprites = new Image[12];
    private int[] spriteChoice = new int[2];
    static String m_saveName;

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

    /**
     * Loader for all sprites in the game
     */
    public void loadSprites() {
        try {
            System.out.println("load called");
            this.sprites[0] = new Image(new FileInputStream("src/resources/images/wall_black.png"));
            this.sprites[1] = new Image(new FileInputStream("src/resources/images/wall_beige.png"));
            this.sprites[2] = new Image(new FileInputStream("src/resources/images/wall_brown.png"));
            this.sprites[3] = new Image(new FileInputStream("src/resources/images/wall_grey.png"));
            this.sprites[4] = new Image(new FileInputStream("src/resources/images/crate.png"));
            this.sprites[5] = new Image(new FileInputStream("src/resources/images/crate_finished.png"));
            this.sprites[6] = new Image(new FileInputStream("src/resources/images/floor_stone.png"));
            this.sprites[7] = new Image(new FileInputStream("src/resources/images/floor_sand.png"));
            this.sprites[8] = new Image(new FileInputStream("src/resources/images/floor_grass.png"));
            this.sprites[9] = new Image(new FileInputStream("src/resources/images/floor_dirt.png"));
            this.sprites[10] = new Image(new FileInputStream("src/resources/images/player.png"));
            this.sprites[11] = new Image(new FileInputStream("src/resources/images/target.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed loading sprites");
            System.exit(1);
        }
    }

    /**
     * Starts a new game using the default save file
     * @param   primaryStage    the Stage that the scene is on
     * @param   wall            int representing the wall sprite the user chose
     * @param   floor           int representing the floor sprite the user chose
     */
    public void startNew (Stage primaryStage, int wall, int floor) {
        loadSprites();
        spriteChoice[0] = wall;
        spriteChoice[1] = floor;

        this.m_primaryStage = primaryStage;
        InputStream in = Load.loadDefaultSaveFile();

        initializeGame(in);
        setEventFilter();
    }

    /**
     * Starts a new game using a file chose by the user
     * @param   primaryStage    the Stage that the scene is on
     * @param   wall            int representing the wall sprite the user chose
     * @param   floor           int representing the floor sprite the user chose
     */
    public void startOld (Stage primaryStage, int wall, int floor) {
        loadSprites();
        spriteChoice[0] = wall;
        spriteChoice[1] = floor;

        this.m_primaryStage = primaryStage;

        try {
            saveFile = Load.fileSelect(primaryStage);
            initializeGame(new FileInputStream(saveFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler for the save button menu item, loads the {@link SavePopupController}
     * @param   event   Detects when the SaveGame button is selected
     */
    @FXML
    void SaveGame(ActionEvent event) {
        List<Level> levels = gameEngine.getLevels();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/SavePopupView.fxml"));
            System.out.println(Main.class.getResource("SavePopupView.fxml"));
            VBox root = loader.load();

            final Stage savePopup = new Stage();

            savePopup.setTitle("Best Sokoban Ever V6");
            savePopup.setScene(new Scene(root));

            savePopup.showAndWait();

            writeFile(levels);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("unable to load SavePopupView");
        }

        System.out.println("Save");
    }

    /**
     * Loads the file selected by the user in {@link Load#fileSelect(Stage)} when Load file is pressed in the menus
     * @param   event   Detects when the LoadGame button is selected
     */
    @FXML
    void LoadGame(ActionEvent event) {
        try {
            saveFile = Load.fileSelect(m_primaryStage);
            initializeGame(new FileInputStream(saveFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the game when the exit game is pressed
     * @param   event   Detects when the ExitGame menu option is selected
     */
    @FXML
    void ExitGame(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Toggles the debug option with {@link StartMeUp#toggleDebug()} when the menu option is selected
     * @param   event   Detects when the toggleDebug menu option is selected
     */
    @FXML
    void ToggleDebug(ActionEvent event) {
        gameEngine.toggleDebug();
        reloadGrid();
    }

    /**
     * NOT WORKING - Toggles the music player on when the option os selected
     * @param   event   Detects when the toggleMusic menu option is selected
     */
    @FXML
    void ToggleMusic(ActionEvent event) {
        if (!gameEngine.isPlayingMusic()) {
            gameEngine.playMusic();
        } else{
            gameEngine.stopMusic();
        }
    }

    /**
     * Displays the about dialog box
     * @param   event   Detects when the aboutGame menu option is selected
     */
    @FXML
    void ShowAbout(ActionEvent event) {
        String title = "About This Game";
        String message = "Enjoy the Game!\n";

        newDialog(title, message, null);
    }

    /**
     * NOT WORKING
     * @param   event   Detects when the resetLevel menu option is selected
     */
    @FXML
    void ResetLevel(ActionEvent event) {
    }

    /**
     * Initializes a new gameEngine with {@link StartMeUp}
     * using a .<!-- -->skb file as input.
     * Once the engine is initialized the grid is reloaded.
     * @param   input   The game file to initialize
     */
    public void initializeGame(InputStream input) {
        gameEngine = new StartMeUp(input, false);
        reloadGrid();
    }

    /**
     * Creates an event to detect a key press.
     * Upon key press {@link StartMeUp#handleKey} is called.
     */
    public void setEventFilter() {
        m_primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
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
            highScores(gameEngine.getMovesCount());
            return;
        }

        Level currentLevel = gameEngine.getCurrentLevel();
        Level.LevelIterator levelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getCurrentPosition());
        }
        gameGrid.autosize();
        m_primaryStage.sizeToScene();
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
    public static void newDialog(String dialogTitle, String dialogMessage, Effect dialogMessageEffect) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(m_primaryStage);
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
        try {
            GraphicObject graphicObject = new GraphicObject(gameObject, spriteChoice[0], spriteChoice[1], sprites);
            gameGrid.add(graphicObject, location.y, location.x);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading sprites");
            System.exit(1);
        }
    }

    /**
     * Writes the Level data to an external save file
     * @param   levels  List containing all the levels in the game
     * @throws  IOException Error thrown by the buffered writer
     */
    private void writeFile(List<Level> levels) throws IOException {

        File file = new File("src/resources/saves/"+ m_saveName +".skb");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        bw.write("MapSetName: " + gameEngine.getMapSetName());
        bw.newLine();

        for (Level level:levels) {

            bw.write("LevelName: "+level.getName());
            bw.newLine();

            if (!level.getStatus()) {
                bw.write("IsComplete: F");
            } else {
                bw.write("IsComplete: T");
            }
            bw.newLine();

            bw.write("LevelMoves: "+level.getMoves());
            bw.newLine();

            GameGrid objectsGrid = level.getObjectsGrid();
            GameGrid diamondsGrid = level.getDiamondsGrid();

            GameGrid.GridIterator objectIterator = (GameGrid.GridIterator) objectsGrid.iterator();
            GameGrid.GridIterator diamondIterator = (GameGrid.GridIterator) diamondsGrid.iterator();

            int currentCol = 0;
            while (objectIterator.hasNext()) {
                if (currentCol >= 20) {
                    bw.newLine();
                    currentCol = 0;
                }
                GameObject curObj = objectIterator.next();
                GameObject curDia = diamondIterator.next();
                if (curDia == GameObject.DIAMOND) {
                    if (curObj == GameObject.CRATE) {
                        bw.write("O");
                    } else {
                        bw.write("D");
                    }
                    bw.flush();
                    currentCol++;
                    continue;
                }

                switch (curObj) {
                    case WALL : {
                        bw.write('W');
                        break;
                    }
                    case FLOOR: {
                        bw.write(' ');
                        break;
                    }
                    case KEEPER: {
                        bw.write('S');
                        break;
                    }
                    case CRATE: {
                        bw.write('C');
                        break;
                    }
                }
                bw.flush();
                currentCol++;
            }

            bw.newLine();
            bw.newLine();
            bw.flush();
        }

        bw.close();
        System.out.println("Data Entered in to the file successfully");
    }

    /**
     * Creates a new dialog box containing all of the scores up until the point it is called
     * @param   levelsList  List containing all of the levels in the game
     */
    public static void levelScoreBoard(List<Level> levelsList) {
        StringBuilder highScores = new StringBuilder();

        for (Level level : levelsList) {
            if (level.isComplete()) {
                highScores.append(level.getName()+": "+level.getMoves()+"\n");
            }
        }
        newDialog("Scores so far", highScores.toString(), null);


    }

    /**
     * Creates a new buffered reader get high scores from an external file. 
     * Inputs the players score into the Highscores list.
     * @param   totalScore  The total score for the player in the current game
     */
    public static void highScores(int totalScore){
        List<Integer> scores = new ArrayList<>();
        List<Integer> newScores = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/resources/highScores.txt"));
            int i = 1;
            String line = null;
            while((line = br.readLine()) !=null) {
                System.out.println(i+". ");
                line = line.replace(i+". ", "");
                System.out.println(line);
                Integer score = Integer.valueOf(line);
                scores.add(score);
                newScores.add(score);
                i++;
            }


            for (Integer score : scores) {
                if (totalScore <= score) {
                    newScores.add(newScores.indexOf(score), totalScore);
                    break;
                }
            }
            System.out.println(newScores);

            try{
                newScores.remove(10);
            } catch (IndexOutOfBoundsException e) {}
            System.out.println(newScores);

            writeHighScores(newScores);

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to load high scores");
        }
    }

    /**
     * Writes high scores to an external file
     * @param   scores  list passed from {@link GameController#highScores(int)} containing the scores from the highscores file
     * @throws  IOException Thrown when opening the highScores.txt file, caught in {@link GameController#highScores(int)}
     */
    private static void writeHighScores(List<Integer> scores) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/resources/highScores.txt"));
        int i = 1;
        while (i != scores.size() + 1) {
            bw.write(i+". "+scores.get(i-1));
            bw.newLine();
            i++;
        }

        bw.close();
    }


}
