/** */
package gameentities;

import javafx.scene.layout.StackPane;

/** @author lsteltma */
public class SquarePane {
  private Square square;
  private StackPane stackPane;

  public SquarePane(Square square) {
    this.square = square;
    this.stackPane = new StackPane();
  }

  public Square getSquare() {
    return this.square;
  }

  public StackPane getStackPane() {
    return this.stackPane;
  }
}
