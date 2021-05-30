package screens;

import gameentities.Bag;
import gameentities.TileContainer;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import network.Client;
import network.Server;

/**
 * The controller for the ChangeTilesScreen.
 *
 * @author jbleil
 * @author tthielen
 */
public class ChangeTilesScreenController {

  @FXML private Pane backgroundPane;

  private boolean isMultiplayer;

  private TextField[] countFields = new TextField[27];
  private TextField[] valueFields = new TextField[27];

  GridPane grid1;
  GridPane grid2;
  GridPane grid3;

  private StringBuffer storedChat;

  /**
   * This method gets the Letter of a Tile with the corresponding count and value from the Bag.
   * These informations are getting displayed on the screen in 3 different gridPanes.
   *
   * @author jbleil
   */
  public void initialize() {
    Text value1 = new Text("Value");
    value1.relocate(130, 90);
    backgroundPane.getChildren().add(value1);
    Text count1 = new Text("Count");
    count1.relocate(225, 90);
    backgroundPane.getChildren().add(count1);
    Text value2 = new Text("Value");
    value2.relocate(430, 90);
    backgroundPane.getChildren().add(value2);
    Text count2 = new Text("Count");
    count2.relocate(525, 90);
    backgroundPane.getChildren().add(count2);
    Text value3 = new Text("Value");
    value3.relocate(730, 90);
    backgroundPane.getChildren().add(value3);
    Text count3 = new Text("Count");
    count3.relocate(825, 90);
    backgroundPane.getChildren().add(count3);
    grid1 = new GridPane();
    grid1.setHgap(15);
    grid1.setVgap(10);
    grid1.relocate(100, 120);
    backgroundPane.getChildren().add(grid1);
    grid2 = new GridPane();
    grid2.setHgap(15);
    grid2.setVgap(10);
    grid2.relocate(400, 120);
    backgroundPane.getChildren().add(grid2);
    grid3 = new GridPane();
    grid3.setHgap(15);
    grid3.setVgap(10);
    grid3.relocate(700, 120);
    backgroundPane.getChildren().add(grid3);
  }

  /**
   * this method sets the TextFields for the ChangeTilesScreen and fills them initially with the
   * properties from the current Bag.
   *
   * @author jbleil
   */
  public void setTileScreen() {
    int rowCounter = 0;
    Bag bag = new Bag();
    bag = Client.getGameSession().getBag();
    for (TileContainer t : bag.getTileCounter()) {
      if (rowCounter < 9) {
        grid1.add(new Text(t.getTile().getLetter() + " :"), 0, rowCounter);
        TextField valueField = new TextField();
        valueField.setPrefWidth(80);
        valueField.setText("" + t.getTile().getValue());
        valueFields[rowCounter] = valueField;
        grid1.add(valueField, 1, rowCounter);
        TextField countField = new TextField();
        countField.setPrefWidth(80);
        countField.setText("" + t.getCount());
        countFields[rowCounter] = countField;
        grid1.add(countField, 2, rowCounter);
        rowCounter++;
      } else if (rowCounter < 18) {
        grid2.add(new Text(t.getTile().getLetter() + " :"), 0, rowCounter - 9);
        TextField valueField = new TextField();
        valueField.setPrefWidth(80);
        valueField.setText("" + t.getTile().getValue());
        valueFields[rowCounter] = valueField;
        grid2.add(valueField, 1, rowCounter - 9);
        TextField countField = new TextField();
        countField.setPrefWidth(80);
        countField.setText("" + t.getCount());
        countFields[rowCounter] = countField;
        grid2.add(countField, 2, rowCounter - 9);
        rowCounter++;
      } else {
        grid3.add(new Text(t.getTile().getLetter() + " :"), 0, rowCounter - 18);
        TextField valueField = new TextField();
        valueField.setPrefWidth(80);
        valueField.setText("" + t.getTile().getValue());
        valueFields[rowCounter] = valueField;
        grid3.add(valueField, 1, rowCounter - 18);
        TextField countField = new TextField();
        countField.setPrefWidth(80);
        countField.setText("" + t.getCount());
        countFields[rowCounter] = countField;
        grid3.add(countField, 2, rowCounter - 18);
        rowCounter++;
      }
    }
  }

  /**
   * Stores the changes that were made.
   *
   * @param event the ActionEvent when the submitChanges-Button is pressed.
   * @throws Exception IOException from the back button
   * @author jbleil
   */
  @FXML
  void submitChanges(ActionEvent event) throws Exception {

    boolean countOkay = true;
    boolean valueOkay = true;
    boolean minCountOkay = true;

    for (TextField tf : countFields) {
      String text = tf.getText();
      if (Pattern.matches("[0-9]+", text) && Integer.valueOf(text) < 101) {
        continue;
      } else {
        countOkay = false;
        break;
      }
    }

    if (countOkay) {
      for (TextField tf : valueFields) {
        String text = tf.getText();
        if (Pattern.matches("[0-9]+", text) && Integer.valueOf(text) < 21) {
          continue;
        } else {
          valueOkay = false;
          break;
        }
      }
    }

    if (countOkay && valueOkay) {
      int totalCount = 0;
      for (int i = 0; i < countFields.length; i++) {
        totalCount += Integer.valueOf(countFields[i].getText());
      }
      minCountOkay = (totalCount >= Server.getPlayerList().size() * 7);
    }

    if (countOkay && valueOkay && minCountOkay) {
      Bag bag = new Bag();
      int counter = 0;
      for (TileContainer t : bag.getTileCounter()) {
        t.getTile().setValue(Integer.parseInt(valueFields[counter].getText()));
        t.setCount(Integer.parseInt(countFields[counter].getText()));
        counter++;
      }
      bag.refreshBag();
      Client.getGameSession().setBag(bag);
      Client.getGameSession().sendGameStateMessage(false);
      back(event);
    } else if (!countOkay) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setTitle("Error");
      errorAlert.setHeaderText("Invalid tile count entered.");
      errorAlert.setContentText("It must be a number between 0 and 100.");
      errorAlert.showAndWait();
      return;
    } else if (!valueOkay) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setTitle("Error");
      errorAlert.setHeaderText("Invalid tile value entered.");
      errorAlert.setContentText("It must be a number between 0 and 20.");
      errorAlert.showAndWait();
      return;
    } else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setTitle("Error");
      errorAlert.setHeaderText("Not enough tiles.");
      errorAlert.setContentText(
          "Because there are "
              + Server.getPlayerList().size()
              + " players, there have to be at least "
              + Server.getPlayerList().size() * 7
              + " tiles in the bag.");
      errorAlert.showAndWait();
      return;
    }
  }

  /**
   * Gets the user back to the LobbyScreen.
   *
   * @param event the ActionEvent when the back-Button is pressed
   * @throws Exception IOException
   * @author jbleil
   */
  @FXML
  void back(ActionEvent event) throws Exception {
    if (isMultiplayer) {
      FXMLLoader loader = new FXMLLoader();
      Parent content =
          loader.load(
              getClass()
                  .getClassLoader()
                  .getResourceAsStream("screens/resources/LobbyScreen.fxml"));
      LobbyScreenController lobbyScreenController = loader.getController();
      lobbyScreenController.refreshChat(storedChat);
      lobbyScreenController.addipAndPort();
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    } else {
      FXMLLoader loader = new FXMLLoader();
      Parent content =
          loader.load(
              getClass()
                  .getClassLoader()
                  .getResourceAsStream("screens/resources/SinglePlayerLobbyScreen.fxml"));
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    }
  }

  /**
   * saves the status if it is a multiplayer game or not to return to the right screen.
   *
   * @author jbleil
   * @param multiplayer is multiplayer or not
   */
  void setMultiplayer(boolean multiplayer) {
    isMultiplayer = multiplayer;
  }

  /**
   * Stores the chat for when the host goes back after editing the tiles.
   *
   * @author tikrause
   * @param storedChat chat that has been stored
   */
  void storeChat(StringBuffer storedChat) {
    this.storedChat = storedChat;
  }
}
