package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * this class provides the controller for the Existing Profile Screen
 *
 * @author jbleil
 */
public class ExistingProfileScreenController {

  /**
   * This method serves as the Listener for "START GAME"-Button It redirects the user to the Online
   * or Offline Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void startGame(ActionEvent event) throws Exception {
    StartScreen.getStage()
        .setScene(
            new Scene(FXMLLoader.load(new File("OnlineOrOfflineScreen.fxml").toURI().toURL())));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "EDIT PROFILE"-Button It redirects the user to the Edit
   * Profile Screen
   *
   * @author jbleil
   * @param event
   */
  @FXML
  void editProfile(ActionEvent event) throws Exception {
    StartScreen.getStage()
        .setScene(new Scene(FXMLLoader.load(new File("EditProfileScreen.fxml").toURI().toURL())));
    StartScreen.getStage().show();
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
    StartScreen.getStage()
        .setScene(new Scene(FXMLLoader.load(new File("StartScreen.fxml").toURI().toURL())));
    StartScreen.getStage().show();
  }
}
