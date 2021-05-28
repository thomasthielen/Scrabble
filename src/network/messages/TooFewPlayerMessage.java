package network.messages;

import gameentities.Player;

/**
 * Implementation of the TooFewPlayerMessage. It is created to inform the client that he is now the
 * only player left in the game session after the other players have left. He can't continue playing
 * and must be informed.
 *
 * @author tikrause
 */
public class TooFewPlayerMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  /**
   * Constructor: creating a TooFewPlayerMessage with the MessageType.TOO_FEW and the player object
   * of the creator.
   *
   * @author tikrause
   * @param p player object of the creator
   */
  public TooFewPlayerMessage(Player p) {
    super(MessageType.TOO_FEW, p);
  }
}
