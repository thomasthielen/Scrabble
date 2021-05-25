package ai;

import java.util.ArrayList;

import gameentities.Square;

public class PossibleMove {
  private ArrayList<Square> squares;
  int value;

  public PossibleMove(ArrayList<Square> squares, int value) {
    this.squares = squares;
    this.value = value;
  }

  public ArrayList<Square> getsquares() {
    return this.squares;
  }

  public int getValue() {
    return this.value;
  }

  // public void setvalue
}
