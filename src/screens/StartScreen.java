package screens;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class runs the initial start of the UI and sets the stage for all
 * thefollowing scenes
 *
 * @author jbleil
 */
public class StartScreen extends Application {

	private static Stage stage = new Stage();

	/**
	 * the main method launches the the Application
	 *
	 * @author jbleil
	 * @param args
	 */
	public static void main(String[] args) {
		launch();
	}

	/**
	 * this method sets the scene for the Start screen
	 *
	 * @author jbleil
	 */
	public void start(Stage s) throws Exception {
		stage.setScene(new Scene(FXMLLoader.load(new File("StartScreen.fxml").toURI().toURL())));
		stage.show();
	}

	/**
	 * this method returns the Stage of the Application
	 *
	 * @author jbleil
	 * @return
	 */
	public static Stage getStage() {
		return stage;
	}
}
