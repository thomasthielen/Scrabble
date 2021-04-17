package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * this class provides the controller for the start Screen
 * 
 * @author jbleil
 *
 */

public class StartScreenController {
	
	/**
	 * This method serves as the Listener for "NEW PROFILE"-Button
     * It redirects the user to the New Profile Screen
	 * 
	 * @author jbleil
	 * @param event
	 * @throws Exception
	 */
	
	@FXML
	 void createNewProfile(ActionEvent event) throws Exception{
		StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("NewProfileScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }
	
	/**
	 * This method serves as the Listener for "EXISTING PROFILE"-Button
     * It redirects the user to the Existing Profile Screen
	 * 
	 * @author jbleil
	 * @param event
	 * @throws Exception
	 */

    @FXML
    void existingProfile(ActionEvent event) throws Exception {
    	StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("ExistingProfileScreen.fxml").toURI().toURL())));
    	StartScreen.getStage().show();
    }
}
