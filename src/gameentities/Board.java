package gameentities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Implementation of the Board object.
 *
 * <p>The board holds all squares in an ArrayList.
 *
 * @author tthielen
 */
public class Board implements Serializable {
  private static final long serialVersionUID = 1L;

  private ArrayList<Square> squares = new ArrayList<Square>();

  /**
   * Constructor: Creates a Board object and fills it with 225 Squares with the premium squares at
   * the correct positions.
   *
   * @author tthielen
   */
  public Board() {
    for (int x = 1; x < 16; x++) {
      for (int y = 1; y < 16; y++) {
        squares.add(new Square(x, y));
      }
    }
  }

  /**
   * Places the given tile on the square.
   *
   * @author tthielen
   * @param posX the position of the square on the x-axis
   * @param posY the position of the square on the y-axis
   * @param tile the tile which is meant to be placed
   */
  public void placeTile(int posX, int posY, Tile tile) {
    getSquare(posX, posY).placeTile(tile);
  }

  /**
   * Recalls the tile from the square.
   *
   * @author tthielen
   * @param posX the position of the square on the x-axis
   * @param posY the position of the square on the y-axis
   */
  public Tile recallTile(int posX, int posY) {
    return getSquare(posX, posY).recallTile();
  }

  /**
   * Returns the tile of the square (or null, if the square is empty).
   *
   * @author tthielen
   * @param posX the position of the square on the x-axis
   * @param posY the position of the square on the y-axis
   * @return the tile at the given position (or null, if none exist)
   */
  public Tile getTile(int posX, int posY) {
    return getSquare(posX, posY).getTile();
  }

  /**
   * Returns the Square at the given coordinates (primarily used in other methods of this class).
   *
   * @author tthielen
   * @param posX the position of the square on the x-axis
   * @param posY the position of the square on the y-axis
   * @return the square at the given position
   */
  public Square getSquare(int posX, int posY) {
    int index = (posX - 1) * 15 + posY - 1;
    // e.g. (2,7) => (2 - 1) * 15 + 7 - 1 = 21
    // -1 because the ArrayList starts with index = 0
    return squares.get(index);
  }

  /**
   * Returns the square to the top of the given square or null if the given square is on the top.
   *
   * @author tthielen
   * @param square the square which gives the relative position
   * @return the square to the top of the given square
   */
  public Square getUpperNeighbour(Square square) {
    if (square.getY() < 15) {
      return squares.get(squares.indexOf(square) + 1);
    } else {
      return null;
    }
  }

  /**
   * Returns the square to the bottom of the given square or null if the given square is on the
   * bottom.
   *
   * @author tthielen
   * @param square the square which gives the relative position
   * @return the square to the bottom of the given square
   */
  public Square getLowerNeighbour(Square square) {
    if (square.getY() > 1) {
      return squares.get(squares.indexOf(square) - 1);
    } else {
      return null;
    }
  }

  /**
   * Returns the square to the left of the given square or null if the given square is leftmost.
   *
   * @author tthielen
   * @param square the square which gives the relative position
   * @return the square to the left of the given square
   */
  public Square getLeftNeighbour(Square square) {
    if (square.getX() > 1) {
      return squares.get(squares.indexOf(square) - 15);
    } else {
      return null;
    }
  }

  /**
   * Returns the square to the left of the given square or null if the given square is rightmost.
   *
   * @author tthielen
   * @param square the square which gives the relative position
   * @return the square to the right of the given square
   */
  public Square getRightNeighbour(Square square) {
    if (square.getX() < 15) {
      return squares.get(squares.indexOf(square) + 15);
    } else {
      return null;
    }
  }

  // The following 3 methods are used in the secondary word check
  // TODO: Use those in the main word check as well and delete the others
  // TODO: Simplify them, for god's sake, those are a nightmare

  /**
   * Returns the next neighbour (right/bottom) of the given square according to the given boolean.
   *
   * @author tthielen
   * @param square the square which gives the relative position
   * @param rowMain indicates whether the main word is a row
   * @return the square relative to the given square and boolean
   */
  public Square getNextNeighbour(Square square, boolean rowMain) {
    if (rowMain) {
      if (square.getY() > 1) {
        return squares.get(squares.indexOf(square) - 1);
      } else {
        return null;
      }
    } else {
      if (square.getX() < 15) {
        return squares.get(squares.indexOf(square) + 15);
      } else {
        return null;
      }
    }
  }

  /**
   * Returns the previous neighbour (left/top) of the given square according to the given boolean.
   *
   * @author tthielen
   * @param square the square which gives the relative position
   * @param rowMain indicates whether the main word is a row
   * @return the square relative to the given square and boolean
   */
  public Square getPreviousNeighbour(Square square, boolean rowMain) {
    if (rowMain) {
      if (square.getY() < 15) {
        return squares.get(squares.indexOf(square) + 1);
      } else {
        return null;
      }
    } else {
      if (square.getX() > 1) {
        return squares.get(squares.indexOf(square) - 15);
      } else {
        return null;
      }
    }
  }

  /**
   * Returns whether the given square has a neighbouring square which holds a tile which was played
   * in a previous turn.
   *
   * @author tthielen
   * @param square the square which gives the relative position
   * @param rowMain indicates whether the main word is a row
   * @return the square relative to the given square and boolean
   */
  public boolean hasPreviouslyPlayedNeighbour(Square square, boolean rowMain) {
    if (rowMain) {
      if (this.getUpperNeighbour(square) != null) {
        if (this.getUpperNeighbour(square).isPreviouslyPlayed()) {
          return true;
        }
      }
      if (this.getLowerNeighbour(square) != null) {
        if (this.getLowerNeighbour(square).isPreviouslyPlayed()) {
          return true;
        }
      }
      return false;
    } else {
      if (this.getLeftNeighbour(square) != null) {
        if (this.getLeftNeighbour(square).isPreviouslyPlayed()) {
          return true;
        }
      }
      if (this.getRightNeighbour(square) != null) {
        if (this.getRightNeighbour(square).isPreviouslyPlayed()) {
          return true;
        }
      }
      return false;
    }
  }
  /**
   * Returns whether the given square has a neighbouring square which holds a tile which was played
   * in a previous turn.
   *
   * @author tthielen
   * @param square the square which gives the relative position
   * @return the square relative to the given square and boolean
   */
  public boolean hasPreviouslyPlayedNeighbour(Square square) {
    if (this.getUpperNeighbour(square) != null) {
      if (this.getUpperNeighbour(square).isPreviouslyPlayed()) {
        return true;
      }
    }
    if (this.getLowerNeighbour(square) != null) {
      if (this.getLowerNeighbour(square).isPreviouslyPlayed()) {
        return true;
      }
    }
    if (this.getLeftNeighbour(square) != null) {
      if (this.getLeftNeighbour(square).isPreviouslyPlayed()) {
        return true;
      }
    }
    if (this.getRightNeighbour(square) != null) {
      if (this.getRightNeighbour(square).isPreviouslyPlayed()) {
        return true;
      }
    }
    return false;
  }

  // Testing Purposes TODO: DELETE
  public ArrayList<Square> getSquareList() {
    return this.squares;
  }
}
