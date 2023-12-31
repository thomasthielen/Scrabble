package network.messages;

import gameentities.Player;

/**
 * Implementation of the connect message. A ConnectMessage is created for every Player entering a
 * game session.
 *
 * @author lsteltma
 */
public class ConnectMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  /**
   * Constructor: Creates a ConnectMessage by using the MessageType.CONNECT and the Player object of
   * the creator.
   *
   * @author lsteltma
   * @param p player object that has sent the message
   */
  public ConnectMessage(Player p) {
    super(MessageType.CONNECT, p);
  }
}
