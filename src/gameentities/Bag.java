package gameentities;

import java.util.ArrayList;

/**
 * Implementation of the Bag object.
 *
 * <p>For each GameSession, a Bag object is created and automatically filled with the tiles of a
 * game scrabble.
 *
 * @author tthielen
 */
public class Bag {

  /**
   * Object that holds a type of tile as well as its current count within the bag.
   *
   * @author tthielen
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

  private boolean isEmpty = true; // Indicates whether the bag is empty
  // List of TileContainers, used to count the tile-types (e.g. 7x "E" remaining)
  private ArrayList<TileContainer> tileCounter = new ArrayList<TileContainer>();
  // List of actual Tiles
  private ArrayList<Tile> remainingTiles = new ArrayList<Tile>();

  /**
   * Constructor: Creates a Bag object and uses fillBag() to fill the remainingTiles ArrayList.
   *
   * @author tthielen
   */
  public Bag() {
    isEmpty = false;
    fillBag();
  }

  /**
   * Fills the bag by filling tileCounter and remainingTiles of the bag
   *
   * @author tthielen
   */
  private void fillBag() {
    // Add the tiles and their counts in tileCounter
    tileCounter.add(new TileContainer(new Tile('E', 1), 12));
    tileCounter.add(new TileContainer(new Tile('A', 1), 9));
    tileCounter.add(new TileContainer(new Tile('I', 1), 9));
    tileCounter.add(new TileContainer(new Tile('O', 1), 8));
    tileCounter.add(new TileContainer(new Tile('N', 1), 6));
    tileCounter.add(new TileContainer(new Tile('R', 1), 6));
    tileCounter.add(new TileContainer(new Tile('T', 1), 6));
    tileCounter.add(new TileContainer(new Tile('L', 1), 4));
    tileCounter.add(new TileContainer(new Tile('S', 1), 4));
    tileCounter.add(new TileContainer(new Tile('U', 1), 4));
    tileCounter.add(new TileContainer(new Tile('D', 2), 4));
    tileCounter.add(new TileContainer(new Tile('G', 2), 3));
    tileCounter.add(new TileContainer(new Tile('B', 3), 2));
    tileCounter.add(new TileContainer(new Tile('C', 3), 2));
    tileCounter.add(new TileContainer(new Tile('M', 3), 2));
    tileCounter.add(new TileContainer(new Tile('P', 3), 2));
    tileCounter.add(new TileContainer(new Tile('F', 4), 2));
    tileCounter.add(new TileContainer(new Tile('H', 4), 2));
    tileCounter.add(new TileContainer(new Tile('V', 4), 2));
    tileCounter.add(new TileContainer(new Tile('W', 4), 2));
    tileCounter.add(new TileContainer(new Tile('Y', 4), 2));
    tileCounter.add(new TileContainer(new Tile('K', 5), 1));
    tileCounter.add(new TileContainer(new Tile('J', 8), 1));
    tileCounter.add(new TileContainer(new Tile('X', 8), 1));
    tileCounter.add(new TileContainer(new Tile('Q', 10), 1));
    tileCounter.add(new TileContainer(new Tile('Z', 10), 1));
    tileCounter.add(new TileContainer(new Tile('*', 0), 2)); // Wildcard / Blank Tile

    // Iterate through tileCounter and use the count to fill remainingTiles with the
    // correct amounts of tiles
    for (TileContainer tc : tileCounter) {
      for (int i = 0; i < tc.getCount(); i++) {
        remainingTiles.add(tc.getTile());
      }
    }
  }

  /**
   * Returns a random tile, reduces the count in the corresponding TileContainer and removes the
   * tile from remainingTiles.
   *
   * @author tthielen
   * @return Tile, randomly drawn from the bag
   */
  public Tile drawTile() {
    if (!this.isEmpty) {
      // Draw a random tile out of remainingTiles
      int rand = (int) (remainingTiles.size() * Math.random());
      Tile randTile = remainingTiles.get(rand);

      // Remove the tile from remainingTiles
      remainingTiles.remove(rand);

      // Decrease the count of the respective tile in tileCounter
      for (TileContainer tc : tileCounter) {
        if (tc.getTile().equals(randTile)) {
          tc.decCount();
          break;
        }
      }

      // Set isEmpty to true if
      isEmpty = remainingTiles.size() == 0;
      return randTile;

    } else {
      System.out.println("The bag is empty.");
      return null;
    }
  }

  /**
   * Adds the given tile to remaininTiles and increases the count in its respective TileContainer in
   * tileCounter.
   *
   * @author tthielen
   * @param Tile to be added to the bag
   */
  public void addTile(Tile t) {
    remainingTiles.add(t);
    for (TileContainer tc : tileCounter) {
      if (tc.getTile().equals(t)) {
        tc.incCount();
        break;
      }
    }
  }

  /**
   * Returns the total count of remaining tiles in the bag.
   *
   * @author tthielen
   * @return count of tiles
   */
  public int getRemainingCount() {
    int count = 0;
    for (TileContainer tc : tileCounter) {
      count += tc.getCount();
    }
    return count;
  }
  
  /**
   * Returns the count of the given tile char
   * 
   * @author tthielen
   * @param character
   * @return 
   */
  public int getRemainingTileCount(char c) {
    for (TileContainer tc : tileCounter) {
      if (tc.getTile().getLetter() == c) {
        return tc.getCount();
      }
    }
    return 0; // dummy return
  }

  /**
   * Returns whether the bag is empty.
   *
   * @author tthielen
   * @return isEmpty
   */
  public boolean isEmpty() {
    return this.isEmpty;
  }
}
