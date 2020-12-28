package game;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.FileNotFoundException;

class GraphicObject extends ImageView {

    /**
     *
     * @param obj
     */
    GraphicObject(GameObject obj, int wallChoice, int floorChoice, Image[] sprites) throws FileNotFoundException {

        switch (obj) {
            case WALL:

                switch (wallChoice) {
                    case 0: {
                        this.setImage(sprites[0]);
                        break;
                    }
                    case 1: {
                        this.setImage(sprites[1]);
                        break;
                    }
                    case 2: {
                        this.setImage(sprites[2]);
                        break;
                    }
                    case 3: {
                        this.setImage(sprites[3]);
                        break;
                    }
                }
                break;

            case CRATE:
                this.setImage(sprites[4]);
                break;

            case DIAMOND:
                this.setImage(sprites[11]);

                if (StartMeUp.isDebugActive()) {
                    FadeTransition ft = new FadeTransition(Duration.millis(1000), this);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.2);
                    ft.setCycleCount(Timeline.INDEFINITE);
                    ft.setAutoReverse(true);
                    ft.play();
                }

                break;

            case KEEPER:
                this.setImage(sprites[10]);
                break;

            case FLOOR:
                switch (floorChoice) {
                    case (0): {
                        this.setImage(sprites[6]);
                        break;
                    }
                    case (1): {
                        this.setImage(sprites[7]);
                        break;
                    }
                    case (2): {
                        this.setImage(sprites[8]);
                        break;
                    }
                    case (3): {
                        this.setImage(sprites[9]);
                        break;
                    }
                }
                break;

            case CRATE_ON_DIAMOND:
                this.setImage(sprites[5]);
                break;


            default:
                String message = "Error in Level constructor. Object not recognized.";
                StartMeUp.getLogger().severe(message);
                throw new AssertionError(message);
        }

        this.setFitHeight(32);
        this.setFitWidth(32);

    }

}
