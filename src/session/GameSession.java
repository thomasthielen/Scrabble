package session;

import data.DataHandler;
import gameentities.Bag;
import gameentities.Board;
import gameentities.Player;
import gameentities.Premium;
import gameentities.Square;
import gameentities.Tile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import network.Client;
import network.Server;
import screens.EndScreenController;
import screens.GameScreenController;
import screens.LobbyScreenController;
import screens.SinglePlayerLobbyScreenController;

/**
 * Class used to manage all game entities and necessary methods.
 *
 * @author tthielen
 * @author tikrause
 */
public class GameSession {

  // Objects which are synchronized between the players
  private ArrayList<Player> players; // Holds all participating players
  private Bag bag; // The bag of the game
  private Board board; // The board of the game

  private Player ownPlayer; // The Player of this GameSession object

  private ArrayList<Square> occupiedSquares =
      new ArrayList<Square>(); // List of temporarily placed tiles

  // Controllers extracted from the GUI handling
  private GameScreenController gameScreenController;
  private LobbyScreenController lobbyScreenController;
  private SinglePlayerLobbyScreenController splsc;
  private EndScreenController endScreenController;

  // The round timer and its values
  private Timer timer;
  private static final int RESET = 600;
  private int seconds = RESET;

  // booleans to indicate the game status
  private boolean isRunning = false; // game is running (contrary to still in lobby)

  // Counters for word value (used in checkMove() & scoreSquare() )
  private int dwsCount = 0; // amount of double word squares in the move
  private int twsCount = 0; // amount of triple word squares in the move

  private int turnValue = 0; // Value of the current turn (including bonuses of premium squares)

  private int successiveScorelessTurns = 0; // Amount of scoreless turns, used in end game handling

  /**
   * Creates a GameSession object by creating the player-list, the bag and the board.
   *
   * @author tthielen
   * @param player the own player object of this GameSession object
   */
  public GameSession(Player player) {
    players = new ArrayList<Player>();
    bag = new Bag();
    board = new Board();
    ownPlayer = player;
    ownPlayer.createRack(this);

    ownPlayer.setCurrentlyPlaying(Client.isHost() && !player.isBot());
    sendGameStateMessage(true);
  }

  /**
   * Initializes the GameSession-timer.
   *
   * @author tthielen
   */
  public void startTimer() {
    timer = new Timer();
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            // As long as the game is active:
            if (isRunning) {
              // Count down towards 0
              seconds--;
              // Kick the currently playing player at 0
              if (seconds == 0) {
                kickPlayer();
              }
              // Refresh the timer in the GUI
              if (gameScreenController != null) {
                gameScreenController.refreshTimerText(printTime(), seconds);
              }
            }
          }
        },
        1000,
        1000);
  }

  /**
   * Cancels the GameSession-timer.
   *
   * @author tthielen
   */
  public void cancelTimer() {
    if (timer != null) {
      timer.cancel();
    }
  }

  /**
   * Resets the GameSession-timer.
   *
   * @author tthielen
   */
  private void resetTimer() {
    this.seconds = RESET;
  }

  /**
   * Returns the seconds of the timer formatted in a mm:ss format.
   *
   * @author tthielen
   * @return the seconds formatted correctly
   */
  private String printTime() {
    int minutes = this.seconds / 60;
    int seconds = this.seconds % 60;
    String minutesText;
    String secondsText;
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

  /**
   * Initializes the GameScreen by calling setPlayable according to the player being host or not and
   * transferring the chat of the lobby.
   *
   * @author tikrause
   * @param chat all previously written messages in chat
   */
  public void initializeGameScreen(String chat) {
    Platform.runLater(
        new Runnable() {
          @Override
          public void run() {
            gameScreenController.setPlayable(Client.isHost());
            gameScreenController.takeOverChat(chat);
          }
        });
  }

  /**
   * Initializes the GameScreen by calling setPlayable according to the player being host or not.
   *
   * @author tikrause
   */
  public void initializeSinglePlayerGameScreen() {
    Platform.runLater(
        new Runnable() {
          @Override
          public void run() {
            gameScreenController.setPlayable(true);
          }
        });
  }

  /**
   * Synchronizes by overwriting values with the data held by the given GameState object.
   *
   * @author tthielen
   * @author tikrause
   * @param overrideGameState the GameState which contains the data to synchronize
   */
  public void synchronize(GameState overrideGameState) {
    // Don't synchronize if the game is running AND the received message is the first
    if (!overrideGameState.isConnectGameState() || !isRunning) {
      this.seconds = RESET;
      this.players = overrideGameState.getPlayers();

      for (Player p : this.players) {
        if (p.equals(ownPlayer)) {
          ownPlayer = p;
        }
      }

      if (!overrideGameState.isPlayersOnly() && gameScreenController != null) {
        setPlayable();
      }

      if (!overrideGameState.isPlayersOnly() && overrideGameState.getBag() != null) {
        setBag(overrideGameState.getBag());
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
          Platform.runLater(
              new Runnable() {
                @Override
                public void run() {
                  gameScreenController.refreshPlayerNames();
                }
              });
        }
      }

      if (!ownPlayer.isBot() && lobbyScreenController != null) {
        lobbyScreenController.refreshPlayerList();
        resetTimer();
      } else if (!ownPlayer.isBot() && splsc != null) {
        splsc.refreshPlayerList();
      }
    }
  }

  /**
   * Sends a GameState message to all other players by creating a GameState object with the local
   * variables.
   *
   * @author tthielen
   * @param connectMessage indicates whether this is the first message
   */
  public void sendGameStateMessage(boolean connectMessage) {
    GameState overrideGameState = new GameState(this, connectMessage);
    Client.sendGameState(overrideGameState);
    if (gameScreenController != null) {
      gameScreenController.setPlayable(ownPlayer.isCurrentlyPlaying());
    }
    resetTimer();
  }

  // PLAYER TURN OPTIONS:

  // 1: Skip the turn

  /**
   * Skips the turn of the player by switching to the next player. Also increases
   * successiveScorelessTurns as no points were earned.
   *
   * @author tthielen
   */
  public void skipTurn() {
    successiveScorelessTurns++;
    if (!ownPlayer.isBot() || Server.getPlayerList().size() > 1) {
      nextPlayer();
    }
  }

  // 2: Swap tiles

  /**
   * Swaps the selected tiles for new ones by calling the exchangeTiles() function of the
   * currentPlayer. Also increases successiveScorelessTurns as no points were earned.
   *
   * @author tthielen
   * @param positions the positions on the rack of the tiles which are meant to be placed.
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
   * @param posX the position on the x-axis
   * @param posY the position on the y-axis
   * @param tile the tile which is meant to be placed
   * @param tilePosition the position of the tile on the rack
   */
  public void placeTile(int posX, int posY, Tile tile, int tilePosition) {
    board.placeTile(posX, posY, tile);
    ownPlayer.playTile(tilePosition);
    occupiedSquares.add(board.getSquare(posX, posY));
  }

  /**
   * Temporarily places the given tile on the square corresponding to the given coordinates. Removes
   * the tile from the rack of the player. Additionally, it saves the square in placedSquares, which
   * is used to test if a legal moved is being played.
   *
   * @author tthielen
   * @param posX the position on the x-axis
   * @param posY the position on the y-axis
   * @param tile the tile which is meant to be placed
   */
  public void placeTile(int posX, int posY, Tile tile) {
    board.placeTile(posX, posY, tile);
    ownPlayer.playTileBot(tile);
    occupiedSquares.add(board.getSquare(posX, posY));
  }

  /**
   * Recalls the temporarily placed tile from the board and places it back on the rack of the
   * player.
   *
   * @author tthielen
   * @param posX the position on the x-axis
   * @param posY the position on the y-axis
   */
  public void recallTile(int posX, int posY) {
    // Take the tile from the respective square
    Tile recallTile = board.recallTile(posX, posY);
    // Reset the letter of the tile, should it have been a wildcard tile
    recallTile.resetLetter();
    // Return the tile to the rack of the player
    ownPlayer.returnTile(recallTile);
    // Remove the respective square from the list of placedSquares
    occupiedSquares.remove(board.getSquare(posX, posY));
  }

  /**
   * Recalls all placed tiles to the rack.
   *
   * @author tthielen
   */
  public void recallAll() {
    ArrayList<Integer> posXcollection = new ArrayList<Integer>();
    ArrayList<Integer> posYcollection = new ArrayList<Integer>();

    for (Square s : occupiedSquares) {
      posXcollection.add(s.getX());
      posYcollection.add(s.getY());
    }

    int iterations = occupiedSquares.size();
    for (int i = 0; i < iterations; i++) {
      recallTile(posXcollection.get(i), posYcollection.get(i));
    }
  }

  /**
   * Checks whether the current move (represented by occupiedSquares) is a legal move. If it isn't,
   * it returns false, otherwise it returns true and calculates the value of the move.
   *
   * @author tthielen
   * @return boolean indicating whether a legal move was placed
   */
  public boolean checkMove() {

    // Important: In default, even a single placed tile means the main word is in a row
    // Thus, the (possibly) resulting row word is checked as well as the resulting column word
    boolean column = false;

    turnValue = 0;

    // Checks whether a tile has already been placed
    if (occupiedSquares.size() == 0) {
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
      for (Square s : occupiedSquares) {
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
      for (Square s : occupiedSquares) {
        if (board.hasPreviouslyPlayedNeighbour(s)) {
          adjacent = true;
          break;
        }
      }
      if (!adjacent) {
        return false;
      }
    }

    if (firstMove && occupiedSquares.size() < 2) {
      return false;
    }

    // Checks whether the placed tiles all are either in a row or a column.
    // Returns false if neither is the case.
    if (occupiedSquares.size() > 1) {

      int posX = occupiedSquares.get(0).getX();
      int posY = occupiedSquares.get(0).getY();

      if (posX == occupiedSquares.get(1).getX()) {
        column = true;
        for (Square s : occupiedSquares) {
          if (s.getX() != posX) {
            return false;
          }
        }
      } else if (posY == occupiedSquares.get(1).getY()) {
        column = false;
        for (Square s : occupiedSquares) {
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

    if (column) { // COLUMN

      // Search for lowest square on which a tile was placed
      Square lowestSquare = occupiedSquares.get(0);
      for (int i = 1; i < occupiedSquares.size(); i++) {
        if (occupiedSquares.get(i).getY() < lowestSquare.getY()) {
          lowestSquare = occupiedSquares.get(i);
        }
      }

      // Search for highest square on which a tile was placed
      Square highestSquare = occupiedSquares.get(0);
      for (int i = 1; i < occupiedSquares.size(); i++) {
        if (occupiedSquares.get(i).getY() > highestSquare.getY()) {
          highestSquare = occupiedSquares.get(i);
        }
      }

      // Start on the highest square or a square with a previous tile above
      Square iterator = highestSquare;
      if (board.getUpperNeighbour(iterator) != null) {
        while (board.getUpperNeighbour(iterator).isTaken()) {
          iterator = board.getUpperNeighbour(iterator);
          if (board.getUpperNeighbour(iterator) == null) {
            break;
          }
        }
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
      if (sb.length() > 1) {
        if (!data.DataHandler.checkWord(newWord)) {
          return false;
        }
      }

    } else { // ROW

      // Search for rightmost square on which a tile was placed
      Square rightmostSquare = occupiedSquares.get(0);
      for (int i = 1; i < occupiedSquares.size(); i++) {
        if (occupiedSquares.get(i).getX() > rightmostSquare.getX()) {
          rightmostSquare = occupiedSquares.get(i);
        }
      }

      // Search for leftmost square on which a tile was placed
      Square leftmostSquare = occupiedSquares.get(0);
      for (int i = 1; i < occupiedSquares.size(); i++) {
        if (occupiedSquares.get(i).getX() < leftmostSquare.getX()) {
          leftmostSquare = occupiedSquares.get(i);
        }
      }

      // Start on the leftmost square or a square with a previous tile to the left
      Square iterator = leftmostSquare;
      if (board.getLeftNeighbour(iterator) != null) {
        while (board.getLeftNeighbour(iterator).isTaken()) {
          iterator = board.getLeftNeighbour(iterator);
          if (board.getLeftNeighbour(iterator) == null) {
            break;
          }
        }
      }

      StringBuffer sb = new StringBuffer();
      int wordValue = 0;
      dwsCount = 0;
      twsCount = 0;

      // (!) Used to test if the main word needs to be checked when only one tile was placed
      final Square leftOld = iterator;

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
        if (sb.length() > 1) {
          if (!data.DataHandler.checkWord(newWord)) {
            return false;
          }
        }
      }
    }

    // CHECKS all SECONDARY words formed for correctness & calculates their VALUE

    for (Square s : occupiedSquares) {

      // Check if the placedSquare has a neighbor
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
        if (board.getPreviousNeighbour(iterator, !column) != null) {
          while (board.getPreviousNeighbour(iterator, !column).isTaken()) {
            iterator = board.getPreviousNeighbour(iterator, !column);
            if (board.getPreviousNeighbour(iterator, !column) == null) {
              break;
            }
          }
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

          if (board.getNextNeighbour(iterator, !column) == null) {
            break;
          }
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
        if (sb.length() > 1) {
          if (!data.DataHandler.checkWord(newWord)) {
            return false;
          }
        }
      }
    }

    // "Bingo" / Bonus
    if (occupiedSquares.size() == 7) {
      turnValue += 50;
    }

    // Finally: If not a single condition was violated, return true
    return true;
  }

  /**
   * Scores the tile according to the square (i.e. according to premium squares).
   *
   * @author tthielen
   * @param square the square on which we want to calculate the value
   * @return the value of the tile on this specific square
   */
  private int scoreSquare(Square square) {
    if (!square.isPreviouslyPlayed()) {
      switch (square.getPremium()) {
        case DLS:
          return 2 * square.getTile().getValue();
        case TLS:
          return 3 * square.getTile().getValue();
        case DWS:
          dwsCount++;
          return square.getTile().getValue();
        case TWS:
          twsCount++;
          return square.getTile().getValue();
        case STAR:
          // First play is doubled in value as per rule book
          dwsCount++;
          return square.getTile().getValue();
        case NONE:
          return square.getTile().getValue();
        default:
          return 0;
      }
    } else {
      return square.getTile().getValue();
    }
  }

  /**
   * Can be called if the current move is legal. Saves the value of the move in the score, draws
   * back to 7 tiles, empties values and calls nextPlayer().
   *
   * @author tthielen
   */
  public void makePlay() {
    ownPlayer.incScore(turnValue);
    ownPlayer.refillRack();
    if (checkEndCondition()) {
      endGame();
    }

    for (Square s : occupiedSquares) {
      s.setPreviouslyPlayed();
    }
    occupiedSquares.removeAll(occupiedSquares);

    successiveScorelessTurns = 0;
    if (!ownPlayer.isBot() || Server.getPlayerList().size() > 0) {
      nextPlayer();
    }
  }

  // OTHER METHODS:

  /**
   * Sets currentlyPlaying of the player of the last turn to false. Afterwards it sets
   * currentlyPlaying of the next player (according to the order of the ArrayList) to true. Also
   * sends a GameStateMessage to all other players.
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
    sendGameStateMessage(false);
    for (Player p : players) {
      if (p.isCurrentlyPlaying()) {
        Player playing = p;
        if (playing.isBot()) {
          Client.notifyBots(DataHandler.getOwnPlayer(), playing);
        }
      }
    }

    if (gameScreenController != null) {
      Platform.runLater(
          new Runnable() {
            @Override
            public void run() {
              gameScreenController.refreshPlayerNames();
            }
          });
    }
  }

  /**
   * Called as soon as one of the two end game conditions occurs.
   *
   * @author tikrause
   */
  public void endGame() {
    Collections.sort(players);
    boolean hasWon = (players.get(players.size() - 1).equals(ownPlayer));
    DataHandler.addStatistics(DataHandler.getOwnPlayerId(), hasWon, ownPlayer.getScore());
  }

  /**
   * Checks whether the rack as well as the bag are empty.
   *
   * @author tthielen
   * @return whether the (usual) end condition is reached
   */
  private boolean checkEndCondition() {
    return ownPlayer.getRack().getTiles().isEmpty() && bag.isEmpty();
  }

  /**
   * Called to kick the player due to using more than 10 minutes overtime.
   *
   * @author tthielen
   */
  private void kickPlayer() {
    if (ownPlayer.isCurrentlyPlaying()) {
      nextPlayer();
      Platform.runLater(
          new Runnable() {
            @Override
            public void run() {
              gameScreenController.leaveGameCall();
            }
          });
      timer.cancel();
    }
  }

  /**
   * Calls switch to GameScreen in lobbyScreenController.
   *
   * @author tthielen
   * @param chat the complete log of chat written in the lobby
   */
  public void switchToGameScreen(String chat) {
    Platform.runLater(
        new Runnable() {
          @Override
          public void run() {
            if (lobbyScreenController != null) {
              lobbyScreenController.switchToGameScreen(chat);
            } else {
              splsc.switchToGameScreen();
            }
          }
        });
  }

  /**
   * Calls setPlayable() in gameScreenController to enable or disable buttons.
   *
   * @author tthielen
   */
  public void setPlayable() {
    Platform.runLater(
        new Runnable() {
          @Override
          public void run() {
            gameScreenController.setPlayable(ownPlayer.isCurrentlyPlaying());
          }
        });
  }

  /**
   * Sets the bag by overwriting the bag object and referencing the new bag in the rack.
   *
   * @author tthielen
   * @param bag the bag which is meant to overwrite the previous bag
   */
  public void setBag(Bag bag) {
    this.bag = bag;
    this.ownPlayer.getRack().synchronizeBag(this);
  }

  /**
   * Sets the corresponding GameScreenController.
   *
   * @author tthielen
   * @param gsc the corresponding GameScreenController
   */
  public void setGameScreenController(GameScreenController gsc) {
    this.gameScreenController = gsc;
    setPlayable();
  }

  /**
   * Sets the corresponding LobbyScreenController.
   *
   * @author tthielen
   * @param lsc the corresponding LobbyScreenController
   */
  public void setLobbyScreenController(LobbyScreenController lsc) {
    this.lobbyScreenController = lsc;
  }

  /**
   * Sets the corresponding SinglePlayerLobbyScreenController.
   *
   * @author tthielen
   * @param splsc the corresponding SinglePlayerLobbyScreenController
   */
  public void setSinglePlayerLobbyScreenController(SinglePlayerLobbyScreenController splsc) {
    this.splsc = splsc;
  }

  /**
   * Sets the corresponding EndScreenController.
   *
   * @author tthielen
   * @param esc the corresponding EndScreenController
   */
  public void setEndScreenController(EndScreenController esc) {
    this.endScreenController = esc;
  }

  /**
   * Sets whether the game is running or not.
   *
   * @author tthielen
   * @param running whether the game is running or not
   */
  public void setIsRunning(boolean running) {
    this.isRunning = running;
  }

  /**
   * Returns the bag of the session. Used to draw tiles from the bag in the class Rack.
   *
   * @author tthielen
   * @return the bag of the GameSession
   */
  public Bag getBag() {
    return bag;
  }

  /**
   * Returns the board of the session.
   *
   * @author tthielen
   * @return the board of the GameSession
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Returns the ownPlayer of the session.
   *
   * @author tthielen
   * @return the ownPlayer of the GameSession
   */
  public Player getPlayer() {
    return ownPlayer;
  }

  /**
   * Returns the ownPlayer of the session.
   *
   * @author tthielen
   * @return the player list of the GameSession
   */
  public ArrayList<Player> getPlayerList() {
    return players;
  }

  /**
   * Returns the value of this turn.
   *
   * @author tthielen
   * @return the turnValue of the GameSession
   */
  public int getTurnValue() {
    return turnValue;
  }

  /**
   * Returns the current amount of successive scoreless turns.
   *
   * @author tthielen
   * @return the successiveScorelessTurns of the GameSession
   */
  public int getSuccessiveScorelessTurns() {
    return successiveScorelessTurns;
  }

  /**
   * Returns the corresponding GameScreenController of the session.
   *
   * @author tthielen
   * @return the GameScreenController of the GameSession
   */
  public GameScreenController getGameScreenController() {
    return this.gameScreenController;
  }

  /**
   * Returns the corresponding LobbyScreenController of the session.
   *
   * @author tthielen
   * @return the LobbyScreenController of the GameSession
   */
  public LobbyScreenController getLobbyScreenController() {
    return this.lobbyScreenController;
  }

  /**
   * Returns the corresponding SinglePlayerLobbyScreenController of the session.
   *
   * @author tthielen
   * @return the SinglePlayerLobbyScreenController of the GameSession
   */
  public SinglePlayerLobbyScreenController getSinglePlayerLobbyScreenController() {
    return this.splsc;
  }

  /**
   * Returns the corresponding EndScreenController of the session.
   *
   * @author tthielen
   * @return the EndScreenController of the GameSession
   */
  public EndScreenController getEndScreenController() {
    return this.endScreenController;
  }
}
