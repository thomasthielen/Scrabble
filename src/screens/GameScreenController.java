package screens;

import data.DataHandler;
import data.StatisticKeys;
import gameentities.Player;
import gameentities.Rack;
import gameentities.Square;
import gameentities.SquarePane;
import gameentities.Tile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.Client;
import network.Server;
import session.GameSession;

/**
 * This class provides the Controller for the Game Screen and handles all the interaction with the
 * Players during the game.
 *
 * @author tthielen
 * @author lsteltma
 * @author jbleil
 * @author tikrause
 * @author jluellig
 */
public class GameScreenController {

  /**
   * gameBoard represents the Container for the Game Board, the tiles and the (for the game flow
   * necessary) buttons.
   */
  @FXML private GridPane gameBoard;

  /** chatPane represents the Container for the Chat. */
  @FXML private Pane chatPane;

  /** playerStatisticsPane represents the Container for the Player Statistics. */
  @FXML private ScrollPane playerStatisticsScrollPane;

  @FXML private Pane playerStatisticsPane;

  @FXML private Pane backgroundPane;

  @FXML private Pane tutorialPane;

  /** rackPane represents the Container for the Tiles in the Rack. */
  @FXML private FlowPane rackPane;

  /** swapPane represents the Container for the swap feature. */
  @FXML private Pane swapPane;

  /** swapRack lies in the swapPane and holds the player's tiles. */
  @FXML private FlowPane swapRack;

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

  private GridPane previousPlayerList;

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

  private ArrayList<String> tutorialTexts = new ArrayList<String>();
  private Text tutorialText = new Text();
  private ArrayList<Boolean> notYetShown = new ArrayList<Boolean>();

  /**
   * initializes the GameScreen.
   *
   * @author jbleil
   * @author tthielen
   */
  public void initialize() throws Exception {

    gameSession = Client.getGameSession();
    gameSession.setGameScreenController(this);

    // Disable all buttons which require a move to be made
    recallButton.setDisable(true);
    submitButton.setDisable(true);
    // Enable the swap button
    openSwapButton.setDisable(false);
    // Set the endGame button to invisible
    endGame.setVisible(false);

    tutorialPane.setVisible(false);

    // Fill the gameBoard with SquarePanes which are also held in squarePanes
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
    chatField.setFocusTraversable(false);
    chatField.setWrapText(true);

    int humanPlayer = 0;
    for (Player p : gameSession.getPlayerList()) {
      if (!p.isBot()) {
        humanPlayer++;
      }
    }
    chatButton.setVisible(humanPlayer > 1);

    setPlayerStatistics();
    initializeCloseHandler();
    refreshPlayerNames();

    ImageView starView =
        new ImageView(
            new Image(
                getClass().getClassLoader().getResourceAsStream("screens/resources/Stern.png")));
    starView.setFitHeight(22);
    starView.setFitWidth(22);
    gameBoard.add(starView, 7, 7);

    // Tutorial

    tutorialTexts.add(
        "It's your turn! "
            + "\nSelect a tile from your rack and place it "
            + "\non the board."
            + "\nTry to create a valid word through the star.");
    tutorialTexts.add(
        "You can always return the tiles which you"
            + "\nplaced on the board during your turn."
            + "\nTo do that simply select one of your tiles"
            + "\non the board and return it to its place "
            + "\non the rack."
            + "\nOr press the 'Recall' button to return"
            + "\nall tiles at once.");
    tutorialTexts.add(
        "You placed a valid word!"
            + "\nThe 'Submit' button displays how much"
            + "\npoints submitting this move would net."
            + "\nIf you are content with the amount, simply"
            + "\npress the button.");
    tutorialTexts.add(
        "You selected a wildcard tile."
            + "\nThose can be played as any tile you like,"
            + "\nbut will (usually) net you no points."
            + "\nAfter placing the tile on the board"
            + "\nyou will be asked to enter a letter."
            + "\nIf you choose to return the wildcard tile"
            + "\nto your rack, the chosen letter will be"
            + "\nresetted.");

    for (int i = 0; i < tutorialTexts.size(); i++) {
      notYetShown.add(true);
    }

    tutorialText.relocate(5, 5);
    tutorialText.setFill(Paint.valueOf("#f88c00"));

    if (Client.isTutorial()) {
      tutorialPane.setVisible(true);
      tutorialPane.getChildren().add(tutorialText);
      updateTutorialText(0);
    }
  }

  /**
   * Sets the rack according to the rack in the back end.
   *
   * @author tthielen
   * @param isFirstTime whether it is the first time setting the rack
   */
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
      gameSession.sendGameStateMessage(false);
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
   * Handles the player clicking on the rack. There are 4 different scenarios which have to be
   * considered.
   *
   * @author tthielen
   * @author jbleil
   * @param event MouseEvent that is triggered when the rack is clicked
   */
  @FXML
  void rackClicked(MouseEvent event) {
    // Only react in any way if it is the player's turn
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

              int position = rackTiles.indexOf(returnTile);

              // Get the correct square via the coordinates taken in the board selection
              gameSession.recallTile(boardSelectedX, boardSelectedY, position);

              // Find the corresponding SquarePane and thus the StackPane
              Square boardSquare = gameSession.getBoard().getSquare(boardSelectedX, boardSelectedY);
              StackPane boardStackPane = null;

              for (SquarePane sp : squarePanes) {
                if (sp.getSquare() == boardSquare) {
                  boardStackPane = sp.getStackPane();
                }
              }

              // Reset the opacity of the returned tile on the rack
              Node n = rackPane.getChildren().get(position);
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
                  boardSelectedX, boardSelectedY, clickedOnTile, rackTiles.indexOf(clickedOnTile));

              gameBoardTiles.add(clickedOnTile);
              gameBoardTiles.remove(returnTile);

              // OPTION 2: The clicked on tile is the selected tile on the board
            } else if (boardSelected && clickedOnTile == returnTile) {
              deselectAll();

              gameBoardTiles.remove(clickedOnTile);
              int position = rackTiles.indexOf(clickedOnTile);

              gameSession.recallTile(boardSelectedX, boardSelectedY, position);
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
              Node n = rackPane.getChildren().get(position);
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

            if (clickedOnTile.isWildCard()) {
              updateTutorialText(3);
            }
          }
        }
      }
      refreshRecall();
      refreshSubmit();
    }
  }

  /**
   * Serves as the Listener for the submit wildcard button.
   *
   * @author tthielen
   * @author jbleil
   * @param event the ActionEvent called by the button
   */
  @FXML
  void submitWildcard(ActionEvent event) {
    wildcardPane.setVisible(false);
    wildcardTile.setLetter(wildcardChar);
    placeTileOnBoard(wildcardTile, wildcardStackPane);
    refreshSubmit();
  }

  // onEnterWildcard
  // closeWildcardPane

  /**
   * Serves as a Listener for the GridPane, which displays the GameBoard. There are 6 different
   * scenarios which have to be considered.
   *
   * @author tthielen
   * @author lsteltma
   * @author jbleil
   * @param event MouseEvent that is triggered when the board is clicked
   */
  @FXML
  void boardClicked(MouseEvent event) throws Exception {
    if (playable && !swapPaneOpen) {
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

                    Tile returnTile = gameSession.getBoard().getTile(clickedOnX, clickedOnY);
                    returnTile.setSelected(false);
                    returnTile.setPlacedTemporarily(false);
                    int position = rackTiles.indexOf(returnTile);
                    gameSession.recallTile(clickedOnX, clickedOnY, position);

                    // Reset the opacity of the returned tile on the rack
                    Node n = rackPane.getChildren().get(position);
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

                    // recall both tiles in the back ends
                    gameSession.recallTile(
                        clickedOnX,
                        clickedOnY,
                        rackTiles.indexOf(clickedOnTile)); // the clicked on tile
                    gameSession.recallTile(
                        boardSelectedX,
                        boardSelectedY,
                        rackTiles.indexOf(selectedTile)); // the selected tile
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
                  gameSession.recallTile(
                      boardSelectedX, boardSelectedY, rackTiles.indexOf(selectedTile));
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

    if (Client.isTutorial()) {
      updateTutorialText(1);
    }
  }

  /**
   * Serves as the Listener for "Player Statistics"-Button. If the statistics are opened it closes
   * them and if they are closed it opens them.
   *
   * @author jbleil
   * @param event the ActionEvent called by the button
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
   * Serves as the Listener for "X"-Button from the playerStatisticsPane. It closes the player
   * statistics.
   *
   * @author jbleil
   * @param event the ActionEvent called by the button
   */
  @FXML
  void closePlayerStatistics(ActionEvent event) throws Exception {
    playerStatisticsScrollPane.setVisible(false);
  }

  /**
   * Serves as the Listener for "CHAT"-Button. If the chat is open, it closes it and if it's closed,
   * it opens it.
   *
   * @author jbleil
   * @param event the ActionEvent called by the button
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
   * Restores the chat from the lobby screen into the game screen when the game has started.
   *
   * @author tikrause
   * @param chat the string of chat from the lobby
   */
  public void takeOverChat(String chat) {
    chatHistory = new StringBuffer(chat);
    chatField.setText(chatHistory.toString());
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
      errorAlert.setTitle("Error");
      errorAlert.setHeaderText("Message is empty.");
      errorAlert.setContentText("The chat message has to contain at least one character.");
      errorAlert.showAndWait();
    } else {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setTitle("Error");
      errorAlert.setHeaderText("Message too long.");
      errorAlert.setContentText("The maximum length of a chat message is 140 characters.");
      errorAlert.showAndWait();
    }
  }

  // onEnter

  /**
   * When a text message is received by another player in the game session, the chat pane is updated
   * and the received message is shown.
   *
   * @author tikrause
   * @param p player that has sent the message
   * @param chat message that has been received
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
   * Serves as the Listener for "X"-Button from the chatPane. It closes the chat.
   *
   * @author jbleil
   * @param event the ActionEvent called by the button
   */
  @FXML
  void closeChat(ActionEvent event) throws Exception {
    chatPane.setVisible(false);
  }

  /**
   * Serves as the Listener for the "Leave Game"-Button. It asks the user whether he wants to leave
   * the game and removes him after his confirmation.
   *
   * @author tikrause
   * @param event user clicks the 'LEAVE GAME'-Button
   */
  @FXML
  void leaveGame(ActionEvent event) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation dialog");
    alert.setHeaderText("You are trying to leave the game.");
    alert.setContentText(
        "When you leave the game, you get disconnected from the server "
            + "and can't join this session anymore.");
    Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
    cancelButton.setText("Cancel");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      try {
        if (Client.isHost()) {
          Server.resetPlayerLists();
        } else if (Client.getGameSession().getPlayer().isCurrentlyPlaying()) {
          Client.getGameSession().nextPlayer();
        }
        Client.disconnectClient(DataHandler.getOwnPlayer());
        if (Client.isHost()) {
          Server.shutdown();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      leave();
    } else {
      event.consume();
    }
  }

  /**
   * Removes the player from the game and informs him if he exceeded the 10 minute limit.
   *
   * @author tikrause
   */
  public void leaveGameCall() {
    try {
      Client.disconnectClient(DataHandler.getOwnPlayer());
      if (Client.isHost()) {
        Server.shutdown();
      }
      leave();
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Information");
      alert.setHeaderText("You have been kicked from the game.");
      alert.setContentText(
          "Due to exceeding the ten-minute overtime limit, you have been removed from the game.");
      alert.showAndWait();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Resets the window handler and brings the user back to the OnlineOrOfflineScrren.
   *
   * @author tikrause
   */
  public void leave() {
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
   * Serves as the Listener for the Enter-key in the chat text field. It serves as an alternative to
   * the send message button.
   *
   * @param event ActionEvent when enter is pressed in the text field
   * @author jluellig
   */
  @FXML
  void onEnter(ActionEvent event) throws Exception {
    sendMessage(event);
  }

  // openSwapPane

  /**
   * Serves as the Listener for the swap pane. Used to select or deselect the tiles in the rack.
   *
   * @author tthielen
   * @author jbleil
   * @param event the ActionEvent called by the button
   */
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

  /**
   * Serves as the Listener for the submit button in the popUp-Window which opens when you click the
   * "Swap"-Button from the gameBoardPane.
   *
   * @author tthielen
   * @author jbleil
   * @param event the ActionEvent called by the button
   */
  @FXML
  void swapTiles(ActionEvent event) throws Exception {
    swapPaneOpen = false;
    gameSession.exchangeTiles(positions);
    swapTiles.clear();
    positions.clear();
    closeSwapPane(event);
    setRack(false);
  }

  // closeSwapPane

  /**
   * Serves as the Listener for "Submit"-Button from the gameBoardPane. The Button gets enabled if
   * the user lays a valid word on the gameBoard. If the user presses the Button the laid word is
   * submitted and it's the next players turn.
   *
   * @author tthielen
   * @author jbleil
   * @param event the ActionEvent called by the button
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
   * Serves as the Listener for the "Recall"-Button from the gameBoardPane. It allows the user to
   * call back the tiles he has laid on the gameBoard in the current move.
   *
   * @author tthielen
   * @author jbleil
   * @param event the ActionEvent called by the button
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
    gameSession.recallAll(false);
    // Refresh the submit button
    refreshSubmit();

    gameBoardTiles.clear();
    recallButton.setDisable(true);
  }

  /**
   * Serves as the Listener for "Swap"-Button from the gameBoardPane. Opens a popUp-window in which
   * the user can swap one or more tiles out of his rack with new tiles from the bag.
   *
   * @author jbleil
   * @author tthielen
   * @param event the ActionEvent called by the button
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
  }

  /**
   * Serves as the Listener for "Close"-Button from the swap window. Closes the window.
   *
   * @author jbleil
   * @param event the ActionEvent called by the button
   */
  @FXML
  void closeSwapPane(ActionEvent event) throws Exception {
    swapPaneOpen = false;
    swapPane.setVisible(false);
    swapRack.getChildren().clear();
  }

  /**
   * Serves as the Listener for the Enter-key in the wildcard text field. It serves as an
   * alternative to the submit wildcard button.
   *
   * @param event the ActionEvent when enter is pressed in the wildcard text field
   * @author jluellig
   */
  @FXML
  void onEnterWildcard(ActionEvent event) {
    submitWildcard(event);
  }

  /**
   * Serves as a Listener for the close Button in the wildcardPane. It closes the wildCardPane.
   *
   * @author jbleil
   * @param event the ActionEvent called by the button
   */
  @FXML
  void closeWildcardPane(ActionEvent event) {
    int position = rackTiles.indexOf(wildcardTile);

    wildcardPane.setVisible(false);
    gameSession.recallTile(wildcardSquare.getX(), wildcardSquare.getY(), position);
    wildcardStackPane.getChildren().clear();

    deselectAll();
    gameBoardTiles.remove(wildcardTile);

    wildcardTile.setPlacedTemporarily(false);
    Node n = rackPane.getChildren().get(position);
    rackPanes.get(rackPanes.indexOf(n)).setOpacity(1);

    refreshSubmit();
  }

  /**
   * Serves as a Listener for the skipTurn Button. It instantly skips the turn of the user.
   *
   * @author jbleil
   * @param event the ActionEvent called by the button
   */
  @FXML
  void skipTurn(ActionEvent event) throws Exception {
    recallLetters(event);
    deselectAll();
    paintAllAsDeselected();
    gameSession.skipTurn();
    setRack(false);
  }

  /**
   * Serves as a Listener for the end game button. Ends the game via condition 2.
   *
   * @author jbleil
   * @param event the ActionEvent called by the button
   */
  @FXML
  void endGame(ActionEvent event) throws Exception {
    Client.reportEndGame(DataHandler.getOwnPlayer());
    switchToEndScreen();
  }

  /**
   * Calls a switch to the end screen.
   *
   * @author jbleil
   */
  public void switchToEndScreen() {
    gameSession.endGame();
    DataHandler.getOwnPlayer().refreshStatistics();
    FXMLLoader loader = new FXMLLoader();
    Parent content;
    try {
      content =
          loader.load(
              getClass().getClassLoader().getResourceAsStream("screens/resources/EndScreen.fxml"));
      StartScreen.getStage().setScene(new Scene(content));
      StartScreen.getStage().show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Deselects all tiles on the board and rack within the back end.
   *
   * @author tthielen
   */
  private void deselectAll() {
    for (Tile t : rackTiles) {
      t.setSelected(false);
    }
    for (Tile t : gameBoardTiles) {
      t.setSelected(false);
    }
  }

  /**
   * Refreshes the submit button according to the checkMove() method. Also displays the value of the
   * turn if a legal move was placed.
   *
   * @author tthielen
   */
  private void refreshSubmit() {
    if (!wildcardPane.isVisible()) {
      if (gameSession.checkMove()) {
        submitButton.setDisable(false);
        submitButton.setText("Submit +" + gameSession.getTurnValue());
        updateTutorialText(2);
      } else {
        submitButton.setDisable(true);
        submitButton.setText("Submit");
      }
    }
  }

  /**
   * Places a given tile on the board via its corresponding StackPane.
   *
   * @author tthielen
   * @param tile the tile which is meant to be placed
   * @param sp the StackPane on which the tile is meant to be placed
   */
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

  /**
   * Paints a tile as selected or not via its StackPane.
   *
   * @author tthielen
   * @param sp the StackPane which needs to be modified.
   * @param selected whether the tile is meant to be painted as selected or not.
   */
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

  /**
   * Paints all tiles as deselected, parallel to the back end method.
   *
   * @author tthielen
   */
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

  /**
   * Refreshes the recall button according to whether any tile is currently placed on the board.
   *
   * @author tthielen
   */
  private void refreshRecall() {
    if (gameBoardTiles.size() > 0) {
      recallButton.setDisable(false);
    } else {
      recallButton.setDisable(true);
    }
  }

  /**
   * Refreshes the count of the remaining tiles on the bag button.
   *
   * @author tthielen
   */
  private void refreshBagCount() {
    bagButton.setText("BAG\n " + this.gameSession.getBag().getRemainingCount());
  }

  /**
   * Refreshes the timer text according to the given text and seconds.
   *
   * @author tthielen
   * @param text the timer text already formatted to mm:ss
   * @param seconds the remaining seconds, used to recolor the text <31 seconds
   */
  public void refreshTimerText(String text, int seconds) {
    timerText.setText(text);
    if (seconds <= 30) {
      timerText.setFill(Color.RED);
    } else {
      timerText.setFill(Color.BLACK);
    }
  }

  /**
   * Sets the visibility of multiple buttons according to whether it is the player's turn or not.
   *
   * @author tthielen
   * @param ownTurn whether it is the player's turn or not
   */
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

      openSwapButton.setDisable(gameSession.getBag().getRemainingCount() < 7);
    }
  }

  /**
   * Opens the wildcardPane if the given clicked tile is a wildcard. Also checks the input by the
   * player with a regex and enables or disables the wildcardSubmit button accordingly.
   *
   * @author tthielen
   * @param clickedTile the tile on the rack which the player clicked on
   */
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
      if (Client.isTutorial()) {
        updateTutorialText(3);
      }
    }
  }

  /**
   * Refreshes the game board with the placed tiles of the other players.
   *
   * @author tthielen
   */
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

  /**
   * Sets the player statistics which are displayed when the statistics button is pressed.
   *
   * @author jbleil
   * @author jluellig
   */
  public void setPlayerStatistics() {
    ArrayList<Player> players = Client.getGameSession().getPlayerList();
    playerStatisticsScrollPane.setFitToWidth(true);
    for (int i = 0; i < players.size(); i++) {
      Text name = new Text(players.get(i).getUsername() + " :\n");
      name.setFont(new Font(16));
      name.setFill(Paint.valueOf("#707070"));
      name.relocate(10, 45 + 75 * i);
      playerStatisticsPane.getChildren().add(name);
      if (!players.get(i).isBot()) {
        if (i > 1) {
          playerStatisticsPane.setPrefHeight(playerStatisticsPane.getPrefHeight() + (i - 1) * 60);
        }
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
        statistics.setFont(new Font(12));
        statistics.setFill(Paint.valueOf("#707070"));
        statistics.relocate(10, 65 + 75 * i);
        playerStatisticsPane.getChildren().add(statistics);
      }
    }
  }

  /**
   * Refreshes the player names in the upper left corner according to the current scores and the
   * currently playing player.
   *
   * @author jbleil
   */
  public void refreshPlayerNames() {
    for (Node node : backgroundPane.getChildren()) {
      if (node instanceof GridPane) {
        GridPane gp = (GridPane) node;
        if (gp == previousPlayerList) {
          gp.getChildren().clear();
        }
      }
    }

    ArrayList<Player> players = Client.getGameSession().getPlayerList();
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    for (int i = 0; i < players.size(); i++) {
      grid.add(
          new ImageView(
              new Image(
                  getClass()
                      .getClassLoader()
                      .getResourceAsStream(players.get(i).getAvatar().getUrl()),
                  45,
                  45,
                  true,
                  true)),
          0,
          i);
      Text name = new Text(players.get(i).getUsername());
      name.setFill(Paint.valueOf("#f88c00"));
      grid.add(name, 1, i);
      Text points = new Text("-  " + players.get(i).getScore());
      points.setFill(Paint.valueOf("#f88c00"));
      grid.add(points, 2, i);
      if (players.get(i).isCurrentlyPlaying()) {
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        points.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
      } else {
        name.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 10));
        points.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 10));
      }
    }
    grid.relocate(10, 14);
    previousPlayerList = grid;
    backgroundPane.getChildren().add(grid);
  }

  /**
   * Informs the player that all other human players except him have left the game and he can't
   * continue playing.
   *
   * @author tikrause
   */
  public void tooFewPlayerAlert() {
    Alert errorAlert = new Alert(AlertType.ERROR);
    errorAlert.setTitle("Error");
    errorAlert.setHeaderText("Too few players.");
    errorAlert.setContentText(
        "You can't play any longer because you are the only player left in the game session.");
    errorAlert.showAndWait();
    leave();
  }

  /**
   * Informs the player that the host has left the game and he can't continue playing.
   *
   * @author tikrause
   */
  public void hostHasLeft() {
    Alert errorAlert = new Alert(AlertType.ERROR);
    errorAlert.setTitle("Error");
    errorAlert.setHeaderText("The host has left.");
    errorAlert.setContentText(
        "The host has left the game and therefore you have been disconnected from the server.");
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
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation dialog");
                alert.setHeaderText("You are trying to leave the game.");
                alert.setContentText(
                    "When you leave the game, you get disconnected from the server and can't "
                        + "join this session anymore.");
                Button cancelButton =
                    (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
                cancelButton.setText("Cancel");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                  try {
                    if (Client.isHost()) {
                      Server.resetPlayerLists();
                    } else if (Client.getGameSession().getPlayer().isCurrentlyPlaying()) {
                      Client.getGameSession().nextPlayer();
                    }
                    Client.disconnectClient(DataHandler.getOwnPlayer());
                    if (Client.isHost()) {
                      Server.shutdown();
                    }
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  Platform.exit();
                  System.exit(0);
                } else {
                  event.consume();
                }
              }
            });
  }

  /**
   * Returns the saved rackTiles of this turn.
   *
   * @author tthielen
   * @return rackTiles
   */
  public ArrayList<Tile> getRackTiles() {
    return this.rackTiles;
  }

  /**
   * Serves as a Listener for the Text "Soni Sokell" and displays an instance of SiteLinkScreen.
   *
   * @author jbleil
   * @param event the MouseEvent that gets thrown when clicking on the "Soni Sokell" Text
   */
  @FXML
  void openNameScreen(MouseEvent event) {
    NameLinkScreen nls = new NameLinkScreen();
    Stage stage = new Stage();
    nls.start(stage);
  }

  /**
   * Serves as a Listener for the Text "freeicons.io" and displays an instance of SiteLinkScreen.
   *
   * @author jbleil
   * @param event the MouseEvent that gets thrown when clicking on the "freeicons.io" Text
   */
  @FXML
  void openSiteScreen(MouseEvent event) {
    SiteLinkScreen sls = new SiteLinkScreen();
    Stage stage = new Stage();
    sls.start(stage);
  }

  /**
   * Updates the tutorial text according to the given tutorialNumber.
   *
   * @author tthielen
   * @param tutorialNumber the number of the tutorial text which is meant to be displayed
   */
  private void updateTutorialText(int tutorialNumber) {
    if (notYetShown.get(tutorialNumber) && Client.isTutorial()) {
      tutorialText.setText(tutorialTexts.get(tutorialNumber));
      tutorialPane.setPrefHeight(10 + tutorialText.getLayoutBounds().getHeight());
      notYetShown.set(tutorialNumber, false);
    }
  }
}
