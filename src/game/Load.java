package game;

import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
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

    /**
     * Loads the default SampleGame.skb and initializes the game.
     */
    static InputStream loadDefaultSaveFile() {
        InputStream in = Load.class.getClassLoader().getResourceAsStream("resources/SampleGame.skb");

        return in;
    }

    /**
     * Opens file search with filters for a .<!-- -->skb extension,
     * loads chosen file using {@link GameController#initializeGame}
     * @throws FileNotFoundException    Thrown when there is an error with retrieving the file
     */
    public static File fileSelect(Stage primaryStage) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        File saveFile = fileChooser.showOpenDialog(primaryStage);

        if (saveFile != null) {
            if (StartMeUp.isDebugActive()) {
                StartMeUp.logger.info("Loading save file: " + saveFile.getName());
            }
            return saveFile;
        } else {
            throw new FileNotFoundException();
        }
    }
}
