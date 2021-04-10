package gameentities;

import java.util.ArrayList;

/**
 * Implementation of the Bag object.
 * <p>
 * For each GameSession, a Bag object is created and automatically filled with
 * the tiles of a game scrabble.
 * 
 * @author tthielen
 */
public class Bag {

	/**
	 * Object that holds a type of tile as well as its current count within the bag.
	 * 
	 * @author tthielen
	 *
	 */
	class TileContainer {
		private Tile tile;
		private int count;

		/**
		 * Creates a TileContainer with the tile as well as its count within the bag.
		 * 
		 * @author tthielen
		 * @param tile
		 * @param count
		 */
		public TileContainer(Tile tile, int count) {
			this.tile = tile;
			this.count = count;
		}

		/**
		 * Increases the count of the tile.
		 * 
		 * @author tthielen
		 */
		public void incCount() {
			this.count++;
		}

		/**
		 * Decreases the count of the tile.
		 * 
		 * @author tthielen
		 */
		public void decCount() {
			this.count--;
		}

		/**
		 * Returns the count of the tile.
		 * 
		 * @author tthielen
		 * @return count
		 */
		public int getCount() {
			return this.count;
		}

		/**
		 * Returns the tile of the TileContainer.
		 * 
		 * @author tthielen
		 * @return tile
		 */
		public Tile getTile() {
			return this.tile;
		}
	}

	private boolean isEmpty = true;
	private ArrayList<TileContainer> remainingTiles = new ArrayList<TileContainer>();

	/**
	 * Constructor: Creates a Bag object and uses fillBag() to fill the
	 * remainingTiles ArrayList.
	 * 
	 * @author tthielen
	 */
	public Bag() {
		isEmpty = false;
		fillBag();
	}

	/**
	 * Fills the remainingTiles ArrayList with TileContainers containing the tiles
	 * with its letter & value as well as the count of those tiles.
	 * 
	 * @author tthielen
	 */
	private void fillBag() {
		remainingTiles.add(new TileContainer(new Tile('E', 1), 12));
		remainingTiles.add(new TileContainer(new Tile('A', 1), 9));
		remainingTiles.add(new TileContainer(new Tile('I', 1), 9));
		remainingTiles.add(new TileContainer(new Tile('O', 1), 8));
		remainingTiles.add(new TileContainer(new Tile('N', 1), 6));
		remainingTiles.add(new TileContainer(new Tile('R', 1), 6));
		remainingTiles.add(new TileContainer(new Tile('T', 1), 6));
		remainingTiles.add(new TileContainer(new Tile('L', 1), 4));
		remainingTiles.add(new TileContainer(new Tile('S', 1), 4));
		remainingTiles.add(new TileContainer(new Tile('U', 1), 4));
		remainingTiles.add(new TileContainer(new Tile('D', 2), 4));
		remainingTiles.add(new TileContainer(new Tile('G', 2), 4));
		remainingTiles.add(new TileContainer(new Tile('B', 3), 2));
		remainingTiles.add(new TileContainer(new Tile('C', 3), 2));
		remainingTiles.add(new TileContainer(new Tile('M', 3), 2));
		remainingTiles.add(new TileContainer(new Tile('P', 3), 2));
		remainingTiles.add(new TileContainer(new Tile('F', 4), 2));
		remainingTiles.add(new TileContainer(new Tile('H', 4), 2));
		remainingTiles.add(new TileContainer(new Tile('V', 4), 2));
		remainingTiles.add(new TileContainer(new Tile('W', 4), 2));
		remainingTiles.add(new TileContainer(new Tile('Y', 4), 2));
		remainingTiles.add(new TileContainer(new Tile('K', 5), 1));
		remainingTiles.add(new TileContainer(new Tile('J', 8), 1));
		remainingTiles.add(new TileContainer(new Tile('X', 8), 1));
		remainingTiles.add(new TileContainer(new Tile('Q', 10), 1));
		remainingTiles.add(new TileContainer(new Tile('Z', 10), 1));
		remainingTiles.add(new TileContainer(new Tile('*', 0), 2)); // Wildcard / Blank Tile

	}

	/**
	 * Returns a random Tile and reduces the count in the corresponding
	 * TileContainer. If the count is == 1, removes the TileContainer entirely.
	 * 
	 * @author tthielen
	 * @return Tile, randomly drawn from the bag
	 */
	public Tile drawTile() {
		if (!this.isEmpty) {
			int rand = (int) (remainingTiles.size() * Math.random());
			TileContainer randTileContainer = remainingTiles.get(rand);
			if (randTileContainer.getCount() == 1) {
				remainingTiles.remove(randTileContainer);
			} else {
				randTileContainer.decCount();
			}
			return randTileContainer.getTile();
		} else {
			System.out.println("The bag is empty.");
			return null;
		}
	}

	/**
	 * Tries to find a TileContainer in remainingTiles representing the given tile.
	 * If it is found, increases the count of the TileContainer. If not, creates the
	 * TileContainer with the given tile and a count of 1.
	 * 
	 * @author tthielen
	 * @param Tile to be added to the bag
	 */
	public void addTile(Tile t) {
		for (TileContainer tc : remainingTiles) {
			if (tc.getTile() == t) {
				tc.incCount();
				return;
			}
		}
		remainingTiles.add(new TileContainer(t, 1));
	}

}
