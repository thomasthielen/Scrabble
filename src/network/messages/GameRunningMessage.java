package network.messages;

import gameentities.Player;

/**
 * Implementation of the PlayerExistentMessage. A PlayerExistentMessage is created to inform a
 * player that wants to join the server that the game has already started and the player gets
 * rejected.
 *
 * @author tikrause
 */
public class GameRunningMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  /**
   * Constructor: creating a PlayerExistentMessage with the MessageType.GAME_RUNNING and the player
   * object that tries to join the server.
   *
   * @author tikrause
   * @param p player object of the creator
   */
  public GameRunningMessage(Player p) {
    super(MessageType.GAME_RUNNING, p);
  }
}
