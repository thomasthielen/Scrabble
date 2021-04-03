/**
 * 
 */
package chat.messages;

import gameEntities.Tile;

/**
 * Implementation of the letter message. A LetterMessage is created to send
 * tiles
 * 
 * @author lsteltma
 */

public class LetterMessage extends Message {

	/**
	 * Default serialization UID
	 * 
	 * @author lsteltma
	 */

	private static final long serialVersionUID = 1L;
	private Tile letter;

	/**
	 * Constructor: creating a LetterMessage with the MessageType.LETTER, the name
	 * of the creator and the tile which should be send
	 * 
	 * @author lsteltma
	 * @param name
	 * @param letter
	 */

	public LetterMessage(String name, Tile letter) {
		super(MessageType.LETTER, name);
		this.letter = letter;
	}

	/**
	 * getter method for the tile
	 * 
	 * @return the send tile of the message
	 */
	
	public Tile getTile() {
		return this.letter;
	}
}
