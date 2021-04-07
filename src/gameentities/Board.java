package gameentities;

import java.util.ArrayList;

/**
 * Implementation of the Board object.
 * <p>
 * The board holds all squares in an ArrayList.
 * 
 * @author tthielen
 */
public class Board {
	private ArrayList<Square> squares = new ArrayList<Square>();

	/**
	 * Constructor: Creates a Board object and fills it with 225 Squares with the
	 * premium squares at the correct positions.
	 * 
	 * @author tthielen
	 */
	public Board() {
		for (int x = 1; x < 16; x++) {
			for (int y = 1; y < 16; y++) {
				squares.add(new Square(x, y));
			}
		}
	}

	/**
	 * Places the given Tile on the Square.
	 * 
	 * @author tthielen
	 * @param posX
	 * @param posY
	 * @param tile
	 */
	public void placeTile(int posX, int posY, Tile tile) {
		getSquare(posX, posY).placeTile(tile);
	}

	/**
	 * Returns the tile of the square (or null, if the square is empty).
	 * 
	 * @author tthielen
	 * @param posX
	 * @param posY
	 * @return tile
	 */
	public Tile getTile(int posX, int posY) {
		return getSquare(posX, posY).getTile();
	}

	/**
	 * Returns the Square at the given coordinates (primarily used in other methods
	 * of this class).
	 * 
	 * @author tthielen
	 * @param posX
	 * @param posY
	 * @return square
	 */
	public Square getSquare(int posX, int posY) {
		int index = (posX - 1) * 15 + posY - 1;
		// e.g. (2,7) => (2 - 1) * 15 + 7 - 1 = 21
		// -1 because the ArrayList starts with index = 0
		return squares.get(index);
	}

	public Square getUpperNeighbour(Square square) {
		if (square.getY() < 15) {
			return squares.get(squares.indexOf(square) + 1);
		} else {
			return null;
		}
	}

	public Square getLowerNeighbour(Square square) {
		if (square.getY() > 1) {
			return squares.get(squares.indexOf(square) - 1);
		} else {
			return null;
		}
	}

	public Square getRightNeighbour(Square square) {
		if (square.getX() < 15) {
			return squares.get(squares.indexOf(square) + 15);
		} else {
			return null;
		}
	}

	public Square getLeftNeighbour(Square square) {
		if (square.getX() > 1) {
			return squares.get(squares.indexOf(square) - 15);
		} else {
			return null;
		}
	}

	public boolean hasPreviouslyPlayedNeighbour(Square square) {
		if (this.getUpperNeighbour(square).isPreviouslyPlayed() || this.getLeftNeighbour(square).isPreviouslyPlayed()
				|| this.getRightNeighbour(square).isPreviouslyPlayed()
				|| this.getLowerNeighbour(square).isPreviouslyPlayed()) {
			return true;
		} else {
			return false;
		}
	}

	// Testing Purposes TODO: DELETE
	public ArrayList<Square> getSquareList() {
		return this.squares;
	}

}