package screens;

import java.io.File;

import com.sun.javafx.geom.Rectangle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This class provides the Controller for the Game Screen and handles all the interaction
 * with the Players during the game
 * 
 * @author jbleil
 *
 */

public class GameScreenController {
	
	/**
	 * gameBoard represents the Container for the Game Board, the tiles and the
	 * (for the game flow necessary) buttons
	 */
	
    @FXML
    private GridPane gameBoard;
    
    /**
	 * gameBoardPane represents the Container for the Game Board and
	 */

    @FXML
    private Pane gameBoardPane;
    
    /**
	 * chatPane represents the Container for the Chat
	 */

    @FXML
    private Pane chatPane;
    
    /**
	 * playerStatisticsPane represents the Container for the Player Statistics
	 */

    @FXML
    private ScrollPane playerStatisticsPane;
    
    /**
     * This method serves as the Listener for "Player Statistics"-Button
     * If the Statistics are open it closes the and if they are closed it opens them
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */
    
    @FXML
    void playerStatistics(ActionEvent event) throws Exception {
    	if(playerStatisticsPane.isVisible()) {
    		playerStatisticsPane.setVisible(false);
    	}
    	else {
    		playerStatisticsPane.setVisible(true);
    	}
    }
    
    /**
     * This method serves as the Listener for "X"-Button from the playerStatisticsPane
     * It closes the Player Statistics
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */
    
    @FXML
    void closePlayerStatistics(ActionEvent event) throws Exception {
    	playerStatisticsPane.setVisible(false);
    }
    
    /**
     * This method serves as the Listener for "CHAT"-Button
     * If the Chat is open it closes it and if it's closed it opens them
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */
        
    @FXML
    void chat(ActionEvent event) throws Exception {
    	if(chatPane.isVisible()) {
    		chatPane.setVisible(false);
    	}
    	else {
    		chatPane.setVisible(true);
    	}
    }
    
    /**
     * This method serves as the Listener for "X"-Button from the chatPane
     * It closes the Chat
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */
    
    @FXML
    void closeChat(ActionEvent event) throws Exception {
    	chatPane.setVisible(false);
    }
    
    /**
     * This method serves as the Listener for "Leave Game"-Button
     * It let's the user leave the game and redirects him to the StartScreen
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */

    @FXML
    void leaveGame(ActionEvent event) throws Exception {
		StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("StartScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }
    
    /**
     * TODO
     * This method serves as the Listener for "Send"-Button from the chatPane
     * It allows the user to send a message to the chat
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */
    
    @FXML
    void sendMessage(ActionEvent event) throws Exception {

    }
    
    /**
     * TODO
     * This method serves as the Listener for "BAG"-Button from the gameBoardPane
     * It opens a popUp-Window in which the user can see all the remaining tiles
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */
    
    @FXML
    void bag(ActionEvent event) throws Exception {

    }
    
    /**
     * TODO
     * This method serves as the Listener for "Recall"-Button from the gameBoardPane
     * It allows the user to call back the tiles he has laid on the gameBoard in the current move 
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */
    
    @FXML
    void recallLetters(ActionEvent event) throws Exception {

    }
    
    /**
     * TODO
     * This method serves as the Listener for "Submit"-Button from the gameBoardPane
     * The Button gets enabled if the user lays a valid word on the gameBoard
     * If the user presses the Button the layed word is submitted and it's the next players turn
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */

    @FXML
    void submitWord(ActionEvent event) throws Exception {

    }
    
    /**
     * TODO
     * This method serves as the Listener for "Swap"-Button from the gameBoardPane
     * Opens a popUp-Window in which the user can swap one or more tiles out of his rack
     * with new Tiles from the Bag
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */

    @FXML
    void swapTiles(ActionEvent event) throws Exception {

    }
}
