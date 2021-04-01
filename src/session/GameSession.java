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

	public void initiate() {
		players = new ArrayList<Player>();
		bag = new Bag();
	}

	public void addPlayer(String username) {
		if (players.size() < 4) {
			players.add(new Player(username, this.bag));
		} else {
			System.out.println("There are already 4 players in this GameSession!");
		}
	}
}
