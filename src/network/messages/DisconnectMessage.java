package network.messages;

import gameentities.Player;

/**
 * Implementation of the disconnect message. A DisconnectMessage is created for every Player leaving
 * a game session.
 *
 * @author lsteltma
 */
public class DisconnectMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  private boolean host;

  /**
   * Constructor: Creates a DisconnectMessage by using the MessageType.DISCONNECT, the Player object
   * of the person disconnecting and if this person is the host of the game session.
   *
   * @author tikrause
   * @param p player object that has sent the message
   * @param host boolean if the leaving person is the host of the game session
   */
  public DisconnectMessage(Player p, boolean host) {
    super(MessageType.DISCONNECT, p);
    this.host = host;
  }

  /**
   * getter method if the leaving person is the host of the game session.
   *
   * @author tikrause
   * @return host boolean if the leaving person is the host
   */
  public boolean isHost() {
    return this.host;
  }
}
