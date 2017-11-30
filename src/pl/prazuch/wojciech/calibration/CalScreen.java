package pl.prazuch.wojciech.calibration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.ICalibrationProcessHandler;
import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.data.CalibrationResult;
import com.theeyetribe.client.data.CalibrationResult.CalibrationPoint;
import com.theeyetribe.client.data.GazeData;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CalScreen implements ICalibrationProcessHandler,IGazeListener{

	//main timeline
	private Timeline timeline;
	private AnimationTimer timer;

	//variable for storing actual frame
	private Integer i=0;

	//Results results;
	Group root;
	TetStart mainApp;

	boolean eyesVisible;

	int cnt = 0;
	public void start(Scene scene,TetStart main) {
		scene.setCursor(Cursor.NONE);
		
		GazeManager.getInstance().addGazeListener(this);

		this.mainApp = main;
		root = new Group();

		scene.setRoot(root);


		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = ge.getScreenDevices();
		java.awt.Rectangle screenRect = screens[0].getDefaultConfiguration().getBounds();
		int screenWidth = screenRect.width;
		int screenHeight = screenRect.height;

		final List<Point2D> pointPositions = new ArrayList<Point2D>();
		pointPositions.add(new Point2D(0.5*screenWidth,0.5*screenHeight));
		
		pointPositions.add(new Point2D(0.2*screenWidth,0.2*screenHeight));
		pointPositions.add(new Point2D(0.8*screenWidth,0.5*screenHeight));
		pointPositions.add(new Point2D(0.2*screenWidth,0.8*screenHeight));
		
		pointPositions.add(new Point2D(0.8*screenWidth,0.2*screenHeight));
		pointPositions.add(new Point2D(0.8*screenWidth,0.8*screenHeight));
		pointPositions.add(new Point2D(0.5*screenWidth,0.8*screenHeight));
		pointPositions.add(new Point2D(0.2*screenWidth,0.5*screenHeight));
		
		pointPositions.add(new Point2D(0.5*screenWidth,0.2*screenHeight));
		pointPositions.add(new Point2D(0.5*screenWidth,0.5*screenHeight));
		//pointPositions.add(new Point2D(0.5*screenWidth,0.5*screenHeight));

		GazeManager.getInstance().calibrationStart(9, this);
		System.out.println("Started...");


		//create a circle with effect
		final Circle circle = new Circle(12,  Color.rgb(156,216,255));
		circle.setEffect(new Lighting());
		final Circle circleB = new Circle(3,  Color.rgb(0,0,0));
		final Circle circleW = new Circle(1,  Color.rgb(250,250,250));

		//create a text inside a circle
		final Text text = new Text (i.toString());
		text.setStroke(Color.BLACK);
		text.setFont(new Font(7));
		//create a layout for circle with text inside
		final StackPane stack = new StackPane();
		stack.getChildren().addAll(circle, circleB, circleW, text);
		stack.setLayoutX(pointPositions.get(cnt).getX());
		stack.setLayoutY(pointPositions.get(cnt).getY());

		root.getChildren().add(stack);


		//create a timeline for moving the circle
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(true);

		timer = new AnimationTimer() {
			@Override
			public void handle(long l) {
				text.setText(i.toString());
				if(eyesVisible)
					text.setStroke(Color.GREEN);
				else
					text.setStroke(Color.RED);
				i++;
				
				if(i==40) { //oko�o 60Hz
					GazeManager.getInstance().calibrationPointStart(
							(int)pointPositions.get(cnt).getX(),
							(int)pointPositions.get(cnt).getY()
							);
				}
			}

		};

		KeyValue keyValueX = new KeyValue(stack.scaleXProperty(), 2);
		KeyValue keyValueY = new KeyValue(stack.scaleYProperty(), 2);

		Duration duration = Duration.millis(1500);
		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				if(cnt>=0) {
					System.out.println("end "+cnt);
					GazeManager.getInstance().calibrationPointEnd();
				}
				cnt++;
				if(cnt<pointPositions.size()) {
					System.out.println("punkt "+cnt);
					stack.setLayoutX(pointPositions.get(cnt).getX());
					stack.setLayoutY(pointPositions.get(cnt).getY());
					//reset counter
					i = 0;
				}
				else {
					System.out.println("STOP!!!");
				}
			}
		};

		KeyFrame keyFrame = new KeyFrame(duration, onFinished , keyValueX, keyValueY);

		//add the keyframe to the timeline
		timeline.getKeyFrames().add(keyFrame);

		timeline.play();
		timer.start();
	}


	CalibrationResult cresult;
	@Override
	public void onCalibrationResult(CalibrationResult calibResult) {
		System.out.println("onCalibrationResult: "+calibResult.averageErrorDegree);
		cresult = calibResult;
		for(CalibrationPoint cp:calibResult.calibpoints) {
			System.out.println(cp.coordinates.x+" "+cp.coordinates.y+": "+
					cp.meanEstimatedCoords.x+" "+cp.meanEstimatedCoords.y+" > "+cp.standardDeviation.averageStandardDeviationPixels);
		}
		GazeManager.getInstance().removeGazeListener(this);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				timeline.stop();
				mainApp.calibrated(cresult);
			}
		});

	}

	@Override
	public void onGazeUpdate(GazeData gazeData) {
		com.theeyetribe.client.data.Point2D leftPupil = gazeData.leftEye.pupilCenterCoordinates;
		com.theeyetribe.client.data.Point2D rightPupil = gazeData.rightEye.pupilCenterCoordinates;
		eyesVisible = !(isZero(leftPupil) && isZero(rightPupil)); 
	}



	private boolean isZero(com.theeyetribe.client.data.Point2D p) {
		if(p.x==0 && p.y==0) 
			return true;
		return false;
	}

	@Override
	public void onCalibrationStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCalibrationProgress(double progress) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCalibrationProcessing() {
		// TODO Auto-generated method stub
		
	}

} 