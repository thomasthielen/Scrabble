package screens;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import ai.AI;
import data.DataHandler;
import data.StatisticKeys;
import gameentities.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
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

  @FXML private Pane lobbyPane;

  @FXML private Pane chooseAIPane;

  @FXML private Pane tooltipPane;

  @FXML private Rectangle aiPlayerRectangle;

  @FXML private Rectangle dictionaryRectangle;

  @FXML private Rectangle startGameRectangle;

  @FXML private Rectangle uploadDictionaryRectangle;

  @FXML private TextField textField;

  @FXML private TextArea chatField;

  @FXML private Text playerInfo1;
  @FXML private Text playerInfo2;
  @FXML private Text playerInfo3;
  @FXML private Text playerInfo4;

  @FXML private Text playerStatistic1;
  @FXML private Text playerStatistic2;
  @FXML private Text playerStatistic3;
  @FXML private Text playerStatistic4;

  @FXML private Text uploadDictionaryText;
  @FXML private Text selectDictionaryText;
  @FXML private Text editTilesText;

  @FXML private Button fileForm;
  @FXML private Button addAIPlayer;
  @FXML private Button startGame;
  @FXML private Button editTiles;
  @FXML private MenuButton dictionarySelecter;

  @FXML private Button deleteButton1;
  @FXML private Button deleteButton2;
  @FXML private Button deleteButton3;
  private Button[] deleteButtons;

  private ArrayList<Text> playerInfos = new ArrayList<Text>();
  private ArrayList<Text> playerStatistics = new ArrayList<Text>();

  private File chosenDictionary;
  private StringBuffer chatHistory = new StringBuffer();

  public void initialize() throws Exception {
    playerInfos.add(playerInfo1);
    playerInfos.add(playerInfo2);
    playerInfos.add(playerInfo3);
    playerInfos.add(playerInfo4);

    playerStatistics.add(playerStatistic1);
    playerStatistics.add(playerStatistic2);
    playerStatistics.add(playerStatistic3);
    playerStatistics.add(playerStatistic4);

    deleteButtons = new Button[3];
    deleteButtons[0] = deleteButton1;
    deleteButtons[1] = deleteButton2;
    deleteButtons[2] = deleteButton3;
    for (Button b : deleteButtons) {
      b.setVisible(false);
    }

    Client.getGameSession().setLobbyScreenController(this);
    if (!Client.isHost()) {
      fileForm.setDisable(true);
      addAIPlayer.setDisable(true);
      startGame.setDisable(true);
      editTiles.setDisable(true);
      dictionarySelecter.setDisable(true);
      uploadDictionaryText.setOpacity(0.5);
      selectDictionaryText.setOpacity(0.5);
      editTilesText.setOpacity(0.5);
    }

    refreshPlayerList();
    setDictionaryMenu();

    chatField.setEditable(false);
    chatField.setMouseTransparent(true);
    chatField.setFocusTraversable(false);
    chooseAIPane.setVisible(false);
    tooltipPane.setVisible(false);

    initializeCloseHandler();
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

  @FXML
  void openTooltip(MouseEvent event) {
    Text text =
        new Text(
            "You can upload your own dictionary for the\ngame! You can only use text files in which\nevery line starts with the word you want to\nadd to the dictionary. Every other information\n(that will not be used by this game) has to be\nseparated from the word in this line\nby a whitespace.");
    text.relocate(10, 10);
    text.setFill(Paint.valueOf("#f88c00"));
    text.setFont(new Font(14));
    tooltipPane.getChildren().add(text);
    tooltipPane.setVisible(true);
  }

  @FXML
  void closeTooltip(MouseEvent event) {
    tooltipPane.setVisible(false);
  }

  /**
   * This method serves as the Listener for "Leave Lobby"-Button. It lets the user leave the Lobby
   * and cuts all connections.
   *
   * @author tikrause
   * @param event user hits the 'LEAVE LOBBY'-Button
   * @throws Exception
   */
  @FXML
  void leaveLobby(ActionEvent event) throws Exception {
    Client.disconnectClient(DataHandler.getOwnPlayer());
    if (Client.isHost()) {
      Server.serverShutdown();
    }
    leave();
  }

  /**
   * Resets the window handler and brings the user back to the OnlineOrOfflineScrren.
   *
   * @author tikrause
   */
  void leave() {
    StartScreen.getStage()
        .setOnCloseRequest(
            new EventHandler<WindowEvent>() {
              @Override
              public void handle(final WindowEvent event) {
                Platform.exit();
                System.exit(0);
              }
            });
    FXMLLoader loader = new FXMLLoader();
    Parent content;
    try {
      content =
          loader.load(
              getClass()
                  .getClassLoader()
                  .getResourceAsStream("screens/resources/OnlineOrOfflineScreen.fxml"));
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    } catch (IOException e) {
      e.printStackTrace();
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
        Client.getGameSession().setBag(Client.getGameSession().getBag());
        DataHandler.userDictionaryFile(chosenDictionary);
        Client.sendDictionary(DataHandler.getOwnPlayer(), chosenDictionary);
        Client.getGameSession().getPlayer().setCurrentlyPlaying(true);
        Client.reportStartGame(DataHandler.getOwnPlayer(), chatHistory.toString());
        Server.setActive();
        Client.getGameSession().initialiseGameScreen(chatHistory.toString());
        switchToGameScreen(chatHistory.toString());
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
   * Opens the pane to select the difficulty of the added AI.
   *
   * @author tikrause
   * @param event user clicks the button
   */
  @FXML
  void addAIPlayer(ActionEvent event) throws Exception {
    chooseAIPane.setVisible(true);
  }

  /**
   * Adds an easy AI player to the game and updates the player list.
   *
   * @author tikrause
   * @param event user chooses to add an easy AI player
   */
  @FXML
  void easyAIPlayer(ActionEvent event) {
    try {
      int aiCount = Server.getEasyAICount() + 1;
      String aiName = "EasyAI" + aiCount;
      for (AI aiPlayer : Server.getAIPlayerList()) {
        if (aiName.equals(aiPlayer.getPlayer().getUsername())) {
          aiName = "EasyAI" + ++aiCount;
        }
      }
      AI ai = new AI(aiName, false);
      Server.addAIPlayer(ai);
      refreshPlayerList();
      Client.sendChat(ai.getPlayer(), "You will definitely lose!");
      chatHistory.append(ai.getPlayer().getUsername() + ": " + "You will definitely lose!" + "\n");
      chatField.setText(chatHistory.toString());
    } catch (TooManyPlayerException e) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("Too many players.");
      errorAlert.setContentText(
          "You can't add another AI player because there are already the maximum of 4 players in the game.");
      errorAlert.showAndWait();
    }
    closeChooseAIPane(new ActionEvent());
  }

  /**
   * Adds an hard AI player to the game and updates the player list.
   *
   * @author tikrause
   * @param event user chooses to add a hard AI player
   */
  @FXML
  void hardAIPlayer(ActionEvent event) {
    try {
      int aiCount = Server.getHardAICount() + 1;
      String aiName = "HardAI" + aiCount;
      for (AI aiPlayer : Server.getAIPlayerList()) {
        if (aiName.equals(aiPlayer.getPlayer().getUsername())) {
          aiName = "HardAI" + ++aiCount;
        }
      }
      AI ai = new AI(aiName, true);
      Server.addAIPlayer(ai);
      refreshPlayerList();
      Client.sendChat(ai.getPlayer(), "You will have no chance!");
      chatHistory.append(ai.getPlayer().getUsername() + ": " + "You will have no chance!" + "\n");
      chatField.setText(chatHistory.toString());
    } catch (TooManyPlayerException e) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("Too many players.");
      errorAlert.setContentText(
          "You can't add another AI player because there are already the maximum of 4 players in the game.");
      errorAlert.showAndWait();
    }
    closeChooseAIPane(new ActionEvent());
  }

  /**
   * Deletes the second player in the list if it is an AI player and updates the player list.
   *
   * @author tikrause
   * @param event user chooses to delete the AI player
   */
  @FXML
  void deleteAIPlayer1(ActionEvent event) {
    Player ai = Server.getPlayerList().get(1);
    Server.removeAIPlayer(ai);
    refreshPlayerList();
    deleteButton1.setVisible(false);
  }

  /**
   * Deletes the third player in the list if it is an AI player and updates the player list.
   *
   * @author tikrause
   * @param event user chooses to delete the AI player
   */
  @FXML
  void deleteAIPlayer2(ActionEvent event) {
    Player ai = Server.getPlayerList().get(2);
    Server.removeAIPlayer(ai);
    refreshPlayerList();
    deleteButton2.setVisible(false);
  }

  /**
   * Deletes the fourth player in the list if it is an AI player and updates the player list.
   *
   * @author tikrause
   * @param event user chooses to delete the AI player
   */
  @FXML
  void deleteAIPlayer3(ActionEvent event) {
    Player ai = Server.getPlayerList().get(3);
    Server.removeAIPlayer(ai);
    refreshPlayerList();
    deleteButton3.setVisible(false);
  }

  /**
   * Closes the pane to choose the difficulty of the AI.
   *
   * @author tikrause
   * @param event user clicks the exit button
   */
  @FXML
  void closeChooseAIPane(ActionEvent event) {
    chooseAIPane.setVisible(false);
  }

  /**
   * Sends the typed message to all other users in the game session.
   *
   * @author tikrause
   * @param event user types in a message and clicks 'SEND'
   */
  @FXML
  void sendMessage(ActionEvent event) {
    String input = textField.getText().trim();
    if (Pattern.matches(".{1,140}", input)) {
      Client.sendChat(DataHandler.getOwnPlayer(), ": " + input);
      chatHistory.append(DataHandler.getOwnPlayer().getUsername() + ": " + input + "\n");
      chatField.setText(chatHistory.toString());
      textField.clear();
    } else if (input.isBlank()) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("Message is empty.");
      errorAlert.setContentText("The chat message has to contain at least one character.");
      errorAlert.showAndWait();
    } else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("Message too long.");
      errorAlert.setContentText("The maximum length of a chat message is 140 characters.");
      errorAlert.showAndWait();
    }
  }

  /**
   * When a text message is received by another player in the game session, the chat pane is updated
   * and the received message is shown.
   *
   * @author tikrause
   * @param p player that has sent the message
   * @param chat message that has been received
   */
  public void receivedMessage(Player p, String chat) {
    chatHistory.append(p.getUsername() + chat + "\n");
    chatField.setText(chatHistory.toString());
  }

  /**
   * Refreshs the chat when the host comes back from editing the tiles.
   *
   * @author tikrause
   * @param sb chat that has been stored
   */
  void refreshChat(StringBuffer sb) {
    chatHistory = sb;
    chatField.setText(chatHistory.toString());
  }

  /**
   * This method serves as the Listener for the Enter-key in the chat text field. It serves as an
   * alternative to the send message button.
   *
   * @param event ActionEvent when enter is pressed in the text field
   * @author jluellig
   */
  @FXML
  void onEnter(ActionEvent event) {
    sendMessage(event);
  }

  /**
   * this method opens the ChangeTilesScreen.
   *
   * @author jbleil
   * @param event
   */
  @FXML
  void editTiles(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/ChangeTilesScreen.fxml"));
    ChangeTilesScreenController changeTilesScreenController = loader.getController();
    changeTilesScreenController.setMultiplayer(true);
    changeTilesScreenController.setTileScreen();
    changeTilesScreenController.storeChat(chatHistory);
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * this method is responsible for displaying the player names and statistics in the game lobby.
   *
   * @author jbleil
   */
  public void refreshPlayerList() {
    ArrayList<Player> players = Client.getGameSession().getPlayerList();

    if (players.size() > 0) {
      for (Button b : deleteButtons) {
        b.setVisible(false);
      }
      for (int i = 0; i < players.size(); i++) {
        playerInfos.get(i).setText(players.get(i).getUsername());
        playerInfos.get(i).setVisible(true);
        if (!players.get(i).isBot()) {
          HashMap<StatisticKeys, Integer> map = players.get(i).getPlayerStatistics();
          playerStatistics
              .get(i)
              .setText(
                  "Games won: "
                      + map.get(StatisticKeys.WON)
                      + "\nGames played: "
                      + map.get(StatisticKeys.MATCHES)
                      + "\nAverage Points: "
                      + map.get(StatisticKeys.POINTSAVG));
          playerStatistics.get(i).setVisible(true);
        } else {
          playerStatistics.get(i).setText("");
          playerStatistics.get(i).setVisible(false);
          if (i > 0 && Client.isHost()) {
            deleteButtons[i - 1].setVisible(true);
          }
        }
      }

      for (int i = players.size(); i < playerInfos.size() && i < playerStatistics.size(); i++) {
        playerInfos.get(i).setVisible(false);
        playerStatistics.get(i).setVisible(false);
      }
    }
  }

  /**
   * the method addIPAndPort displays the IP-Address and the Port of the current Lobby at the top of
   * the Lobby screen.
   *
   * @author jbleil
   */
  void addIPAndPort() {
    Pane textPane = new Pane();
    Text ip = new Text(0, 0, "IP-Address: " + Server.getIp());
    ip.setFill(Paint.valueOf("#f88c00"));
    ip.setTextAlignment(TextAlignment.CENTER);
    Text port = new Text(0, 20, "Port: " + Server.getPort());
    port.setFill(Paint.valueOf("#f88c00"));
    port.setTextAlignment(TextAlignment.CENTER);
    textPane.getChildren().add(ip);
    textPane.getChildren().add(port);
    textPane.relocate(14, 30);
    lobbyPane.getChildren().add(textPane);
  }

  public void switchToGameScreen(String chat) {
    Client.getGameSession().startTimer();
    Client.getGameSession().setIsRunning(true);
    FXMLLoader loader = new FXMLLoader();
    Parent content;
    try {
      content =
          loader.load(
              getClass().getClassLoader().getResourceAsStream("screens/resources/GameScreen.fxml"));
      GameScreenController gsc = loader.getController();
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
      gsc.takeOverChat(chat);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets all possible dictionaries that are given and implements their handlers if they are
   * selected.
   *
   * @author tikrause
   */
  private void setDictionaryMenu() {
    MenuItem menuItem1 = new MenuItem("Collins Scrabble Words");
    MenuItem menuItem2 = new MenuItem("Enable (Words With Friends)");
    MenuItem menuItem3 = new MenuItem("Sowpods (Europe Scrabble Word List)");
    MenuItem menuItem4 = new MenuItem("TWL06 (North America Scrabble Word List)");

    menuItem1.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dictionarySelecter.setText(menuItem1.getText());
            InputStream is =
                getClass().getClassLoader().getResourceAsStream(Dictionary.COLLINS.getUrl());
            try {
              FileUtils.copyInputStreamToFile(is, chosenDictionary);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
    menuItem2.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dictionarySelecter.setText(menuItem2.getText());
            InputStream is =
                getClass().getClassLoader().getResourceAsStream(Dictionary.ENABLE.getUrl());
            try {
              FileUtils.copyInputStreamToFile(is, chosenDictionary);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
    menuItem3.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dictionarySelecter.setText(menuItem3.getText());
            InputStream is =
                getClass().getClassLoader().getResourceAsStream(Dictionary.SOWPODS.getUrl());
            try {
              FileUtils.copyInputStreamToFile(is, chosenDictionary);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
    menuItem4.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dictionarySelecter.setText(menuItem4.getText());
            InputStream is =
                getClass().getClassLoader().getResourceAsStream(Dictionary.TWL06.getUrl());
            try {
              FileUtils.copyInputStreamToFile(is, chosenDictionary);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });

    dictionarySelecter.getItems().add(menuItem1);
    dictionarySelecter.getItems().add(menuItem2);
    dictionarySelecter.getItems().add(menuItem3);
    dictionarySelecter.getItems().add(menuItem4);

    dictionarySelecter.setText(menuItem1.getText());
    chosenDictionary = new File(Dictionary.COLLINS.getUrl());
  }

  /**
   * informs the other players that the host has left the session and therefore all other players
   * are kicked.
   *
   * @author tikrause
   */
  public void hostHasLeft() {
    Alert errorAlert = new Alert(AlertType.ERROR);
    errorAlert.setHeaderText("The host has left.");
    errorAlert.setContentText(
        "The host has left the game and therefore you have been disconnected from the server.");
    errorAlert.showAndWait();
    leave();
  }

  /**
   * informs the player that tries to join a running game and denies him from joining.
   *
   * @author tikrause
   */
  public void gameAlreadyRunning() {
    Alert errorAlert = new Alert(AlertType.ERROR);
    errorAlert.setHeaderText("Game has already started.");
    errorAlert.setContentText("You can't join the server because the game has already started.");
    errorAlert.showAndWait();
    leave();
  }

  /**
   * Handler for when the user closes the window.
   *
   * @author jluellig
   */
  private void initializeCloseHandler() {
    StartScreen.getStage()
        .setOnCloseRequest(
            new EventHandler<WindowEvent>() {
              @Override
              public void handle(final WindowEvent event) {
                try {
                  Client.disconnectClient(DataHandler.getOwnPlayer());
                  if (Client.isHost()) {
                    Server.serverShutdown();
                  }
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                Platform.exit();
                System.exit(0);
              }
            });
  }
}
