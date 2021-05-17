package screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

public class EditProfileScreenController {

  private static int profileID;
  
  @FXML private TextField nameField;

  /**
   * This method serves as the Listener for "SUBMIT CHANGES"-Button TODO It allows the user to save
   * the changes to the Profile and redirects him back to the Existing Profile Screen
   *
   * @author jbleil
   * @param event
   */
  @FXML
  void submitChanges(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/ExistingProfileScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
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
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/ExistingProfileScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
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
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/ExistingProfileScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * @param id the id of the selected profile in the database that is going to be changed.
   * @author jluellig
   */
  protected static void setProfileId(int id) {
    profileID = id;
    System.out.println(profileID);
  }
}
