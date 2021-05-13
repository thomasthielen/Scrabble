package screens;

import java.io.File;

import data.DataHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import network.Client;
import network.Server;

/**
 * This class provides the Controller for the Lobby Screen
 *
 * @author jbleil
 */
public class LobbyScreenController {

	@FXML private Button fileForm;
	
    @FXML private static Pane lobbyPane;
	
  /**
   * This method serves as the listener for the "Upload dictionary"-Button. It allows the user to
   * upload his own dictionary for the game.
   *
   * @author jluellig
   * @param event ActionEvent when the "Upload dictionary"-Button is clicked
   * @throws Exception
   */
  @FXML
  void uploadDictionary(ActionEvent event) throws Exception {
	  FileChooser fileChooser = new FileChooser();
	  File file = fileChooser.showOpenDialog(StartScreen.getStage());
	  DataHandler.userDictionaryFile(file);
	  fileForm.setText(file.getName());
  }

  /**
   * This method serves as the Listener for "Leave Lobby"-Button It let's the user leave the Lobby
   * and redirects him to the StartScreen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void leaveLobby(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/OnlineOrOfflineScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));    
    StartScreen.getStage().show();
    // TODO
    Client.disconnectClient(DataHandler.getOwnPlayer().getUsername());
    Server.serverShutdown();
  }

  /**
   * This method serves as the Listener for "START GAME"-Button It let's the user start the game and
   * redirects him to the GameScreen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void startGame(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/GameScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }


  /**
   * TODO This method serves as the Listener for "Add AI Player"-Button It allows the user to add AI
   * Players to the Lobby/Game The Button is only enabled if there are less then 4 Players in the
   * Lobby
   *
   * @author jbleil
   * @param event
   */
  @FXML
  void addAIPlayer(ActionEvent event) throws Exception {}
  
  public static void addIPAndPort() {
    Text ip = new Text(100, 100, Client.getIp());
    Text port = new Text(100, 150, "" + Client.getPort());
    lobbyPane = new Pane();
    lobbyPane.getChildren().add(ip);
    lobbyPane.getChildren().add(port);
    lobbyPane.setVisible(true);
  }
}
