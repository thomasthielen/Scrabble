package screens;

import java.util.ArrayList;

import gameentities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import session.*;

/**
 * This class provides the Controller for the Game Screen and handles all the interaction with the
 * Players during the game
 *
 * @author jbleil
 */
public class GameScreenController {

  /**
   * gameBoard represents the Container for the Game Board, the tiles and the (for the game flow
   * necessary) buttons
   */
  @FXML private GridPane gameBoard;

  /** gameBoardPane represents the Container for the Game Board and */
  @FXML private Pane gameBoardPane;

  /** chatPane represents the Container for the Chat */
  @FXML private Pane chatPane;

  /** playerStatisticsPane represents the Container for the Player Statistics */
  @FXML private ScrollPane playerStatisticsPane;

  /** rackPane represents the Container for the Tiles in the Rack */
  @FXML private FlowPane rackPane;

  private static ArrayList<Rectangle> rack = new ArrayList<Rectangle>();

  private static ArrayList<Text> letters = new ArrayList<Text>();

  private static ArrayList<Text> numbers = new ArrayList<Text>();

  private ArrayList<StackPane> rackPanes = new ArrayList<StackPane>();

  private GameSession gameSession = new GameSession();

  private ArrayList<Tile> gameBoardTiles = new ArrayList<Tile>();

  private ArrayList<Tile> swapTiles = new ArrayList<Tile>();

  private ArrayList<Tile> rackTiles = new ArrayList<Tile>();

  /**
   * TODO - set Chat and Player Statistic visibility = false - set 7 Tiles in the Rack - fill the
   * board with Rectangles -> opacity = 100 (see through) - fill gridPane with Rectangles
   *
   * @author jbleil
   * @throws Exception
   */
  public void initialize() throws Exception {

    System.out.println(gameBoard.getColumnConstraints());
    for (int i = 0; i <= 14; i++) {
      for (int j = 0; j <= 14; j++) {
        gameBoard.add(new StackPane(), i, j);
      }
    }
    chatPane.setVisible(false);
    playerStatisticsPane.setVisible(false);
    setRack();
  }

  public void setRack() {
    Rack r = gameSession.getPlayer().getRack();
    r.initialDraw();
    // ArrayList<Tile> tiles = new ArrayList<Tile>();
    rackTiles = r.getTiles();

    for (Tile t : rackTiles) {
      Rectangle rectangle = new Rectangle(22, 22);
      rectangle.setFill(Paint.valueOf("#f88c00"));
      rack.add(rectangle);

      Text text = new Text(String.valueOf(t.getLetter()));
      text.setFill(Color.WHITE);
      letters.add(text);

      Text number = new Text(String.valueOf(t.getValue()));
      number.setFont(new Font(10));
      number.setFill(Color.WHITE);
      numbers.add(number);

      StackPane stackPane = new StackPane();
      stackPane.getChildren().addAll(rectangle, text, number);
      rackPanes.add(stackPane);
      StackPane.setAlignment(number, Pos.BOTTOM_RIGHT);
      rackPane.getChildren().add(stackPane);
    }
  }

  /**
   * If the Rack is clicked the clicked Tile is set as selected
   *
   * @author jbleil
   * @param event
   */
  @FXML
  void rackClicked(MouseEvent event) {
    for (Node node : rackPane.getChildren()) {
      if (node instanceof StackPane) {
        if (node.getBoundsInParent().contains(event.getX(), event.getY())) {
          rackTiles.get(rackPanes.indexOf(node)).setSelected(true);
        }
      }
    }
  }

  /**
   * TODO - initialize a new tile
   *
   * @author jbleil
   * @throws Exception
   */
  public static Tile setTile() throws Exception {
    return null; // dummy return
  }

  /**
   * TODO - get the color, the letter and the number of the currently selected Tile - set the box of
   * the
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void gameBoardClicked(ActionEvent event) throws Exception {}

  /**
   * TODO this methods serves as a Listener for the GridPane, which displays the the GameBoard If a
   * Tile from the Rack is selected it is
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void tileClicked(MouseEvent event) throws Exception {
    for (Node node : gameBoard.getChildren()) {
      if (node instanceof StackPane) {
        if (node.getBoundsInParent().contains(event.getX(), event.getY())) {
          for (Tile tile : rackTiles) {
            if (tile.getSelected()) {
              ((StackPane) node).getChildren().add(new Rectangle(22, 22, Paint.valueOf("#f88c00")));
              ((StackPane) node).getChildren().add(new Text(String.valueOf(tile.getLetter())));
              ((StackPane) node).getChildren().add(new Text(String.valueOf(tile.getValue())));
            }
          }
        }
      }
    }
  }

  /**
   * This method serves as the Listener for "Player Statistics"-Button If the Statistics are open it
   * closes the and if they are closed it opens them
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void playerStatistics(ActionEvent event) throws Exception {
    if (playerStatisticsPane.isVisible()) {
      playerStatisticsPane.setVisible(false);
    } else {
      playerStatisticsPane.setVisible(true);
    }
  }

  /**
   * This method serves as the Listener for "X"-Button from the playerStatisticsPane It closes the
   * Player Statistics
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void closePlayerStatistics(ActionEvent event) throws Exception {
    playerStatisticsPane.setVisible(false);
  }

  /**
   * This method serves as the Listener for "CHAT"-Button If the Chat is open it closes it and if
   * it's closed it opens them
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void chat(ActionEvent event) throws Exception {
    if (chatPane.isVisible()) {
      chatPane.setVisible(false);
    } else {
      chatPane.setVisible(true);
    }
  }

  /**
   * This method serves as the Listener for "X"-Button from the chatPane It closes the Chat
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void closeChat(ActionEvent event) throws Exception {
    chatPane.setVisible(false);
  }

  /**
   * This method serves as the Listener for "Leave Game"-Button It let's the user leave the game and
   * redirects him to the StartScreen
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void leaveGame(ActionEvent event) throws Exception {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("resources/StartScreen.fxml"));
    Parent content = loader.load();
    StartScreen.getStage().setScene(new Scene(content));
    StartScreen.getStage().show();
  }

  /**
   * TODO This method serves as the Listener for "Send"-Button from the chatPane It allows the user
   * to send a message to the chat
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void sendMessage(ActionEvent event) throws Exception {}

  /**
   * TODO This method serves as the Listener for "BAG"-Button from the gameBoardPane It opens a
   * popUp-Window in which the user can see all the remaining tiles
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void bag(ActionEvent event) throws Exception {
    ArrayList<TileContainer> tileCounter = new ArrayList<TileContainer>();
    tileCounter = gameSession.getBag().getTileCounter();
    for (TileContainer tc : tileCounter) {
      char c = tc.getTile().getLetter();
      int count = tc.getCount();
    }
  }

  /**
   * TODO This method serves as the Listener for "Recall"-Button from the gameBoardPane It allows
   * the user to call back the tiles he has laid on the gameBoard in the current move
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void recallLetters(ActionEvent event) throws Exception {
    gameSession.recallAll();
  }

  /**
   * TODO This method serves as the Listener for "Submit"-Button from the gameBoardPane The Button
   * gets enabled if the user lays a valid word on the gameBoard If the user presses the Button the
   * layed word is submitted and it's the next players turn
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void submitWord(ActionEvent event) throws Exception {
    gameSession.makePlay();
  }

  /**
   * TODO This method serves as the Listener for "Swap"-Button from the gameBoardPane Opens a
   * popUp-Window in which the user can swap one or more tiles out of his rack with new Tiles from
   * the Bag
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void swapTiles(ActionEvent event) throws Exception {
    // TODO: zu tauschende Tiles zur ArrayList hinzufügen
    swapTiles.add(null);
  }

  /**
   * TODO This method serves as the Listener for the Submit Button in the popUp-Window which opens
   * when you click the "Swap"-Button from the gameBoardPane
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void submitSwapTiles(ActionEvent event) throws Exception {
    gameSession.exchangeTiles(swapTiles);
  }
}
