package network.messages;

import gameentities.Player;

/**
 * Implementation of the PlayAgainMessage. A PlayAgainMessage is created when the host has ended the
 * game, wants to play again and therefore informs the other clients.
 *
 * @author tikrause
 */
public class PlayAgainMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  /**
   * Constructor: If the host decides to play again at the end screen, the
   * PlayAgainMessage is created with the name of the host.
   *
   * @author tikrause
   * @param player player instance of the host
   */
  public PlayAgainMessage(Player p) {
    super(MessageType.PLAY_AGAIN, p);
  }
}
