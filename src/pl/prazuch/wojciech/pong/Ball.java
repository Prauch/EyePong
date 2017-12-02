package pl.prazuch.wojciech.pong;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by wojciechprazuch on 14.10.2017.
 */
public class Ball implements Movable{

    private int radius;

    private double xPos;
    private double yPos;

    private double xSpeed;
    private double ySpeed;

    private double dy;
    private double dx;

    private double accelerationx;
    private double accelerationy;

    private double MAXBOUNCEVALUE = 5*Math.PI/12;


    private double dt = Constants.dtInSeconds;




    Ball(int radius, int xPos, int yPos, double xSpeed, double ySpeed){

        this.radius = radius;
        this.xPos = xPos;
        this.yPos = yPos;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;

        this.accelerationx = this.xSpeed/dt/3600;
        this.accelerationy = this.ySpeed/dt/3600;



    }


    /*

    accelerate: function(x, y, dx, dy, accel, dt) {
  var x2  = x + (dt * dx) + (accel * dt * dt * 0.5);
  var y2  = y + (dt * dy) + (accel * dt * dt * 0.5);
  var dx2 = dx + (accel * dt) * (dx > 0 ? 1 : -1);
  var dy2 = dy + (accel * dt) * (dy > 0 ? 1 : -1);
  return { nx: (x2-x), ny: (y2-y), x: x2, y: y2, dx: dx2, dy: dy2 };
},

     */

    private void accelerate(){

        double x2 = xPos + (dt * xSpeed) + (accelerationx * dt * dt * 0.5);
        double y2 = yPos + (dt * ySpeed) + (accelerationy * dt * dt * 0.5);

        this.xSpeed = xSpeed + (accelerationx * dt);
        this.ySpeed = ySpeed + (accelerationx * dt);




    }


    public void calculateNewSpeedAfterIntersection(double normalizedIntersectionY)
    {

        double speed = Math.sqrt(xSpeed*xSpeed + ySpeed*ySpeed);

        double bounceAngle = normalizedIntersectionY * MAXBOUNCEVALUE;

        xSpeed = speed * Math.cos(bounceAngle);
        ySpeed = -speed * Math.sin(bounceAngle);

    }




    public void Move(){

        accelerate();
        //System.out.
        xPos += xSpeed;
        yPos += ySpeed;

    }


    public void Draw(GraphicsContext gc){

        gc.fillOval(xPos, yPos, radius, radius);
    }

    @Override
    public void Update() {

    }


    public double getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public double getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public double getySpeed() {
        return ySpeed;
    }

    public void setySpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public double getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
