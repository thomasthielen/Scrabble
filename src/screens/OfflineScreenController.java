package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * this class provides the controller for the Offline Screen
 * 
 * @author jbleil
 */

public class OfflineScreenController {
	
	/**
	 * This method serves as the Listener for "PLAY GAME"-Button
     * It redirects the user to the Lobby Screen
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
	 * This method serves as the Listener for "TRAINING MODE"-Button
     * It redirects the user to the Lobby Screen
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
     * This method serves as the Listener for "Back"-Button
     * It redirects the user to the Online or Offline Screen
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */
    
    @FXML
    void back(ActionEvent event) throws Exception{
    	StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("OnlineOrOfflineScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }
}
