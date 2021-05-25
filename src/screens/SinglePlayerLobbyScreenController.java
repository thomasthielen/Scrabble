package screens;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import ai.AI;
import data.DataHandler;
import data.StatisticKeys;
import gameentities.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import network.Server;
import network.messages.TooManyPlayerException;
import session.Dictionary;
import session.GameState;
import javafx.event.EventHandler;

/**
 * This class provides the Controller for the Single Player Lobby Screen
 *
 * @author tikrause
 */
public class SinglePlayerLobbyScreenController {

  @FXML private Button fileForm;

  @FXML private Button startGame;

  @FXML private Button editTiles;

  @FXML private MenuButton dictionarySelecter;

  @FXML private Pane lobbyPane;

  @FXML private Pane chooseAIPane;

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

  private File chosenDictionary;

  private ArrayList<Text> playerInfos = new ArrayList<Text>();
  private ArrayList<Text> playerStatistics = new ArrayList<Text>();

  /**
   * This method sets the initial state of the Singleplayer Lobby.
   *
   * @author jbleil
   */
  public void initialize() {
    chooseAIPane.setVisible(false);
    tooltipPane.setVisible(false);

    playerInfos.add(playerInfo1);
    playerInfos.add(playerInfo2);
    playerInfos.add(playerInfo3);
    playerInfos.add(playerInfo4);

    playerStatistics.add(playerStatistics1);
    playerStatistics.add(playerStatistics2);
    playerStatistics.add(playerStatistics3);
    playerStatistics.add(playerStatistics4);

    deleteButton1.setVisible(false);
    deleteButton2.setVisible(false);
    deleteButton3.setVisible(false);

    refreshPlayerList();
    setDictionaryMenu();
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
   * This method serves as the Listener for "Leave Lobby"-Button It let's the user leave the Lobby
   * and redirects him to the StartScreen.
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void leaveLobby(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/OnlineOrOfflineScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
    Server.resetLobby();
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
    if (Server.getAIPlayerList().size() > 0) {
      Server.getLobby().getGameSession().setIsRunning(true);
      DataHandler.userDictionaryFile(chosenDictionary);
      Server.getLobby().getGameSession().synchronise(new GameState(Server.getPlayerList()));
      Server.updateAI(new GameState(Server.getPlayerList()));
      for (AI ai : Server.getAIPlayerList()) {
        ai.setDictionary(chosenDictionary);
      }
      Server.getLobby().getGameSession().getPlayer().setCurrentlyPlaying(true);
      Server.getLobby().getGameSession().initialiseSinglePlayerGameScreen();
      switchToGameScreen();
    } else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("Too few players.");
      errorAlert.setContentText(
          "You can't start the game before adding an AI player.\n"
              + "If you want to play alone and learn how to play Scrabble, try the Training Mode.");
      errorAlert.showAndWait();
      return;
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
    chooseAIPane.setVisible(true);
  }

  @FXML
  void easyAIPlayer(ActionEvent event) {
    try {
      AI ai = new AI("EasyAI" + (Server.getEasyAICount() + 1), false, false);
      Server.addAIPlayer(ai);
    } catch (TooManyPlayerException e) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("Too many players.");
      errorAlert.setContentText(
          "You can't add another AI player because there are already the maximum of 3 AI players in the game.");
      errorAlert.showAndWait();
    }
    refreshPlayerList();
    closeChooseAIPane(new ActionEvent());
  }

  @FXML
  void hardAIPlayer(ActionEvent event) {
    try {
      AI ai = new AI("HardAI" + (Server.getHardAICount() + 1), true, false);
      Server.addAIPlayer(ai);
    } catch (TooManyPlayerException e) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("Too many players.");
      errorAlert.setContentText(
          "You can't add another AI player because there are already the maximum of 3 AI players in the game.");
      errorAlert.showAndWait();
    }
    refreshPlayerList();
    closeChooseAIPane(new ActionEvent());
  }

  @FXML
  void closeChooseAIPane(ActionEvent event) {
    chooseAIPane.setVisible(false);
  }

  public void switchToGameScreen() throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content;
    content =
        loader.load(
            getClass().getClassLoader().getResourceAsStream("screens/resources/GameScreen.fxml"));
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /** @author tikrause */
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

  @FXML
  void editTiles(ActionEvent event) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent content =
        loader.load(
            getClass()
                .getClassLoader()
                .getResourceAsStream("screens/resources/ChangeTilesScreen.fxml"));
    ChangeTilesScreenController changeTilesScreenController = loader.getController();
    changeTilesScreenController.initialize();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * this method is responsible for displaying the player names and statistics in the game lobby.
   *
   * @author jbleil
   */
  public void refreshPlayerList() {
    ArrayList<Player> players = Server.getPlayerList();

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
      }
    }

    for (int i = players.size(); i < playerInfos.size() && i < playerStatistics.size(); i++) {
      playerInfos.get(i).setVisible(false);
      playerStatistics.get(i).setVisible(false);
    }
  }

  @FXML
  void deleteAIPlayer1(ActionEvent event) {}

  @FXML
  void deleteAIPlayer2(ActionEvent event) {}

  @FXML
  void deleteAIPlayer3(ActionEvent event) {}
}
