package pl.prazuch.wojciech.calibration;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.data.CalibrationResult;
import com.theeyetribe.client.data.CalibrationResult.CalibrationPoint;
import com.theeyetribe.client.data.GazeData;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class CalResultsScreen implements IGazeListener {


	TetStart mainApp;


	Point2D leftEyePosition = new Point2D(50,100);
	Point2D rightEyePosition = new Point2D(150,100);
	Point2D gazePoint = new Point2D(150,100);

	StackPane lstack = new StackPane();
	StackPane rstack = new StackPane();
	StackPane gstack = new StackPane();

	int screenWidth ;
	int screenHeight;


	CalibrationResult calibResult;

	double error = 0;

	public void start(Scene scene,TetStart main,CalibrationResult cr) {
		mainApp = main;
		calibResult = cr;
		scene.setCursor(Cursor.DEFAULT);

		GazeManager.getInstance().addGazeListener(this);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = dim.width;
		screenHeight = dim.height;

		Group root = new Group();

		scene.setRoot(root);
		//stage.setScene(scene);
		//calibResult = GazeManager.getInstance().getLastCalibrationResult();

		//		
		//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		//		GraphicsDevice[] screens = ge.getScreenDevices();
		//		java.awt.Rectangle screenRect = screens[0].getDefaultConfiguration().getBounds();
		//		int screenWidth = screenRect.width;
		//		int screenHeight = screenRect.height;

		for(CalibrationPoint cp:calibResult.calibpoints) {
			//			System.out.println("in");
			System.out.println(cp.coordinates.x+" "+cp.coordinates.y+": "+
					cp.meanEstimatedCoords.x+" "+cp.meanEstimatedCoords.y+" > "+cp.standardDeviation.averageStandardDeviationPixels);

			final StackPane resultStack = new StackPane();
			double width = cp.standardDeviation.averageStandardDeviationPixels;
			final Circle circle = new Circle(width,  Color.rgb(156,216,255));
			resultStack.getChildren().addAll(circle);
			resultStack.setLayoutX(cp.meanEstimatedCoords.x-width/2);
			resultStack.setLayoutY(cp.meanEstimatedCoords.y-width/2);

			final StackPane pointStack = new StackPane();
			final Circle circleP = new Circle(3,  Color.rgb(10,10,10));
			pointStack.getChildren().addAll(circleP);
			pointStack.setLayoutX(cp.coordinates.x-1);
			pointStack.setLayoutY(cp.coordinates.y-1);

			root.getChildren().addAll(resultStack,pointStack);

			//			drawPoint(g, Color.RED, cp.meanEstimatedCoords, cp.standardDeviation.averageStandardDeviationPixels);
			//			drawPoint(g, Color.BLACK, cp.coordinates);
			//			//			drawPoint(g, Color.BLACK, cp.meanEstimatedCoords);
			//			g.setFont(g.getFont().deriveFont(12f));
			//			g.drawString( new DecimalFormat("#.#").format(cp.accuracy.accuracyDegrees), (int)cp.coordinates.x, (int)cp.coordinates.y);

		}

		Text text = new Text();
		//text.setFont(new Font(20));
		//text.setWrappingWidth(200);
		text.setTextAlignment(TextAlignment.CENTER);
		text.setLayoutX(50);
		text.setLayoutY(20);

		error = calibResult.averageErrorDegree;
		if(!calibResult.result) error=1000;


		String txt = "";

		if(error > 1.5) {
			txt = "You need to repeat the calibration ("+error+")";
		}
		else if(error > 1) {
			txt = "Calibration not very good ("+error+")";
		}
		else {
			txt = "Calibration successful ("+error+")";
		}


		GridPane grid = new GridPane();
		//		grid.setStyle("-fx-background-color: #444444; -fx-content-display: top;");

		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		//grid.setLayoutX(200);
		//grid.setLayoutY(200);
		text = new Text();
		text.setFont(new Font(30));
		//text.setWrappingWidth(200);
		text.setTextAlignment(TextAlignment.CENTER);
		text.setText(txt);
		grid.add(text,0,0);

		final Button btn1 = new Button("Repeat");
		btn1.setFont(new Font(30));
		HBox hbBtn1 = new HBox(20);
		hbBtn1.setAlignment(Pos.CENTER);
		hbBtn1.getChildren().add(btn1);
		btn1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {	mainApp.calibration();}
		});

		final Button btn2 = new Button("Continue");
		btn2.setFont(new Font(30));
		HBox hbBtn2 = new HBox(20);
		hbBtn2.setAlignment(Pos.CENTER);
		hbBtn2.getChildren().add(btn2);
		btn2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {	mainApp.start();}
		});



		grid.add(hbBtn1, 0, 1);
		if(error<=1.5)
			grid.add(hbBtn2, 0, 2);

		root.getChildren().add(grid);
		grid.setPrefSize(dim.width, dim.height);



		final Circle lcircle = new Circle(12,  Color.rgb(156,216,255));
		lstack.getChildren().addAll(lcircle);
		lstack.setLayoutX(leftEyePosition.getX());
		lstack.setLayoutY(leftEyePosition.getY());

		final Circle rcircle = new Circle(12,  Color.rgb(156,216,255));
		rstack.getChildren().addAll(rcircle);
		rstack.setLayoutX(rightEyePosition.getX());
		rstack.setLayoutY(rightEyePosition.getY());

		final Circle gcircle = new Circle(50,  Color.rgb(0,180,0));
		gstack.setOpacity(0.2);
		gstack.getChildren().addAll(gcircle);
		gstack.setLayoutX(rightEyePosition.getX());
		gstack.setLayoutY(rightEyePosition.getY());

		//if(Params.isSet("showeyes"))
		root.getChildren().addAll(lstack,rstack);
		//if(Params.isSet("showgaze"))
		root.getChildren().add(gstack);

	}



	public void onGazeUpdate(GazeData gazeData) {

		// TODO Auto-generated method stub
		com.theeyetribe.client.data.Point2D leftPupil = gazeData.leftEye.pupilCenterCoordinates;
		com.theeyetribe.client.data.Point2D rightPupil = gazeData.rightEye.pupilCenterCoordinates;
		com.theeyetribe.client.data.Point2D smoothedCoords = gazeData.smoothedCoordinates;
		//	if(smoothedCoords.x==0 || smoothedCoords.y==0) return;


		if(isZero(leftPupil)) leftPupil = new com.theeyetribe.client.data.Point2D(-100,-100);
		if(isZero(rightPupil)) rightPupil = new com.theeyetribe.client.data.Point2D(-100,-100);
		if(isZero(smoothedCoords)) smoothedCoords = new com.theeyetribe.client.data.Point2D(-100,-100);

		//		screen.setEyePos(leftPupil, rightPupil);
		//		screen.eyesVisible = !(isZero(leftPupil) && isZero(rightPupil));
		leftEyePosition = new Point2D(leftPupil.x * screenWidth, leftPupil.y * screenHeight);
		rightEyePosition = new Point2D(rightPupil.x * screenWidth, rightPupil.y * screenHeight);
		gazePoint = new Point2D(smoothedCoords.x, smoothedCoords.y);
		//System.out.println(gazePoint.getX()+" "+gazePoint.getY());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lstack.setLayoutX(leftEyePosition.getX());
				lstack.setLayoutY(leftEyePosition.getY());
				//      		lstack.relocate(leftEyePosition.getX(), leftEyePosition.getY());
				rstack.setLayoutX(rightEyePosition.getX());
				rstack.setLayoutY(rightEyePosition.getY());
				//        		System.out.println("updated to "+rightEyePosition.getX()+"-"+rightEyePosition.getY());
				gstack.setLayoutX(gazePoint.getX()-25);
				gstack.setLayoutY(gazePoint.getY()-25);

			}
		});


		//		System.out.println(leftPupil);
		//		System.out.println(rightPupil);

	}

	public static boolean isZero(com.theeyetribe.client.data.Point2D p) { 
		return (p.x==0 || p.y==0);
	}
}
