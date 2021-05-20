package gameentities;

import java.io.Serializable;

/**
 * Object that holds a type of tile as well as its current count within the bag.
 *
 * @author tthielen
 */
public class TileContainer implements Serializable {
  private static final long serialVersionUID = 1L;

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
   * Sets the count of the tile in the bag.
   * 
   * @author tthielen
   * @param count the count of the tile in the bag
   */
  public void setCount(int count) {
    this.count = count;
  }
  
  /**
   * Sets the value of the tile.
   * 
   * @author tthielen
   * @param count the value of the tile
   */
  public void setValue(int value) {
    this.tile.setValue(value);
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
