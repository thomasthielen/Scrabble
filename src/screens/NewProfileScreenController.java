package screens;

import data.DataHandler;
import gameentities.Avatar;
import gameentities.Player;

import java.util.HashMap;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

/**
 * this class provides the controller for the new Profile Screen
 *
 * @author jbleil
 */
public class NewProfileScreenController {

  @FXML private TextField inputForm;

  /**
   * This method serves as the Listener for "START GAME"-Button. It redirects the user to the New
   * Profile Screen and a new Player instance is created and added to the database.
   *
   * @author jluellig
   * @param event ActionEvent when the "Start Game"-Button is clicked
   * @throws Exception
   */
  @FXML
  void startGame(ActionEvent event) throws Exception {
    // TODO add avatar listener
    boolean alreadyUsed = false;
    if (Pattern.matches("[a-zA-Z0-9]{2,15}", inputForm.getText().trim())
        && !(alreadyUsed = isAlreadyUsed(inputForm.getText().trim()))) {
      String playerName = inputForm.getText().trim();
      Avatar a = Avatar.BLUE;
      DataHandler.addPlayer(playerName, a);
      DataHandler.setOwnPlayer(new Player(playerName, a));
      StartScreen.getStage();
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("resources/OnlineOrOfflineScreen.fxml"));
      Parent content = loader.load();
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    } else if (alreadyUsed) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("Username already exists.");
      errorAlert.setContentText("Try a different username.");
      errorAlert.showAndWait();
      inputForm.clear();
    } else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("Input not valid.");
      errorAlert.setContentText(
          "The username must contain 2-15 letters or numbers. It can't contain any special characters.");
      errorAlert.showAndWait();
      inputForm.clear();
    }
  }

  /**
   * This method serves as the Listener for the Enter-key in the text field. It serves as an
   * alternative to the start game button.
   *
   * @param event ActionEvent when enter is pressed in the text field
   * @throws Exception
   * @author jluellig
   */
  @FXML
  void onEnter(ActionEvent event) throws Exception {
    startGame(event);
  }

  /**
   * This method serves as the Listener for "Back"-Button. It redirects the user to the Start
   * Screen.
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void back(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/StartScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * Checks if the given username is already used in the database.
   * 
   * @param username the input username that should be checked
   * @return true if the given username is already a username in the database, otherwise false
   *
   * @author jluellig
   */
  private boolean isAlreadyUsed(String username) {
    HashMap<Integer, String[]> profiles = DataHandler.getPlayerInfo();
    for (int key : profiles.keySet()) {
      String s = (String) profiles.get(key)[0];
      if (s.equals(inputForm.getText().trim())) {
        return true;
      }
    }
    return false;
  }
}
