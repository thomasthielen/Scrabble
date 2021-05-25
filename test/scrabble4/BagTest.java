package scrabble4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gameentities.Bag;
import gameentities.Tile;

/**
 * JUnit Test Class for the Bag class
 *
 * @author lsteltma
 */
class BagTest {
  private Bag bag;

  @BeforeEach
  void init() {
    // initializing the bag
    bag = new Bag();
  }

  @Test
  // test drawTile() and addTile() from the Bag class
  void testBag() {
    // the bag should already be filled after he got initialized
    assertEquals(false, bag.isEmpty());

    // a new bag should contain 100 Tiles
    assertEquals(100, bag.getRemainingCount());

    // draw one Tile from the Bag
    bag.drawTile();
    // now the bag should contain one Tile less
    assertEquals(99, bag.getRemainingCount());

    // add a new Tile to the Bag
    bag.addTile(new Tile('A', 1));
    //now the bag should contain one Tile more
    assertEquals(100, bag.getRemainingCount());
  }
}
