package ai;

import gameentities.Square;
import java.util.ArrayList;

/**
 * Stores a word on the field with its squares, its x and y coordinates and its letters.
 *
 * @author sisselha
 */
public class Word {
  private boolean column;
  private ArrayList<Square> squares;
  private int beginningX;
  private int endingX;
  private int beginningY;
  private int endingY;

  /**
   * Sets the column variable whether the word is a column word or not.
   *
   * @author sisselha
   * @param column indicates whether the word is a column word or not
   */
  public void setColumn(boolean column) {
    this.column = column;
  }
  
  /**
   * Returns x-coordinate of the first letter of the word.
   *
   * @author sisselha
   * @return x-coordinate of the first letter
   */
  public int getBeginningX() {
    return this.beginningX;
  }
  
  /**
   * Returns x-coordinate of the last letter of the word.
   *
   * @author sisselha
   * @return endingX
   */
  public int getEndingX() {
    return this.endingX;
  }
  
  /**
   * Returns y-coordinate of the first letter of the word.
   *
   * @author sisselha
   * @return beginningY
   */
  public int getBeginningY() {
    return this.beginningY;
  }
  
  /**
   * Returns y-coordinate of the last letter of the word.
   *
   * @author sisselha
   * @return endingY
   */
  public int getEndingY() {
    return this.endingY;
  }
  
  /**
   * Returns the square ArrayList.
   *
   * @author sisselha
   * @return squares
   */
  public ArrayList<Square> getsquares() {
    return this.squares;
  }

  /**
   * Sets the square ArrayList.
   *
   * @author sisselha
   * @param squares square ArrayList
   */
  public void setSquares(ArrayList<Square> squares) {
    this.squares = squares;
  }

  /**
   * Returns the variable column whether the word is a column word or not.
   *
   * @author sisselha
   * @return column
   */
  public boolean getColumn() {
    return this.column;
  }

  /**
   * Sets the x-coordinate of the first letter of the word.
   *
   * @author sisselha
   * @param x x-coordinate of the word
   */
  public void setBeginningX(int x) {
    this.beginningX = x;
  }

  /**
   * Sets the x-coordinate of the last letter of the word.
   *
   * @author sisselha
   * @param x x-coordinate of the word
   */
  public void setEndingX(int x) {
    this.endingX = x;
  }
  
  /**
   * Sets the y-coordinate of the first letter of the word.
   *
   * @author sisselha
   * @param y y-coordinate of the word
   */
  public void setBeginningY(int y) {
    this.beginningY = y;
  }

  /**
   * Sets the y-coordinate of the last letter of the word.
   *
   * @author sisselha
   * @param y y-coordinate
   */
  public void setEndingY(int y) {
    this.endingY = y;
  }
}
