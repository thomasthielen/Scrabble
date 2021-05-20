package screens;

import data.DataHandler;
import gameentities.Avatar;
import gameentities.Player;

import java.io.FileInputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * this class provides the controller for the new Profile Screen
 *
 * @author jbleil
 */
public class NewProfileScreenController {

  private static ToggleGroup buttonGroup;

  @FXML private TextField inputForm;

  @FXML private Pane backgroundPane;

  /**
   * This method serves as the Listener for "START GAME"-Button. It redirects the user to the New
   * Profile Screen and a new Player instance is created and added to the database.
   *
   * @author jluellig
   * @param event ActionEvent when the "Start Game"-Button is clicked
   * @throws Exception
   */
  @FXML
  void startGame(ActionEvent event) throws Exception {
    if (buttonGroup.getSelectedToggle() != null) {
      boolean alreadyUsed = false;
      String input = inputForm.getText().trim();
      Avatar avatar = (Avatar) buttonGroup.getSelectedToggle().getUserData();
      if (Pattern.matches("[a-zA-Z0-9]{2,15}", input)
          && !(alreadyUsed = usernameAlreadyUsed(input))) {
        DataHandler.addPlayer(input, avatar);
        DataHandler.setOwnPlayer(new Player(input, avatar));
        StartScreen.getStage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("resources/OnlineOrOfflineScreen.fxml"));
        Parent content = loader.load();
        StartScreen.getStage().setScene(new Scene(content));
        StartScreen.getStage().show();
      } else if (alreadyUsed) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setHeaderText("Username already exists.");
        errorAlert.setContentText("Try a different username.");
        errorAlert.showAndWait();
        inputForm.clear();
      } else {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid.");
        errorAlert.setContentText(
            "The username must contain 2-15 letters or numbers. It can't contain any special characters.");
        errorAlert.showAndWait();
        inputForm.clear();
      }
    } else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("No avatar selected.");
      errorAlert.setContentText("Please select an avatar from the list.");
      errorAlert.showAndWait();
    }
  }

  /**
   * This method serves as the Listener for the Enter-key in the text field. It serves as an
   * alternative to the start game button.
   *
   * @param event ActionEvent when enter is pressed in the text field
   * @throws Exception
   * @author jluellig
   */
  @FXML
  void onEnter(ActionEvent event) throws Exception {
    startGame(event);
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

  /**
   * Checks if the given username is already used in the database.
   *
   * @param username the input username that should be checked
   * @return true if the given username is already a username in the database, otherwise false
   * @author jluellig
   */
  private boolean usernameAlreadyUsed(String username) {
    HashMap<Integer, String[]> profiles = DataHandler.getPlayerInfo();
    String input = inputForm.getText().trim();
    for (int key : profiles.keySet()) {
      String s = (String) profiles.get(key)[0];
      if (s.equals(input)) {
        return true;
      }
    }
    return false;
  }

  /**
   * this method displays all the choosable avatars on the Screen.
   *
   * @author jbleil
   * @throws FileNotFoundException
   */
  protected void addAvatars() throws FileNotFoundException {
    buttonGroup = new ToggleGroup();
    GridPane gridPane = new GridPane();
    backgroundPane.getChildren().add(gridPane);
    gridPane.setPrefWidth(800);
    gridPane.setVgap(20);
    gridPane.setHgap(20);
    gridPane.relocate(247, 270);
    Avatar[] array = Avatar.values();
    int counter = 0;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 6; j++) {
        Image img = new Image(new FileInputStream(array[counter].getUrl()), 52, 52, true, true);
        ToggleButton tb = new ToggleButton("", new ImageView(img));
        tb.setUserData(array[counter]);
        tb.setToggleGroup(buttonGroup);
        gridPane.add(tb, j, i);
        counter++;
      }
    }
  }
}
