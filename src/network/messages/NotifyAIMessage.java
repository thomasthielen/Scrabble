package network.messages;

import gameentities.Player;

/**
 * Implementation of the connect message. A ConnectMessage is created for every Player entering a
 * game session.
 *
 * @author tikrause
 */
public class NotifyAIMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;
  
  private Player aiPlayer;

  /**
   * Constructor: Creates a ConnectMessage by using the MessageType.CONNECT and the Player object of
   * the creator.
   *
   * @author lsteltma
   * @param p player object that has sent the message
   */
  public NotifyAIMessage(Player p, Player aiPlayer) {
    super(MessageType.CONNECT, p);
    this.aiPlayer = aiPlayer;
  }
  
  public Player getAIPlayer() {
	  return this.aiPlayer;
  }
}
