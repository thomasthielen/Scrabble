package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * This class provides the Controller for the Lobby Screen
 * 
 * @author jbleil
 *
 */

public class LobbyScreenController {
	
	/**
	 * TODO
	 * This method serves as the Listener for "Upload dictionary"-Button
     * It allows the user to upload his own dictionaries to the game
	 * 
	 * @author jbleil
	 * @param event
	 * @throws Exception
	 */
	
    @FXML
    void uploadDictionary(ActionEvent event) throws Exception {

    }
    
    /**
     * This method serves as the Listener for "Leave Lobby"-Button
     * It let's the user leave the Lobby and redirects him to the StartScreen
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
     * This method serves as the Listener for "START GAME"-Button
     * It let's the user start the game and redirects him to the GameScreen
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
    
    /**
     * TODO
     * This method serves as the Listener for "Add AI Player"-Button
     * It allows the user to add AI Players to the Lobby/Game
     * The Button is only enabled if there are less then 4 Players in the Lobby
     * 
     * @author jbleil
     * @param event
     */
    
    @FXML
    void addAIPlayer(ActionEvent event) throws Exception {

    }
}
