package screens;

import data.DataHandler;
import gameentities.Avatar;
import java.io.FileNotFoundException;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This class provides the controller for the EditProfileScreen.
 *
 * @author jluellig
 * @author jbleil
 */
public class EditProfileScreenController {

  private static int profileID;
  private static String currentUsername;
  private static Avatar currentAvatar;
  private static ToggleGroup buttonGroup;

  @FXML private TextField nameField;

  @FXML private Pane backgroundPane;

  /**
   * Sets the ID for the profile in the database that is going to be edited.
   *
   * @param id the id of the selected profile in the database that is going to be changed
   * @author jluellig
   */
  protected void loadProfile(int id) {
    profileID = id;
    HashMap<Integer, String[]> profiles = DataHandler.getPlayerInfo();
    currentUsername = (String) profiles.get(id)[0];
    currentAvatar = Avatar.valueOf(profiles.get(id)[1]);
    nameField.setText(currentUsername);
  }
  
  // addAvatars
  
  /**
   * This method serves as the Listener for "SUBMIT CHANGES"-Button. It allows the user to save the
   * changes to the Profile and redirects him back to the Existing Profile Screen.
   *
   * @author jluellig
   * @param event the ActionEvent when the submit changes-Button is pressed
   */
  @FXML
  void submitChanges(ActionEvent event) throws Exception {
    boolean alreadyUsed = false;
    String input = nameField.getText().trim();
    // make changes to username (if it is different)
    if (!input.equals(currentUsername)) {
      if (Pattern.matches("[a-zA-Z0-9]{2,15}", input)
          && !(alreadyUsed = usernameAlreadyUsed(input))) {
        DataHandler.alterPlayerUsername(input, profileID);
        currentUsername = input;
      } else if (alreadyUsed) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Username already exists.");
        errorAlert.setContentText("Try a different username.");
        errorAlert.showAndWait();
        nameField.setText(currentUsername);
        return;
      } else {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Input not valid.");
        errorAlert.setContentText(
            "The username must contain 2-15 letters or numbers. It can't contain any special "
                + "characters.");
        errorAlert.showAndWait();
        nameField.setText(currentUsername);
        return;
      }
    }
    // make changes to avatar (if it is different)
    if (buttonGroup.getSelectedToggle() != null) {
      Avatar newAvatar = (Avatar) buttonGroup.getSelectedToggle().getUserData();
      if (!newAvatar.equals(currentAvatar)) {
        DataHandler.alterPlayerAvatar(newAvatar, profileID);
        currentAvatar = newAvatar;
      }
    } else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setTitle("Error");
      errorAlert.setHeaderText("No avatar selected.");
      errorAlert.setContentText("Please select an avatar from the list.");
      errorAlert.showAndWait();
      return;
    }
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/ExistingProfileScreen.fxml"));
    ExistingProfileScreenController existingProfileScreenController = loader.getController();
    existingProfileScreenController.addProfiles();
    existingProfileScreenController.setSelectedProfile(profileID);
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * Checks if the given username is already used in the database.
   *
   * @param username the input username that should be checked
   * @return true if the given username is already a username in the database, otherwise false
   * @author jluellig
   */
  private boolean usernameAlreadyUsed(String username) {
    HashMap<Integer, String[]> profiles = DataHandler.getPlayerInfo();
    for (int key : profiles.keySet()) {
      String s = (String) profiles.get(key)[0];
      if (s.equals(username)) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * This method serves as the Listener for the Enter-key in the text field. It serves as an
   * alternative to the submit changes button.
   *
   * @param event ActionEvent when enter is pressed in the text field
   * @throws Exception the Exception when the fxml file is not found
   * @author jluellig
   */
  @FXML
  void onEnter(ActionEvent event) throws Exception {
    submitChanges(event);
  }
  
  /**
   * This method serves as the Listener for "DELETE PROFILE"-Button. It allows the user to delete
   * the selected Profile and redirects him back to the Existing Profile Screen
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the deleteProfile Button is clicked
   * @throws Exception the Exception when the fxml file is not found
   */
  @FXML
  void deleteProfile(ActionEvent event) throws Exception {
    DataHandler.deletePlayer(profileID);
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/ExistingProfileScreen.fxml"));
    ExistingProfileScreenController existingProfileScreenController = loader.getController();
    existingProfileScreenController.addProfiles();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
    existingProfileScreenController.checkProfilesEmpty();
  }

  /**
   * This method serves as the Listener for "Back"-Button. It redirects the user to the Existing
   * Profile Screen.
   *
   * @author jbleil
   * @param event the ActionEvent when the back-Button is pressed
   */
  @FXML
  void back(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/ExistingProfileScreen.fxml"));
    ExistingProfileScreenController existingProfileScreenController = loader.getController();
    existingProfileScreenController.addProfiles();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * this method displays all the choosable avatars on the Screen.
   *
   * @author jbleil
   * @throws FileNotFoundException the Exception when the avatar file is not found
   */
  protected void addAvatars() throws FileNotFoundException {
    buttonGroup = new ToggleGroup();
    GridPane gridPane = new GridPane();
    backgroundPane.getChildren().add(gridPane);
    gridPane.setPrefWidth(800);
    gridPane.setVgap(20);
    gridPane.setHgap(20);
    gridPane.relocate(247, 260);
    Avatar[] array = Avatar.values();
    int counter = 0;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 6; j++) {
        Image img =
            new Image(
                getClass().getClassLoader().getResourceAsStream(array[counter].getUrl()),
                52,
                52,
                true,
                true);
        ToggleButton tb = new ToggleButton("", new ImageView(img));
        tb.setUserData(array[counter]);
        tb.setToggleGroup(buttonGroup);
        if (array[counter].equals(currentAvatar)) {
          tb.setSelected(true);
        }
        gridPane.add(tb, j, i);
        counter++;
      }
    }
  }

  /**
   * This method serves as a Listener for the Text "Soni Sokell" and displays an instance of
   * NameLinkScreen.
   *
   * @author jbleil
   * @param event the MouseEvent that gets thrown when clicking on the "Soni Sokell" Text
   * @throws Exception general exception catch
   */
  @FXML
  void openNameScreen(MouseEvent event) throws Exception {
    NameLinkScreen nls = new NameLinkScreen();
    Stage stage = new Stage();
    nls.start(stage);
  }

  /**
   * This method serves as a Listener for the Text "freeicons.io" and displays an instance of
   * SiteLinkScreen.
   *
   * @author jbleil
   * @param event the MouseEvent that gets thrown when clicking on the "freeicons.io" Text
   * @throws Exception general exception catch
   */
  @FXML
  void openSiteScreen(MouseEvent event) throws Exception {
    SiteLinkScreen sls = new SiteLinkScreen();
    Stage stage = new Stage();
    sls.start(stage);
  }
}
