/**
 * 
 */
package chat.messages;

/**
 * @author lsteltma
 *
 */
public class LetterMessage extends Message {
	/**
	 * Default serialization UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param from
	 */
	public LetterMessage(String name) {
		super(MessageType.LETTER, name);
	}

}
