package screens;

import java.util.regex.Pattern;

import data.DataHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import network.Client;

/**
 * this class provides the controller for the Choose Server Screen
 *
 * @author jbleil
 */
public class ChooseServerScreenController {

  @FXML private TextField ipField;

  @FXML private TextField portField;

  /**
   * This method serves as the Listener for "JOIN GAME"-Button It redirects the user to the Lobby
   * Screen
   *
   * @author tikrause
   * @param event
   * @throws Exception
   */
  @FXML
  void joinGame(ActionEvent event) throws Exception {
    if (Pattern.matches("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}", ipField.getText().trim())) {
      if (Pattern.matches("[0-9]{4,5}", portField.getText().trim())) {
        StartScreen.getStage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("resources/LobbyScreen.fxml"));
        Parent content = loader.load();
        StartScreen.getStage().setScene(new Scene(content));
        StartScreen.getStage().show();
        Client.initialiseClient(ipField.getText().trim(), Integer.valueOf(portField.getText().trim()), false);
        Client.connectToServer(DataHandler.getOwnPlayer());
        LobbyScreenController.addIPAndPort();
      } else {
        // TODO
      }
    } else {
      // TODO
    }
  }

  /**
   * This method serves as the Listener for "Back"-Button It redirects the user to the Start Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void back(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/OnlineScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }
}
