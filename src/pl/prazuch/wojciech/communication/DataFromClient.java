package pl.prazuch.wojciech.communication;

/**
 * Created by wojciechprazuch on 30.11.2017.
 */
public class DataFromClient {



    private int playerX;
    private int playerY;

    private int playerHeight;
    private int playerWidth;

    private int gameStarted;







    public DataFromClient()
    {




    }

    public void setDataFromClient(String output){
        String[] data = output.split("\\s+");

        playerX = Integer.parseInt(data[0]);
        playerY = Integer.parseInt(data[1]);

        playerHeight = Integer.parseInt(data[2]);
        playerWidth = Integer.parseInt(data[3]);

        gameStarted = Integer.parseInt(data[4]);

    }

    public String getDataFromClient(){

        StringBuilder sb = new StringBuilder("");

        sb.append(playerX);
        sb.append(" ");

        sb.append(playerY);
        sb.append(" ");

        sb.append(playerHeight);
        sb.append(" ");

        sb.append(playerWidth);
        sb.append(" ");

        sb.append(gameStarted);



        return sb.toString();


    }


    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public int getPlayerHeight() {
        return playerHeight;
    }

    public void setPlayerHeight(int playerHeight) {
        this.playerHeight = playerHeight;
    }

    public int getPlayerWidth() {
        return playerWidth;
    }

    public void setPlayerWidth(int playerWidth) {
        this.playerWidth = playerWidth;
    }

    public int getGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(int gameStarted) {
        this.gameStarted = gameStarted;
    }
}
