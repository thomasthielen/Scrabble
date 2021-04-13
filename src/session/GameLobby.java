package session;

import java.util.ArrayList;
import gameentities.*;

/**
 * GameLobby is started by the host of the game itself. It is used to collect
 * the Player objects and synchronise the first GameState message.
 * 
 * @author tthielen
 */
public class GameLobby {
	private GameState gameState;
//	private Server host;

	/**
	 * Constructor: Creates a first GameState object and the necessary Server/Client
	 * objects of the host.
	 * 
	 * @author tthielen
	 */
	public GameLobby() {
		ArrayList<Player> players = new ArrayList<Player>();
		Bag bag = new Bag();
		Board board = new Board();
		this.gameState = new GameState(players, bag, board);
		// TODO: Create the Server (from the host)
		// TODO: Create the Client (of the host)
	}

	/**
	 * Adds a player to the first GameState object.
	 * 
	 * @author tthielen
	 * @param player
	 */
	public void joinPlayer(Player player) {
		gameState.addPlayer(player);
	}
}
