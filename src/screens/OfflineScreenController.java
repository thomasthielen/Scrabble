package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OfflineScreenController {
	
	private Stage stage = new Stage();
	
	@FXML
    void playGame(ActionEvent event) throws Exception {
    	stage.setScene(new Scene(FXMLLoader.load(new File("LogInScreen.fxml").toURI().toURL())));
    	stage.show();
    }

    @FXML
    void traningMode(ActionEvent event) throws Exception {
    	stage.setScene(new Scene(FXMLLoader.load(new File("LogInScreen.fxml").toURI().toURL())));
    	stage.show();
    }
}
