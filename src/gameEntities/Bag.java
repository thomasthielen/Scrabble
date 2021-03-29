package gameEntities;

import java.util.ArrayList;

/**
 * This class is used for the bag object. For each GameSession, a bag object is
 * created and automatically filled with all tiles.
 * 
 * @author tthielen
 */
public class Bag {

	private boolean isEmpty = true;
	private ArrayList<Tile> remainingTiles;

	/**
	 * Creates a bag object and uses fillBag() to fill the remainingTiles ArrayList
	 * 
	 * @author tthielen
	 * @param username
	 */
	public Bag() {
		isEmpty = false;
		fillBag();
	}

	/**
	 * Fills the remainingTiles ArrayList
	 * 
	 * @author tthielen
	 * @param username
	 */
	private void fillBag() {
		// E
		for (int i = 0; i < 12; i++) {
			remainingTiles.add(new Tile('E', 1));
		}
		// A & I
		for (int i = 0; i < 9; i++) {
			remainingTiles.add(new Tile('A', 1));
			remainingTiles.add(new Tile('I', 1));
		}
		// O
		for (int i = 0; i < 8; i++) {
			remainingTiles.add(new Tile('O', 1));
		}
		// N & R & T
		for (int i = 0; i < 6; i++) {
			remainingTiles.add(new Tile('N', 1));
			remainingTiles.add(new Tile('R', 1));
			remainingTiles.add(new Tile('T', 1));
		}
		// D & G & L & S & U
		for (int i = 0; i < 4; i++) {
			remainingTiles.add(new Tile('D', 2));
			remainingTiles.add(new Tile('G', 2));
			remainingTiles.add(new Tile('L', 1));
			remainingTiles.add(new Tile('S', 1));
			remainingTiles.add(new Tile('U', 1));
		}
		// B & C & F & H & M & P & V & W & Y
		for (int i = 0; i < 2; i++) {
			remainingTiles.add(new Tile('B', 3));
			remainingTiles.add(new Tile('C', 3));
			remainingTiles.add(new Tile('F', 4));
			remainingTiles.add(new Tile('H', 4));
			remainingTiles.add(new Tile('M', 3));
			remainingTiles.add(new Tile('P', 3));
			remainingTiles.add(new Tile('V', 4));
			remainingTiles.add(new Tile('W', 4));
			remainingTiles.add(new Tile('Y', 4));
		}
		// J & K & Q & X & Z
		remainingTiles.add(new Tile('J', 8));
		remainingTiles.add(new Tile('K', 5));
		remainingTiles.add(new Tile('Q', 10));
		remainingTiles.add(new Tile('X', 8));
		remainingTiles.add(new Tile('Z', 10));
		// Blank Tiles
		for (int i = 0; i < 2; i++) {
			remainingTiles.add(new Tile('*', 0));
		}
	}
}
