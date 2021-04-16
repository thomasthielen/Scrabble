package screens;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * 
 * @author jbleil
 *
 */

public class StartScreen extends Application {
	
	private static Stage stage = new Stage();
	
	/**
	 * 
	 * @author jbleil
	 * @param args
	 */
	
	public static void main(String[] args) {
		launch();
	}
	
	/**
	 * 
	 * @author jbleil
	 */
	
	public void start(Stage s) throws Exception{
		stage.setScene(new Scene(FXMLLoader.load(new File("StartScreen.fxml").toURI().toURL())));
		stage.show();
	}
	/**
	 * 
	 * @author jbleil
	 * @return
	 */
	
	public static Stage getStage() {
		return stage;
	}
}
