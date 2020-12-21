package game;
import game.GameController;
import game.Main;

import game.StartMeUp;
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

    }

    @FXML
    void startGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("GameView.fxml"));
            VBox root = loader.load();

            Stage primaryStage = (Stage) startNew.getScene().getWindow();
            primaryStage.setTitle(StartMeUp.GAME_NAME);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            System.out.println(wallSlider.getValue());
            GameController controller = loader.getController();
            controller.startNew(primaryStage, (int)wallSlider.getValue(), (int)floorSlider.getValue());

        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Fxml load failed");
            System.exit(1);
        }
    }

}
