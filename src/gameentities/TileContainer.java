package gameentities;

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
