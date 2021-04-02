package chat.messages;

/**
 * @author lsteltma
 *
 */

public class ConnectMessage extends Message {

	/**
	 * Default serialization UID
	 */
	private static final long serialVersionUID = 1L;

	public ConnectMessage(String name) {
		super(MessageType.CONNECT, name);
	}

}
