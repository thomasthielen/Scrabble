package network.messages;

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

  /**
   * Constructor: Creates a DisconnectMessage by using the MessageType.DISCONNECT and the String
   * name of the person disconnecting
   *
   * @author lsteltma
   * @param name
   */
  public DisconnectMessage(String name, boolean host) {
    super(MessageType.DISCONNECT, name);
    this.host = host;
  }
  
  public boolean isHost() {
	  return this.host;
  }
}
