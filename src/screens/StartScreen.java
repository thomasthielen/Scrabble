package screens;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartScreen extends Application {
	
	public static void main(String[] args) {
		launch();
	}
	
	public void start(Stage s) throws Exception{
		s.setScene(new Scene(FXMLLoader.load(new File("StartScreen.fxml").toURI().toURL())));
		s.show();
	}
}
