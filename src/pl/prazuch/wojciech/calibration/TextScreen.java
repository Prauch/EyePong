package pl.prazuch.wojciech.calibration;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.apache.log4j.Logger;

import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.IGazeListener;
import com.theeyetribe.client.ITrackerStateListener;
import com.theeyetribe.client.data.GazeData;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
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
import pl.prazuch.wojciech.calibration.TetStart;

public class TextScreen implements IGazeListener, ITrackerStateListener{
	Logger log = Logger.getLogger(TextScreen.class);
	TetStart mainApp;

	Point2D leftEyePosition = new Point2D(50,100);
	Point2D rightEyePosition = new Point2D(150,100);
	Point2D gazePoint = new Point2D(150,100);

	StackPane lstack = new StackPane();
	StackPane rstack = new StackPane();
	StackPane gstack = new StackPane();

	int screenWidth ;
	int screenHeight;


	boolean connected = false;
	Text text = new Text();
	Runnable action;
	public void start(String txt,Scene scene,TetStart main,Runnable paction) {

		this.mainApp = main;
		this.action = paction;
		GazeManager.getInstance().addGazeListener(this);
		connected = GazeManager.getInstance().isConnected();
		if(!connected) {
			txt = txt+"\nPod��cz urz�dzenie!";
		}

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] screens = ge.getScreenDevices();
		java.awt.Rectangle screenRect = screens[0].getDefaultConfiguration().getBounds();
		screenWidth = screenRect.width;
		screenHeight = screenRect.height;

//		GridPane g = new GridPane();
		
		Group root = new Group();
		//root.setStyle("-fx-background-color: #000000;");
//		g.add(root,0,0);
//		scene.setRoot(g);

		scene.setRoot(root);
		

		GridPane grid = new GridPane();
		grid.setPrefSize(screenWidth,screenHeight);
		grid.setMaxSize(screenWidth,screenHeight);
		System.out.println("PRefsize = "+screenWidth+" "+screenHeight);
		root.getChildren().add(grid);

//		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//		log.debug("Screen size: "+dim);
//		//System.out.println(text.getWrappingWidth()+","+grid.getHeight());
//		//		grid.setLayoutX(dim.width/2-grid.getWidth()/2);
//		//		grid.setLayoutY(dim.height/2-grid.getHeight()/2);
//		grid.setPrefSize(dim.width, dim.height);

		
//		g.add(grid,0,0);
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

		final Button btn = new Button("OK");
		btn.setFont(new Font(30));
		HBox hbBtn = new HBox(20);
		hbBtn.setAlignment(Pos.CENTER);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 0, 1);


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

		//if(Params.set("showeyes"))
		root.getChildren().addAll(lstack,rstack);
		root.getChildren().add(gstack);
		btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				log.trace("OK button pressed");
				remove();

				action.run();
				//mainApp.calibration();
				// mainApp.slide();
			}
		});
	}

	private void remove() {
		GazeManager.getInstance().removeGazeListener(this);
	}

	@Override
	public void onGazeUpdate(GazeData gazeData) {
		com.theeyetribe.client.data.Point2D leftPupil = gazeData.leftEye.pupilCenterCoordinates;
		com.theeyetribe.client.data.Point2D rightPupil = gazeData.rightEye.pupilCenterCoordinates;
		com.theeyetribe.client.data.Point2D smoothedCoords = gazeData.smoothedCoordinates;

		//		screen.setEyePos(leftPupil, rightPupil);
		if(isZero(leftPupil)) leftPupil = new com.theeyetribe.client.data.Point2D(-100,-100);
		if(isZero(rightPupil)) rightPupil = new com.theeyetribe.client.data.Point2D(-100,-100);
		if(isZero(smoothedCoords)) smoothedCoords = new com.theeyetribe.client.data.Point2D(-100,-100);
		
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

	@Override //nie dzia�a!
	public void onTrackerStateChanged(int trackerState) {
		log.debug("onTrackerStateChanged = "+trackerState);
		if(trackerState==0) {
			text.setText("OK");
		}
		else
			text.setText("XXXXX");

	}

	@Override
	public void OnScreenStatesChanged(int screenIndex,
			int screenResolutionWidth, int screenResolutionHeight,
			float screenPhysicalWidth, float screenPhysicalHeight) {
		// TODO Auto-generated method stub
		log.debug("OnScreenStatesChanged invoked!");
	}


}
