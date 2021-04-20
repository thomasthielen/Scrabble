package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * this class provides the controller for the new Profile Screen
 *
 * @author jbleil
 */
public class NewProfileScreenController {

  /**
   * This method serves as the Listener for "START GAME"-Button It redirects the user to the New
   * Profile Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void startGame(ActionEvent event) throws Exception {
    StartScreen.getStage()
        .setScene(
            new Scene(
                FXMLLoader.load(
                    new File(
                            "resources"
                                + System.getProperty("file.separator")
                                + "OnlineOrOfflineScreen.fxml")
                        .toURI()
                        .toURL())));
    StartScreen.getStage().show();
  }

  /**
   * TODO This method serves as the Listener for TextField It allows the user to enter a new
   * username
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void newName(ActionEvent event) throws Exception {}

  /**
   * This method serves as the Listener for "Back"-Button It redirects the user to the Start Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void back(ActionEvent event) throws Exception {
    StartScreen.getStage()
        .setScene(
            new Scene(
                FXMLLoader.load(
                    new File(
                            "resources" + System.getProperty("file.separator") + "StartScreen.fxml")
                        .toURI()
                        .toURL())));
    StartScreen.getStage().show();
  }
}
