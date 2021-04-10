package gameentities;

import java.util.ArrayList;

import session.GameSession;

/**
 * Implementation of the Player objects.
 * <p>
 * For each player, a Player object containing all relevant attributes is
 * created.
 * 
 * @author tthielen
 */

public class Player {

	private String username;
	private Avatar avatar;
	private int score = 0;
	private Rack rack;

	/**
	 * Constructor: Creates a player object with the given username.
	 * 
	 * @author tthielen
	 * @param username
	 */
	public Player(String username, Avatar avatar, GameSession gameReference) {
		this.username = username;
		this.avatar = avatar;

		this.rack = new Rack(gameReference);
	}

	/**
	 * Fills the Rack of the Player to 7 Tiles
	 * 
	 * @author tthielen
	 */
	public void refillRack() {
		rack.refillDraw();
	}

	/**
	 * The Player chooses Tiles he wants to swap with new Tiles from the bag. Those
	 * Tiles are returned to the Bag, and the new Tiles are drawn from the Bag
	 * before that.
	 * 
	 * @author tthielen
	 * @param Tiles that are meant to be swapped
	 */
	public void exchangeTiles(ArrayList<Tile> swapTiles) {
		rack.exchangeTiles(swapTiles);
	}

	/**
	 * Returns the player username
	 * 
	 * @author tthielen
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the player score
	 * 
	 * @author tthielen
	 * @return score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Sets the player username
	 * 
	 * @author tthielen
	 * @param username
	 */
	public void setUsername(String name) {
		this.username = name;
	}

	/**
	 * Increases the player score by the given value.
	 * 
	 * @author tthielen
	 * @param score
	 */
	public void incScore(int score) {
		this.score += score;
	}

}
