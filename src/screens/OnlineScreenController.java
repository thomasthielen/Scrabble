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
    createMultiPlayerLobby();
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/LobbyScreen.fxml"));
    Parent content = loader.load();
    LobbyScreenController lobbyScreenController = loader.getController();
    lobbyScreenController.setDictionaryMenu();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
    // LobbyScreenController.addIPAndPort();
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
  void createMultiPlayerLobby() throws TooManyPlayerException {
    int port = 8000;
    while (port < 65535) {
      try {
        Server.createServer(port);
        // TODO
        System.out.println(
            "Your lobby has been created. IP: " + Server.getIp() + ", Port: " + Server.getPort());
        Client.initialiseClient("localhost", port, true);
        Client.connectToServer(DataHandler.getOwnPlayer());
        break;
      } catch (BindException e) {
        port++;
      } catch (UnknownHostException e) {
        // TODO
      } catch (InterruptedException e) {
        // TODO
      }
    }
  }
}
