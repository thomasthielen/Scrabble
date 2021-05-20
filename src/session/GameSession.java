package session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import data.DataHandler;
import gameentities.*;
import javafx.application.Platform;
import network.Client;
import network.Server;
import screens.GameScreenController;
import screens.LobbyScreenController;

/**
 * Class used to manage all game entities and necessary methods.
 *
 * @author tthielen
 */
public class GameSession {

  // Objects which are synchronised between the players
  private ArrayList<Player> players; // Holds all participating players
  private Bag bag; // The bag of the game
  private Board board; // The board of the game

  private boolean isActive = false; // Indicates whether the game is live

  private Player ownPlayer; // The Player of this GameSession object

  private ArrayList<Square> placedSquares = new ArrayList<Square>(); // List of temporary tiles

  private GameScreenController gameScreenController;
  private LobbyScreenController lobbyScreenController;

  private Timer timer;

  private static final int RESET = 600;
  private int seconds = RESET;

  private boolean isRunning = false;
  private boolean gscInitialized = false;
  private boolean multiPlayer;

  private int successiveScorelessTurns = 0;

  private int turnValue = 0; // Value of the current turn (including bonuses of premium squares)

  // Counters for word value (used in checkMove() & scoreSquare() )
  private int dwsCount = 0;
  private int twsCount = 0;

  /**
   * Constructor: Creates a GameSession object and creates the player-list, the bag and the board.
   *
   * @author tthielen
   */
  public GameSession(Player p, boolean multiPlayer) {
    players = new ArrayList<Player>();
    bag = new Bag();
    board = new Board();
    ownPlayer = p;
    ownPlayer.createRack(this);
    this.multiPlayer = multiPlayer;

    if (multiPlayer) {
      ownPlayer.setCurrentlyPlaying(Client.isHost());
      GameState overrideGameState = new GameState(this);
      Client.updateGameState(ownPlayer, overrideGameState);
    } else {
      ownPlayer.setCurrentlyPlaying(true);
    }

    initialise();
  }

  /**
   * Initialises the GameSession-Timers
   *
   * @author tthielen
   */
  public void initialise() {
    timer = new Timer();
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            if (isRunning) {
              seconds--;
              if (seconds <= 0) {
                kickPlayer();
              }
              // System.out.println(printTime());
            }
          }
        },
        1000,
        1000);
  }

  public void initialiseGameScreen() {
    Platform.runLater(
        new Runnable() {
          @Override
          public void run() {
            if (multiPlayer) {
              gameScreenController.setPlayable(Client.isHost());
            } else {
              gameScreenController.setPlayable(true);
            }
          }
        });
  }

  /**
   * Synchronises the GameState objects between players
   *
   * @author tikrause
   */
  public void synchronise(GameState overrideGameState) {
    // If the own turn is over: Create a GameState object and send it
    this.seconds = RESET;
    this.players = overrideGameState.getPlayers();
    for (Player p : this.players) {
      if (p.equals(ownPlayer)) {
        ownPlayer = p;
      }
    }
    if (!overrideGameState.isPlayersOnly() && gscInitialized) {
      setPlayable();
    }
    if (!overrideGameState.isPlayersOnly() && overrideGameState.getBag() != null) {
      this.bag = overrideGameState.getBag();
      this.ownPlayer.getRack().synchroniseBag(this);
      this.board = overrideGameState.getBoard();
      this.successiveScorelessTurns = overrideGameState.getSuccessiveScorelessTurns();
      if (gameScreenController != null) {
        Platform.runLater(
            new Runnable() {
              @Override
              public void run() {
                gameScreenController.loadPlacedTiles();
              }
            });
      }
    }
  }

  public void sendGameStateMessage() {
    GameState overrideGameState = new GameState(this);
    Client.updateGameState(ownPlayer, overrideGameState);
    gameScreenController.setPlayable(ownPlayer.isCurrentlyPlaying());
  }

  public void setGameScreenController(GameScreenController gsc) {
    this.gameScreenController = gsc;
    gscInitialized = true;
    setPlayable();
  }

  public void setLobbyScreenController(LobbyScreenController lsc) {
    this.lobbyScreenController = lsc;
  }
  // PLAYER TURN OPTIONS:

  // 1: Skip turn

  /**
   * Skips the turn of the player by switching to the next player.
   *
   * @author tthielen
   */
  public void skipTurn() {
    successiveScorelessTurns++;
    nextPlayer();
  }

  // 2: Swap tiles

  /**
   * Swaps the selected tiles for new ones by calling the exchangeTiles() function of the
   * currentPlayer.
   *
   * @author tthielen
   * @param swapTiles
   */
  public void exchangeTiles(ArrayList<Integer> positions) {
    ownPlayer.exchangeTiles(positions);
    successiveScorelessTurns++;
    nextPlayer();
  }

  // 3: Make a play

  /**
   * Temporarily places the given tile on the square corresponding to the given coordinates. Removes
   * the tile from the rack of the player. Additionally, it saves the square in placedSquares, which
   * is used to test if a legal moved is being played.
   *
   * @author tthielen
   * @param posX
   * @param posY
   * @param tile
   */
  public void placeTile(int posX, int posY, Tile tile, int tilePosition) {
    board.placeTile(posX, posY, tile);
    ownPlayer.playTile(tilePosition);
    placedSquares.add(board.getSquare(posX, posY));
  }

  public void placeTile(int posX, int posY, Tile tile) {
    board.placeTile(posX, posY, tile);
    ownPlayer.playTileAI(tile);
    placedSquares.add(board.getSquare(posX, posY));
  }

  /**
   * Returns the temporarily placed tile from the board and places it back to the rack of the
   * player.
   *
   * @author tthielen
   * @param posX
   * @param posY
   */
  public void recallTile(int posX, int posY) {

    // Take the tile from the respective square
    Tile rTile = board.recallTile(posX, posY);
    // Reset the letter of the tile, should it have been a wildcard tile
    rTile.resetLetter();
    // Return the tile to the rack of the player
    ownPlayer.returnTile(rTile);
    // Remove the respective square from the list of placedSquares
    placedSquares.remove(board.getSquare(posX, posY));
  }

  /**
   * Recalls ALL placed tiles to the rack.
   *
   * @author tthielen
   */
  public void recallAll() {
    ArrayList<Integer> posXCollection = new ArrayList<Integer>();
    ArrayList<Integer> posYCollection = new ArrayList<Integer>();

    for (Square s : placedSquares) {
      posXCollection.add(s.getX());
      posYCollection.add(s.getY());
    }

    int iterations = placedSquares.size();
    for (int i = 0; i < iterations; i++) {
      recallTile(posXCollection.get(i), posYCollection.get(i));
    }
  }

  /**
   * Checks whether the current move (represented by occupiedSquares) is a legal move. If it isn't,
   * it returns false, otherwise it returns true and calculates the value of the move.
   *
   * @author tthielen
   * @return legalMove
   */
  public boolean checkMove() {

    // Important: In default, even a single placed tile means the main word is in a row
    // Thus, the (possibly) resulting row word is checked as well as the resulting column word
    boolean column = false;

    turnValue = 0;

    // Checks whether a tile has already been placed
    if (placedSquares.size() == 0) {
      return false;
    }

    // Checks whether any tile of the placedSquares is adjacent to the a previously
    // played square OR, if it is the first move, at least one tile is placed on the
    // STAR-square
    boolean firstMove = true;
    for (Square s : board.getSquareList()) {
      if (s.isPreviouslyPlayed()) {
        firstMove = false;
        break;
      }
    }

    if (firstMove) {
      boolean starHit = false;
      for (Square s : placedSquares) {
        if (s.getPremium() == Premium.STAR) {
          starHit = true;
          break;
        }
      }
      if (!starHit) {
        return false;
      }
    } else {
      boolean adjacent = false;
      for (Square s : placedSquares) {
        if (board.hasPreviouslyPlayedNeighbour(s)) {
          adjacent = true;
          break;
        }
      }
      if (!adjacent) {
        return false;
      }
    }

    if (firstMove && placedSquares.size() < 2) {
      return false;
    }

    // Checks whether the placed tiles all are either in a row or a column.
    // Returns false if neither is the case.
    if (placedSquares.size() > 1) {

      int posX = placedSquares.get(0).getX();
      int posY = placedSquares.get(0).getY();

      if (posX == placedSquares.get(1).getX()) {
        column = true;
        for (Square s : placedSquares) {
          if (s.getX() != posX) {
            return false;
          }
        }
      } else if (posY == placedSquares.get(1).getY()) {
        column = false;
        for (Square s : placedSquares) {
          if (s.getY() != posY) {
            return false;
          }
        }
      } else {
        return false;
      }
    }

    // Checks whether the placed tiles are coherent (including already placed tiles)
    // & CHECKS the MAIN word and calculates its VALUE
    // TODO: Simplify the main word check into one single statement

    if (column) { // COLUMN

      // Search for lowest square on which a tile was placed
      Square lowestSquare = placedSquares.get(0);
      for (int i = 1; i < placedSquares.size(); i++) {
        if (placedSquares.get(i).getY() < lowestSquare.getY()) {
          lowestSquare = placedSquares.get(i);
        }
      }

      // Search for highest square on which a tile was placed
      Square highestSquare = placedSquares.get(0);
      for (int i = 1; i < placedSquares.size(); i++) {
        if (placedSquares.get(i).getY() > highestSquare.getY()) {
          highestSquare = placedSquares.get(i);
        }
      }

      // Start on the highest square or a square with a previous tile above
      Square iterator = highestSquare;
      while (board.getUpperNeighbour(iterator).isTaken()) {
        iterator = board.getUpperNeighbour(iterator);
      }

      StringBuffer sb = new StringBuffer();
      int wordValue = 0;
      dwsCount = 0;
      twsCount = 0;

      // Go down to the lowest square with a tile
      do {
        sb.append(iterator.getTile().getLetter());

        // Score the Tile
        // Premium Squares apply only when newly placed tiles cover them
        wordValue += scoreSquare(iterator);
        if (iterator.getY() > 1) {
          iterator = board.getLowerNeighbour(iterator);
        } else {
          break;
        }
      } while (iterator.isTaken());

      for (int i = 0; i < dwsCount; i++) {
        wordValue = 2 * wordValue;
      }
      for (int i = 0; i < twsCount; i++) {
        wordValue = 3 * wordValue;
      }

      turnValue += wordValue;

      // If the lowest tile before the first gap is not on the same height or lower
      // than the lowest, newly placed tile, return false
      if (iterator.getY() > lowestSquare.getY()) {
        return false;
      }

      String newWord = sb.toString();
      System.out.println("Main word: " + newWord + " " + data.DataHandler.checkWord(newWord));
      if (sb.length() > 1) {
        if (!data.DataHandler.checkWord(newWord)) {
          return false;
        }
      }

    } else { // ROW

      // Search for rightmost square on which a tile was placed
      Square rightmostSquare = placedSquares.get(0);
      for (int i = 1; i < placedSquares.size(); i++) {
        if (placedSquares.get(i).getX() > rightmostSquare.getX()) {
          rightmostSquare = placedSquares.get(i);
        }
      }

      // Search for leftmost square on which a tile was placed
      Square leftmostSquare = placedSquares.get(0);
      for (int i = 1; i < placedSquares.size(); i++) {
        if (placedSquares.get(i).getX() < leftmostSquare.getX()) {
          leftmostSquare = placedSquares.get(i);
        }
      }

      // Start on the leftmost square or a square with a previous tile to the left
      Square iterator = leftmostSquare;
      while (board.getLeftNeighbour(iterator).isTaken()) {
        iterator = board.getLeftNeighbour(iterator);
      }

      // (!) Used to test if the main word needs to be checked when only one tile was placed
      Square leftOld = iterator;

      StringBuffer sb = new StringBuffer();
      int wordValue = 0;
      dwsCount = 0;
      twsCount = 0;

      // Go to the right to the rightmost square with a tile
      do {
        sb.append(iterator.getTile().getLetter());

        // Score the Tile
        // Premium Squares apply only when newly placed tiles cover them
        wordValue += scoreSquare(iterator);
        if (iterator.getX() < 15) {
          iterator = board.getRightNeighbour(iterator);
        } else {
          break;
        }
      } while (iterator.isTaken());

      for (int i = 0; i < dwsCount; i++) {
        wordValue = 2 * wordValue;
      }
      for (int i = 0; i < twsCount; i++) {
        wordValue = 3 * wordValue;
      }

      // (!) Used to test if the main word needs to be checked when only one tile was placed
      Square rightOld = board.getLeftNeighbour(iterator);

      // (!) Test occurs here:
      if (leftOld != rightOld) {

        turnValue += wordValue;

        // If the lowest tile before the first gap is not on the same height or lower
        // than the lowest, newly placed tile, return false
        if (iterator.getX() < rightmostSquare.getX()) {
          return false;
        }

        String newWord = sb.toString();
        System.out.println("Main word: " + newWord + " " + data.DataHandler.checkWord(newWord));
        if (sb.length() > 1) {
          if (!data.DataHandler.checkWord(newWord)) {
            return false;
          }
        }
      }
    }

    // CHECKS all SECONDARY words formed for correctness & calculates their VALUE

    for (Square s : placedSquares) {

      // Check if the placedSquare has a neighbour
      // If(!column): On the y-axis
      // If(column): On the x-axis

      if (board.hasPreviouslyPlayedNeighbour(s, !column)) {

        StringBuffer sb = new StringBuffer();
        int wordValue = 0;
        dwsCount = 0;
        twsCount = 0;

        Square iterator = s;

        // If(!column): Get the top taken square
        // If(column): Get the leftmost taken square
        while (board.getPreviousNeighbour(iterator, !column).isTaken()) {
          iterator = board.getPreviousNeighbour(iterator, !column);
        }

        // If(!column): From top to bottom
        // If(column): From left to right
        while (board.getNextNeighbour(iterator, !column).isTaken()) {

          // Append the char of the tile to the StringBuffer
          sb.append(iterator.getTile().getLetter());

          // Score the Tile
          // Premium Squares apply only when newly placed tiles cover them
          wordValue += scoreSquare(iterator);

          iterator = board.getNextNeighbour(iterator, !column);
        }

        do {
          sb.append(iterator.getTile().getLetter());

          // Score the Tile
          // Premium Squares apply only when newly placed tiles cover them
          wordValue += scoreSquare(iterator);

          // Go to the next square, while the border is not reached yet
          if (column) {
            if (iterator.getX() < 15) {
              iterator = board.getNextNeighbour(iterator, !column);
            } else {
              break;
            }
          } else {
            if (iterator.getY() > 1) {
              iterator = board.getNextNeighbour(iterator, !column);
            } else {
              break;
            }
          }

        } while (iterator.isTaken());

        for (int i = 0; i < dwsCount; i++) {
          wordValue = 2 * wordValue;
        }
        for (int i = 0; i < twsCount; i++) {
          wordValue = 3 * wordValue;
        }

        turnValue += wordValue;

        String newWord = sb.toString();
        System.out.println(
            "Secondary word: " + newWord + " " + data.DataHandler.checkWord(newWord));
        if (!data.DataHandler.checkWord(newWord)) {
          return false;
        }
      }
    }

    // Bingo / Bonus
    if (placedSquares.size() == 7) {
      turnValue += 50;
    }

    return true; // If no condition is violated, return true
  }

  /**
   * Scores the tile according to the square (i.e. according to premium squares).
   *
   * @author tthielen
   * @param iterator
   * @return score
   */
  private int scoreSquare(Square iterator) {
    if (!iterator.isPreviouslyPlayed()) {
      switch (iterator.getPremium()) {
        case DLS:
          return 2 * iterator.getTile().getValue();
        case TLS:
          return 3 * iterator.getTile().getValue();
        case DWS:
          dwsCount++;
          return iterator.getTile().getValue();
        case TWS:
          twsCount++;
          return iterator.getTile().getValue();
        case STAR: // First play is doubled in value
          dwsCount++;
          return iterator.getTile().getValue();
        case NONE:
          return iterator.getTile().getValue();
        default:
          return 0;
      }
    } else {
      return iterator.getTile().getValue();
    }
  }

  /**
   * Can be called if the current move is legal. Saves the value of the move in the score, draws
   * back to 7 tiles, empties values, synchronises GameState and calls nextPlayer().
   *
   * @author tthielen
   */
  public void makePlay() {
    ownPlayer.incScore(turnValue);
    ownPlayer.refillRack();
    if (checkEndCondition()) {
      endGame();
    }

    for (Square s : placedSquares) {
      s.setPreviouslyPlayed();
    }
    placedSquares.removeAll(placedSquares);

    successiveScorelessTurns = 0;
    nextPlayer();
  }

  // OTHER METHODS:

  /**
   * Sets currentlyPlaying of the player of the last turn to false. Afterwards it sets
   * currentlyPlaying of the next player (according to the order of the ArrayList) to true.
   *
   * @author tthielen
   */
  public void nextPlayer() {
    seconds = RESET;
    turnValue = 0;
    for (Player p : players) {
      if (p.isCurrentlyPlaying()) {
        p.setCurrentlyPlaying(false);

        int index = players.indexOf(p);
        if (index == players.size() - 1) {
          players.get(0).setCurrentlyPlaying(true);
        } else {
          players.get(index + 1).setCurrentlyPlaying(true);
        }
        break;
      }
    }
    if (multiPlayer) {
      sendGameStateMessage();
    }
    Server.updateAI(new GameState(this));
    for (Player p : players) {
      if (p.isCurrentlyPlaying()) {
        Player playing = p;
        if (playing.isAI()) {
          // TODO AI makes move
        }
      }
    }
  }

  /**
   * Returns the bag of the session. Used to draw tiles from the bag in the class Rack.
   *
   * @author tthielen
   * @return bag
   */
  public Bag getBag() {
    return bag;
  }

  /**
   * Returns the board of the session.
   *
   * @author tthielen
   * @return board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Returns the ownPlayer of the session.
   *
   * @author tthielen
   * @return ownPlayer
   */
  public Player getPlayer() {
    return ownPlayer;
  }

  /**
   * Returns the ownPlayer of the session.
   *
   * @author tthielen
   * @return ownPlayer
   */
  public ArrayList<Player> getPlayerList() {
    return players;
  }

  /**
   * @author tikrause
   * @return
   */
  public boolean getMultiPlayer() {
    return multiPlayer;
  }

  /**
   * Returns the value of this turn.
   *
   * @author tthielen
   * @return turnValue
   */
  public int getTurnValue() {
    return turnValue;
  }

  public int getSuccessiveScorelessTurns() {
    return successiveScorelessTurns;
  }

  public void setIsRunning(boolean running) {
    this.isRunning = running;
  }
  /**
   * Changes the isActive state of the GameSession.
   *
   * @author tthielen
   * @param isActive
   */
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * Returns whether the GameSession is active.
   *
   * @author tthielen
   * @return isActive
   */
  public boolean isActive() {
    return this.isActive;
  }

  private boolean checkEndCondition() {
    return ownPlayer.getRack().getTiles().isEmpty() && bag.isEmpty();
  }

  /**
   * Method for what happens when the game successfully ends.
   *
   * @author jluellig
   */
  public void endGame() {
    System.out.println("endGame called");
    Collections.sort(players);
    boolean hasWon = (players.get(players.size() - 1).equals(ownPlayer));
    DataHandler.addStatistics(DataHandler.getOwnPlayerId(), hasWon, ownPlayer.getScore());
    // TODO end game screen (statistiken des aktuellen spiels, ranking, end game, new game)
  }

  private void kickPlayer() {
    if (ownPlayer.isCurrentlyPlaying()) {
      nextPlayer();
      Platform.runLater(
          new Runnable() {
            @Override
            public void run() {
              gameScreenController.leave();
            }
          });
      timer.cancel();
    }
  }

  public String printTime() {
    int minutes = this.seconds / 60;
    int seconds = this.seconds % 60;
    String minutesText, secondsText;
    if (minutes == 10) {
      minutesText = "" + minutes;
    } else {
      minutesText = "0" + minutes;
    }
    if (seconds < 10) {
      secondsText = "0" + seconds;
    } else {
      secondsText = "" + seconds;
    }
    return minutesText + ":" + secondsText;
  }

  public void switchToGameScreen() {
    Platform.runLater(
        new Runnable() {
          @Override
          public void run() {
            lobbyScreenController.switchToGameScreen();
          }
        });
  }

  public void setPlayable() {
    Platform.runLater(
        new Runnable() {
          @Override
          public void run() {
            gameScreenController.setPlayable(ownPlayer.isCurrentlyPlaying());
          }
        });
  }

  public GameScreenController getGameScreenController() {
    return this.gameScreenController;
  }

  public LobbyScreenController getLobbyScreenController() {
    return this.lobbyScreenController;
  }
}
