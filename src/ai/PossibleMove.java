package ai;

import gameentities.Square;
import java.util.ArrayList;

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

}
