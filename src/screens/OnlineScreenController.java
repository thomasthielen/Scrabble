package screens;

import data.DataHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import session.MultiPlayerLobby;

/**
 * this class provides the controller for the Online Screen
 *
 * @author jbleil
 */
public class OnlineScreenController {

  /**
   * This method serves as the Listener for "HOST GAME"-Button It redirects the user to the Lobby
   * Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void hostGame(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/LobbyScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
    // TODO @author tikrause
    createMultiPlayerLobby();
    LobbyScreenController.addIPAndPort();
  }

  /**
   * This method serves as the Listener for "JOIN GAME"-Button It redirects the user to the Choose
   * Server Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void joinGame(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/ChooseServerScreen.fxml"));
    Parent content = loader.load();
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
    loader.setLocation(getClass().getResource("resources/OnlineOrOfflineScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * creates a new lobby for a Multiplayer game
   *
   * @author tikrause
   */
  void createMultiPlayerLobby() {
    MultiPlayerLobby mpl = new MultiPlayerLobby(DataHandler.getOwnPlayer());
  }
}
