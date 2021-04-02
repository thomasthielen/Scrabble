/**
 * 
 */
package chat.messages;

import gameEntities.Bag;
import gameEntities.Player;

/**
 * @author lsteltma
 *
 */

public class UpdateGameStateMessage extends Message {
	
	/**
	 * Default serialization UID
	 */
	
	private static final long serialVersionUID = 1L;
	private Bag bag;
	private Player[] players;
	
	/**
	 * @param from
	 */
	
	public UpdateGameStateMessage(String name, Bag bag, Player[] players) {
		super(MessageType.UPDATE_GAME_STATE, name);
		this.bag = bag;
		this.players = players;
	}
	
	public Player[] getPlayers() {
		return this.players;
	}

	public Bag getBag() {
		return this.bag;
	}
}
