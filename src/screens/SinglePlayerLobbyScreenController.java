package screens;

import ai.AI;
import data.DataHandler;
import data.StatisticKeys;
import gameentities.Player;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import network.Client;
import network.Server;
import network.messages.TooManyPlayerException;
import org.apache.commons.io.IOUtils;
import session.Dictionary;

/**
 * This class provides the Controller for the Single Player Lobby Screen.
 *
 * @author tikrause
 * @author jbleil
 * @author jluellig
 */
public class SinglePlayerLobbyScreenController {

  @FXML private Button fileForm;

  @FXML private Button startGame;

  @FXML private Button editTiles;

  @FXML private MenuButton dictionarySelecter;

  @FXML private Pane lobbyPane;

  @FXML private Pane chooseBotPane;

  @FXML private Pane tooltipPane;

  @FXML private Text playerInfo1;
  @FXML private Text playerInfo2;
  @FXML private Text playerInfo3;
  @FXML private Text playerInfo4;

  @FXML private Text playerStatistics1;
  @FXML private Text playerStatistics2;
  @FXML private Text playerStatistics3;
  @FXML private Text playerStatistics4;

  @FXML private Button deleteButton1;
  @FXML private Button deleteButton2;
  @FXML private Button deleteButton3;
  private Button[] deleteButtons;

  private File chosenDictionary;

  private ArrayList<Text> playerInfos = new ArrayList<Text>();
  private ArrayList<Text> playerStatistics = new ArrayList<Text>();

  /**
   * This method sets the initial state of the Singleplayer Lobby.
   *
   * @author jbleil
   */
  public void initialize() {
    chooseBotPane.setVisible(false);
    tooltipPane.setVisible(false);

    playerInfos.add(playerInfo1);
    playerInfos.add(playerInfo2);
    playerInfos.add(playerInfo3);
    playerInfos.add(playerInfo4);

    playerStatistics.add(playerStatistics1);
    playerStatistics.add(playerStatistics2);
    playerStatistics.add(playerStatistics3);
    playerStatistics.add(playerStatistics4);

    deleteButtons = new Button[3];
    deleteButtons[0] = deleteButton1;
    deleteButtons[1] = deleteButton2;
    deleteButtons[2] = deleteButton3;
    for (Button b : deleteButtons) {
      b.setVisible(false);
    }

    Client.getGameSession().setSinglePlayerLobbyScreenController(this);

    refreshPlayerList();
    setDictionaryMenu();

    initializeCloseHandler();
  }

  /**
   * This method serves as the listener for the "Upload dictionary"-Button. It allows the user to
   * upload his own dictionary for the game.
   *
   * @author jluellig
   * @param event ActionEvent when the "Upload dictionary"-Button is clicked
   */
  @FXML
  void uploadDictionary(ActionEvent event) {
    try {
      FileChooser fileChooser = new FileChooser();
      fileChooser
          .getExtensionFilters()
          .addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
      File file = fileChooser.showOpenDialog(StartScreen.getStage());
      // check if the file is too large
      if (file.length() > 524288000) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Error");
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
      errorAlert.setTitle("Information");
      errorAlert.setHeaderText("No choice made.");
      errorAlert.setContentText("You haven't chosen a file to upload your own dictionary.");
      errorAlert.showAndWait();
    }
  }

  /**
   * Opens the tooltip.
   *
   * @param event the MouseEvent when the mouse enters the range of the tooltip
   * @author jbleil
   */
  @FXML
  void openTooltip(MouseEvent event) {
    Text text =
        new Text(
            "You can upload your own dictionary for the"
                + "\ngame! You can only use text files in which"
                + "\nevery line starts with the word you want to"
                + "\nadd to the dictionary. Every other information"
                + "\n(that will not be used by this game) has to be"
                + "\nseparated from the word in this line"
                + "\nby a whitespace.");
    text.relocate(10, 10);
    text.setFill(Paint.valueOf("#f88c00"));
    text.setFont(new Font(14));
    tooltipPane.getChildren().add(text);
    tooltipPane.setVisible(true);
  }

  /**
   * Closes the tooltip.
   *
   * @param event the MouseEvent when the mouse leaves the range of the tooltip
   * @author jbleil
   */
  @FXML
  void closeTooltip(MouseEvent event) {
    tooltipPane.setVisible(false);
  }

  /**
   * This method serves as the Listener for "Leave Lobby"-Button. It lets the user leave the Lobby,
   * resets the window listener and redirects him to the OnlineOrOfflineScreen.
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the Leave Lobby Button is clicked
   * @throws Exception the Exception that is thrown when the FXML file is not found
   */
  @FXML
  void leaveLobby(ActionEvent event) throws Exception {
    Client.disconnectClient(DataHandler.getOwnPlayer());
    Server.shutdown();
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
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/OnlineOrOfflineScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * This method serves as the Listener for "START GAME"-Button. It lets the user start the game and
   * redirects him to the GameScreen.
   *
   * @author jbleil
   * @param event ActionEvent that gets triggered when the Start Game Button is clicked
   * @throws Exception the Exception that is thrown when the FXML file is not found
   */
  @FXML
  void startGame(ActionEvent event) throws Exception {
    if (Server.getBotPlayerList().size() > 0) {
      Client.getGameSession().setBag(Client.getGameSession().getBag());
      DataHandler.userDictionaryFile(chosenDictionary);
      Client.sendDictionary(chosenDictionary);
      Client.getGameSession().getPlayer().setCurrentlyPlaying(true);
      Client.reportStartGame("");
      Server.setActive();
      Client.getGameSession().initializeSinglePlayerGameScreen();
      switchToGameScreen();
    } else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setTitle("Error");
      errorAlert.setHeaderText("Too few players.");
      errorAlert.setContentText(
          "You can't start the game before adding an AI player.\n"
              + "If you want to play alone and learn how to play Scrabble, try the Training Mode.");
      errorAlert.showAndWait();
      return;
    }
  }

  /**
   * Opens the pane to select the difficulty of the added AI.
   *
   * @author tikrause
   * @param event user clicks the button
   */
  @FXML
  void addBotPlayer(ActionEvent event) throws Exception {
    chooseBotPane.setVisible(true);
  }

  /**
   * Adds an easy AI player to the game and updates the player list.
   *
   * @author tikrause
   * @param event user chooses to add an easy AI player
   */
  @FXML
  void easyBotPlayer(ActionEvent event) {
    try {
      int aiCount = Server.getEasyBotCount() + 1;
      String aiName = "EasyAI" + aiCount;
      for (AI aiPlayer : Server.getBotPlayerList()) {
        if (aiName.equals(aiPlayer.getPlayer().getUsername())) {
          aiName = "EasyAI" + ++aiCount;
        }
      }
      AI ai = new AI(aiName, false);
      Server.addBotPlayer(ai);
    } catch (TooManyPlayerException e) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setTitle("Error");
      errorAlert.setHeaderText("Too many players.");
      errorAlert.setContentText(
          "You can't add another AI player because there are already "
              + "the maximum of 3 AI players in the game.");
      errorAlert.showAndWait();
    }
    refreshPlayerList();
    closeChooseBotPane(new ActionEvent());
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
      int aiCount = Server.getHardBotCount() + 1;
      String aiName = "HardAI" + aiCount;
      for (AI aiPlayer : Server.getBotPlayerList()) {
        if (aiName.equals(aiPlayer.getPlayer().getUsername())) {
          aiName = "HardAI" + ++aiCount;
        }
      }
      AI ai = new AI(aiName, true);
      Server.addBotPlayer(ai);
    } catch (TooManyPlayerException e) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setTitle("Error");
      errorAlert.setHeaderText("Too many players.");
      errorAlert.setContentText(
          "You can't add another AI player because there are already "
              + "the maximum of 3 AI players in the game.");
      errorAlert.showAndWait();
    }
    refreshPlayerList();
    closeChooseBotPane(new ActionEvent());
  }

  /**
   * Deletes the first AI player in the list and updates the player list.
   *
   * @author tikrause
   * @param event user chooses to delete the AI player
   */
  @FXML
  void deleteBotPlayer1(ActionEvent event) {
    Player ai = Server.getPlayerList().get(1);
    Server.removeBotPlayer(ai);
    refreshPlayerList();
    deleteButton1.setVisible(false);
  }

  /**
   * Deletes the second AI player in the list and updates the player list.
   *
   * @author tikrause
   * @param event user chooses to delete the AI player
   */
  @FXML
  void deleteBotPlayer2(ActionEvent event) {
    Player ai = Server.getPlayerList().get(2);
    Server.removeBotPlayer(ai);
    refreshPlayerList();
    deleteButton2.setVisible(false);
  }

  /**
   * Deletes the second AI player in the list and updates the player list.
   *
   * @author tikrause
   * @param event user chooses to delete the AI player
   */
  @FXML
  void deleteBotPlayer3(ActionEvent event) {
    Player ai = Server.getPlayerList().get(3);
    Server.removeBotPlayer(ai);
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
  void closeChooseBotPane(ActionEvent event) {
    chooseBotPane.setVisible(false);
  }

  /**
   * Sends the user to the GameScreen.
   *
   * @author jbleil
   */
  public void switchToGameScreen() {
    FXMLLoader loader = new FXMLLoader();
    Parent content;
    try {
      content =
          loader.load(
              getClass().getClassLoader().getResourceAsStream("screens/resources/GameScreen.fxml"));
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets all possible dictionaries that are given and implements their handlers if they are
   * selected.
   *
   * @author tikrause
   * @author jluellig
   */
  public void setDictionaryMenu() {
    MenuItem menuItem1 = new MenuItem("Collins Scrabble Words");
    menuItem1.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dictionarySelecter.setText(menuItem1.getText());
            try {
              File temp = File.createTempFile("scrabbleDict", ".txt");
              temp.deleteOnExit();
              FileOutputStream out = new FileOutputStream(temp);
              IOUtils.copy(
                  getClass().getClassLoader().getResourceAsStream(Dictionary.COLLINS.getUrl()),
                  out);
              chosenDictionary = temp;
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });

    MenuItem menuItem2 = new MenuItem("Enable (Words With Friends)");
    menuItem2.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dictionarySelecter.setText(menuItem2.getText());
            try {
              File temp = File.createTempFile("scrabbleDict", ".txt");
              temp.deleteOnExit();
              FileOutputStream out = new FileOutputStream(temp);
              IOUtils.copy(
                  getClass().getClassLoader().getResourceAsStream(Dictionary.ENABLE.getUrl()), out);
              chosenDictionary = temp;
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });

    MenuItem menuItem3 = new MenuItem("Sowpods (Europe Scrabble Word List)");
    menuItem3.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dictionarySelecter.setText(menuItem3.getText());
            try {
              File temp = File.createTempFile("scrabbleDict", ".txt");
              temp.deleteOnExit();
              FileOutputStream out = new FileOutputStream(temp);
              IOUtils.copy(
                  getClass().getClassLoader().getResourceAsStream(Dictionary.SOWPODS.getUrl()),
                  out);
              chosenDictionary = temp;
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });

    MenuItem menuItem4 = new MenuItem("TWL06 (North America Scrabble Word List)");
    menuItem4.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            dictionarySelecter.setText(menuItem4.getText());
            try {
              File temp = File.createTempFile("scrabbleDict", ".txt");
              temp.deleteOnExit();
              FileOutputStream out = new FileOutputStream(temp);
              IOUtils.copy(
                  getClass().getClassLoader().getResourceAsStream(Dictionary.TWL06.getUrl()), out);
              chosenDictionary = temp;
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });

    dictionarySelecter.getItems().add(menuItem1);
    dictionarySelecter.getItems().add(menuItem2);
    dictionarySelecter.getItems().add(menuItem3);
    dictionarySelecter.getItems().add(menuItem4);

    dictionarySelecter.setText(menuItem1.getText());
    try {
      File temp = File.createTempFile("scrabbleDict", ".txt");
      temp.deleteOnExit();
      FileOutputStream out = new FileOutputStream(temp);
      IOUtils.copy(
          getClass().getClassLoader().getResourceAsStream(Dictionary.COLLINS.getUrl()), out);
      chosenDictionary = temp;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Opens the ChangeTilesScreen. Listener for the "Edit Tiles"-Button.
   *
   * @param event the ActionEvent when the edit Tiles-Button is pressed.
   * @throws Exception
   * @author jbleil
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
    changeTilesScreenController.setMultiplayer(false);
    changeTilesScreenController.setTileScreen();
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
          if (i > 0) {
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
   * Handler for when the user closes the window.
   *
   * @author tikrause
   */
  private void initializeCloseHandler() {
    StartScreen.getStage()
        .setOnCloseRequest(
            new EventHandler<WindowEvent>() {
              @Override
              public void handle(final WindowEvent event) {
                try {
                  Client.disconnectClient(DataHandler.getOwnPlayer());
                  Server.shutdown();
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                Platform.exit();
                System.exit(0);
              }
            });
  }
}
