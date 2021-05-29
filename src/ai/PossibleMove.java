package ai;

import gameentities.Square;
import java.util.ArrayList;

/**
 * Represents a possible move by holding the respective squares and the turn value.
 *
 * @author sisselha
 */
public class PossibleMove {
  private ArrayList<Square> squares;
  int value;

  /**
   * Constructor: Saves the given squares and the turn value.
   *
   * @author sisselha
   * @param squares the squares on which tiles were placed
   * @param value the value of the turn
   */
  public PossibleMove(ArrayList<Square> squares, int value) {
    this.squares = squares;
    this.value = value;
  }

  /**
   * Returns the squares of the PossibleMove.
   *
   * @author sisselha
   * @return squares
   */
  public ArrayList<Square> getsquares() {
    return this.squares;
  }

  /**
   * Returns the value of the PossibleMove.
   *
   * @author sisselha
   * @return value
   */
  public int getValue() {
    return this.value;
  }
}
