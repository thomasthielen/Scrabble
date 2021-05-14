package network.messages;

import gameentities.Player;

/**
 * Implementation of the SendChatMessage. A SendChatMessage is created to send network messages
 *
 * @author tikrause
 */
public class SendChatMessage extends Message {

  /** Default serialization UID */
  private static final long serialVersionUID = 1L;

  private String message;

  /**
   * Constructor: creating a SendChatMessage with the MessageType.SEND_CHAT, the name of the creator
   * and the text that is sent as a network message
   *
   * @author tikrause
   * @param from
   * @param message
   */
  public SendChatMessage(Player p, String message) {
    super(MessageType.SEND_CHAT, p);
    this.message = message;
  }

  /**
   * getter method for the text of the message that is sent
   *
   * @author tikrause
   * @return message text
   */
  public String getMessage() {
    return this.message;
  }
}
