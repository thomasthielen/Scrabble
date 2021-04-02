package chat.messages;

/**
 * @author lsteltma
 *
 */

public class DisconnectMessage extends Message {

	/**
	 * Default serialization UID
	 */
	private static final long serialVersionUID = 1L;

	public DisconnectMessage(String name) {
		super(MessageType.DISCONNECT, name);
	}

}
