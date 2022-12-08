package cs1302.omega;

import javafx.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
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
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * This is the app class for the Snake Game. The snake is controlled by left, right, up, down keys.
 */
public class OmegaApp extends  Application {

    private Scene scene1;
    private Scene scene2;
    private Stage stage;
    private Label scoreLabel;
    private Timeline timeline;
    private Group root;
    private Canvas canvas;
    private GraphicsContext gc;
    private Image foodPicture;
    private boolean isGameOver;

    private int score = 0;
    private Point head;
    private List<Point> body = new LinkedList<>();
    private int snakeDirection;
    private final int height = 600;
    private final int width = 600;
    private final int columns = 15;
    private final int rows = 15;
    private final int squareDim = height / columns;
    private int xCoord;
    private int yCoord;

    /**
     * This is the constructor for the OmegaApp class that initializes variables for Snake game.
     */
    public OmegaApp() {
        this.stage = null;
        this.scene1 = null;
        this.root = new Group();
        this.canvas = new Canvas(width, height);
        this.gc = canvas.getGraphicsContext2D();
        initializeSnake();
        this.head = body.get(0);

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

        //Scene 2
        Label label2 = new Label("Game Over!");
        label2.setFont(new Font("Lucida Sans Unicode", 70));
        label2.setTextFill(Color.web("4c7cfc"));
        scoreLabel = new Label();
        scoreLabel.setFont(new Font("Lucida Sans Unicode", 45));
        Button button2 = new Button("Restart");
        button2.setMinSize(200, 75);
        button2.setStyle("-fx-font-size:25");
        button2.setOnAction(e -> {
            try {
                restart();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        VBox layout2 = new VBox(20);
        layout2.setAlignment(Pos.CENTER);
        layout2.getChildren().addAll(label2, this.scoreLabel, button2);
        layout2.setBackground(new Background(
                new BackgroundFill(Color.web("f5e5dc"),
                        CornerRadii.EMPTY,
                        Insets.EMPTY)));
        scene2 = new Scene(layout2, width, height);

        //Scene 1 Key Press
        scene1.setOnKeyPressed(e -> handle(e));
        createFood();

        EventHandler<ActionEvent> handler = event -> {
            try {
                play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        timeline = new Timeline(new KeyFrame(Duration.millis(135), handler));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Sets a new scene to show the Game Over screen.
     */
    public void onGameOver() {
        timeline.stop();
        this.scoreLabel.setText("Score: " + this.score);
        stage.setScene(scene2);
        stage.show();
    }

    /**
     * Restarts game if user clicks restart button.
     * @throws Exception
     */
    public void restart() throws Exception {
        stage.close();
        new OmegaApp().start(new Stage());
    }

    /**
     * Performs all main operations for game including setting up background, snake, food,
     * and performing all game operations like moving the snake and eating the food.
     * @param gc
     */
    public void play() {
        if (isGameOver) {
            onGameOver();
            return;
        }
        //Use row, column indices to create background grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                boolean isEven = Math.abs(j - i) % 2 == 0;
                if (isEven) {
                    gc.setFill(Color.web("f5e5dc"));
                } else {
                    gc.setFill(Color.web("ebd7cc"));
                }
                gc.fillRect(squareDim * i,
                        squareDim * j,
                        squareDim,
                        squareDim);
            }
        }

        //Shows apple in grid
        int xLocation = xCoord * squareDim;
        int yLocation = yCoord * squareDim;

        gc.drawImage(foodPicture,
                xLocation,
                yLocation,
                squareDim,
                squareDim);

        populateSnake();

        //Shows score board
        gc.setFill(Color.BLACK);
        Font font = new Font("Lucida Sans Unicode", 30);
        gc.setFont(font);
        gc.fillText("SCORE: " + score, 15, 590);

        moveSnake();

        //Score gets updated, new segment gets added, and food gets created
        //everytime the snake eats more food.
        if (head.getX() == xCoord && head.getY() == yCoord) {
            Point pointNew = new Point(-1, -1);
            body.add(pointNew);
            score = score + 1;
            createFood();
        }

        isGameOver();
    }

    /**
     * Controls keyboard input for moving the snake in various directions.
     * @param keyEvent
     */

    public void handle(KeyEvent keyEvent) {
        //Uses keyboard input to control snake movement
        int up = 0;
        int down = 1;
        int left = 2;
        int right = 3;
        if (keyEvent.getCode() == KeyCode.RIGHT && snakeDirection != left) {
            snakeDirection = right;
        } else if (keyEvent.getCode() == KeyCode.LEFT && snakeDirection != right) {
            snakeDirection = left;
        } else if (keyEvent.getCode() == KeyCode.UP && snakeDirection != down) {
            snakeDirection = up;
        } else if (keyEvent.getCode() == KeyCode.DOWN && snakeDirection != up) {
            snakeDirection = down;
        }
    }


    /**
     *  Creates the snake using Point class objects.
     */
    public void initializeSnake() {
        //Add two points to initial snake
        Point point1 = new Point(10, 10);
        Point point2 = new Point(10, 10);

        body.add(point1);
        body.add(point2);

    }


    /**
     * Mimics snake movement by shifting each block up by one.
     */
    public void moveSnake() {
        //Mimics snake movement based on keyboard input
        for (int i = body.size() - 1; i >= 1; i--) {
            body.get(i).setX(body.get(i - 1).getX());
            body.get(i).setY(body.get(i - 1).getY());

        }
        if (snakeDirection == 0) {
            head.setY(head.getY() - 1);
        } else if (snakeDirection == 1) {
            head.setY(head.getY() + 1);
        } else if (snakeDirection == 2) {
            head.setX(head.getX() - 1);
        } else if (snakeDirection == 3) {
            head.setX(head.getX() + 1);
        }

    }

    /**
     * Draws the snake on the grid.
     * @param gc
     */
    public void populateSnake() {
        Paint snakeColor = Color.web("4c7cfc");
        gc.setFill(snakeColor);

        //Fill Head
        int xLocationHead = head.getX() * squareDim;
        int yLocationHead = head.getY() * squareDim;
        gc.fillRoundRect(xLocationHead,
                yLocationHead,
                squareDim,
                squareDim,
                15,
                15);

        //Fill rest of body
        for (int i = 1; i < body.size(); i++) {
            int xLocationBody = body.get(i).getX() * squareDim;
            int yLocationBody = body.get(i).getY() * squareDim;
            gc.fillRoundRect(xLocationBody,
                    yLocationBody,
                    squareDim,
                    squareDim,
                    15,
                    15);
        }

    }

    /**
     * Generates random grid box for food and sets foodPicture to be
     * apple image.
     */
    public void createFood() {
        //Generate random row and column for food placement
        xCoord = (int)(Math.random() * rows);
        yCoord = (int)(Math.random() * columns);
        foodPicture = new Image("file:resources/apple.png");
    }

    /**
     * Checks snake game to see if game over.
     */
    public void isGameOver() {
        //Destroy itself
        for (int i = 1; i < body.size(); i++) {
            if (head.getX() == body.get(i).getX()
                    && head.getY() == body.get(i).getY()) {
                isGameOver = true;
                break;
            }
        }

        //Bump into walls
        if (head.getX() * squareDim == width
                || head.getY() * squareDim == height
                || head.getX() < 0 || head.getY() < 0) {
            isGameOver = true;
        }

    }
}
