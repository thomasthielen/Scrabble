package screens;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class EditProfileScreenController {

  /**
   * This method serves as the Listener for "SUBMIT CHANGES"-Button TODO It allows the user to save
   * the changes to the Profile and redirects him back to the Existing Profile Screen
   *
   * @author jbleil
   * @param event
   */
  @FXML
  void submitChanges(ActionEvent event) throws Exception {
    StartScreen.getStage()
        .setScene(
            new Scene(
                FXMLLoader.load(
                    new File(
                            "resources"
                                + System.getProperty("file.separator")
                                + "ExistingProfileScreen.fxml")
                        .toURI()
                        .toURL())));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "DELETE PROFILE"-Button TODO It allows the user to
   * delete the selected Profile and redirects him back to the Existing Profile Screen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void deleteProfile(ActionEvent event) throws Exception {
    StartScreen.getStage()
        .setScene(
            new Scene(
                FXMLLoader.load(
                    new File(
                            "resources"
                                + System.getProperty("file.separator")
                                + "ExistingProfileScreen.fxml")
                        .toURI()
                        .toURL())));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "Back"-Button It redirects the user to the Existing
   * Profile Screen
   *
   * @author jbleil
   * @param event
   */
  @FXML
  void back(ActionEvent event) throws Exception {
    StartScreen.getStage()
        .setScene(
            new Scene(
                FXMLLoader.load(
                    new File(
                            "resources"
                                + System.getProperty("file.separator")
                                + "ExistingProfileScreen.fxml")
                        .toURI()
                        .toURL())));
    StartScreen.getStage().show();
  }
}
