package game;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

class GraphicObject extends ImageView {
    
    /**
     *
     * @param obj
     */
    GraphicObject(GameObject obj, int wallChoice, int floorChoice, Image[] sprites) throws FileNotFoundException {
        InputStream spriteLocation = null;
        switch (obj) {
            case WALL:
                switch (wallChoice) {
                    case (0): {
                        this.setImage(sprites[0]);
                    }
                    case (1): {
                        this.setImage(sprites[1]);
                    }
                    case (2): {
                        this.setImage(sprites[2]);
                    }
                    case (3): {
                        this.setImage(sprites[3]);
                    }
                }
                break;

            case CRATE:
                this.setImage(sprites[4]);
                break;

            case DIAMOND:
                this.setImage(sprites[4]);

                // TODO: fix memory leak.
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
                this.setImage(sprites[4]);
                break;

            case FLOOR:
                switch (floorChoice) {
                    case (0): {
                        this.setImage(sprites[6]);
                    }
                    case (1): {
                        this.setImage(sprites[7]);
                    }
                    case (2): {
                        this.setImage(sprites[8]);
                    }
                    case (3): {
                        this.setImage(sprites[9]);
                    }
                }
                break;

            case CRATE_ON_DIAMOND:
                this.setImage(sprites[5]);
                break;


            default:
                String message = "Error in Level constructor. Object not recognized.";
                StartMeUp.logger.severe(message);
                throw new AssertionError(message);
        }

        this.setFitHeight(32);
        this.setFitWidth(32);

        /*if (obj != GameObject.WALL) {
            this.setArcHeight(50);
            this.setArcWidth(50);
        }

        if (StartMeUp.isDebugActive()) {
            this.setStroke(Color.RED);
            this.setStrokeWidth(0.25);
        }*/
    }

}
