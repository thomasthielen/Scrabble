package chat.messages;

/**
 * Implementation of the SendChatMessage. A SendChatMessage is created to send chat messages
 * 
 * @author tikrause
 *
 */

public class SendChatMessage extends Message {

	/**
	 * Default serialization UID
	 */
	
	private static final long serialVersionUID = 1L;
	private String message;
	
	/**
	 * Constructor: creating a SendChatMessage with the MessageType.SEND_CHAT, the name
	 * of the creator and the text that is sent as a chat message
	 * 
	 * @author tikrause
	 * @param from
	 * @param message
	 */
	
	public SendChatMessage(String name, String message) {
		super(MessageType.SEND_CHAT, name);
		this.message = message;
	}
	
	/**
	 * getter method for the text of the message that is sent
	 * 
	 * @author tikrause
	 * @return message text
	 */
	
	public String getMessage() {
		return this.message;
	}

}
