package gameEntities;

/**
 * This class is used for the tile objects. The object attributes include the
 * letter as well as the value (meaning the amount of points the tile will net)
 * of the tile
 * 
 * @author tthielen
 */

public class Tile {

	private char letter;
	private int value;

	/**
	 * Creates a tile object with the given letter + value
	 * 
	 * @author tthielen
	 * @param letter, value
	 */
	public Tile(char letter, int value) {
		this.letter = letter;
		this.value = value;
	}

	/**
	 * Returns the tile letter
	 * 
	 * @author tthielen
	 * @return letter
	 */
	public char getLetter() {
		return this.letter;
	}

	/**
	 * Returns the tile value
	 * 
	 * @author tthielen
	 * @return value
	 */
	public int getValue() {
		return this.value;
	}

}
