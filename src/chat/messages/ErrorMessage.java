package chat.messages;

/**
 * @author lsteltma
 *
 */

public class ErrorMessage extends Message {

	/**
	 * Default serialization UID
	 */
	private static final long serialVersionUID = 1L;

	public ErrorMessage(String name) {
		super(MessageType.ERROR, name);
	}

}
