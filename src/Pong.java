/**
 * Created by wojciechprazuch on 10.10.2017.
 */
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Pong extends Application {


    private ArrayList<Movable> gameObjects = new ArrayList<>();

    private static final int width = 800;
    private static final int height = 600;
    private double playerOneYPos = height / 2;
    private double playerTwoYPos = height / 2;
    private double ballXPos = width / 2;
    private double ballYPos = height / 2;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private boolean gameStarted;

    Ball ball;
    Paddle player1;
    Paddle player2;

    public void start(Stage stage) throws Exception {
        gameObjects.add(ball);
        gameObjects.add(player1);
        gameObjects.add(player2);

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);
        canvas.setOnMouseMoved(e ->  playerOneYPos  = e.getY());
        canvas.setOnMouseClicked(e ->  gameStarted = true);
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        tl.play();
    }



    private void showStartingScreen(GraphicsContext gc) {

        ball = new Ball(15, width/2, height/2, 0, 0);
        player1 = new Paddle(100, 15, 0, 0);
        player2 = new Paddle(100, 15, width - 15, 0);


        gc.setStroke(Color.YELLOW);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.strokeText("Click to Start", width / 2, height / 2);
        //ballXSpeed = new Random().nextInt(2) == 0 ? 1: -1;
        //ballYSpeed = new Random().nextInt(2) == 0 ? 1: -1;
    }

    private void run(GraphicsContext gc) {
        prepareGraphicsContext(gc);

        if(gameStarted) {
            ball

            TickComputersAI(ballXPos, ballYPos);
            gc.fillOval(ballXPos, ballYPos, BALL_R, BALL_R);
        } else {
            showStartingScreen(gc);
        }


        if(isBallOutOfTheWindow()) ballYSpeed *=-1;

        if(hasPlayer2Won()) {
            increaseScoreOf(2);
            gameStarted = false;
        }
        if(hasPlayer1Won()) {
            increaseScoreOf(1);
            gameStarted = false;
        }
        if( ((ballXPos + BALL_R > playerTwoXPos) && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + PLAYER_HEIGHT) ||
                ((ballXPos < playerOneXPos + PLAYER_WIDTH) && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + PLAYER_HEIGHT)) {
            ballYSpeed += 1 * Math.signum(ballYSpeed);
            ballXSpeed += 1 * Math.signum(ballXSpeed);
            ballXSpeed *= -1;
            ballYSpeed *= -1;
        }

        fillPlayer(gc, playerOneXPos, playerOneYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
        fillPlayer(gc, playerTwoXPos, playerTwoYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
        displayScore(gc);


    }



    private void displayScore(GraphicsContext gc) {
        gc.fillText(scoreP1 + "\t\t\t\t\t\t\t\t" + scoreP2, width / 2, 100);
    }

    private void fillPlayer(GraphicsContext gc, double playerXPos, double playerYPos, int playerWidth, int playerHeight) {
        gc.fillRect(playerXPos, playerYPos, playerWidth, playerHeight);
    }

    private boolean hasPlayer1Won() {
        return (ballXPos > playerTwoXPos + PLAYER_WIDTH);
    }

    private boolean hasPlayer2Won() {
        return (ballXPos < playerOneXPos - PLAYER_WIDTH);
    }

    private void increaseScoreOf(int numOfPlayer)
    {
        if(numOfPlayer == 1)
        {
            scoreP1++;
        }
        else
        {
            scoreP2++;
        }
    }

    private boolean isBallOutOfTheWindow() {
        return (ballYPos + BALL_R > height || ballYPos < 0);
    }

    private void prepareGraphicsContext(GraphicsContext gc)
    {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));
    }

    private void TickComputersAI(double ballXPos, double ballYPos)
    {
        if(ballXPos < width - width  / 4) {
            playerTwoYPos = ballYPos - PLAYER_HEIGHT / 2;
        }  else {
            playerTwoYPos =  ballYPos > playerTwoYPos + PLAYER_HEIGHT / 2 ?playerTwoYPos += 1: playerTwoYPos - 1;
        }
    }










}