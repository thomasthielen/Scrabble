package screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * this class provides the controller for the Online or Offline Screen.
 *
 * @author jbleil
 */
public class OnlineOrOfflineScreenController {

  /**
   * This method serves as the Listener for "ONLINE"-Button It redirects the user to the Online
   * Screen.
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the Online Button is clicked
   * @throws Exception the Exception that is thrown when the FXML file is not found
   */
  @FXML
  void onlineGame(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass().getClassLoader().getResourceAsStream("screens/resources/OnlineScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "SINGLEPLAYER"-Button It redirects the user to the
   * Offline Screen.
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the Offline Tutorial Button is clicked
   * @throws Exception the Exception that is thrown when the FXML file is not found
   */
  @FXML
  void offlineGame(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/OfflineScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "Back"-Button It redirects the user to the Start Screen.
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the Back Tutorial Button is clicked
   * @throws Exception the Exception that is thrown when the FXML file is not found
   */
  @FXML
  void back(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass().getClassLoader().getResourceAsStream("screens/resources/StartScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }
}
