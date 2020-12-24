package game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {


    @FXML
    private Button startNew;

    @FXML
    private Slider wallSlider;

    @FXML
    private Slider floorSlider;

    @FXML
    private Button loadGame;

    @FXML
    private Button exitGame;

    @FXML
    void endGame(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void loadOld(ActionEvent event) {
        Stage primaryStage = (Stage) exitGame.getScene().getWindow();
        GameController controller = loadGameController(primaryStage);
        controller.startOld(primaryStage, (int)wallSlider.getValue(), (int)floorSlider.getValue());
    }

    @FXML
    void startGame(ActionEvent event) {
        Stage primaryStage = (Stage) exitGame.getScene().getWindow();
        GameController controller = loadGameController(primaryStage);
        controller.startNew(primaryStage, (int)wallSlider.getValue(), (int)floorSlider.getValue());
    }

    GameController loadGameController(Stage primaryStage) {
        GameController controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("GameView.fxml"));
            VBox root = loader.load();

            primaryStage.setTitle(StartMeUp.GAME_NAME);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            System.out.println(wallSlider.getValue());
            controller = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed loading GameController");
            System.exit(1);
        }
        return controller;
    }

}
