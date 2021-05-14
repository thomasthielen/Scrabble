package session;

import java.io.Serializable;
import java.util.ArrayList;

import gameentities.Bag;
import gameentities.Board;
import gameentities.Player;

/**
 * Class used to synchronise game entities between players. At each necessary moment, the
 * synchronise() method of GameSession is called and the momentary GameState object is sent to /
 * received by all other players.
 *
 * @author tthielen
 */
public class GameState implements Serializable {
  private static final long serialVersionUID = 1L;

  private ArrayList<Player> players;
  private Bag bag;
  private Board board;

  /**
   * Constructor: Creates a GameState object by saving all relevant attributes within.
   *
   * @author tthielen
   * @param players
   * @param bag
   * @param board
   */
  public GameState(ArrayList<Player> players, Bag bag, Board board) {
    this.players = players;
    this.bag = bag;
    this.board = board;
  }

  public void addPlayer(Player player) {
    this.players.add(player);
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
}
