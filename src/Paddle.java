/**
 * Created by wojciechprazuch on 14.10.2017.
 */
public class Paddle implements Movable{


    private int width;
    private int height;

    private int xPos;
    private int yPos;


    Paddle(int height, int width, int xPos, int yPos){

        this.height = height;
        this.width = width;

        this.xPos = xPos;
        this.yPos = yPos;


    }

    public void Move(){

    }

    public void Paint(){

    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}
