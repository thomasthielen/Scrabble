/**
 * 
 */
package chat.messages;

import gameEntities.Bag;
import gameEntities.Player;

/**
 * Implementation of the update game state message. A UpdateGameStateMessage is
 * created to update the current state of the game message that something
 * 
 * @author lsteltma
 */

public class UpdateGameStateMessage extends Message {

	/**
	 * Default serialization UID
	 * 
	 * @author lsteltma
	 */

	private static final long serialVersionUID = 1L;
	private Bag bag;
	private Player[] players;

	/**
	 * Constructor: creating a UpdateGameStateMessage with the
	 * MessageType.UPDATE_GAME_STATE, the name of the creator, the current state of
	 * the bag and a list of players
	 * 
	 * @author lsteltma
	 * @param from
	 */

	public UpdateGameStateMessage(String name, Bag bag, Player[] players) {
		super(MessageType.UPDATE_GAME_STATE, name);
		this.bag = bag;
		this.players = players;
	}

	/**
	 * getter method for the player list
	 * 
	 * @return list of players
	 */

	public Player[] getPlayers() {
		return this.players;
	}

	/**
	 * getter method for the bag
	 * 
	 * @return the current state of the bag
	 */

	public Bag getBag() {
		return this.bag;
	}
}
