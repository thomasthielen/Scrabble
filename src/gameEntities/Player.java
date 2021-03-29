package gameEntities;

/**
 * This class is used for the player objects. For each
 * player, a player object, which stores all relevant attributes, is created and
 * stored in the GameSession.
 * 
 * @author tthielen
 */

public class Player {

	private String username;
	private int score;

	/**
	 * Creates a player object with the given username
	 * 
	 * @author tthielen
	 * @param username
	 */
	public Player(String username) {
		this.username = username;
		this.score = 0;
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
