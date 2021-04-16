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

public class OnlineOrOfflineScreenController {
	
	/**
	 * 
	 * @author jbleil
	 * @param event
	 * @throws Exception
	 */
	
    @FXML
    void onlineGame(ActionEvent event) throws Exception {
    	StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("OnlineScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }
    
    /**
     * 
     * @author jbleil
     * @param event
     * @throws Exception
     */

    @FXML
    void offlineGame(ActionEvent event) throws Exception {
    	StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("OfflineScreen.fxml").toURI().toURL())));
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
    	StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("StartScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
    }

}
