package network.messages;

import gameentities.Player;

/**
 * Implementation of the TooManyPlayerMessage. A TooManyPlayerMessage is created to inform a player
 * that wants to join the server that there are already 4 players in the lobby and the player gets
 * rejected.
 *
 * @author tikrause
 */
public class TooManyPlayerMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  /**
   * Constructor: creating a TooManyPlayerMessage with the MessageType.TOO_MANY and the player
   * object of the creator.
   *
   * @author tikrause
   * @param p player object of the creator
   */
  public TooManyPlayerMessage(Player p) {
    super(MessageType.TOO_MANY, p);
  }
}
