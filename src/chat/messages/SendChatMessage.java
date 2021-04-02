package chat.messages;

/**
 * @author lsteltma
 *
 */
public class SendChatMessage extends Message {

	/**
	 * Default serialization UID
	 */
	private static final long serialVersionUID = 1L;

	public SendChatMessage(String name) {
		super(MessageType.SUCCESS, name);
	}

}
