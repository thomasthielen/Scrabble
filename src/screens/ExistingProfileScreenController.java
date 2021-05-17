package screens;

import data.DataHandler;
import gameentities.Avatar;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * this class provides the controller for the Existing Profile Screen
 *
 * @author jbleil
 */
public class ExistingProfileScreenController {

  @FXML private ScrollPane chooseProfilePane;
  @FXML private Pane profileList = new Pane();
  private static ToggleGroup buttonGroup;
  private static HashMap<Integer, String[]> profiles;
  @FXML private Pane test = new Pane();

  /**
   * This method serves as the Listener for "START GAME"-Button. It redirects the user to the Online
   * or Offline Screen.
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void startGame(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/OnlineOrOfflineScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
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
      Image img = new Image(new FileInputStream(avatar.getUrl()));
      ToggleButton tb = new ToggleButton(username, new ImageView(img));
      tb.setToggleGroup(buttonGroup);
      profileList.getChildren().add(tb);
    }
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
      int id = 0;
      Iterator<Integer> it = profiles.keySet().iterator();
      for (Toggle t : buttonGroup.getToggles()) {
        if (t.isSelected()) {
          id = it.next();
          break;
        } else {
          it.next();
        }
      }
      StartScreen.getStage();
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("resources/EditProfileScreen.fxml"));
      EditProfileScreenController editProfileScreenController = loader.getController();
      editProfileScreenController.loadProfile(id);
      Parent content = loader.load();
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    } else {
      // TODO
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
