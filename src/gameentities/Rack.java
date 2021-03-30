package gameentities;

import java.util.ArrayList;

/**
 * Implementation of the Rack object.
 * <p>
 * For each Player, a Rack object is created which contains the Tiles of the Player in an ArrayList.
 * 
 * @author tthielen
 */
public class Rack {
	private ArrayList<Tile> tiles = new ArrayList<Tile>();
	//References
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
		for(int i = 0; i < 7; i++) {
			tiles.add(bag.drawTile());
		}
	}
}
