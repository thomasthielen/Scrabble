package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * 
 * @author jbleil
 *
 */

public class OfflineScreenController {
	
	/**
	 * 
	 * @author jbleil
	 * @param event
	 * @throws Exception
	 */
	
	@FXML
    void playGame(ActionEvent event) throws Exception {
		StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("LobbyScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }
	
	/**
	 * 
	 * @author jbleil
	 * @param event
	 * @throws Exception
	 */

    @FXML
    void traningMode(ActionEvent event) throws Exception {
    	StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("LobbyScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }
    
    /**
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */
    
    @FXML
    void back(ActionEvent event) throws Exception{
    	StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("ProfileSelectionScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }
}
