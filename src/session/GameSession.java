package session;

import java.util.ArrayList;

import gameentities.*;

/**
 * Class used to manage all game entities and necessary methods.
 * 
 * @author tthielen
 */
public class GameSession {
	private ArrayList<Player> players;
	private Bag bag;
	private Board board;
	
	private boolean isActive = false;

	public GameSession() {
		players = new ArrayList<Player>();
		bag = new Bag();
		board = new Board();
	}

	public void addPlayer(String username, Avatar avatar) {
		if (players.size() < 4) {
			players.add(new Player(username, avatar, this));
		} else {
			System.out.println("There are already 4 players in this GameSession!");
		}
	}
	
	public void initialize() {
		//TODO: Implement addPlayer() through Database/Connection handling
	}
	
	public Bag getBag() {
		return bag;
	}

}
