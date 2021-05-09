package gameentities;

/**
 * Implementation of the Tile objects.
 *
 * <p>The attributes include the letter as well as the value of the tile.
 *
 * @author tthielen
 */
public class Tile {

  private char letter;
  private int value;
  private boolean wildcard;

  private boolean placedTemporarily;
  private boolean placedFinally;
  private boolean selected;

  /**
   * Constructor: Creates a tile object with the given letter + value.
   *
   * @author tthielen
   * @param letter
   * @param value
   */
  public Tile(char letter, int value) {
    this.letter = letter;
    this.value = value;
    wildcard = (letter == '*');
  }

  public Tile(Tile tile) {
    this.letter = tile.getLetter();
    this.value = tile.getValue();
    this.wildcard = tile.isWildCard();
  }

  /**
   * Returns whether the given tile equals the tile it is called upon.
   *
   * @author Thomas
   * @param Tile
   * @return equals
   */
  // TODO: This method was commented out because it messed with the indexOf()-method.
  // If, contrary to expectation, it is required, then I will have to get creative.
  //
  //  @Override
  //  public boolean equals(Object obj) {
  //    if (obj == null) {
  //      return false;
  //    }
  //
  //    if (obj.getClass() != this.getClass()) {
  //      return false;
  //    }
  //
  //    final Tile OTHER = (Tile) obj;
  //
  //    if (this.letter != OTHER.getLetter()) {
  //      return false;
  //    }
  //
  //    return true;
  //  }

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
}
