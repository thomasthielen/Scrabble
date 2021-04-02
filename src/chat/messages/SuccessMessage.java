package chat.messages;

/**
 * @author lsteltma
 *
 */

public class SuccessMessage extends Message {

	/**
	 * Default serialization UID
	 */
	
	private static final long serialVersionUID = 1L;

	public SuccessMessage(String name) {
		super(MessageType.SUCCESS, name);
	}

}
