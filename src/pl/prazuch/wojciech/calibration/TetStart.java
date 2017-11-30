package pl.prazuch.wojciech.calibration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.data.CalibrationResult;
import com.theeyetribe.client.data.Point2D;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pl.prazuch.wojciech.Pong;
//import pl.kasprowski.tet.screens.CalResultsScreen;
//import pl.kasprowski.tet.screens.CalScreen;
//import pl.kasprowski.tet.screens.TextScreen;


/**
 * Kolejno��:
 * eyetrackersetup() - ekran powitalny
 * precalibration() - ekran informacyjny dla kalibracji
 * calibration() - odpalenie kalibracji
 * start() - tu mo�na wrzyci� w�asny kod
 * done() - koniec i zapisanie wynik�w  
 * @author Pawe�
 *
 */
public class TetStart extends Application {
	static Logger log = Logger.getLogger(TetStart.class);

	boolean calibrate = true;
	
	Stage stage;
	Scene scene;
	TetStart me;
	
	@Override
	public void start(Stage stage) throws Exception {
		try {
			log.info("-------------------------------------------------------------------------------");
			log.info("Application start");


			me = this;
			boolean works = GazeManager.getInstance().activate(GazeManager.ApiVersion.VERSION_1_0,
					GazeManager.ClientMode.PUSH);
			log.debug("works:" + works + " isConnected:" + GazeManager.getInstance().isConnected());

			works = (works && GazeManager.getInstance().isConnected());

			if (!works) {
				throw new RuntimeException(
						"Cannot connect to the Eyetribe. Plug the machine and retry!");
			}

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							log.info("Application closed by Alt-F4");
							System.exit(0);
						}
					});
				}
			});
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					log.info("Deactivating GazeManager");
					GazeManager.getInstance().deactivate();
				}
			});

			this.stage = stage;
			scene = new Scene(new Group());
			stage.setScene(scene);


			stage.setFullScreen(true);
			stage.show();

			eyetrackersetup();

		} catch (Exception ex) {
			ex.printStackTrace();
			log.fatal(ex);
			System.exit(-1);
		}

	}

	public void setTitle(String txt) {
		stage.setTitle(txt);
	}

	public void runAction(String action) {
		log.debug("Running action "+action);
	}
		
	
	public void eyetrackersetup() {
		TextScreen tf = new TextScreen();
		tf.start("Position the eyetracker in such a way, \nthat eyes are placed somewhere in the middle of the screen", scene,
				this, new Runnable() {
					public void run() {
//						if (Params.isSet("calibrate"))
							me.precalibration();
//						else
//							me.runAction("action");
					};
				});

	}

	public void precalibration() {
		TextScreen tf = new TextScreen();
		tf.start("You will see a moving point on the screen\nFollow it with your gaze!", scene,
				this, new Runnable() {
					public void run() {
						me.calibration();
					};
				});

	}

	public void calibration() {
		log.debug("Starting calibration");
		CalScreen calScreen = new CalScreen();
		calScreen.start(scene, this);

	}

	public void calibrated(CalibrationResult cresult) {
		log.debug("Calibrated with " + cresult.averageErrorDegree + " error");
		CalResultsScreen c = new CalResultsScreen();
		c.start(scene, this, cresult);
	}

	public void start() {
		TextScreen tf = new TextScreen();
		tf.start("To miejsce na wstawienie swojego kodu!", scene,
				this, new Runnable() {
					public void run() {
						me.done();
					};
				});


		Pong pong = new Pong();
		try {
			pong.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	
	public void done() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		String fileName = "et$user$stim$" + sdf.format(new Date());
		String gdir = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/gdt";
		new File(gdir).mkdirs();

		log.info("Saving results in " + gdir + "/" + fileName + ".gdt");
		// results.save(fileName+".gaz"); // format tekstowy

		TextScreen tf = new TextScreen();
		tf.start("Thank you!", scene, this, new Runnable() {
			public void run() {
				System.exit(0);
			}
		});
		log.info("Application end");
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	
}
