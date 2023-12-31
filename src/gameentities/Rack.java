package gameentities;

import java.io.Serializable;
import java.util.ArrayList;
import session.GameSession;

/**
 * Implementation of the Rack object.
 *
 * <p>For each Player, a Rack object is created which contains the Tiles of the Player in an
 * ArrayList.
 *
 * @author tthielen
 */
public class Rack implements Serializable {
  private static final long serialVersionUID = 1L;

  private ArrayList<Tile> tiles = new ArrayList<Tile>();
  // Used for tile positioning
  private Tile[] tileArray = new Tile[7];
  // References
  private Bag bag;

  /**
   * Constructor: Creates a Rack object and assigns Player and Bag references.
   *
   * @author tthielen
   * @param gameReference the GameSession this rack belongs to
   */
  public Rack(GameSession gameReference) {
    this.bag = gameReference.getBag();
  }

  /**
   * Synchronizes the bag of this rack with the on from the GameSession.
   *
   * @author tthielen
   * @param gameReference the GameSession this rack should be synchronized with
   */
  public void synchronizeBag(GameSession gameReference) {
    this.bag = gameReference.getBag();
  }

  /**
   * Draws the initial seven Tiles from the Bag.
   *
   * @author tthielen
   */
  public void initialDraw() {
    while (tiles.size() < 7) {
      tiles.add(bag.drawTile());
    }
    for (int i = 0; i < 7; i++) {
      tileArray[i] = tiles.get(i);
    }
  }

  /**
   * Redraws tiles until there are 7 tiles on the rack again or the bag is empty.
   *
   * @author tthielen
   * @param ai whether the refilling player is an AI or not.
   */
  public void refillDraw(boolean ai) {
    while (this.tiles.size() < 7) {
      if (bag.isEmpty()) {
        break;
      }
      Tile newTile = bag.drawTile();
      tiles.add(newTile);
      if (!ai) {
        for (int i = 0; i < tileArray.length; i++) {
          if (tileArray[i] == null) {
            tileArray[i] = newTile;
            break;
          }
        }
      }
    }
    if (!ai) {
      tiles.clear();
      for (int i = 0; i < tileArray.length; i++) {
        if (tileArray[i] != null) {
          tiles.add(tileArray[i]);
          tileArray[i] = null;
        }
      }
      for (int i = 0; i < tiles.size(); i++) {
        tileArray[i] = tiles.get(i);
      }
    } else {
      for (int i = 0; i < tileArray.length; i++) {
        tileArray[i] = null;
      }
    }
  }

  /**
   * The Player chooses Tiles he wants to swap with new Tiles from the bag. Those Tiles are returned
   * to the Bag, and the new Tiles are drawn from the Bag before that.
   *
   * @author tthielen
   * @param positions positions of the current tiles that are meant to be swapped
   */
  public void exchangeTiles(ArrayList<Integer> positions) {
    ArrayList<Tile> returnTiles = new ArrayList<Tile>();

    // Go through the given positions, add the tiles at those to returnTiles and remove them from
    // the Array
    for (int i : positions) {
      returnTiles.add(tileArray[i]);
      tileArray[i] = null;
    }

    // Clear the ArrayList tiles
    tiles.clear();

    // And refill it with the remaining tiles of the Array
    for (int i = 0; i < tileArray.length; i++) {
      if (tileArray[i] != null) {
        tiles.add(tileArray[i]);
      }
    }

    for (Tile t : returnTiles) {
      this.bag.addTile(t);
    }

    refillDraw(false);
  }

  /**
   * Plays a tile to the board.
   *
   * @author tthielen
   * @param position the position of the tile that should be played on the board
   */
  public void playTile(int position) {
    tileArray[position] = null;
    this.tiles.clear();
    for (int i = 0; i < tileArray.length; i++) {
      if (tileArray[i] != null) {
        tiles.add(tileArray[i]);
      }
    }
  }

  /**
   * Plays a tile of the AI. Doesn't consider exact position of the tile.
   *
   * @author tthielen
   * @param tile the tile of the AI that should be played
   */
  public void playTileBot(Tile tile) {
    Tile removeTile = null;
    for (Tile t : tiles) {
      if (t.getLetter() == tile.getLetter()) {
        removeTile = t;
        break;
      }
    }
    tiles.remove(removeTile);
  }

  /**
   * Adds a tile back to the rack on the correct position.
   *
   * @author tthielen
   * @param tile the tile that should be added to the rack
   * @param position the position of the tile on the rack
   */
  public void returnTile(Tile tile, int position) {
    if (tileArray[position] == null) {
      tileArray[position] = tile;
    }
    this.tiles.clear();
    for (int i = 0; i < tileArray.length; i++) {
      if (tileArray[i] != null) {
        tiles.add(tileArray[i]);
      }
    }
  }

  /**
   * Adds a tile back to the rack. Used for AI.
   *
   * @author tthielen
   * @param tile the tile that should be added to the rack
   */
  public void returnTile(Tile tile) {
    tiles.add(tile);
  }

  /**
   * Returns the rack's tiles.
   *
   * @author tthielen
   * @return rack
   */
  public ArrayList<Tile> getTiles() {
    return tiles;
  }

  /**
   * Returns the tile at the given index.
   *
   * @author tthielen
   * @return rack
   */
  public Tile getTile(int pos) {
    return tiles.get(pos);
  }
}
