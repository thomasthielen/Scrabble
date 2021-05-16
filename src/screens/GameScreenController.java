package screens;

import java.io.File;
import java.util.ArrayList;

import data.DataHandler;
import gameentities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import network.Client;
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
  @FXML private ScrollPane playerStatisticsPane;

  /** rackPane represents the Container for the Tiles in the Rack */
  @FXML private FlowPane rackPane;

  /** swapRack */
  @FXML private FlowPane swapRack;

  /** swapPane has */
  @FXML private Pane swapPane;

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

  private ArrayList<SquarePane> squarePanes = new ArrayList<SquarePane>();

  private double eventX = 0;

  private double eventY = 0;

  private Button submitButton;
  private Button recallButton;
  private Button swapButton;
  
  private Text currentlyPlaying = new Text();

  private int boardSelectedX = 0;
  private int boardSelectedY = 0;

  /**
   * TODO - set Chat and Player Statistic visibility = false - set 7 Tiles in the Rack - fill the
   * board with Rectangles -> opacity = 100 (see through) - fill gridPane with Rectangles
   *
   * @author jbleil
   * @throws Exception
   */
  public void initialize() throws Exception {

    // Load the dictionary TODO: Outsource this! Especially to be able to choose the dictionary
    DataHandler.userDictionaryFile(new File("resources/Collins Scrabble Words (2019).txt"));
    
    gameSession = Client.getGameSession();
    gameSession.setGameScreenController(this);

    // Initialise the submit button and disable it
    for (Node node : gameBoardPane.getChildren()) {
      if (node instanceof Button) {
        Button button = (Button) node;
        if (button.getText().contains("Submit")) {
          submitButton = button;
          submitButton.setDisable(true);
        } else if (button.getText().equals("Recall")) {
          recallButton = button;
          recallButton.setDisable(true);
        } else if (button.getText().equals("Swap")) {
          swapButton = button;
          swapButton.setDisable(false);
        }
      }
    }

    // Fill the gameBoard with SquarePanes which are also held in squarePanes (!)
    System.out.println(gameBoard.getColumnConstraints());
    for (int j = 0; j <= 14; j++) {
      for (int i = 0; i <= 14; i++) {
        SquarePane sp = new SquarePane(gameSession.getBoard().getSquare(i + 1, 15 - j));
        squarePanes.add(sp);
        gameBoard.add(sp.getStackPane(), i, j);
      }
    }
    // Fill the StackPanes of squarePanes into boardPanes
    for (SquarePane sp : squarePanes) {
      boardPanes.add(sp.getStackPane());
    }

    // Set the openable windows to invisible
    chatPane.setVisible(false);
    playerStatisticsPane.setVisible(false);
    swapPane.setVisible(false);

    setRack(true);
  }

  public void setRack(boolean isFirstTime) {
    rack.clear();
    letters.clear();
    numbers.clear();
    rackPanes.clear();
    rackPane.getChildren().clear();
    rackTiles.clear();
    
    for (Player p : gameSession.getPlayerList()) {
      System.out.println(p.isCurrentlyPlaying() + " Username:" + p.getUsername()); 
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

            // Call placeTileOnBoard
            placeTileOnBoard(clickedOnTile, boardStackPane);

            // Reduce the opacity of the tile on the rack
            rackPanes.get(rackPanes.indexOf(node)).setOpacity(0.5);

            // Set the back end values
            clickedOnTile.setSelected(false);
            clickedOnTile.setPlacedTemporarily(true);
            gameSession.placeTile(boardSelectedX, boardSelectedY, clickedOnTile);

            gameBoardTiles.add(clickedOnTile);
            gameBoardTiles.remove(returnTile);

            // OPTION 2: The clicked on tile is the selected tile on the board
          } else if (boardSelected && clickedOnTile == returnTile) {
            deselectAll();
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
                  gameSession.placeTile(boardSelectedX, boardSelectedY, clickedOnTile);
                  gameSession.placeTile(clickedOnX, clickedOnY, selectedTile);

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
                gameSession.placeTile(clickedOnX, clickedOnY, selectedTile);

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
      gameSession.placeTile(tileToPlaceX, tileToPlaceY, tileToPlace);
    }
    refreshRecall();
    refreshSubmit();
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
    swapPane.setVisible(false);
    swapRack.getChildren().clear();
  }

  @FXML
  void swapTiles(ActionEvent event) throws Exception {
    gameSession.exchangeTiles(swapTiles, positions);
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
    if (gameSession.checkMove()) {
      submitButton.setDisable(false);
      submitButton.setText("Submit +" + gameSession.getTurnValue());
    } else {
      submitButton.setDisable(true);
      submitButton.setText("Submit");
    }
  }

  private void placeTileOnBoard(Tile tile, StackPane sp) {
    sp.getChildren().clear();
    // Set the text of the newly placed tile
    Text text = new Text(String.valueOf(tile.getLetter()));
    text.setFill(Color.WHITE);
    // Set the number of the newly placed tile
    Text number = new Text(String.valueOf(tile.getValue()));
    number.setFont(new Font(10));
    number.setFill(Color.WHITE);
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
  
  public void setPlayable(boolean ownTurn) {
    if (!ownTurn) {
      submitButton.setVisible(false);
      recallButton.setVisible(false);
      swapButton.setVisible(false);
      currentlyPlaying.setVisible(true);
    } else {
      submitButton.setVisible(true);
      recallButton.setVisible(true);
      swapButton.setVisible(true);
      currentlyPlaying.setVisible(false);
    }
  }

  // Test method for GridPane exchange
  public GridPane modifyPane(GridPane pane) {
    for (Node node : pane.getChildren()) {
      if (node instanceof StackPane) {
        ((StackPane) node).getChildren().clear();
      }
    }
    return pane;
  }
}
