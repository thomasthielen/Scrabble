package screens;

import data.DataHandler;
import java.net.BindException;
import java.net.ConnectException;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import network.Client;

/**
 * This class provides the controller for the Choose Server Screen.
 *
 * @author jbleil
 * @author tikrause
 * @author jluellig
 */
public class ChooseServerScreenController {

  @FXML private TextField ipField;

  @FXML private TextField portField;

  /**
   * This method serves as the Listener for "JOIN GAME"-Button. It joins an existing server if the
   * input of the user is correct and redirects the user to the Lobby Screen.
   *
   * @author tikrause
   * @param event the ActionEvent when the join Game-Button is pressed
   * @throws Exception the Exception when the fxml file is not found
   */
  @FXML
  void joinGame(ActionEvent event) throws Exception {
    try {
      if (Pattern.matches(
          "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}", ipField.getText().trim())) {
        if (Pattern.matches("[1-9][0-9]{3,4}", portField.getText().trim())) {
          Client.initializeClient(
              ipField.getText().trim(), Integer.valueOf(portField.getText().trim()), false);
          Client.connectToServer(DataHandler.getOwnPlayer());
          FXMLLoader loader = new FXMLLoader();
          Parent content =
              loader.load(
                  getClass()
                      .getClassLoader()
                      .getResourceAsStream("screens/resources/LobbyScreen.fxml"));
          StartScreen.getStage().setScene(new Scene(content));
          StartScreen.getStage().show();
        } else {
          Alert errorAlert = new Alert(AlertType.ERROR);
          errorAlert.setTitle("Error");
          errorAlert.setHeaderText("Input not valid.");
          errorAlert.setContentText("The port number is not correct.");
          errorAlert.showAndWait();
          portField.clear();
        }
      } else {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Input not valid.");
        errorAlert.setContentText(
            "The IP address is not in the correct format and therefore no valid address.");
        errorAlert.showAndWait();
        ipField.clear();
        portField.clear();
      }
    } catch (BindException | ConnectException e) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setTitle("Error");
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
   * @throws Exception the Exception when the fxml file is not found
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
   * @throws Exception the Exception when the fxml file is not found
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
   * This method serves as the Listener for "Back"-Button. It redirects the user to the Start
   * Screen.
   *
   * @author jbleil
   * @param event the ActionEvent that gets triggered when the back Button is clicked
   * @throws Exception the Exception when the fxml file is not found
   */
  @FXML
  void back(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass().getClassLoader().getResourceAsStream("screens/resources/OnlineScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }
}
