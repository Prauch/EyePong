package pl.prazuch.wojciech.communication;

/**
 * Created by wojciechprazuch on 30.11.2017.
 */




public class DataToClient {

    private int ballX;
    private int ballY;

    private int gameScorePlayer1;
    private int gameScorePlayer2;

    private int gameActive;

    private int opponentX;
    private int opponentY;







    public DataToClient()
    {



    }


    public void setDataToClient(String output)
    {
        String[] data = output.split("\\s+");

        ballX = Integer.parseInt(data[0]);
        ballY = Integer.parseInt(data[1]);

        gameScorePlayer1 = Integer.parseInt(data[2]);
        gameScorePlayer2 = Integer.parseInt(data[3]);

        gameActive = Integer.parseInt(data[4]);

        opponentX = Integer.parseInt(data[5]);
        opponentY = Integer.parseInt(data[6]);

    }


    public String getDataToClient()
    {
        StringBuilder sb = new StringBuilder("");

        sb.append(ballX);
        sb.append(" ");

        sb.append(ballY);
        sb.append(" ");

        sb.append(gameScorePlayer1);
        sb.append(" ");

        sb.append(gameScorePlayer2);
        sb.append(" ");

        sb.append(gameActive);
        sb.append(" ");

        sb.append(opponentX);
        sb.append(" ");

        sb.append(opponentY);


        return sb.toString();

    }


    public int getBallX() {
        return ballX;
    }

    public void setBallX(int ballX) {
        this.ballX = ballX;
    }

    public int getBallY() {
        return ballY;
    }

    public void setBallY(int ballY) {
        this.ballY = ballY;
    }

    public int getGameScorePlayer1() {
        return gameScorePlayer1;
    }

    public void setGameScorePlayer1(int gameScorePlayer1) {
        this.gameScorePlayer1 = gameScorePlayer1;
    }

    public int getGameScorePlayer2() {
        return gameScorePlayer2;
    }

    public void setGameScorePlayer2(int gameScorePlayer2) {
        this.gameScorePlayer2 = gameScorePlayer2;
    }

    public int getGameActive() {
        return gameActive;
    }

    public void setGameActive(int gameActive) {
        this.gameActive = gameActive;
    }

    public int getOpponentX() {
        return opponentX;
    }

    public void setOpponentX(int opponentX) {
        this.opponentX = opponentX;
    }

    public int getOpponentY() {
        return opponentY;
    }

    public void setOpponentY(int opponentY) {
        this.opponentY = opponentY;
    }
}
