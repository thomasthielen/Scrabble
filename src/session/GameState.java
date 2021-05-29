package session;

import gameentities.Bag;
import gameentities.Board;
import gameentities.Player;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class used to synchronize game entities between players. At each necessary moment, the
 * synchronize() method of GameSession is called and the momentary GameState object is sent to /
 * received by all other players.
 *
 * @author tthielen
 * @author tikrause
 */
public class GameState implements Serializable {
  private static final long serialVersionUID = 1L;

  private ArrayList<Player> players;
  private Bag bag;
  private Board board;
  private int successiveScorelessTurns;
  private boolean playersOnly;
  private boolean connectGameState = false;

  /**
   * Constructor: Creates a GameState object by saving all relevant attributes within.
   *
   * @author tthielen
   * @param players the players that should be added to the GameState
   * @param bag the bag that should be added to the GameState
   * @param board the board that should be added to the GameState
   */
  public GameState(ArrayList<Player> players, Bag bag, Board board) {
    this.players = players;
    this.bag = bag;
    this.board = board;
    this.playersOnly = false;
  }

  /**
   * Constructor: Creates a GameState object with the player list to send the initial game session
   * object.
   *
   * @author tikrause
   * @param players the players that should be added to the GameState
   */
  public GameState(ArrayList<Player> players) {
    this.players = players;
    this.playersOnly = true;
  }

  /**
   * Constructor: Creates a GameState Object with a GameSession and the information whether the
   * GameState is sent with the connect message.
   *
   * @author tthielen
   * @param gameSession the gamesession that should be added
   * @param connectGameState if the gamestate is sent with the connect message
   */
  public GameState(GameSession gameSession, boolean connectGameState) {
    this.players = gameSession.getPlayerList();
    this.bag = gameSession.getBag();
    this.board = gameSession.getBoard();
    this.successiveScorelessTurns = gameSession.getSuccessiveScorelessTurns();
    this.playersOnly = false;
    this.connectGameState = connectGameState;
  }

  /**
   * Returns the players.
   *
   * @author tthielen
   * @return players
   */
  public ArrayList<Player> getPlayers() {
    return this.players;
  }

  /**
   * Returns the bag.
   *
   * @author tthielen
   * @return bag
   */
  public Bag getBag() {
    return this.bag;
  }

  /**
   * Returns the currentPlayer.
   *
   * @author tthielen
   * @return currentPlayer
   */
  public Board getBoard() {
    return this.board;
  }

  /**
   * Returns whether the GameState is sent with a connect message.
   *
   * @return connectGameState
   * @author tthielen
   */
  public boolean isConnectGameState() {
    return this.connectGameState;
  }

  /**
   * Returns whether the GameState is players only.
   *
   * @return playersOnly
   * @author tthielen
   */
  public boolean isPlayersOnly() {
    return this.playersOnly;
  }

  /**
   * Returns how many successive scoreless turns there have been in this gamestate.
   *
   * @return successiveScorelessTurns
   * @author tthielen
   */
  public int getSuccessiveScorelessTurns() {
    return this.successiveScorelessTurns;
  }
}
