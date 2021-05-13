package session;

import java.util.ArrayList;
import gameentities.*;

/**
 * SinglePlayerLobby is started by the host of the game itself. It is used to collect the Player
 * objects and synchronize the first GameState message.
 *
 * @author tthielen
 */
public class SinglePlayerLobby {
  private GameState gameState;
  private ArrayList<Player> players;

  /**
   * Constructor: Creates a first GameState object
   *
   * @author tthielen
   */
  public SinglePlayerLobby(Player p) {
    players = new ArrayList<Player>();
    //joinPlayer(p);
    Bag bag = new Bag();
    Board board = new Board();
    this.gameState = new GameState(players, bag, board);
  }

  /**
   * Adds a player to the first GameState object.
   *
   * @author tthielen
   * @param player
   */
  public void joinPlayer(Player player) {
    gameState.addPlayer(player);
  }

  
  // @author tikrause
  public GameState getGameState() {
    return this.gameState;
  }
}
