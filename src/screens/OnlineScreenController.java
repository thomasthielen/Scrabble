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
 * this class provides the controller for the Online Screen.
 *
 * @author jbleil
 */
public class OnlineScreenController {

  /**
   * This method serves as the Listener for "HOST GAME"-Button It redirects the user to the Lobby
   * Screen.
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the Host Game Button is clicked
   * @throws Exception the Exception that is thrown when the FXML file is not found
   */
  @FXML
  void hostGame(ActionEvent event) throws Exception {
    createMultiPlayerLobby();
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass().getClassLoader().getResourceAsStream("screens/resources/LobbyScreen.fxml"));
    LobbyScreenController lobbyScreenController = loader.getController();
    lobbyScreenController.addipAndPort();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "JOIN GAME"-Button It redirects the user to the Choose
   * Server Screen
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the Join Game Button is clicked
   * @throws Exception the Exception that is thrown when the FXML file is not found
   */
  @FXML
  void joinGame(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/ChooseServerScreen.fxml"));
    ChooseServerScreenController chooseServerScreenController = loader.getController();
    chooseServerScreenController.initializeTab();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "Back"-Button It redirects the user to the Online or
   * Offline Screen.
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
   * creates a new lobby for a multiplayer game, initializes the server at port 8000 and connects
   * the host to the server. If the port is already used, port 8001 is tried and so on.
   *
   * @author tikrause
   */
  void createMultiPlayerLobby() {
    int port = 8000;
    while (port < 65535) {
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
