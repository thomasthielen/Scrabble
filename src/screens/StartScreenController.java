package screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * this class provides the controller for the start Screen
 *
 * @author jbleil
 */
public class StartScreenController {

  /**
   * This method serves as the Listener for "NEW PROFILE"-Button It redirects the user to the New
   * Profile Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void createNewProfile(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/NewProfileScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "EXISTING PROFILE"-Button It redirects the user to the
   * Existing Profile Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void existingProfile(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/ExistingProfileScreen.fxml"));
    Parent content = loader.load();
    ExistingProfileScreenController.addProfiles();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }
}
