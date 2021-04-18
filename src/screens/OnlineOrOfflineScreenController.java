package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * this class provides the controller for the Online or Offline Screen
 *
 * @author jbleil
 */
public class OnlineOrOfflineScreenController {

  /**
   * This method serves as the Listener for "ONLINE"-Button It redirects the user to the Online
   * Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void onlineGame(ActionEvent event) throws Exception {
    StartScreen.getStage()
        .setScene(new Scene(FXMLLoader.load(new File("OnlineScreen.fxml").toURI().toURL())));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "SINGLEPLAYER"-Button It redirects the user to the
   * Offline Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void offlineGame(ActionEvent event) throws Exception {
    StartScreen.getStage()
        .setScene(new Scene(FXMLLoader.load(new File("OfflineScreen.fxml").toURI().toURL())));
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
