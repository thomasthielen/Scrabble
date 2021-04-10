package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OnlineScreenController {
	
	   @FXML
	    void hostNewGame(ActionEvent event) throws Exception {
		   Stage stage = new Stage();
		   stage.setScene(new Scene(FXMLLoader.load(new File("LogInScreen.fxml").toURI().toURL())));
		   stage.show();
	    }

	    @FXML
	    void joinExistingGame(ActionEvent event) throws Exception {
	    	Stage stage = new Stage();
	    	stage.setScene(new Scene(FXMLLoader.load(new File("LogInScreen.fxml").toURI().toURL())));
	    	stage.show();
	    }
}
