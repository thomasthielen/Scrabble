package network.messages;

import java.util.HashMap;

import data.StatisticKeys;

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

  
  private HashMap<StatisticKeys, Integer> playerStatistics;

  /**
   * Constructor: Creates a ConnectMessage by using the MessageType.CONNECT and the String name of
   * the person connecting
   *
   * @author lsteltma
   * @param name
   * @param stats
   */
  public ConnectMessage(String name,  HashMap<StatisticKeys, Integer> stats) {
    super(MessageType.CONNECT, name);
    this.playerStatistics = stats;
  }

  /**
   * 
   * @author tikrause
   * @return playerStatistics
   */
  public HashMap<StatisticKeys, Integer> getPlayerStatistics() {
    return this.playerStatistics;
  }
}
