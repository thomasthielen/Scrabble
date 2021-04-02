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
	private String message;
	
	/**
	 * @param from
	 * @param message
	 */
	
	public SendChatMessage(String name, String message) {
		super(MessageType.SEND_CHAT_MESSAGE, name);
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

}
