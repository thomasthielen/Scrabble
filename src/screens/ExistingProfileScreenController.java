package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class ExistingProfileScreenController {
	
    @FXML
    void startGame(ActionEvent event) throws Exception{
    	StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("OnlineOrOfflineScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }
    

    @FXML
    void back(ActionEvent event) throws Exception{
    	StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("ProfileSelectionScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }
}
