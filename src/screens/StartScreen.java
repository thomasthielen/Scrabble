package screens;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartScreen extends Application {
	
	private static Stage stage = new Stage();
	
	public static void main(String[] args) {
		launch();
	}
	
	public void start(Stage s) throws Exception{
		stage.setScene(new Scene(FXMLLoader.load(new File("ProfileSelectionScreen.fxml").toURI().toURL())));
		stage.show();
	}
	
	public static Stage getStage() {
		return stage;
	}
}
