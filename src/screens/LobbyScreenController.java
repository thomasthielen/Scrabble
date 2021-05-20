package screens;

import java.io.File;
import java.io.IOException;

import AI.AI;
import data.DataHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import network.Client;
import network.Server;
import network.messages.TooManyPlayerException;
import session.Dictionary;
import javafx.event.EventHandler;

/**
 * This class provides the Controller for the Lobby Screen
 *
 * @author jbleil
 */
public class LobbyScreenController {

  @FXML private Button fileForm;

  @FXML private Pane lobbyPane;

  @FXML private MenuButton dictionarySelecter;

  @FXML private Rectangle aiPlayerRectangle;

  @FXML private Rectangle dictionaryRectangle;

  @FXML private Rectangle startGameRectangle;

  @FXML private Rectangle uploadDictionaryRectangle;

  @FXML private TextField textField;

  @FXML private TextArea chatField;

  private File chosenDictionary;

  public void initialize() throws Exception {
    Client.getGameSession().setLobbyScreenController(this);
    if (!Client.isHost()) {
      for (Node node : lobbyPane.getChildren()) {
        if (node instanceof Button) {
          Button b = (Button) node;
          if (b.getText().equals("START GAME")
              || b.getText().equals("Upload dictionary")
              || b.getText().equals("Add AI Player")) {
            b.setDisable(true);
          }
        } else if (node instanceof MenuButton) {
          MenuButton mb = (MenuButton) node;
          if (mb.getText().equals("Dictionaries")) {
            mb.setDisable(true);
          }
        }
      }
    } else {
      aiPlayerRectangle.setVisible(false);
      dictionaryRectangle.setVisible(false);
      startGameRectangle.setVisible(false);
      uploadDictionaryRectangle.setVisible(false);
    }
  }

  /**
   * This method serves as the listener for the "Upload dictionary"-Button. It allows the user to
   * upload his own dictionary for the game.
   *
   * @author jluellig
   * @param event ActionEvent when the "Upload dictionary"-Button is clicked
   * @throws Exception
   */
  @FXML
  void uploadDictionary(ActionEvent event) throws Exception {
    try {
      FileChooser fileChooser = new FileChooser();
      fileChooser
          .getExtensionFilters()
          .addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
      File file = fileChooser.showOpenDialog(StartScreen.getStage());
      // check if the file is too large
      if (file.length() > 524288000) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setHeaderText("File too large.");
        errorAlert.setContentText("The file that you have chosen exceeds the limit of 500 MB.");
        errorAlert.showAndWait();
        return;
      }
      chosenDictionary = file;
      MenuItem menuItemNew = new MenuItem(file.getName());

      menuItemNew.setOnAction(
          new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              chosenDictionary = file;
            }
          });
      dictionarySelecter.getItems().add(menuItemNew);
      dictionarySelecter.setText(menuItemNew.getText());
    } catch (NullPointerException npe) {
      Alert errorAlert = new Alert(AlertType.INFORMATION);
      errorAlert.setHeaderText("No choice made.");
      errorAlert.setContentText("You haven't chosen a file to upload your own dictionary.");
      errorAlert.showAndWait();
    }
  }

  /**
   * This method serves as the Listener for "Leave Lobby"-Button It let's the user leave the Lobby
   * and redirects him to the StartScreen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void leaveLobby(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/OnlineOrOfflineScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
    // TODO
    Client.disconnectClient(DataHandler.getOwnPlayer());
    if (Client.isHost()) {
      Server.serverShutdown();
    }
  }

  /**
   * This method serves as the Listener for "START GAME"-Button It let's the user start the game and
   * redirects him to the GameScreen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void startGame(ActionEvent event) throws Exception {
    if (Client.isHost()) {
      if (Server.getPlayerList().size() > 1) {
        Client.getGameSession().setIsRunning(true);
        DataHandler.userDictionaryFile(chosenDictionary);
        Client.sendDictionary(DataHandler.getOwnPlayer(), chosenDictionary);
        Client.getGameSession().getPlayer().setCurrentlyPlaying(true);
        Client.reportStartGame(DataHandler.getOwnPlayer());
        Client.getGameSession().initialiseGameScreen();
        switchToGameScreen();
      } else {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setHeaderText("Too few players.");
        errorAlert.setContentText(
            "You can't start a MultiPlayer game alone. Please wait for other players to join your lobby or add an AI player.\n\n"
                + "If you want to play alone and learn how to play Scrabble, try the Training Mode.");
        errorAlert.showAndWait();
        return;
      }
    }
  }

  /**
   * TODO This method serves as the Listener for "Add AI Player"-Button It allows the user to add AI
   * Players to the Lobby/Game The Button is only enabled if there are less then 4 Players in the
   * Lobby
   *
   * @author tikrause
   * @param event
   */
  @FXML
  void addAIPlayer(ActionEvent event) throws Exception {
    if (Client.isHost()) {
      try {
        AI ai = new AI("AIPlayer" + (Server.getAIPlayerList().size() + 1), null);
        Server.addAIPlayer(ai);
      } catch (TooManyPlayerException e) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setHeaderText("Too many players.");
        errorAlert.setContentText(
            "You can't add another AI player because there are already the maximum of 4 players in the game.");
        errorAlert.showAndWait();
      }
    }
  }

  @FXML
  void sendMessage(ActionEvent event) {}

  /**
   * the method addIPAndPort displays the IP-Adress and the Port of the current Lobby at the top of
   * the Lobby
   *
   * @author jbleil
   */
  public void addIPAndPort() {
    Pane textPane = new Pane();
    Text ip = new Text(0, 0, "IP-Address: " + Server.getIp());
    ip.setFill(Paint.valueOf("#f88c00"));
    ip.setTextAlignment(TextAlignment.CENTER);
    Text port = new Text(0, 20, "Port: " + Server.getPort());
    port.setFill(Paint.valueOf("#f88c00"));
    port.setTextAlignment(TextAlignment.CENTER);
    textPane.getChildren().add(ip);
    textPane.getChildren().add(port);
    textPane.relocate(350, 100);
    lobbyPane.getChildren().add(textPane);
  }

  public void switchToGameScreen() {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/GameScreen.fxml"));
    Parent content;
    try {
      content = loader.load();
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void setDictionaryMenu() {
    MenuItem menuItem1 = new MenuItem("Collins Scrabble Words");
    MenuItem menuItem2 = new MenuItem("Enable (Words With Friends)");
    MenuItem menuItem3 = new MenuItem("Sowpods (Europe Scrabble Word List)");
    MenuItem menuItem4 = new MenuItem("TWL06 (North America Scrabble Word List)");

    menuItem1.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dictionarySelecter.setText(menuItem1.getText());
            chosenDictionary = new File(Dictionary.COLLINS.getUrl());
          }
        });
    menuItem2.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dictionarySelecter.setText(menuItem2.getText());
            chosenDictionary = new File(Dictionary.ENABLE.getUrl());
          }
        });
    menuItem3.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dictionarySelecter.setText(menuItem3.getText());
            chosenDictionary = new File(Dictionary.SOWPODS.getUrl());
          }
        });
    menuItem4.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dictionarySelecter.setText(menuItem4.getText());
            chosenDictionary = new File(Dictionary.TWL06.getUrl());
          }
        });

    dictionarySelecter.getItems().add(menuItem1);
    dictionarySelecter.getItems().add(menuItem2);
    dictionarySelecter.getItems().add(menuItem3);
    dictionarySelecter.getItems().add(menuItem4);

    dictionarySelecter.setText(menuItem1.getText());
    chosenDictionary = new File(Dictionary.COLLINS.getUrl());
  }
}
