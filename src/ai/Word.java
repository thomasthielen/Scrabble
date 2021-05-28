package ai;

import gameentities.Square;
import java.util.ArrayList;
/**
 * stores a word on the field with its squares, its x and y coordinates and its letters
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
   * sets the column variable whether the word is a column word or not
   *
   * @author sisselha
   * @param column
   */
  public void setColumn(boolean column) {
    this.column = column;
  }
  /**
   * returns x-coordinate of the first letter of the word
   * 
   * @author sisselha 
   * @return
   */
  public int getBeginningX() {
    return this.beginningX;
  }
  /**
   * returns x-coordinate of the last letter of the word
   * 
   * @author sisselha 
   * @return
   */
  public int getEndingX() {
    return this.endingX;
  }
  /**
   * returns y-coordinate of the first letter of the word
   * 
   * @author sisselha 
   * @return
   */

  public int getBeginningY() {
    return this.beginningY;
  }
  /**
   * returns y-coordinate of the last letter of the word
   * 
   * @author sisselha 
   * @return
   */

  public int getEndingY() {
    return this.endingY;
  }
  /**
   * returns the square ArrayList
   * 
   * @author sisselha 
   * @return
   */

  public ArrayList<Square> getsquares() {
    return this.squares;
  }
  
  /**
   * sets the square ArrayList
   * 
   * @author sisselha 
   * @param squares square ArrayList
   */

  public void setSquares(ArrayList<Square> squares) {
    this.squares = squares;
  }
  
  /**
   * returns the variable column whether the word 
   * is a column word or not
   * 
   * @author sisselha 
   * @return
   */

  public boolean getColumn() {
    return this.column;
  }
  
  /**
   * sets the x-coordinate of the first letter of the word
   * 
   * @author sisselha 
   * @param x x-coordinate of the word
   */

  public void setBeginningX(int x) {
    this.beginningX = x;
  }
  
  /**
   * sets the x-coordinate of the last letter of the word
   * 
   * @author sisselha 
   * @param x x-coordinate of the word
   */

  public void setEndingX(int x) {
    this.endingX = x;
  }
  /**
   * sets the y-coordinate of the first letter of the word
   * 
   * @author sisselha 
   * @param y y-coordinate of the word
   */

  public void setBeginningY(int y) {
    this.beginningY = y;
  }
  
  /**
   * sets the y-coordinate of the last letter of the word
   * 
   * @author sisselha 
   * @param y y-coordinate
   */

  public void setEndingY(int y) {
    this.endingY = y;
  }
}
