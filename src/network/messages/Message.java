package network.messages;

import java.io.Serializable;
import gameentities.Player;

/**
 * Abstract class for all messages that can be sent between the server and the clients
 *
 * <p>class implements methods that should be contained in all messages that are sent
 *
 * @author tikrause
 */
public abstract class Message implements Serializable {
  private static final long serialVersionUID = 1L;

  private MessageType mType;
  private Player from;

  /**
   * Constructor: creates a message that contains the message type and the owner of the message
   *
   * @author tikrause
   * @param type
   * @param from
   */
  public Message(MessageType type, Player p) {
    this.mType = type;
    this.from = p;
  }

  /**
   * getter method for the message type
   *
   * @author tikrause
   * @return message type
   */
  public MessageType getMessageType() {
    return this.mType;
  }

  /**
   * getter method for the message owner
   *
   * @author tikrause
   * @return from
   */
  public Player getFrom() {
    return this.from;
  }

  /**
   * setter method for the message owner
   *
   * @author tikrause
   * @param name
   */
  public void setFrom(Player p) {
    this.from = p;
  }
}
