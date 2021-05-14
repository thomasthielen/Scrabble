package network.messages;

import gameentities.Player;

/**
 * Implementation of the disconnect message. A DisconnectMessage is created for every Player leaving
 * a game session
 *
 * @author lsteltma
 */
public class DisconnectMessage extends Message {

  /**
   * Default serialization UID
   *
   * @author lsteltma
   */
  private static final long serialVersionUID = 1L;
  
  private boolean host;
  private Player p;

  /**
   * Constructor: Creates a DisconnectMessage by using the MessageType.DISCONNECT and the String
   * name of the person disconnecting
   *
   * @author lsteltma
   * @param name
   */
  public DisconnectMessage(Player p, boolean host) {
    super(MessageType.DISCONNECT, p);
    this.host = host;
    this.p = p;
  }
  
  public boolean isHost() {
	  return this.host;
  }
  
  /**
   * @author tikrause
   * @return p
   */
  public Player getPlayer() {
    return this.p;
  }
}
