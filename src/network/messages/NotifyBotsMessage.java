package network.messages;

import gameentities.Player;

/**
 * Implementation of the NotifyBotsMessage. A NotifyBotsMessage is created if the next player is an
 * AI player and therefore informs the server to let the AI player make a move.
 *
 * @author tikrause
 */
public class NotifyBotsMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  private Player aiPlayer;

  /**
   * Constructor: Creates a NotifyBotsMessage by using the MessageType.NOTIFY_AI, the Player object
   * of the creator and the AI player that has to make a move next.
   *
   * @author tikrause
   * @param p player object that has sent the message
   */
  public NotifyBotsMessage(Player p, Player aiPlayer) {
    super(MessageType.NOTIFY_AI, p);
    this.aiPlayer = aiPlayer;
  }

  /**
   * getter method for the AI player that has to make a move next.
   *
   * @author tikrause
   * @return aiPlayer player that has to make a move next
   */
  public Player getBotPlayer() {
    return this.aiPlayer;
  }
}
