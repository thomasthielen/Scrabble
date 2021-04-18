package network.messages;

import session.GameState;

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

	private GameState game;

	/**
	 * Constructor: creating a UpdateGameStateMessage with the
	 * MessageType.UPDATE_GAME_STATE, the name of the creator and the current
	 * GameState that should be sent in the message
	 *
	 * @author tikrause
	 * @param from
	 * @param game
	 */
	public UpdateGameStateMessage(String name, GameState game) {
		super(MessageType.UPDATE_GAME_STATE, name);
		this.game = game;
	}

	/**
	 * getter method for the current GameState instance
	 *
	 * @author tikrause
	 * @return game
	 */
	public GameState getGameState() {
		return this.game;
	}
}
