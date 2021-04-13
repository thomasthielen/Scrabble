package gameentities;

/**
 * Implementation of the Tile objects.
 * <p>
 * The attributes include the letter as well as the value of the tile.
 * 
 * @author tthielen
 */

public class Tile {

	private char letter;
	private int value;

	/**
	 * Constructor: Creates a tile object with the given letter + value.
	 * 
	 * @author tthielen
	 * @param letter
	 * @param value
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
