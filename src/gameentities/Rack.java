package gameentities;

import java.util.ArrayList;

import session.GameSession;

/**
 * Implementation of the Rack object.
 *
 * <p>
 * For each Player, a Rack object is created which contains the Tiles of the
 * Player in an ArrayList.
 *
 * @author tthielen
 */
public class Rack {
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	// References
	private Bag bag;

	/**
	 * Constructor: Creates a Rack object and assigns Player and Bag references
	 *
	 * @author tthielen
	 * @param player
	 * @param bag
	 */
	public Rack(GameSession gameReference) {
		this.bag = gameReference.getBag();
	}

	/**
	 * Draws the initial seven Tiles from the Bag
	 *
	 * @author tthielen
	 */
	public void initialDraw() {
		for (int i = 0; i < 7; i++) {
			tiles.add(bag.drawTile());
		}
	}

	/**
	 * Redraws tiles until there are 7 tiles on the rack again or the bag is empty.
	 *
	 * @author tthielen
	 */
	public void refillDraw() {
		while (this.tiles.size() < 7) {
			if (bag.isEmpty()) {
				return;
			}
			tiles.add(bag.drawTile());
		}
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
		ArrayList<Tile> returnTiles = new ArrayList<Tile>();

		for (Tile t : swapTiles) {
			if (this.tiles.contains(t)) {
				returnTiles.add(t);
				this.tiles.remove(t);
			} else {
				System.out.println("Error: Chosen tile does not exist on rack!");
			}
		}

		for (Tile t : returnTiles) {
			this.bag.addTile(t);
		}

		refillDraw();
	}

	/**
	 * Plays a tile to the board.
	 *
	 * @author tthielen
	 * @param tile
	 */
	public void playTile(Tile tile) {
		this.tiles.remove(tile);
	}

	/**
	 * Adds a tile back to the rack.
	 *
	 * @author tthielen
	 * @param tile
	 */
	public void returnTile(Tile tile) {
		this.tiles.add(tile);
	}

	/**
	 * Returns the rack's tiles
	 *
	 * @author tthielen
	 * @return rack
	 */
	public ArrayList<Tile> getTiles() {
		return tiles;
	}

	/**
	 * Returns the tile at the given index
	 *
	 * @author tthielen
	 * @return rack
	 */
	public Tile getTile(int pos) {
		return tiles.get(pos);
	}
}
