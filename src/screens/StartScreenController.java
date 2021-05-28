package screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * this class provides the controller for the start Screen.
 *
 * @author jbleil
 */
public class StartScreenController {

  /**
   * This method serves as the Listener for "NEW PROFILE"-Button It redirects the user to the New
   * Profile Screen.
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the New Profile Button is clicked
   * @throws Exception the Exception that is thrown when the FXML file is not found
   */
  @FXML
  void createNewProfile(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content = loader.load(getClass().getClassLoader().getResourceAsStream("screens/resources/NewProfileScreen.fxml"));
    NewProfileScreenController newProfileScreenController = loader.getController();
    newProfileScreenController.addAvatars();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "EXISTING PROFILE"-Button It redirects the user to the
   * Existing Profile Screen.
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the Existing Profile Button is clicked
   * @throws Exception the Exception that is thrown when the FXML file is not found
   */
  @FXML
  void existingProfile(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content = loader.load(getClass().getClassLoader().getResourceAsStream("screens/resources/ExistingProfileScreen.fxml"));
    ExistingProfileScreenController existingProfileScreenController = loader.getController();
    existingProfileScreenController.addProfiles();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
    existingProfileScreenController.checkProfilesEmpty();
  }
}
