package network.messages;

/**
 * Implementation of the connect message. A ConnectMessage is created for every
 * Player entering a game session
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

	/**
	 * Constructor: Creates a ConnectMessage by using the MessageType.CONNECT and
	 * the String name of the person connecting
	 * 
	 * @author lsteltma
	 * @param name
	 */

	public ConnectMessage(String name) {
		super(MessageType.CONNECT, name);
	}

}
