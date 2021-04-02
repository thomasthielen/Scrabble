/**
 * 
 */
package chat.messages;

/**
 * @author lsteltma
 *
 */
public class UpdateGameStateMessage extends Message {
	/**
	 * Default serialization UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param from
	 */
	public UpdateGameStateMessage(String name) {
		super(MessageType.UPDATE_GAME_STATE, name);
	}

}
