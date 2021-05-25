package scrabble4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gameentities.Tile;

/**
 * JUnit Test Class for the Tile class
 *
 * @author lsteltma
 */
class TileTest {
  private Tile tile;
  
  @BeforeEach
  void init() {
    // initializing the Tile as a wildcard
    tile = new Tile('*', 0);
  }
  
  @Test
  void testWildcard() {
    // set the Letter of the wildcard
    tile.setLetter('B');
    // check if the char of the Tile got updated
    assertEquals('B', tile.getLetter());
    // check if the Value is correct
    assertEquals(0, tile.getValue());
    
    // reset the wildcard
    tile.resetLetter();
    // check if the char got reseted
    assertEquals('*', tile.getLetter());
    // check if the Value is correct
    assertEquals(0, tile.getValue());
  }
}
