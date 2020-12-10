package game;

import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controls the interactions between the User and the Game, including implementing the menu options and movement.
 */
public class StartMeUp {
    /**
     * Defines a public String for the name of the Game as BestSokobanEverV6.
     */
    public static final String GAME_NAME = "BestSokobanEverV6";
    /**
     * Creates a public object of the {@link GameLogger} class.
     */
    public static GameLogger logger;


    private static boolean debug = false;
    private Level currentLevel;
    private String mapSetName;
    private List<Level> levels;
    private boolean gameComplete = false;
    private int movesCount = 0;
    private MediaPlayer player;

    /**
     * Constructor for the game engine instanced in {@link Main#initializeGame(InputStream)}.
     * Creates a new game logger, stores {@link StartMeUp#loadGameFile(InputStream)} in levels,
     * calls {@link StartMeUp#getNextLevel()} and stores the return value in the currentLevel variable.
     * @param input         The game file to be loaded
     * @param production    Possibly a redundant parameter
     */
    public StartMeUp(InputStream input, boolean production) {
        try {
            logger = new GameLogger();
            levels = loadGameFile(input);
            currentLevel = getNextLevel();

            if (production) {
                createPlayer();
            }
        } catch (IOException x) {
            System.out.println("Cannot create logger.");
        } catch (NoSuchElementException e) {
            logger.warning("Cannot load the default save file: " + e.getStackTrace());
        } catch (LineUnavailableException e) {
            logger.warning("Cannot load the music file: " + e.getStackTrace());
        }
    }

    /**
     * Getter to retrieve the value of debug.
     * @return  Boolean value debug
     */
    public static boolean isDebugActive() {
        return debug;
    }

    /**
     * Getter to retrieve movesCount.
     * @return  int value movesCount
     */
    public int getMovesCount() {
        return movesCount;
    }

    /**
     * Getter for the mapSetName variable.
     * @return  String value mapSetName
     */
    public String getMapSetName() {
        return mapSetName;
    }

    /**
     * Takes keyboard input and calls {@link #move(Point)} to move the player object.
     * @param code  key pressed by the player
     */
    public void handleKey(KeyCode code) {
        switch (code) {
            case UP:
                move(new Point(-1, 0));
                break;

            case RIGHT:
                move(new Point(0, 1));
                break;

            case DOWN:
                move(new Point(1, 0));
                break;

            case LEFT:
                move(new Point(0, -1));
                break;

            default:
                // TODO: implement something funny.
        }

        if (isDebugActive()) {
            System.out.println(code);
        }
    }

    /**
     * Moves the player and moves any objects that need to move as well
     * @param delta Change in position of the player object
     */
    public void move(Point delta) {
        if (isGameComplete()) {
            return;
        }

        Point keeperPosition = currentLevel.getKeeperPosition();
        GameObject keeper = currentLevel.getObjectAt(keeperPosition);
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        GameObject keeperTarget = currentLevel.getObjectAt(targetObjectPoint);

        if (StartMeUp.isDebugActive()) {
            System.out.println("Current level state:");
            System.out.println(currentLevel.toString());
            System.out.println("Keeper pos: " + keeperPosition);
            System.out.println("Movement source obj: " + keeper);
            System.out.printf("Target object: %s at [%s]", keeperTarget, targetObjectPoint);
        }

        boolean keeperMoved = false;

        switch (keeperTarget) {

            case WALL:
                break;

            case CRATE:

                GameObject crateTarget = currentLevel.getTargetObject(targetObjectPoint, delta);
                if (crateTarget != GameObject.FLOOR) {
                    break;
                }

                currentLevel.moveGameObjectBy(keeperTarget, targetObjectPoint, delta);
                currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            case FLOOR:
                currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            default:
                logger.severe("The object to be moved was not a recognised GameObject.");
                throw new AssertionError("This should not have happened. Report this problem to the developer.");
        }

        if (keeperMoved) {
            keeperPosition.translate((int) delta.getX(), (int) delta.getY());
            movesCount++;
            if (currentLevel.isComplete()) {
                if (isDebugActive()) {
                    System.out.println("Level complete!");
                }

                currentLevel = getNextLevel();
            }
        }
    }

    /**
     * Loads the game for the InputStream value.
     * Assigns values to the variables that are used in other methods throughout the program.
     * @param input File containing values of the game file
     * @return      Game files parsed into a List that is then accessed when making changes to the game
     */
    public List<Level> loadGameFile(InputStream input) {
        List<Level> levels = new ArrayList<>(5);
        int levelIndex = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            boolean parsedFirstLevel = false;
            List<String> rawLevel = new ArrayList<>();
            String levelName = "";

            while (true) {
                String line = reader.readLine();

                // Break the loop if EOF is reached
                if (line == null) {
                    if (rawLevel.size() != 0) {
                        Level parsedLevel = new Level(levelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                    }
                    break;
                }

                if (line.contains("MapSetName")) {
                    mapSetName = line.replace("MapSetName: ", "");
                    continue;
                }

                if (line.contains("LevelName")) {
                    if (parsedFirstLevel) {
                        Level parsedLevel = new Level(levelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                        rawLevel.clear();
                    } else {
                        parsedFirstLevel = true;
                    }

                    levelName = line.replace("LevelName: ", "");
                    continue;
                }

                line = line.trim();
                line = line.toUpperCase();
                // If the line contains at least 2 WALLS, add it to the list
                if (line.matches(".*W.*W.*")) {
                    rawLevel.add(line);
                }
            }

        } catch (IOException e) {
            logger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            logger.severe("Cannot open the requested file: " + e);
        }

        return levels;
    }

    /**
     * Getter for gameComplete.
     * @return boolean value of gameComplete
     */
    public boolean isGameComplete() {
        return gameComplete;
    }

    /**
     * Defines a music Player to allow the game to play music when music is toggled on.
     * @throws  LineUnavailableException    Thrown when the music file cannot be read, caught in {@link StartMeUp}
     */
    public void createPlayer() throws LineUnavailableException {
//        File filePath = new File(getClass().getClassLoader().getResource("music/puzzle_theme.wav").toString());
//        Media music = new Media(filePath.toURI().toString());
//        player = new MediaPlayer(music);
//       player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
    }

    /**
     * Runs play on the player, starting the music.
     */
    public void playMusic() {
//        player.play();
    }

    /**
     * Runs stop on the player, stopping the music.
     */
    public void stopMusic() {
//        player.stop();
    }

    /**
     * Getter for the current status of the music Player.
     * @return  Boolean - Not implemented
     */
    public boolean isPlayingMusic() {
//        return player.getStatus() == MediaPlayer.Status.PLAYING;
        return false;
    }

    /**
     * Getter for the next level from the List levels
     * @return  the level that the game needs to load next
     */
    public Level getNextLevel() {
        if (currentLevel == null) {
            return levels.get(0);
        }

        int currentLevelIndex = currentLevel.getIndex();
        if (currentLevelIndex < levels.size()) {
            return levels.get(currentLevelIndex + 1);
        }

        gameComplete = true;
        return null;
    }

    /**
     * Getter for the Current level
     * @return  the current Level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Inverts the value of debug.
     */
    public void toggleDebug() {
        debug = !debug;
    }

}