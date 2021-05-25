package gameentities;

import java.io.Serializable;

/**
 * Implementation of the Tile objects.
 *
 * <p>The attributes include the letter as well as the value of the tile.
 *
 * @author tthielen
 * @author lsteltma
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
   * Constructor to create a tile object by copying the properties of a given tile.
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
   * @param letter the letter that should be placed as the wildcard
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

  /**
   * Returns whether the tile is placed temporarily.
   *
   * @return placedTemporarily
   * @author lsteltma
   */
  public boolean isPlacedTemporarily() {
    return placedTemporarily;
  }

  /**
   * Sets whether the tile is placed temporarily.
   *
   * @param b whether the tile should be placed temporarily
   * @author lsteltma
   */
  public void setPlacedTemporarily(boolean b) {
    placedTemporarily = b;
  }

  /**
   * Return whether the tile is placed finally.
   *
   * @return placedFinally
   * @author lsteltma
   */
  public boolean getPlacedFinally() {
    return placedFinally;
  }

  /**
   * Sets whether the tile is placed finally.
   *
   * @param b whether the tile should be placed finally
   * @author lsteltma
   */
  public void setPlacedFinally(boolean b) {
    placedFinally = b;
  }

  /**
   * Returns whether the tile is selected.
   *
   * @return selected
   * @author tthielen
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * Sets whether the tile is selected.
   *
   * @param b whether the tile should be selected
   * @author tthielen
   */
  public void setSelected(boolean b) {
    selected = b;
  }

  /**
   * Sets the value of the tile.
   *
   * @param value the value the tile should have
   * @author tthielen
   */
  public void setValue(int value) {
    this.value = value;
  }
}
