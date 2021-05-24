package screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import data.DataHandler;
import data.StatisticKeys;
import gameentities.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
import network.Client;
import network.Server;
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
  @FXML private Pane gameBoardPane; // TODO: Might want to rename this compared to gameBoard

  /** chatPane represents the Container for the Chat */
  @FXML private Pane chatPane;

  /** playerStatisticsPane represents the Container for the Player Statistics */
  @FXML private ScrollPane playerStatisticsScrollPane;

  @FXML private Pane playerStatisticsPane;

  /** rackPane represents the Container for the Tiles in the Rack */
  @FXML private FlowPane rackPane;

  /** swapRack */
  @FXML private FlowPane swapRack;

  /** swapPane has */
  @FXML private Pane swapPane;

  @FXML private Pane wildcardPane;

  @FXML private TextField wildcardTextField;

  @FXML private Button wildcardClose;

  @FXML private Button wildcardSubmit;

  @FXML private TextArea chatField;
  @FXML private TextField textField;
  @FXML private Button chatButton;

  @FXML private Button openSwapButton;
  @FXML private Button submitButton;
  @FXML private Button recallButton;
  @FXML private Button skipTurnButton;

  @FXML private Button endGame;

  @FXML private Button bagButton;

  @FXML private Text timerText;

  private static ArrayList<Rectangle> rack = new ArrayList<Rectangle>();

  private static ArrayList<Text> letters = new ArrayList<Text>();

  private static ArrayList<Text> numbers = new ArrayList<Text>();

  private ArrayList<StackPane> rackPanes = new ArrayList<StackPane>();

  private ArrayList<StackPane> swapPanes = new ArrayList<StackPane>();

  private ArrayList<StackPane> boardPanes = new ArrayList<StackPane>();

  private GameSession gameSession;

  private ArrayList<Tile> gameBoardTiles = new ArrayList<Tile>();

  private ArrayList<Tile> swapTiles = new ArrayList<Tile>();
  private ArrayList<Integer> positions = new ArrayList<Integer>();

  private ArrayList<Tile> rackTiles = new ArrayList<Tile>();

  private Tile tileToPlace;
  private int tileToPlaceX;
  private int tileToPlaceY;

  private Tile wildcardTile;
  private StackPane wildcardStackPane;
  private Square wildcardSquare;
  private char wildcardChar;

  private ArrayList<SquarePane> squarePanes = new ArrayList<SquarePane>();

  private double eventX = 0;

  private double eventY = 0;

  private Button submitSwapButton;

  private Text currentlyPlaying = new Text();
  private boolean playable = false;
  private boolean swapPaneOpen = false;

  private int boardSelectedX = 0;
  private int boardSelectedY = 0;

  private StringBuffer chatHistory;
  private int unreadMessages = 0;

  /**
   * TODO - set Chat and Player Statistic visibility = false - set 7 Tiles in the Rack - fill the
   * board with Rectangles -> opacity = 100 (see through) - fill gridPane with Rectangles
   *
   * @author jbleil
   * @throws Exception
   */
  public void initialize() throws Exception {

    if (Server.getLobby() != null) {
      gameSession = Server.getLobby().getGameSession();
    } else {
      gameSession = Client.getGameSession();
    }
    gameSession.setGameScreenController(this);

    // Disable all buttons which require a move to be made
    recallButton.setDisable(true);
    submitButton.setDisable(true);
    // Enable the swap button
    openSwapButton.setDisable(false);
    // Set the endGame button to invisible
    endGame.setVisible(false);

    // Fill the gameBoard with SquarePanes which are also held in squarePanes (!)
    System.out.println(gameBoard.getColumnConstraints());
    squarePanes.clear();
    for (int j = 0; j <= 14; j++) {
      for (int i = 0; i <= 14; i++) {
        SquarePane sp = new SquarePane(gameSession.getBoard().getSquare(i + 1, 15 - j));
        squarePanes.add(sp);
        gameBoard.add(sp.getStackPane(), i, j);
      }
    }
    // Fill the StackPanes of squarePanes into boardPanes
    boardPanes.clear();
    for (SquarePane sp : squarePanes) {
      boardPanes.add(sp.getStackPane());
    }

    // Set the openable windows to invisible
    chatPane.setVisible(false);
    playerStatisticsScrollPane.setVisible(false);
    swapPane.setVisible(false);
    wildcardPane.setVisible(false);

    setRack(true);
    chatField.setEditable(false);
    chatField.setMouseTransparent(true);
    chatField.setFocusTraversable(false);

    chatButton.setVisible(gameSession.getMultiPlayer());

    setPlayerStatistics();
  }

  public void setRack(boolean isFirstTime) {
    rack.clear();
    letters.clear();
    numbers.clear();
    rackPanes.clear();
    rackPane.getChildren().clear();
    rackTiles.clear();

    for (Player p : gameSession.getPlayerList()) {
      if (p.isCurrentlyPlaying()) {
        currentlyPlaying.setText(p.getUsername());
        currentlyPlaying.setFill(Color.BLACK);
        break;
      }
    }

    Rack r = gameSession.getPlayer().getRack();
    if (isFirstTime) {
      // Call the initial draw of tiles in the back end
      r.initialDraw();
      if (gameSession.getMultiPlayer()) {
        gameSession.sendGameStateMessage();
      }
    }

    // Save those tiles in the ArrayList rackTiles
    for (Tile t : r.getTiles()) {
      rackTiles.add(t);
    }

    // For each tile on the rack:
    for (Tile t : rackTiles) {
      // Set a rectangle
      Rectangle rectangle = new Rectangle(22, 22);
      rectangle.setFill(Paint.valueOf("#f88c00"));
      rack.add(rectangle);
      // Set the letter
      Text text = new Text(String.valueOf(t.getLetter()));
      if (t.isWildCard()) {
        text.setFill(Paint.valueOf("dad3f2"));
      } else {
        text.setFill(Color.WHITE);
      }
      letters.add(text);
      // Set the value
      Text number = new Text(String.valueOf(t.getValue()));
      number.setFont(new Font(10));
      if (t.isWildCard()) {
        number.setFill(Paint.valueOf("dad3f2"));
      } else {
        number.setFill(Color.WHITE);
      }
      numbers.add(number);

      // And add those as children to a StackPane, which is saved in rackPanes
      StackPane stackPane = new StackPane();
      stackPane.getChildren().addAll(rectangle, text, number);
      rackPanes.add(stackPane);
      StackPane.setAlignment(number, Pos.BOTTOM_RIGHT);
      rackPane.getChildren().add(stackPane);
    }

    refreshBagCount();
  }

  /**
   * If the Rack is clicked the clicked Tile is set as selected
   *
   * @author jbleil
   * @param event
   */
  @FXML
  void rackClicked(MouseEvent event) {
    if (playable) {
      // Extract the coordinates from the event
      eventX = event.getX();
      eventY = event.getY();
      // Go through all elements of the rackPane
      for (Node node : rackPane.getChildren()) {
        if (node instanceof StackPane) {
          // As soon as the correct StackPane is found
          if (node.getBoundsInParent().contains(eventX, eventY)) {

            boolean boardSelected = false;
            Tile returnTile = null;
            Tile clickedOnTile = rackTiles.get(rackPanes.indexOf(node));

            // Check if any tile on the board is selected
            for (Tile t : gameBoardTiles) {
              if (t.isSelected()) {
                boardSelected = true;
                returnTile = t;
                break;
              }
            }

            // OPTION 1: Tile on board selected AND tile on rack not placed temporarily
            if (boardSelected && !clickedOnTile.isPlacedTemporarily()) {
              // Exchange both tiles

              // Back end methods
              returnTile.setSelected(false);
              returnTile.setPlacedTemporarily(false);

              // Get the correct square via the coordinates taken in the board selection
              gameSession.recallTile(boardSelectedX, boardSelectedY);

              // Find the corresponding SquarePane and thus the StackPane
              Square boardSquare = gameSession.getBoard().getSquare(boardSelectedX, boardSelectedY);
              StackPane boardStackPane = null;

              for (SquarePane sp : squarePanes) {
                if (sp.getSquare() == boardSquare) {
                  boardStackPane = sp.getStackPane();
                }
              }

              // Reset the opacity of the returned tile on the rack
              int i = rackTiles.indexOf(returnTile);
              Node n = rackPane.getChildren().get(i);
              rackPanes.get(rackPanes.indexOf(n)).setOpacity(1);

              wildcardTile = clickedOnTile;
              wildcardSquare = boardSquare;
              wildcardStackPane = boardStackPane;

              // If the selected tile on the rack is a wildcard, a selection dialog will open
              chooseWildcard(clickedOnTile);

              placeTileOnBoard(clickedOnTile, boardStackPane);

              // Reduce the opacity of the tile on the rack
              rackPanes.get(rackPanes.indexOf(node)).setOpacity(0.5);

              // Set the back end values
              clickedOnTile.setSelected(false);
              clickedOnTile.setPlacedTemporarily(true);
              gameSession.placeTile(
                  boardSelectedX, boardSelectedY, clickedOnTile, rackPanes.indexOf(node));

              gameBoardTiles.add(clickedOnTile);
              gameBoardTiles.remove(returnTile);

              // OPTION 2: The clicked on tile is the selected tile on the board
            } else if (boardSelected && clickedOnTile == returnTile) {
              deselectAll();

              gameBoardTiles.remove(clickedOnTile);

              gameSession.recallTile(boardSelectedX, boardSelectedY);
              clickedOnTile.setPlacedTemporarily(false);

              // Find the corresponding SquarePane and thus the StackPane
              Square boardSquare = gameSession.getBoard().getSquare(boardSelectedX, boardSelectedY);
              StackPane boardStackPane = null;

              for (SquarePane sp : squarePanes) {
                if (sp.getSquare() == boardSquare) {
                  boardStackPane = sp.getStackPane();
                }
              }

              boardStackPane.getChildren().clear();

              // Reset the opacity of the returned tile on the rack
              int i = rackTiles.indexOf(clickedOnTile);
              Node n = rackPane.getChildren().get(i);
              rackPanes.get(rackPanes.indexOf(n)).setOpacity(1);

              // OPTION 3: The clicked on tile is currently selected:
            } else if (clickedOnTile.isSelected()) {
              // Deselect the tile (and all others)
              deselectAll();
              paintAllAsDeselected();

              // OPTION 4: The clicked on tile can be selected:
            } else if (!clickedOnTile.isPlacedTemporarily()) {
              // Deselect all tiles in the back end
              deselectAll();
              // Paint all tiles to the "unselected colour"
              paintAllAsDeselected();
              // Set the clicked on tile as selected in the back end
              rackTiles.get(rackPanes.indexOf(node)).setSelected(true);
              // Paint the tile to the "selected colour"
              StackPane sp = (StackPane) node;
              paintTileAsSelected(sp, true);
            }
          }
        }
      }
      refreshRecall();
      refreshSubmit();
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
   * TODO this methods serves as a Listener for the GridPane, which displays the the GameBoard If a
   * Tile from the Rack is selected it is
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void tileClicked(MouseEvent event) throws Exception {
    if (playable && !swapPaneOpen) {
      // TODO: This method should be renamed, as it is called as soon as any square on the board is
      // clicked
      boolean rackSelected = false;

      // Go through all StackPanes in gameBoard and search for the one on which the user clicked
      for (Node node : gameBoard.getChildren()) {
        if (node instanceof StackPane) {
          if (node.getBoundsInParent().contains(event.getX(), event.getY())) {

            // Get the square from the index
            int index = boardPanes.indexOf(node);
            int clickedOnX = index % 15 + 1;
            int clickedOnY = 15 - index / 15;
            Square square = gameSession.getBoard().getSquare(clickedOnX, clickedOnY);
            boolean react;

            if (square.getTile() != null) {
              react = !square.getTile().getPlacedFinally();
            } else {
              react = true;
            }

            if (react) {

              // Check if any tile on the rack is selected
              for (Tile tile : rackTiles) {
                if (tile.isSelected() && !tile.isPlacedTemporarily()) {
                  // OPTION 1: Yes, the tile on the rack will be placed...

                  tileToPlace = tile;
                  tileToPlaceX = clickedOnX;
                  tileToPlaceY = clickedOnY;

                  // Check if the clicked on square is already taken
                  if (square.isTaken()) {

                    // OPTION 1.1: Exchange both tiles

                    // back end methods
                    Tile returnTile = gameSession.getBoard().getTile(clickedOnX, clickedOnY);
                    returnTile.setSelected(false);
                    returnTile.setPlacedTemporarily(false);
                    gameSession.recallTile(clickedOnX, clickedOnY);

                    // Reset the opacity of the returned tile on the rack
                    int i = rackTiles.indexOf(returnTile);
                    Node n = rackPane.getChildren().get(i);
                    rackPanes.get(rackPanes.indexOf(n)).setOpacity(1);

                    gameBoardTiles.remove(returnTile);
                  }

                  // OPTION 1.2: Simply place the tile on the square

                  wildcardTile = tile;
                  wildcardSquare = square;
                  wildcardStackPane = (StackPane) node;

                  // If the selected tile on the rack is a wildcard, a selection dialog will open
                  chooseWildcard(tile);

                  placeTileOnBoard(tile, (StackPane) node);

                  // Set the back end values
                  tile.setSelected(false);
                  tile.setPlacedTemporarily(true);

                  gameBoardTiles.add(tile);

                  rackSelected = true;
                  break;
                }
              }

              // OPTION 2: No, and therefore a tile on the board is selected or will be selected
              if (!rackSelected) {

                Tile clickedOnTile = square.getTile();

                // Check whether a tile on the board is selected
                boolean boardSelected = false;
                Tile selectedTile = null;
                for (Tile t : gameBoardTiles) {
                  if (t.isSelected()) {
                    boardSelected = true;
                    selectedTile = t;
                    break;
                  }
                }

                // If the square holds a tile
                if (clickedOnTile != null) {

                  // OPTION 2.1: The clicked on tile is selected:
                  if (clickedOnTile.isSelected()) {
                    // deselect it
                    deselectAll();
                    paintAllAsDeselected();

                    // OPTION 2.2: Another tile on the board is selected:
                  } else if (boardSelected) {
                    // Swap the tiles!

                    // recall both tiles in the back end
                    gameSession.recallTile(clickedOnX, clickedOnY); // the clicked on tile
                    gameSession.recallTile(boardSelectedX, boardSelectedY); // the selected tile
                    // place both tiles at their new positions in the back end
                    gameSession.placeTile(
                        boardSelectedX,
                        boardSelectedY,
                        clickedOnTile,
                        rackTiles.indexOf(clickedOnTile));
                    gameSession.placeTile(
                        clickedOnX, clickedOnY, selectedTile, rackTiles.indexOf(selectedTile));

                    // update the GUI:

                    // Find the corresponding SquarePane and thus the StackPane
                    Square selectedSquare =
                        gameSession.getBoard().getSquare(boardSelectedX, boardSelectedY);
                    StackPane selectedStackPane = null;
                    for (SquarePane sp : squarePanes) {
                      if (sp.getSquare() == selectedSquare) {
                        selectedStackPane = sp.getStackPane();
                      }
                    }

                    // place the tiles on the GUI
                    placeTileOnBoard(clickedOnTile, selectedStackPane);
                    placeTileOnBoard(selectedTile, (StackPane) node);

                    // deselect all tiles in the back end
                    deselectAll();
                    paintAllAsDeselected();

                    // OPTION 2.3: Select the clicked on tile:
                  } else {
                    // deselect all tiles & select the tile of the square
                    deselectAll();
                    clickedOnTile.setSelected(true);
                    boardSelectedX = square.getX();
                    boardSelectedY = square.getY();
                    // Mark the selected tile
                    for (SquarePane sp : squarePanes) {
                      if (sp.getSquare() == square) {
                        paintTileAsSelected(sp.getStackPane(), true);
                      }
                    }
                  }
                  // OPTION 2.4: Move the selected tile to an empty location
                } else if (clickedOnTile == null && boardSelected) {
                  gameSession.recallTile(boardSelectedX, boardSelectedY);
                  gameSession.placeTile(
                      clickedOnX, clickedOnY, selectedTile, rackTiles.indexOf(selectedTile));

                  // Find the corresponding SquarePane and thus the StackPane
                  Square selectedSquare =
                      gameSession.getBoard().getSquare(boardSelectedX, boardSelectedY);
                  StackPane selectedStackPane = null;
                  for (SquarePane sp : squarePanes) {
                    if (sp.getSquare() == selectedSquare) {
                      selectedStackPane = sp.getStackPane();
                    }
                  }

                  placeTileOnBoard(selectedTile, (StackPane) node);
                  selectedStackPane.getChildren().clear();

                  deselectAll();
                }
              }
            }
            break;
          }
        }
      }

      // Reset the color of the tile & decrease its opacity
      if (rackSelected) {
        for (Node node : rackPane.getChildren()) {
          if (node instanceof StackPane) {
            if (node.getBoundsInParent().contains(eventX, eventY)) {
              rackPanes.get(rackPanes.indexOf(node)).setOpacity(0.5);
              for (Node n : rackPanes.get(rackPanes.indexOf(node)).getChildren()) {
                if (n instanceof Rectangle) {
                  Rectangle rectangle = (Rectangle) n;
                  rectangle.setFill(Paint.valueOf("#f88c00"));
                }
              }
            }
          }
        }
        // and call the back end method
        gameSession.placeTile(
            tileToPlaceX, tileToPlaceY, tileToPlace, rackTiles.indexOf(tileToPlace));
      }
      refreshRecall();
      refreshSubmit();
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
    if (playerStatisticsScrollPane.isVisible()) {
      playerStatisticsScrollPane.setVisible(false);
    } else {
      playerStatisticsScrollPane.setVisible(true);
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
    playerStatisticsScrollPane.setVisible(false);
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
      unreadMessages = 0;
      chatButton.setText("Chat");
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
    try {
      Client.disconnectClient(DataHandler.getOwnPlayer());
      if (Client.isHost()) {
    	  Server.serverShutdown();
      }
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
    leave();
  }

  /**
   * This method serves as the Listener for "Send"-Button from the chatPane. It allows the user to
   * send a message to the chat to all the other users in the game.
   *
   * @author tikrause
   * @param event
   * @throws Exception
   */
  @FXML
  void sendMessage(ActionEvent event) throws Exception {
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
   * This method serves as the Listener for the Enter-key in the chat text field. It serves as an
   * alternative to the send message button.
   *
   * @param event ActionEvent when enter is pressed in the text field
   * @throws Exception
   * @author jluellig
   */
  @FXML
  void onEnter(ActionEvent event) throws Exception {
    sendMessage(event);
  }

  /**
   * @author tikrause
   * @param p
   * @param chat
   */
  public void receivedMessage(Player p, String chat) {
    if (!chatPane.isVisible()) {
      Platform.runLater(
          new Runnable() {
            @Override
            public void run() {
              chatButton.setText("Chat (" + ++unreadMessages + ")");
            }
          });
    }
    chatHistory.append(p.getUsername() + chat + "\n");
    chatField.setText(chatHistory.toString());
  }

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
    // Reset the opacity of all tiles on the rack
    for (Node node : rackPane.getChildren()) {
      if (node instanceof StackPane) {
        rackPanes.get(rackPanes.indexOf(node)).setOpacity(1);
      }
    }
    // Clear the board from temporarily placed tiles
    for (SquarePane sp : squarePanes) {
      if (sp.getSquare().getTile() != null) {
        if (sp.getSquare().getTile().isPlacedTemporarily()) {
          sp.getStackPane().getChildren().clear();
        }
      }
    }
    // Reset temporarilyPlaced for all tiles on the rack
    for (Tile tile : rackTiles) {
      tile.setPlacedTemporarily(false);
    }
    // Call the back end method
    gameSession.recallAll();
    // Refresh the submit button
    refreshSubmit();

    gameBoardTiles.clear();
    recallButton.setDisable(true);
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
    for (Tile t : gameBoardTiles) {
      t.setPlacedTemporarily(false);
      t.setPlacedFinally(true);
    }
    for (StackPane sp : boardPanes) {
      for (Node node : sp.getChildren()) {
        if (node instanceof Rectangle) {
          ((Rectangle) node).setFill(Paint.valueOf("#cccccc"));
        } else if (node instanceof Text) {
          ((Text) node).setFill(Color.BLACK);
        }
      }
    }
    gameSession.makePlay();
    gameBoardTiles.clear();
    setRack(false);
    refreshSubmit();
    refreshRecall();
    setPlayable(gameSession.getPlayer().isCurrentlyPlaying());
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
  void openSwapPane(ActionEvent event) throws Exception {
    swapPaneOpen = true;

    recallLetters(event);
    deselectAll();
    paintAllAsDeselected();
    swapPane.setVisible(true);
    swapPanes.clear();
    swapRack.setHgap(20);
    swapRack.setAlignment(Pos.CENTER);
    rack.clear();
    letters.clear();
    numbers.clear();

    for (Node node : swapPane.getChildren()) {
      if (node instanceof Button && ((Button) node).getText().equals("Swap")) {
        submitSwapButton = (Button) node;
        submitSwapButton.setDisable(true);
      }
    }

    for (Tile t : rackTiles) {
      // Set a rectangle
      Rectangle rectangle = new Rectangle(22, 22);
      rectangle.setFill(Paint.valueOf("#f88c00"));
      rack.add(rectangle);
      // Set the letter
      Text text = new Text(String.valueOf(t.getLetter()));
      text.setFill(Color.WHITE);
      letters.add(text);
      // Set the value
      Text number = new Text(String.valueOf(t.getValue()));
      number.setFont(new Font(10));
      number.setFill(Color.WHITE);
      numbers.add(number);

      // And add those as children to a StackPane, which is saved in rackPanes
      StackPane stackPane = new StackPane();
      stackPane.getChildren().addAll(rectangle, text, number);
      swapPanes.add(stackPane);
      StackPane.setAlignment(number, Pos.BOTTOM_RIGHT);
      swapRack.getChildren().add(stackPane);
    }
    // this.gameBoard = modifyPane(gameBoard); // Test for GridPane exchange
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
  void submitSwapTiles(ActionEvent event) throws Exception {}

  @FXML
  void closeSwapPane(ActionEvent event) throws Exception {
    swapPaneOpen = false;
    swapPane.setVisible(false);
    swapRack.getChildren().clear();
  }

  @FXML
  void swapTiles(ActionEvent event) throws Exception {
    swapPaneOpen = false;
    gameSession.exchangeTiles(positions);
    swapTiles.clear();
    positions.clear();
    closeSwapPane(event);
    setRack(false);
  }

  @FXML
  void swapPaneClicked(MouseEvent event) {
    // Extract the coordinates from the event
    double eventX = event.getX();
    double eventY = event.getY();
    // Go through all elements of the swapRack
    for (Node node : swapRack.getChildren()) {
      if (node instanceof StackPane) {
        if (node.getBoundsInParent().contains(eventX, eventY)) {
          Tile clickedOnTile = rackTiles.get(swapPanes.indexOf(node));
          if (clickedOnTile.isSelected()) {
            clickedOnTile.setSelected(false);
            positions.remove(swapTiles.indexOf(clickedOnTile));
            swapTiles.remove(clickedOnTile);
            paintTileAsSelected((StackPane) node, false);
          } else {
            clickedOnTile.setSelected(true);
            swapTiles.add(clickedOnTile);
            positions.add(rackTiles.indexOf(clickedOnTile));
            paintTileAsSelected((StackPane) node, true);
          }
        }
      }
    }

    submitSwapButton.setDisable(positions.size() == 0);
  }

  @FXML
  void submitWildcard(ActionEvent event) {
    wildcardPane.setVisible(false);
    wildcardTile.setLetter(wildcardChar);
    placeTileOnBoard(wildcardTile, wildcardStackPane);
    refreshSubmit();
  }

  /**
   * this method serves as a Listener for the close Button in the wildcardPane. It closes the
   * wildCardPane.
   *
   * @author jbleil
   * @param event
   */
  @FXML
  void closeWildcardPane(ActionEvent event) {
    wildcardPane.setVisible(false);
    gameSession.recallTile(wildcardSquare.getX(), wildcardSquare.getY());
    wildcardStackPane.getChildren().clear();

    deselectAll();
    gameBoardTiles.remove(wildcardTile);

    wildcardTile.setPlacedTemporarily(false);
    int i = rackTiles.indexOf(wildcardTile);
    Node n = rackPane.getChildren().get(i);
    rackPanes.get(rackPanes.indexOf(n)).setOpacity(1);

    refreshSubmit();
  }

  /**
   * This method serves as a Listener for the skipTurn Button. It enables the user to skip a turn.
   *
   * @author jbleil
   * @param event
   * @throws Exception
   */
  @FXML
  void skipTurn(ActionEvent event) throws Exception {
    recallLetters(event);
    deselectAll();
    paintAllAsDeselected();
    gameSession.skipTurn();
    setRack(false);
  }

  @FXML
  void endGame(ActionEvent event) {
    gameSession.endGame();
  }

  // All following methods are functions used multiple times in the methods above

  private void deselectAll() {
    for (Tile t : rackTiles) {
      t.setSelected(false);
    }
    for (Tile t : gameBoardTiles) {
      t.setSelected(false);
    }
  }

  private void refreshSubmit() {
    if (!wildcardPane.isVisible()) {
      if (gameSession.checkMove()) {
        submitButton.setDisable(false);
        submitButton.setText("Submit +" + gameSession.getTurnValue());
      } else {
        submitButton.setDisable(true);
        submitButton.setText("Submit");
      }
    }
  }

  private void placeTileOnBoard(Tile tile, StackPane sp) {
    sp.getChildren().clear();

    // Set the text of the newly placed tile
    Text text = new Text(String.valueOf(tile.getLetter()));
    if (tile.isWildCard()) {
      text.setFill(Paint.valueOf("dad3f2"));
    } else {
      text.setFill(Color.WHITE);
    }

    // Set the number of the newly placed tile
    Text number = new Text(String.valueOf(tile.getValue()));
    number.setFont(new Font(10));
    if (tile.isWildCard()) {
      number.setFill(Paint.valueOf("dad3f2"));
    } else {
      number.setFill(Color.WHITE);
    }

    StackPane.setAlignment(number, Pos.BOTTOM_RIGHT);
    // Add those to the corresponding StackPane
    sp.getChildren().add(new Rectangle(21, 21, Paint.valueOf("#f88c00")));
    sp.getChildren().add(text);
    sp.getChildren().add(number);
  }

  private void paintTileAsSelected(StackPane sp, boolean selected) {
    for (Node n : sp.getChildren()) {
      if (n instanceof Rectangle) {
        Rectangle rectangle = (Rectangle) n;
        if (selected) {
          rectangle.setFill(Paint.valueOf("#f8d200"));
        } else {
          rectangle.setFill(Paint.valueOf("#f88c00"));
        }
      }
    }
  }

  private void paintAllAsDeselected() {
    for (StackPane sp : rackPanes) {
      for (Node n : sp.getChildren()) {
        if (n instanceof Rectangle) {
          Rectangle rectangle = (Rectangle) n;
          rectangle.setFill(Paint.valueOf("#f88c00"));
        }
      }
    }
    for (SquarePane sp : squarePanes) {
      if (sp.getSquare().getTile() != null) {
        if (!sp.getSquare().getTile().getPlacedFinally()) {
          for (Node n : sp.getStackPane().getChildren()) {
            if (n instanceof Rectangle) {
              Rectangle rectangle = (Rectangle) n;
              rectangle.setFill(Paint.valueOf("#f88c00"));
            }
          }
        }
      }
    }
  }

  private void refreshRecall() {
    if (gameBoardTiles.size() > 0) {
      recallButton.setDisable(false);
    } else {
      recallButton.setDisable(true);
    }
  }

  private void refreshBagCount() {
    bagButton.setText("BAG\n " + this.gameSession.getBag().getRemainingCount());
  }

  public void refreshTimerText(String text, int seconds) {
    timerText.setText(text);
    if (seconds <= 30) {
      timerText.setFill(Color.RED);
    } else {
      timerText.setFill(Color.BLACK);
    }
  }

  public void setPlayable(boolean ownTurn) {
    if (!ownTurn) {
      playable = false;
      submitButton.setVisible(false);
      recallButton.setVisible(false);
      openSwapButton.setVisible(false);
      skipTurnButton.setVisible(false);
      currentlyPlaying.setVisible(true);

      endGame.setVisible(false);
    } else {
      playable = true;
      submitButton.setVisible(true);
      recallButton.setVisible(true);
      openSwapButton.setVisible(true);
      skipTurnButton.setVisible(true);
      currentlyPlaying.setVisible(false);

      if (gameSession.getSuccessiveScorelessTurns() >= 6) {
        endGame.setVisible(true);
      } else {
        endGame.setVisible(false);
      }
    }
  }

  private void chooseWildcard(Tile clickedTile) {
    if (clickedTile.isWildCard()) {
      wildcardTextField.setText("");
      wildcardPane.setVisible(true);
      wildcardSubmit.setDisable(true);

      wildcardTextField
          .textProperty()
          .addListener(
              new ChangeListener<String>() {
                @Override
                public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue,
                    String newValue) {
                  if (newValue.matches("[a-zA-Z]")) {
                    wildcardSubmit.setDisable(false);
                    newValue = newValue.toUpperCase();
                    wildcardChar = newValue.charAt(0);
                  } else {
                    wildcardSubmit.setDisable(true);
                  }
                }
              });
    }
  }

  public void loadPlacedTiles() {

    squarePanes.clear();

    ArrayList<Object> toRemove = new ArrayList<Object>();
    for (Node node : gameBoard.getChildren()) {
      if (node instanceof StackPane) {
        toRemove.add(node);
      }
    }
    gameBoard.getChildren().removeAll(toRemove);

    for (int j = 0; j <= 14; j++) {
      for (int i = 0; i <= 14; i++) {
        SquarePane sp = new SquarePane(gameSession.getBoard().getSquare(i + 1, 15 - j));
        squarePanes.add(sp);
        gameBoard.add(sp.getStackPane(), i, j);
      }
    }
    // Fill the StackPanes of squarePanes into boardPanes
    boardPanes.clear();
    for (SquarePane sp : squarePanes) {
      boardPanes.add(sp.getStackPane());
    }

    StackPane stackPane = null;

    for (Square square : gameSession.getBoard().getSquareList()) {
      if (square.getTile() != null) {
        for (SquarePane sp : squarePanes) {
          if (sp.getSquare().getX() == square.getX() && sp.getSquare().getY() == square.getY()) {
            stackPane = sp.getStackPane();
            break;
          }
        }
        placeTileOnBoard(square.getTile(), stackPane);
      }
    }

    for (StackPane sp : boardPanes) {
      for (Node node : sp.getChildren()) {
        if (node instanceof Rectangle) {
          ((Rectangle) node).setFill(Paint.valueOf("#cccccc"));
        } else if (node instanceof Text) {
          ((Text) node).setFill(Color.BLACK);
        }
      }
    }

    refreshBagCount();
  }

  public void leave() {
    StartScreen.getStage();
    FXMLLoader loader = new FXMLLoader();
    Parent content;
    try {
      content = loader.load(getClass().getClassLoader().getResourceAsStream("screens/resources/StartScreen.fxml"));
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setPlayerStatistics() {
    ArrayList<Player> players = Client.getGameSession().getPlayerList();

    for (int i = 0; i < players.size(); i++) {
      Text name = new Text(players.get(i).getUsername() + " :\n");
      name.setFont(new Font(20));
      name.setFill(Paint.valueOf("#f88c00"));
      playerStatisticsPane.getChildren().add(name);
      if (!players.get(i).isAI()) {
        HashMap<StatisticKeys, Integer> map = players.get(i).getPlayerStatistics();
        Text statistics =
            new Text(
                "Games won: "
                    + map.get(StatisticKeys.WON)
                    + "\nGames played: "
                    + map.get(StatisticKeys.MATCHES)
                    + "\nAverage Points: "
                    + map.get(StatisticKeys.POINTSAVG)
                    + "\n");
        statistics.setFont(new Font(10));
        playerStatisticsPane.getChildren().add(statistics);
      }
    }
  }

  /** @author tikrause */
  public void tooFewPlayerAlert() {
    Alert errorAlert = new Alert(AlertType.ERROR);
    errorAlert.setHeaderText("Too few players.");
    errorAlert.setContentText(
        "You can't play any longer because you are the only player left in the game session.");
    errorAlert.showAndWait();
    leave();
  }

  /** @author tikrause */
  public void hostHasLeft() {
    Alert errorAlert = new Alert(AlertType.ERROR);
    errorAlert.setHeaderText("The host has left.");
    errorAlert.setContentText(
        "The host has left the game and therefore you have been disconnected from the server.");
    errorAlert.showAndWait();
    leave();
  }

  /**
   * @author tikrause
   * @param chat
   */
  public void takeOverChat(String chat) {
    chatHistory = new StringBuffer(chat);
    chatField.setText(chatHistory.toString());
  }
}
