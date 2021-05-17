package network.messages;

import gameentities.Player;

/**
 * Implementation of the success message. A SuccessMessage is created to send a message that
 * something succeeded as it should
 *
 * @author lsteltma
 */
public class StartGameMessage extends Message {

  /**
   * Default serialization UID
   *
   * @author lsteltma
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor: creating a SuccessMessage with the MessageType.SUCCESS and the name of the creator
   *
   * @author lsteltma
   * @param name
   */
  public StartGameMessage(Player p) {
    super(MessageType.START_GAME, p);
  }
}
