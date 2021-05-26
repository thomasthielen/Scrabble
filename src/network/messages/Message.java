package network.messages;

import java.io.Serializable;
import gameentities.Player;

/**
 * Abstract class for all messages that can be sent between the server and the clients.
 *
 * <p>class implements methods that should be contained in all messages that are sent
 *
 * @author tikrause
 */
public abstract class Message implements Serializable {
  private static final long serialVersionUID = 1L;

  private MessageType type;
  private Player from;

  /**
   * Constructor: creates a message that contains the message type and the creator of the message.
   *
   * @author tikrause
   * @param type type of the message that has been sent
   * @param p player object that has sent the message
   */
  public Message(MessageType type, Player p) {
    this.type = type;
    this.from = p;
  }

  /**
   * getter method for the message type.
   *
   * @author tikrause
   * @return type type of the message that has been sent
   */
  public MessageType getMessageType() {
    return this.type;
  }

  /**
   * getter method for the message creator.
   *
   * @author tikrause
   * @return from player object that has sent the message
   */
  public Player getPlayer() {
    return this.from;
  }
}
