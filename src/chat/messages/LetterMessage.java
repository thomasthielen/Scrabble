/**
 * 
 */
package chat.messages;

import gameEntities.Tile;

/**
 * @author lsteltma
 *
 */

public class LetterMessage extends Message {
	
	/**
	 * Default serialization UID
	 */
	
	private static final long serialVersionUID = 1L;
	private Tile letter;
	
	/**
	 * @param from
	 * @param letter
	 */
	
	public LetterMessage(String name, Tile letter) {
		super(MessageType.LETTER, name);
		this.letter = letter;
	}

	public Tile getTile() {
		return this.letter;
	}
}
