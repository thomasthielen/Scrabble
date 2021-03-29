package gameEntities;

/**
 * Implementation of the Player objects.
 * <p>
 * For each player, a Player object containing all relevant attributes is created.
 * 
 * @author tthielen
 */

public class Player {

	private String username;
	private int score;
	private Rack rack;
	//Reference
	private Bag bag;

	/**
	 * Constructor: Creates a player object with the given username.
	 * 
	 * @author tthielen
	 * @param username
	 */
	public Player(String username, Bag bag) {
		this.username = username;
		this.score = 0;
		this.rack = new Rack(this, this.bag);
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
	 * Sets the player score
	 * 
	 * @author tthielen
	 * @param score
	 */
	public void setScore(int score) {
		this.score = score;
	}

}
