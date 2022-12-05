package cs1302.omega;

import javafx.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

/**
 * This is the app class for the Snake Game. The snake is controlled by left, right, up, down keys.
 */
public class OmegaApp extends  Application {

    private Scene scene1;
    private Stage stage;
    private Timeline timeline;
    private Group root;
    private Canvas canvas;
    private GraphicsContext gc;

    private final int height = 600;
    private final int width = 600;
    private final int columns = 15;
    private final int rows = 15;
    private final int squareDim = height / columns;

    /**
     * This is the constructor for the OmegaApp class that initializes variables for Snake game.
     */
    public OmegaApp() {
        this.stage = null;
        this.scene1 = null;
        this.root = new Group();
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
    }

    /**
     * This is the start method that has an override. It creates the scene(s) and
     * sets up timelines for the app.
     * @param stage primary stage of the app
     */
    @Override
    public void start(Stage stage) {
        this.root.getChildren().add(canvas);
        this.stage = stage;
        this.stage.setTitle("Snake Game!");
        this.scene1 = new Scene(root);
        this.stage.setScene(scene1);
        this.stage.show();

        EventHandler<ActionEvent> handler = event -> {
            try {
                play(gc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        timeline = new Timeline(new KeyFrame(Duration.millis(150), handler));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Performs all main operations for game including setting up background, snake, food,
     * and performing all game operations like moving the snake and eating the food.
     * @param gc
     */
    private void play(GraphicsContext gc) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("8fcd39"));
                } else {
                    gc.setFill(Color.web("a8d949"));
                }
                gc.fillRect(squareDim * i,
                        squareDim * j,
                        squareDim,
                        squareDim);
            }
        }
    }
}
