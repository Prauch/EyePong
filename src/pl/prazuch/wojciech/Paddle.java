package pl.prazuch.wojciech;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by wojciechprazuch on 14.10.2017.
 */
public class Paddle implements Movable{


    private int width;
    private int height;

    private double xPos;
    private double yPos;


    Paddle(int height, int width, int xPos, int yPos){

        this.height = height;
        this.width = width;

        this.xPos = xPos;
        this.yPos = yPos;


    }

    public void Move(){

    }

    public void Draw(GraphicsContext gc){

        gc.fillRect(xPos, yPos, width, height);
    }

    @Override
    public void Update() {

    }


    public int getWidth() {
        return width;
    }


    public double getNormalizedRelativeIntersectionY(double ballYPosWhenIntersected){

        double relativeIntersectionY = (this.yPos + height/2) - ballYPosWhenIntersected;


        double normalizedIntersectionY = (relativeIntersectionY/(height/2));

        return normalizedIntersectionY;

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

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }
}
