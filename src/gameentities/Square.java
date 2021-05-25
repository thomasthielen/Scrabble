package gameentities;

import java.io.Serializable;

/**
 * Implementation of the Square objects.
 *
 * <p>The attributes include the coordinates, the premium square status and, if placed, the Tile on
 * top of it.
 *
 * @author tthielen
 * @author sisselha
 */
public class Square implements Serializable {
  private static final long serialVersionUID = 1L;

  Premium premium = Premium.NONE; // Indicates Premium variant

  private int posX; // On X-Axis: [1,15]
  private int posY; // On Y-Axis: [1,15]

  private Tile tile; // Saves the Tile which is put on the Square
  private boolean taken = false; // Indicates whether the Square is taken
  private boolean previousPlay = false; // Whether this Square holds a Tile from a previous move
  private boolean currentPlay = false; // Whether this square holds a tile from the current move
  private boolean isWithinColumnWord = false;
  private boolean isWithinRowWord = false;

  /**
   * Constructor: Creates a Square object, saves the coordinates and checks whether those require a
   * premium square.
   *
   * @author tthielen
   * @param posX the X position of the square
   * @param posY the Y position of the square
   */
  public Square(int posX, int posY) {
    this.posX = posX;
    this.posY = posY;

    switch (posX) {
      case 1:
      case 15:
        if (posY == 1 || posY == 8 || posY == 15) {
          premium = Premium.TWS;
        } else if (posY == 4 || posY == 12) {
          premium = Premium.DLS;
        }
        break;

      case 2:
      case 14:
        if (posY == 2 || posY == 14) {
          premium = Premium.DWS;
        } else if (posY == 6 || posY == 10) {
          premium = Premium.TLS;
        }
        break;

      case 3:
      case 13:
        if (posY == 3 || posY == 13) {
          premium = Premium.DWS;
        } else if (posY == 7 || posY == 9) {
          premium = Premium.DLS;
        }
        break;

      case 4:
      case 12:
        if (posY == 4 || posY == 12) {
          premium = Premium.DWS;
        } else if (posY == 1 || posY == 8 || posY == 15) {
          premium = Premium.DLS;
        }
        break;

      case 5:
      case 11:
        if (posY == 5 || posY == 11) {
          premium = Premium.DWS;
        }
        break;

      case 6:
      case 10:
        if (posY == 2 || posY == 6 || posY == 10 || posY == 14) {
          premium = Premium.TLS;
        }
        break;

      case 7:
      case 9:
        if (posY == 3 || posY == 7 || posY == 9 || posY == 13) {
          premium = Premium.DLS;
        }
        break;

      case 8:
        if (posY == 1 || posY == 15) {
          premium = Premium.TWS;
        } else if (posY == 4 || posY == 12) {
          premium = Premium.DLS;
        } else if (posY == 8) {
          premium = Premium.STAR;
        }
        break;

      default:
        System.out.println("Error: Invalid coordinates!");
    }
  }

  /**
   * Places the given Tile on the Square and marks the Square as taken.
   *
   * @author tthielen
   * @param tile the tile that should be placed
   */
  public void placeTile(Tile tile) {
    this.tile = tile;
    this.taken = true;
    this.currentPlay = true;
  }

  /**
   * Recalls the tile from the square and returns it.
   *
   * @author tthielen
   * @param tile the tile that should be recalled
   */
  public Tile recallTile() {
    final Tile returnTile = this.tile;
    this.tile = null;
    this.taken = false;
    this.currentPlay = false;
    return returnTile;
  }

  /**
   * Sets that this square holds a tile from a previous move.
   *
   * @author tthielen
   */
  public void setPreviouslyPlayed() {
    this.currentPlay = false;
    this.previousPlay = true;
  }

  /**
   * Returns the premium status of the square.
   *
   * @author tthielen
   * @return premium
   */
  public Premium getPremium() {
    return this.premium;
  }

  /**
   * Returns the tile of the square (or null, if the square is empty).
   *
   * @author tthielen
   * @return tile
   */
  public Tile getTile() {
    return this.tile;
  }

  /**
   * Returns whether the square is already taken (or not).
   *
   * @author tthielen
   * @return taken
   */
  public boolean isTaken() {
    return this.taken;
  }

  /**
   * Returns whether this square holds a tile from a previous move.
   *
   * @author tthielen
   * @return previousPlay
   */
  public boolean isPreviouslyPlayed() {
    return this.previousPlay;
  }

  /**
   * Returns whether this square holds a tile from the current move.
   *
   * @author tthielen
   * @return currentlyPlay
   */
  public boolean isCurrentlyPlayed() {
    return this.currentPlay;
  }

  /**
   * Returns the x-coordinate of the square.
   *
   * @author tthielen
   * @return posX
   */
  public int getX() {
    return this.posX;
  }

  /**
   * Returns the y-coordinate of the square.
   *
   * @author tthielen
   * @return posY
   */
  public int getY() {
    return this.posY;
  }

  /**
   * Returns whether this square is within a row word.
   *
   * @return isWithinRowWord
   * @author sisselha
   */
  public boolean isWithinRowWord() {
    return this.isWithinRowWord;
  }

  /**
   * Returns whether this square is within a column word.
   *
   * @return isWithinRowWord
   * @author sisselha
   */
  public boolean isWithinColumnWord() {
    return this.isWithinColumnWord;
  }

  /**
   * Sets whether this square is within a column word.
   *
   * @author sisselha
   */
  public void setWithinColumnWord() {
    this.isWithinColumnWord = true;
  }

  /**
   * Sets whether this square is within a row word.
   *
   * @author sisselha
   */
  public void setWithinRowWord() {
    this.isWithinRowWord = true;
  }
}
