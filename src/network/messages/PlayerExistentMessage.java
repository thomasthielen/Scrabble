package network.messages;

import gameentities.Player;

/**
 * Implementation of the PlayerExistentMessage. A PlayerExistentMessage is created to inform a player that
 * he tries to join a lobby where his username is already used.
 *
 * @author tikrause
 */
public class PlayerExistentMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  /**
   * Constructor: creating a PlayerExistentMessage with the MessageType.PLAYER_EXISTENT and the player
   * object that tries to join the server.
   *
   * @author tikrause
   * @param p player object of the creator
   */
  public PlayerExistentMessage(Player p) {
    super(MessageType.PLAYER_EXISTENT, p);
  }
}
