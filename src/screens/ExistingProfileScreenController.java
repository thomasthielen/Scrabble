package screens;

import data.DataHandler;
import gameentities.Avatar;
import gameentities.Player;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

/**
 * this class provides the controller for the Existing Profile Screen
 *
 * @author jbleil
 */
public class ExistingProfileScreenController {

  @FXML private ScrollPane chooseProfilePane;
  @FXML private FlowPane profileList;
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
        DataHandler.setOwnPlayer(new Player(username, avatar));
        StartScreen.getStage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("resources/OnlineOrOfflineScreen.fxml"));
        Parent content = loader.load();
        StartScreen.getStage().setScene(new Scene(content));
        StartScreen.getStage().show();
      } else {
      	Alert errorAlert = new Alert(AlertType.ERROR);
          errorAlert.setHeaderText("No profile selected.");
          errorAlert.setContentText(
              "Please select a profile from the list above or create a new one.");
          errorAlert.showAndWait();
      }
  }

  /**
   * Adds all profiles of the database to the ScrollPane so that the user can choose one of them to
   * play or to edit.
   *
   * @author jluellig
   * @throws FileNotFoundException
   */
  protected void addProfiles() throws FileNotFoundException {
    buttonGroup = new ToggleGroup();
    profiles = DataHandler.getPlayerInfo();
    for (Integer id : profiles.keySet()) {
      String username = (String) profiles.get(id)[0];
      Avatar avatar = Avatar.valueOf(profiles.get(id)[1]);
      Image img = new Image(new FileInputStream(avatar.getUrl()), 52, 52, true, true);
      ToggleButton tb = new ToggleButton(username, new ImageView(img));
      tb.setUserData(id);
      tb.setToggleGroup(buttonGroup);
      profileList.getChildren().add(tb);
    }
    profileList.setHgap(20);
    profileList.setVgap(20);
    chooseProfilePane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
    chooseProfilePane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
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
      loader.setLocation(getClass().getResource("resources/EditProfileScreen.fxml"));
      Parent content = loader.load();
      EditProfileScreenController editProfileScreenController = loader.getController();
      editProfileScreenController.loadProfile(id);
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    } else {
    	Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setHeaderText("No profile selected.");
        errorAlert.setContentText(
            "Please select a profile from the list above or create a new one.");
        errorAlert.showAndWait();
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
    loader.setLocation(getClass().getResource("resources/StartScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }
}
