package screens;

import data.DataHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import java.net.BindException;
import java.net.ConnectException;
import java.util.regex.Pattern;
import network.Client;
import network.messages.TooManyPlayerException;

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
    try {
      if (Pattern.matches(
          "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}", ipField.getText().trim())) {
        if (Pattern.matches("[0-9]{4,5}", portField.getText().trim())) {
          try {
            Client.initialiseClient(
                ipField.getText().trim(), Integer.valueOf(portField.getText().trim()), false);
            Client.connectToServer(DataHandler.getOwnPlayer());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("resources/LobbyScreen.fxml"));
            Parent content = loader.load();
            StartScreen.getStage().setScene(new Scene(content));
            StartScreen.getStage().show();
          } catch (TooManyPlayerException te) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("Too much players.");
            errorAlert.setContentText("There are already the maximum of 4 players in the game.");
            errorAlert.showAndWait();
          }
        } else {
          Alert errorAlert = new Alert(AlertType.ERROR);
          errorAlert.setHeaderText("Input not valid.");
          errorAlert.setContentText("The port number is not correct.");
          errorAlert.showAndWait();
          ipField.clear();
          portField.clear();
        }
      } else {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid.");
        errorAlert.setContentText(
            "The IP address is not in the correct format and therefore no valid address.");
        errorAlert.showAndWait();
        ipField.clear();
        portField.clear();
      }
    } catch (BindException | ConnectException e) {
      // TODO dauert ewig und st�rzt fast ab, bevor das AlertFenster erscheint
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("Input not valid.");
      errorAlert.setContentText("There is no server running on the given IP address and port.");
      errorAlert.showAndWait();
      ipField.clear();
      portField.clear();
    }
  }

  /**
   * This method serves as the Listener for the Enter-key in the first text field. It serves as an
   * alternative to the join game button.
   *
   * @param event ActionEvent when enter is pressed in the text field
   * @throws Exception
   * @author jluellig
   */
  @FXML
  void onEnter(ActionEvent event) throws Exception {
    joinGame(event);
  }

  /**
   * This method serves as the Listener for the Enter-key in the second text field. It serves as an
   * alternative to the join game button.
   *
   * @param event ActionEvent when enter is pressed in the text field
   * @throws Exception
   * @author jluellig
   */
  @FXML
  void onEnter2(ActionEvent event) throws Exception {
    joinGame(event);
  }

  /**
   * Initializes the function for the first text field to switch to the second by pressing TAB.
   *
   * @author jluellig
   */
  protected void initializeTab() {
    ipField.setOnKeyPressed(
        new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent ke) {
            if (ke.getCode().equals(KeyCode.TAB)) {
              portField.requestFocus();
            }
          }
        });
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
