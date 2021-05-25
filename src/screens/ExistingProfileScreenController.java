package screens;

import data.DataHandler;
import gameentities.Avatar;
import gameentities.Player;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * this class provides the controller for the Existing Profile Screen
 *
 * @author jbleil
 */
public class ExistingProfileScreenController {

  @FXML private ScrollPane chooseProfilePane;
  @FXML private GridPane profileList;
  private static ToggleGroup buttonGroup;
  private static HashMap<Integer, String[]> profiles;

  /**
   * This method serves as the Listener for "START GAME"-Button. It redirects the user to the Online
   * or Offline Screen.
   *
   * @author jluellig
   * @param event
   * @throws Exception
   */
  @FXML
  void startGame(ActionEvent event) throws Exception {
    if (buttonGroup.getSelectedToggle() != null) {
      int id = (int) buttonGroup.getSelectedToggle().getUserData();
      String username = (String) profiles.get(id)[0];
      Avatar avatar = Avatar.valueOf(profiles.get(id)[1]);
      DataHandler.setOwnPlayerId(id);
      DataHandler.setOwnPlayer(new Player(username, avatar));
      StartScreen.getStage();
      FXMLLoader loader = new FXMLLoader();
      Parent content = loader.load(getClass().getClassLoader().getResourceAsStream("screens/resources/OnlineOrOfflineScreen.fxml"));
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    } else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("No profile selected.");
      errorAlert.setContentText("Please select a profile from the list above or create a new one.");
      errorAlert.showAndWait();
    }
  }

  /**
   * Adds all profiles of the database to the ScrollPane so that the user can choose one of them to
   * play or to edit.
   *
   * @author jluellig
   * @throws Exception
   */
  protected void addProfiles() throws Exception {
    buttonGroup = new ToggleGroup();
    profiles = DataHandler.getPlayerInfo();
    profileList.setPrefWidth(790);
    int rows = 0;
    int collumns = 0;
    for (Integer id : profiles.keySet()) {
      String username = (String) profiles.get(id)[0];
      Avatar avatar = Avatar.valueOf(profiles.get(id)[1]);
      Image img = new Image(getClass().getClassLoader().getResourceAsStream(avatar.getUrl()), 52, 52, true, true);
      ToggleButton tb = new ToggleButton(username, new ImageView(img));
      tb.setUserData(id);
      tb.setPrefWidth(260);
      tb.setAlignment(Pos.CENTER_LEFT);
      tb.setToggleGroup(buttonGroup);
      profileList.add(tb, collumns, rows);
      collumns++;
      if (collumns == 3) {
        rows++;
        collumns = 0;
      }
    }
    profileList.setHgap(10);
    profileList.setVgap(10);
    chooseProfilePane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
    chooseProfilePane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
  }

  /**
   * This method serves as the Listener for "EDIT PROFILE"-Button. It redirects the user to the Edit
   * Profile Screen.
   *
   * @author jluellig
   * @param event
   */
  @FXML
  void editProfile(ActionEvent event) throws Exception {
    if (buttonGroup.getSelectedToggle() != null) {
      int id = (int) buttonGroup.getSelectedToggle().getUserData();
      StartScreen.getStage();
      FXMLLoader loader = new FXMLLoader();
      Parent content = loader.load(getClass().getClassLoader().getResourceAsStream("screens/resources/EditProfileScreen.fxml"));
      EditProfileScreenController editProfileScreenController = loader.getController();
      editProfileScreenController.loadProfile(id);
      editProfileScreenController.addAvatars();
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    } else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("No profile selected.");
      errorAlert.setContentText("Please select a profile from the list above or create a new one.");
      errorAlert.showAndWait();
    }
  }

  /**
   * Pre-selects the edited profile.
   *
   * @param id the ID of the profile that should be pre-selected
   * @author jluellig
   */
  protected void setSelectedProfile(int id) {
    List<Toggle> toggles = buttonGroup.getToggles();
    for (Toggle t : toggles) {
      if ((int) t.getUserData() == id) {
        t.setSelected(true);
        break;
      }
    }
  }

  /**
   * This method serves as the Listener for "Back"-Button. It redirects the user to the Start
   * Screen.
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void back(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    Parent content = loader.load(getClass().getClassLoader().getResourceAsStream("screens/resources/StartScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * Checks if there are profiles to show.
   *
   * @author jluellig
   * @throws Exception
   */
  protected void checkProfilesEmpty() throws Exception {
    if (profiles.isEmpty()) {
      Alert errorAlert = new Alert(AlertType.INFORMATION);
      errorAlert.setHeaderText("There are no existing profiles.");
      errorAlert.setContentText("Please create a new profile.");
      Optional<ButtonType> result = errorAlert.showAndWait();
      if (result.get() == ButtonType.OK) {
        FXMLLoader loader = new FXMLLoader();
        Parent content = loader.load(getClass().getClassLoader().getResourceAsStream("screens/resources/NewProfileScreen.fxml"));
        NewProfileScreenController newProfileScreenController = loader.getController();
        newProfileScreenController.addAvatars();
        StartScreen.getStage().setScene(new Scene(content));
        StartScreen.getStage().show();
      }
    }
  }
}
