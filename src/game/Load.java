package game;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Load {
    private Stage primaryStage;
    private StartMeUp gameEngine;
    private GridPane gameGrid;
    private File saveFile;
    private String mapSetName;

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
     * Creates a new {@link  GraphicObject} at a position, with game object as the parameter.
     * @param   gameObject  The character at that position, defines whether it is a wall, air etc.
     * @param   location    The position the game object needs to be added in.
     */
    public void addObjectToGrid(GameObject gameObject, Point location) {
        GraphicObject graphicObject = new GraphicObject(gameObject);
        gameGrid.add(graphicObject, location.y, location.x);
    }
}
