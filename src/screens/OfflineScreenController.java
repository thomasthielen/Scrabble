package screens;

import java.net.BindException;
import java.net.UnknownHostException;

import data.DataHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import network.Client;
import network.Server;
import network.messages.TooManyPlayerException;

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
   * This method serves as the Listener for "TRAINING MODE"-Button It redirects the user to the
   * Lobby Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
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
   * @param event
   * @throws Exception
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
      } catch (UnknownHostException | InterruptedException | TooManyPlayerException e) {
        e.printStackTrace();
      }
    }
  }
}
