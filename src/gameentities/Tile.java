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
	private boolean wildcard;

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
		wildcard = (letter == '*');
	}

	/**
	 * Returns whether the given tile equals the tile it is called upon
	 * 
	 * @author Thomas
	 * @param Tile
	 * @return equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj.getClass() != this.getClass()) {
			return false;
		}

		final Tile OTHER = (Tile) obj;

		if (this.letter != OTHER.getLetter()) {
			return false;
		}

		return true;
	}

	/**
	 * Resets the letter to '*' if the tile is a wildcard.
	 * 
	 * @author tthielen
	 */
	public void resetLetter() {
		if (this.wildcard) {
			this.letter = '*';
		}
	}

	/**
	 * Sets the letter of the tile to the paramter char if the tile is a wildcard.
	 * 
	 * @author tthielen
	 * @param letter
	 */
	public void setLetter(char letter) {
		if (this.wildcard) {
			this.letter = letter;
		}
	}

	/**
	 * Returns the tile letter.
	 * 
	 * @author tthielen
	 * @return letter
	 */
	public char getLetter() {
		return this.letter;
	}

	/**
	 * Returns the tile value.
	 * 
	 * @author tthielen
	 * @return value
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Returns whether this tile is a wildcard tile.
	 * 
	 * @author tthielen
	 * @return isWildcard
	 */
	public boolean isWildCard() {
		return this.wildcard;
	}

}
