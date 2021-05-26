package ai;

import gameentities.Square;
import java.util.ArrayList;

public class Word {
  private boolean column;
  private ArrayList<Square> squares;
  private int beginningX;
  private int endingX;
  private int beginningY;
  private int endingY;
  private char letter;

  public void setColumn(boolean column) {
    this.column = column;
  }

  public char getLetter() {
    return this.letter;
  }

  public void setLetter(char c) {
    this.letter = c;
  }

  public int getBeginningX() {
    return this.beginningX;
  }

  public ArrayList<Square> getsquares() {
    return this.squares;
  }

  public void setSquares(ArrayList<Square> squares) {
    this.squares = squares;
  }

  public int getEndingX() {
    return this.endingX;
  }

  public int getBeginningY() {
    return this.beginningY;
  }

  public int getEndingY() {
    return this.endingY;
  }

  public boolean getColumn() {
    return this.column;
  }

  public void setBeginningX(int x) {
    this.beginningX = x;
  }

  public void setEndingX(int x) {
    this.endingX = x;
  }

  public void setBeginningY(int y) {
    this.beginningY = y;
  }

  public void setEndingY(int y) {
    this.endingY = y;
  }
}
