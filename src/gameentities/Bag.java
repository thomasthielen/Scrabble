package gameentities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Implementation of the Bag object.
 *
 * <p>For each GameSession, a Bag object is created and automatically filled with the tiles of a
 * game scrabble.
 *
 * @author tthielen
 */
public class Bag implements Serializable {

  private static final long serialVersionUID = 1L;

  // Indicates whether the bag is empty
  private boolean isEmpty = true;
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
   * Fills the bag by filling tileCounter and remainingTiles of the bag.
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

    refreshBag();
  }

  /**
   * Iterate through tileCounter and use the count to fill remainingTiles with the correct amounts
   * of tiles.
   *
   * @author tthielen
   */
  public void refreshBag() {
    remainingTiles.clear();
    for (TileContainer tc : tileCounter) {
      for (int i = 0; i < tc.getCount(); i++) {
        remainingTiles.add(new Tile(tc.getTile()));
      }
    }
  }

  /**
   * Returns a random tile, reduces the count in the corresponding TileContainer and removes the
   * tile from remainingTiles.
   *
   * @author tthielens
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
      return null;
    }
  }

  /**
   * Adds the given tile to remainingTiles and increases the count in its respective TileContainer
   * in tileCounter.
   *
   * @author tthielen
   * @param tile the tile which is meant to be added to the bag
   */
  public void addTile(Tile tile) {
    remainingTiles.add(tile);
    for (TileContainer tc : tileCounter) {
      if (tc.getTile().equals(tile)) {
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
    return remainingTiles.size();
  }

  /**
   * Returns the whole tileCounter ArrayList.
   *
   * @author tthielen
   * @return tileCounter
   */
  public ArrayList<TileContainer> getTileCounter() {
    return tileCounter;
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
