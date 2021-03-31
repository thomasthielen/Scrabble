package gameentities;

import java.util.ArrayList;

/**
 * Implementation of the Rack object.
 * <p>
 * For each Player, a Rack object is created which contains the Tiles of the
 * Player in an ArrayList.
 * 
 * @author tthielen
 */
public class Rack {
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	// References
	private Player player;
	private Bag bag;

	/**
	 * Constructor: Creates a Rack object and assigns Player and Bag references
	 * 
	 * @author tthielen
	 * @param player
	 * @param bag
	 */
	public Rack(Player player, Bag bag) {
		this.player = player;
		this.bag = bag;
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
	 * Redraws Tiles until there are 7 Tiles on the Rack
	 * 
	 * @author tthielen
	 */
	public void refillDraw() {
		while (this.tiles.size() < 7) {
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
}
