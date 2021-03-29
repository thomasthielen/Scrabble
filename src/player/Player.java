package player;

/**
 * <h1>Player Class</h1> This class is used for the player objects. For each
 * player, a player object, which stores all relevant attributes, is created and
 * stored in the GameSession.
 * 
 * @author tthielen
 */

public class Player {

	private String username;
	private int score;

	/**
	 * <h1>getUsername Method</h1>
	 * @author tthielen
	 * @return The username of the player object
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * <h1>getScore Method</h1>
	 * @author tthielen
	 * @return The score of the player object
	 */
	public int getScore() {
		return score;
	}
	
	public void setUsername(String name) {
		this.username = name;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}
