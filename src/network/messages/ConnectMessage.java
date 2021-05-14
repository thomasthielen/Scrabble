package network.messages;

import gameentities.Player;

/**
 * Implementation of the connect message. A ConnectMessage is created for every Player entering a
 * game session
 *
 * @author lsteltma
 */
public class ConnectMessage extends Message {

  /**
   * Default serialization UID
   *
   * @author lsteltma
   */
  private static final long serialVersionUID = 1L;

  private Player p;

  /**
   * Constructor: Creates a ConnectMessage by using the MessageType.CONNECT and the String name of
   * the person connecting
   *
   * @author lsteltma
   * @param name
   * @param p
   */
  public ConnectMessage(Player p) {
    super(MessageType.CONNECT, p);
    this.p = p;
  }

  /**
   * @author tikrause
   * @return p
   */
  public Player getPlayer() {
    return this.p;
  }
}
