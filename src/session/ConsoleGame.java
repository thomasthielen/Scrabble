package session;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import data.DataHandler;
import gameentities.*;

/**
 * This class is runnable in the console. With it, the game can be played without a GUI, and is
 * therefore ideal for testing purposes.
 *
 * @author tthielen
 */
public class ConsoleGame {
  private static GameSession gs = new GameSession(new Player("tthielen", null));
  private static Board board;
  private static Player p1;

  private static char tileChar;
  private static Tile tile;
  private static int posX = 0;
  private static int posY = 0;

  /**
   * Main method to run the game in the console.
   *
   * @author tthielen
   * @param args
   * @throws IOExceptions
   */
  public static void main(String[] args) throws IOException {

    DataHandler.userDictionaryFile(new File("resources/Collins Scrabble Words (2019).txt"));

    gs.setActive(true);

    board = gs.getBoard();
    //    board.placeTile(8, 8, new Tile('A',1));
    //    board.getSquare(8, 8).setPreviouslyPlayed();
    p1 = gs.getPlayer();

    p1.getRack().initialDraw();

    while (gs.isActive()) {
      printBoard();
      printRack(p1);

      posX = 0;
      posY = 0;

      boolean tileChosen = requestPlayerInput(gs.checkMove());
      if (tileChosen) {
        while (board.getSquare(posX, posY).isTaken()) {
          System.out.println("\nThe chosen square is already taken!");
          requestPlayerInput(gs.checkMove());
        }
        gs.placeTile(posX, posY, tile);
      }
    }
  }

  /**
   * Prints the board in its current state, as well as the count of tiles left in the bag.
   *
   * @author tthielen
   */
  private static void printBoard() {
    System.out.println();
    for (int y = 15; y > 0; y--) {

      System.out.print(y + "\t");

      for (int x = 1; x < 16; x++) {

        Square s = gs.getBoard().getSquare(x, y);

        if (!s.isTaken()) {
          switch (s.getPremium()) {
            case DLS:
              System.out.print("[dl]\t");
              break;
            case TLS:
              System.out.print("[tl]\t");
              break;
            case DWS:
              System.out.print("[dw]\t");
              break;
            case TWS:
              System.out.print("[tw]\t");
              break;
            case STAR:
              System.out.print("[**]\t");
              break;
            case NONE:
              System.out.print("[  ]\t");
              break;
            default:
              System.out.print("[  ]\t");
              break;
          }
        } else {
          if (s.isPreviouslyPlayed()) {
            System.out.print("[" + s.getTile().getLetter() + " ]\t");
          } else {
            System.out.print("[+" + s.getTile().getLetter() + "]\t");
          }
        }
        if (x == 15) {
          System.out.println();
          System.out.println();
        }
      }
    }

    System.out.print("\t");
    for (int x = 1; x < 16; x++) {
      System.out.print(x + "\t");
    }

    System.out.print(
        "\n\nThere are " + gs.getBag().getRemainingCount() + " tiles remaining in the bag.");
  }

  /**
   * Prints the rack of the given player, as well as the score of them.
   *
   * @author tthielen
   * @param player
   */
  private static void printRack(Player p) {
    System.out.println("\n");
    System.out.println(p.getUsername() + "'s score: " + p.getScore());
    System.out.println(p.getUsername() + "'s rack: ");
    ArrayList<Tile> tiles = p.getRack().getTiles();

    System.out.print("Tile:\t");
    for (Tile t : tiles) {
      System.out.print(t.getLetter() + "\t");
    }

    System.out.print("\nValue:\t");
    for (Tile t : tiles) {
      System.out.print(t.getValue() + "\t");
    }

    System.out.println("\n");
  }

  /**
   * Requests input from the player. There are several commands available besides simply choosing a
   * letter from the rack or pressing ENTER to submit the move (if possible). Returns true if only a
   * letter was chosen.
   *
   * @author tthielen
   * @param canMakePlay
   * @return tileChosen
   * @throws IOException
   */
  private static boolean requestPlayerInput(boolean canMakePlay) throws IOException {
    posX = 0;
    posY = 0;
    tileChar = Character.MIN_VALUE;

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    ArrayList<Tile> tiles = p1.getRack().getTiles();
    ArrayList<Character> chars = new ArrayList<Character>();

    for (Tile t : tiles) {
      chars.add(t.getLetter());
    }

    System.out.println("\nType 'recall' to return all tiles to the rack.");
    if (canMakePlay) {
      System.out.println("Press ENTER to submit for " + gs.getTurnValue() + " points.");
    }
    System.out.print("Choose a tile from your rack (letter): ");
    while (!chars.contains(tileChar)) {

      String s = br.readLine();
      s = s.toUpperCase();

      if (s.length() > 1) {
        if (s.equals("RECALL")) {
          gs.recallAll();
          return false;
        } else {
          System.out.print("Invalid command! ");
          continue;
        }
      } else if (s.equals("")) {
        if (canMakePlay) {
          gs.makePlay();
          return false;
        } else {
          System.out.print("Cannot sumbit: Not a valid move! ");
          continue;
        }
      }

      tileChar = s.charAt(0);

      if (!chars.contains(tileChar)) {
        System.out.print("Chosen tile is not on rack! ");
      }
    }

    for (Tile t : p1.getRack().getTiles()) {
      if (t.getLetter() == tileChar) {
        tile = t;
        break;
      }
    }

    while (tileChar == '*') {
      System.out.print("Choose any letter: ");
      String s = br.readLine();

      if (!(s.length() == 1)) {
        System.out.print("Invalid letter! ");
        continue;
      }

      if (!Character.isLetter(s.charAt(0))) {
        System.out.print("Invalid letter! ");
        continue;
      }

      tileChar = s.charAt(0);
    }

    tile.setLetter(tileChar);

    while (posX < 1 || posX > 15) {
      System.out.print("Enter x-coordinate from [1,15]: ");
      try {
        posX = Integer.parseInt(br.readLine());
      } catch (NumberFormatException nfe) {
        System.err.println("Not a number!");
      }
    }

    while (posY < 1 || posY > 15) {
      System.out.print("Enter y-coordinate from [1,15]: ");
      try {
        posY = Integer.parseInt(br.readLine());
      } catch (NumberFormatException nfe) {
        System.err.println("Not a number!");
      }
    }

    return true;
  }
}
