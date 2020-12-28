package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;

/**
 * Handles the operations of the game by extending Application.
 * Manages the stages and initializing the game.
 */
public class Main extends Application {

    /**
     * The main method, calls launch from the application class.
     *
     * @param args Input on program start
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println("Done!");
    }

    /**
     * Defines the initial start conditions for the menus and the grid,
     * overrides the start method of application.
     * This method uses the {@link Load#loadDefaultSaveFile}
     * and {@link StartMeUp}
     *
     * @param primaryStage The main game stage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/StartView.fxml"));
            System.out.println(Main.class.getResource("StartView.fxml"));
            AnchorPane root = loader.load();

            primaryStage.setTitle("Best Sokoban Ever V6");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fxml load failed");
            System.exit(1);
        }
    }
}
