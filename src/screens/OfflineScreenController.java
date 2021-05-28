package screens;

import data.DataHandler;
import java.net.BindException;
import java.net.UnknownHostException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import network.Client;
import network.Server;
import network.messages.TooManyPlayerException;

/**
 * this class provides the controller for the Offline Screen.
 *
 * @author jbleil
 */
public class OfflineScreenController {

  /**
   * This method serves as the Listener for "PLAY GAME"-Button It redirects the user to the Lobby
   * Screen.
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the Play Game Button is clicked
   * @throws Exception the Exception that is thrown when the FXML file is not found
   */
  @FXML
  void playGame(ActionEvent event) throws Exception {
    createSinglePlayerLobby();
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/SinglePlayerLobbyScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "TUORIAL MODE"-Button It redirects the user to the
   * Lobby Screen
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the Play Tutorial Button is clicked
   * @throws Exception the Exception that is thrown when the FXML file is not found
   */
  @FXML
  void trainingMode(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/TutorialScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "Back"-Button It redirects the user to the Online or
   * Offline Screen
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the Back Button is clicked
   * @throws Exception the Exception that is thrown when the FXML file is not found
   */
  @FXML
  void back(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/OnlineOrOfflineScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * Creates a single player lobby by creating a server connection that can't be connected to at the
   * 'JOIN GAME' function.
   *
   * @author tikrause
   */
  private void createSinglePlayerLobby() {
    int port = 800;
    while (port < 1000) {
      try {
        Server.createServer(port);
        Client.initialiseClient("localhost", port, true);
        Client.connectToServer(DataHandler.getOwnPlayer());
        break;
      } catch (BindException e) {
        port++;
      } catch (InterruptedException | TooManyPlayerException e) {
        e.printStackTrace();
      }
    }
  }
}
