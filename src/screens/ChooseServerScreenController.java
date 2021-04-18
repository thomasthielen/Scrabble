package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * this class provides the controller for the Choose Server Screen
 *
 * @author jbleil
 */
public class ChooseServerScreenController {

	/**
	 * This method serves as the Listener for "JOIN GAME"-Button It redirects the
	 * user to the Lobby Screen
	 *
	 * @author jbleil
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void joinGame(ActionEvent event) throws Exception {
		StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("LobbyScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
	}

	/**
	 * This method serves as the Listener for "Back"-Button It redirects the user to
	 * the Start Screen
	 *
	 * @author jbleil
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void back(ActionEvent event) throws Exception {
		StartScreen.getStage().setScene(new Scene(FXMLLoader.load(new File("StartScreen.fxml").toURI().toURL())));
		StartScreen.getStage().show();
	}
}
