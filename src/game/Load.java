package game;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

/**
 * @author Daniel Long
 * Handles load methods for the {@link GameController}
 */
public class Load {

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
     * @param   primaryStage    The stage to display the file selection popup on
     * @return  File saveFile   The file selected by the user
     * @throws  FileNotFoundException   Thrown when there is an error with retrieving the file
     */
    public static File fileSelect(Stage primaryStage) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        File saveFile = fileChooser.showOpenDialog(primaryStage);

        if (saveFile != null) {
            if (StartMeUp.isDebugActive()) {
                StartMeUp.getLogger().info("Loading save file: " + saveFile.getName());
            }
            return saveFile;
        } else {
            throw new FileNotFoundException();
        }
    }
}
