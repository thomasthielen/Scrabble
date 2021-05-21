package network.messages;

import gameentities.Player;

/**
 * Implementation of the EndGameMessage. An EndGameMessage is created when someone ends the game and
 * the others should be informed.
 *
 * @author tikrause
 */
public class EndGameMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  /**
   * Constructor: If an end game condition is achieved and someone decides to end the game, the
   * EndGameMessage is created with the name of the creator.
   *
   * @author tikrause
   * @param player
   */
  public EndGameMessage(Player p) {
    super(MessageType.END_GAME, p);
  }
}
