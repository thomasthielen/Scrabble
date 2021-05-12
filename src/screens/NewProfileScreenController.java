package screens;

import data.DataHandler;
import gameentities.Avatar;
import gameentities.Player;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
   * @param event
   * @throws Exception
   */
  @FXML
  void startGame(ActionEvent event) throws Exception {
    // TODO add avatar listener
    if (Pattern.matches("[a-zA-Z0-9]{2,15}", inputForm.getText().trim())) {
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
    loader.setLocation(getClass().getResource("resources/StartScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }
}
