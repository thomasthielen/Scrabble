/**
 * Implementation of the SquarePane objects
 *
 * <p>The attributes include a Square and a StackPane
 *
 * @author lsteltma
 */
package gameentities;

import java.io.Serializable;

import javafx.scene.layout.StackPane;

public class SquarePane implements Serializable {
  private static final long serialVersionUID = 1L;

  private Square square;
  private StackPane stackPane;

  /**
   * Constructor: Creates a SquarePane object by adding a given square to a new StackPane
   *
   * @author lsteltma
   * @param square
   */
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
