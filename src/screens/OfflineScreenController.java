package screens;

import data.DataHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import network.Server;

/**
 * this class provides the controller for the Offline Screen
 *
 * @author jbleil
 */
public class OfflineScreenController {

  /**
   * This method serves as the Listener for "PLAY GAME"-Button It redirects the user to the Lobby
   * Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void playGame(ActionEvent event) throws Exception {
    Server.initializeLobby();
    Server.addPlayer(DataHandler.getOwnPlayer());
    FXMLLoader loader = new FXMLLoader();
    Parent content = loader.load(getClass().getClassLoader().getResourceAsStream("screens/resources/SinglePlayerLobbyScreen.fxml"));
    SinglePlayerLobbyScreenController splsc = loader.getController();
    splsc.setDictionaryMenu();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "TRAINING MODE"-Button It redirects the user to the
   * Lobby Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void trainingMode(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    Parent content = loader.load(getClass().getClassLoader().getResourceAsStream("screens/resources/LobbyScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "Back"-Button It redirects the user to the Online or
   * Offline Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void back(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    Parent content = loader.load(getClass().getClassLoader().getResourceAsStream("screens/resources/OnlineOrOfflineScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }
}
