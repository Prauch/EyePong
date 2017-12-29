package pl.prazuch.wojciech.pong; /**
 * Created by wojciechprazuch on 10.10.2017.
 */
import java.awt.*;
import java.util.ArrayList;

import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.data.GazeData;
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
import pl.prazuch.wojciech.calibration.TetStart;
import pl.prazuch.wojciech.communication.ClientSocket;
import pl.prazuch.wojciech.communication.DataFromClient;
import pl.prazuch.wojciech.communication.DataToClient;

public class Pong extends Application implements IGazeListener {


    private ArrayList<Movable> gameObjects = new ArrayList<>();

    private int width;
    private int height;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private int gameStarted;

    private String hostname;
    private String port;


    Ball ball;
    Paddle player1;
    Paddle player2;

//    GazeManager gm;

    DataToClient dataToClient;
    DataFromClient dataFromClient;

    ClientSocket clientSocket;


    public Pong(String hostname, String port)
    {
        this.hostname = hostname;
        this.port = port;
    }

    public void start(Stage stage) throws Exception {

        width = Toolkit.getDefaultToolkit().getScreenSize().width;
        height = Toolkit.getDefaultToolkit().getScreenSize().height-50;


        dataFromClient = new DataFromClient();
        dataToClient = new DataToClient();
        clientSocket = new ClientSocket(hostname, port);

//        boolean success =  gm.
//        gm.addGazeListener(this);


        GazeManager.getInstance().addGazeListener(this);



        ball = new Ball(15, width/2, height/2, 2, 2);
        player1 = new Paddle(100, 15, 0, 0);
        player2 = new Paddle(100, 15, width - 15, 0);
        gameObjects.add(ball);
        gameObjects.add(player1);
        gameObjects.add(player2);

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(Constants.dtInMiliseconds), e -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);
        canvas.setOnMouseMoved(e ->  player1.setyPos(e.getY()) );
        canvas.setOnMouseClicked(e ->  gameStarted = 1);

        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();

        tl.play();
    }



    private void showStartingScreen(GraphicsContext gc) {

        gc.setStroke(Color.YELLOW);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.strokeText("Click to Start", width / 2, height / 2);


    }

    private void run(GraphicsContext gc) {
        prepareGraphicsContext(gc);

        dataToClient = clientSocket.receiveDataToClient();
        parseDataToClient(dataToClient);

        if(gameStarted>0) {

            DrawObjects(gc);
//            TickComputersAI(ball.getxPos(), ball.getyPos());
            
        } else {
            showStartingScreen(gc);
        }


//        checkGameWorldRulesAndCorrectIfNecessary();

        if(didPlayerMoveOutsideTheWindowBorder(player1))
        {
            player1.setyPos(height-player1.getHeight());
        }

        DrawObjects(gc);





        displayScore(gc);

        writeToDataFromClient();
        clientSocket.setDataFromClient(dataFromClient);


    }

    private void writeToDataFromClient() {

        dataFromClient.setPlayerX((int)player1.getxPos());
        dataFromClient.setPlayerY((int)player1.getyPos());
        dataFromClient.setPlayerHeight(player1.getHeight());
        dataFromClient.setPlayerWidth(player1.getWidth());
        dataFromClient.setGameStarted(1);


    }




    private boolean didPlayerMoveOutsideTheWindowBorder(Paddle player) {
        if(player.getyPos() + player.getHeight() > height)
            return true;
        else
            return false;
    }


    private void DrawObjects(GraphicsContext gc){
        for(int i = 0; i < gameObjects.size(); i++)
        {
            gameObjects.get(i).Draw(gc);
        }
    }

    private void MoveObjects(){


        for(int i = 0; i < gameObjects.size(); i++)
        {
            gameObjects.get(i).Move();
        }

    }

    private void parseDataToClient(DataToClient dataToClient)
    {
        this.player2.setxPos(dataToClient.getOpponentX());
        this.player2.setyPos(dataToClient.getOpponentY());

        this.ball.setxPos(dataToClient.getBallX());
        this.ball.setyPos(dataToClient.getBallY());

        this.scoreP1 = dataToClient.getGameScorePlayer1();
        this.scoreP2 = dataToClient.getGameScorePlayer2();

        this.gameStarted = dataToClient.getGameActive();

    }




//    private void centerBall(Ball ball){
//        ball.setyPos(height/2);
//        ball.setxPos(width/2);
//    }


//    private void checkGameWorldRulesAndCorrectIfNecessary(){
//
//
//        if(didBallHitHorizontalBorders())  ball.setySpeed(ball.getySpeed()*(-1));
//
//        if(hasPlayer2Won()) {
//            increaseScoreOfPlayer(2);
//            gameStarted = false;
//            resetBall(ball);
//        }
//        if(hasPlayer1Won()) {
//            increaseScoreOfPlayer(1);
//            gameStarted = false;
//            resetBall(ball);
//        }
//
//        if( didBallHitAnyOfThePaddles()) {
//
//
//            if(whichPlayerDidTheBallHit()==Player.PLAYER1)
//            {
//                ball.calculateNewSpeedAfterIntersection(player1.getNormalizedRelativeIntersectionY(ball.getyPos()));
//            }
//            else
//            {
//                ball.calculateNewSpeedAfterIntersection(player2.getNormalizedRelativeIntersectionY(ball.getyPos()));
//                ball.setxSpeed(ball.getxSpeed()*(-1));
//            }
//
//
//        }
//
////        if(didPlayerMoveOutsideTheWindowBorder(player1))
////        {
////            player1.setyPos(height-player1.getHeight());
////        }
////        if(didPlayerMoveOutsideTheWindowBorder(player2))
////        {
////            player2.setyPos(height-player2.getHeight());
////        }
//
//    }

//    private void resetBall(Ball ball) {
//        centerBall(ball);
//        ball.setySpeed(1);
//        ball.setxSpeed(1);
//
//    }

    private void displayScore(GraphicsContext gc) {
        gc.fillText(scoreP1 + "\t\t\t\t\t\t\t\t" + scoreP2, width / 2, 100);
    }


//    private boolean hasPlayer1Won() {
//        return (ball.getxPos() > player2.getxPos() + player2.getWidth());
//    }
//
//    private boolean hasPlayer2Won() {
//        return (ball.getxPos() < player1.getxPos() - player1.getWidth());
//    }

    private void increaseScoreOfPlayer(int numOfPlayer)
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

//    private boolean didBallHitHorizontalBorders() {
//        return (ball.getyPos() + ball.getRadius() > height || ball.getyPos() <  0);
//    }

    private void prepareGraphicsContext(GraphicsContext gc)
    {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));
    }


    @Override
    public void onGazeUpdate(GazeData gazeData) {

        if(doesPlayerLookAtHisHalfOfTheBoard(gazeData))
        player1.moveInADirectionOf(gazeData.rawCoordinates.y);
        else
            player1.setyPos(player1.getyPos());
    }



    private boolean doesPlayerLookAtHisHalfOfTheBoard(GazeData gazeData)
    {
        return Math.abs(gazeData.rawCoordinates.x-player1.getxPos()) < width / 2;
    }

//    private void TickComputersAI(double ballXPos, double ballYPos)
//    {
//        if(ballXPos < width - width  / 4) {
//            player2.setyPos(ballYPos - player2.getHeight()/2);
//        }  else {
//            player2.setyPos(ballYPos > player2.getyPos() + player2.getHeight() / 2 ?player2.getyPos() + 1: player2.getyPos() - 1);
//        }
//    }


//    private boolean didBallHitAnyOfThePaddles(){
//       if (((ball.getxPos() + ball.getRadius() > player2.getxPos()) && (ball.getyPos() >= player2.getyPos()) && (ball.getyPos() <= player2.getyPos() + player2.getHeight())) ||
//                ((ball.getxPos() - ball.getRadius() < player1.getxPos()) && (ball.getyPos() >= player1.getyPos()) && (ball.getyPos() <= player1.getyPos() + player1.getHeight())))
//        {
//            return true;
//        }
//        else
//            return false;
//
//    }


//    private Player whichPlayerDidTheBallHit(){
//
//        if ((ball.getxPos() + ball.getRadius() > player2.getxPos()) && (ball.getyPos() >= player2.getyPos()) && (ball.getyPos() <= player2.getyPos() + player2.getHeight()))
//            return Player.PLAYER2;
//        else
//            return Player.PLAYER1;
//
//
//    }










}
