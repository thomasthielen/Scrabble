package gameentities;

import java.io.Serializable;

/**
 * Implementation of the Tile objects.
 *
 * <p>The attributes include the letter as well as the value of the tile.
 *
 * @author tthielen
 */
public class Tile implements Serializable {
  private static final long serialVersionUID = 1L;

  private char letter;
  private int value;
  private boolean wildcard;

  private boolean placedTemporarily;
  private boolean placedFinally;
  private boolean selected;

  /**
   * Constructor to create a tile object with the given letter and value.
   *
   * @author tthielen
   * @param letter the letter of the tile
   * @param value the value of the tile
   */
  public Tile(char letter, int value) {
    this.letter = letter;
    this.value = value;
    wildcard = (letter == '*');
  }

  /**
   * Constructor to create a tile object by copying the properties of a given tile
   *
   * @author tthielen
   * @param tile the tile from which the properties are meant to be copied
   */
  public Tile(Tile tile) {
    this.letter = tile.getLetter();
    this.value = tile.getValue();
    this.wildcard = tile.isWildCard();
  }

  /**
   * Resets the letter to '*' if the tile is a wildcard.
   *
   * @author tthielen
   */
  public void resetLetter() {
    if (this.wildcard) {
      this.letter = '*';
    }
  }

  /**
   * Sets the letter of the tile to the paramter char if the tile is a wildcard.
   *
   * @author tthielen
   * @param letter
   */
  public void setLetter(char letter) {
    if (this.wildcard) {
      this.letter = letter;
    }
  }

  /**
   * Returns the tile letter.
   *
   * @author tthielen
   * @return letter
   */
  public char getLetter() {
    return this.letter;
  }

  /**
   * Returns the tile value.
   *
   * @author tthielen
   * @return value
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Returns whether this tile is a wildcard tile.
   *
   * @author tthielen
   * @return isWildcard
   */
  public boolean isWildCard() {
    return this.wildcard;
  }

  public boolean isPlacedTemporarily() {
    return placedTemporarily;
  }

  public void setPlacedTemporarily(boolean b) {
    placedTemporarily = b;
  }

  public boolean getPlacedFinally() {
    return placedFinally;
  }

  public void setPlacedFinally(boolean b) {
    placedFinally = b;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean b) {
    selected = b;
  }

  public void setValue(int value) {
    this.value = value;
  }
}
