package session;

import data.DataHandler;

/**
 * SinglePlayerLobby is started by the host of the game itself. It is used to collect the Player
 * objects and synchronize the first GameState message.
 *
 * @author tthielen
 */
public class SinglePlayerLobby {
  private GameSession gameSession;

  /**
   * Constructor: Creates a first GameState object
   *
   * @author tthielen
   */
  public SinglePlayerLobby() {
    this.gameSession = new GameSession(DataHandler.getOwnPlayer(), false);
  }

  /**
   * @author tikrause
   * @return gameSession
   */
  public GameSession getGameSession() {
    return this.gameSession;
  }
}
