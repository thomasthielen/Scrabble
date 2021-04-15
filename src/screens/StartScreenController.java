package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class StartScreenController {
	
	@FXML
	 void createNewProfile(ActionEvent event) throws Exception{
		StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("NewProfileScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }

    @FXML
    void existingProfile(ActionEvent event) throws Exception {
    	StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("ExistingProfileScreen.fxml").toURI().toURL())));
    	StartScreen.getStage().show();
    }
}
