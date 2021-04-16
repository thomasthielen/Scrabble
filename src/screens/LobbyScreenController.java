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

public class LobbyScreenController {
	
	/**
	 * 
	 * @author jbleil
	 * @param event
	 * @throws Exception
	 */
	
    @FXML
    void uploadDictionary(ActionEvent event) throws Exception {

    }
    
    /**
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */
    
    @FXML
    void leaveLobby(ActionEvent event) throws Exception {
		StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("StartScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }
    
    /**
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */

    @FXML
    void startGame(ActionEvent event) throws Exception {
		StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("GameScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }
}
