package network.messages;

import gameentities.Player;

/**
 * Implementation of the connect message. A ConnectMessage is created for every Player entering a
 * game session
 *
 * @author lsteltma
 */
public class PlayerMessage extends Message {

  /**
   * Default serialization UID
   *
   * @author lsteltma
   */
  private static final long serialVersionUID = 1L;

  
  private Player player;

  /**
   * Constructor: Creates a ConnectMessage by using the MessageType.CONNECT and the String name of
   * the person connecting
   *
   * @author lsteltma
   * @param name
   * @param stats
   */
  public PlayerMessage(Player p) {
    super(MessageType.PLAYER, p.getUsername());
    this.player = p;
    System.out.println("wird erzeugt");
  }

  /**
   * 
   * @author tikrause
   * @return playerStatistics
   */
  public Player getPlayer() {
    return this.player;
  }
}
