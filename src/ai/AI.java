package ai;

import data.BitOptionKeys;
import data.DataHandler;
import gameentities.Board;
import gameentities.Player;
import gameentities.Square;
import gameentities.Tile;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import network.Client;
import network.Server;
import session.GameSession;
import session.GameState;

/** @author sisselha */
public class AI {

  private ArrayList<PossibleMove> moves;
  private ArrayList<Word> words;
  private GameSession gameReference;
  private StringBuffer buffer;
  private ArrayList<Tile> tiles;
  private boolean difficult;
  private boolean dictionaryInitialized = false;
  private File dictionary;

  /**
   * ee
   *
   * @author sisselha
   * @param username username
   * @param difficult difficulty of the AI
   */
  public AI(String username, boolean difficult) {
    this.gameReference = new GameSession(new Player(username));
    this.difficult = difficult;
  }

  public void initializeAI(File dictionary) {
    this.dictionary = dictionary;
    this.gameReference.setBag(Client.getGameSession().getBag());
    this.gameReference.getPlayer().getRack().initialDraw();
    Server.updateRackOfAIPlayer(this);
  }
  /**
   * If the AI has to make the first move, the method initializes all possible moves the AI can make
   * with the given letters.
   *
   * @author sisselha
   */
  public void getTheFirstMove() {
    this.moves = new ArrayList<PossibleMove>();
    ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
    for (int i = 0; i < this.tiles.size(); i++) {
      for (int j = 0; j < this.tiles.size(); j++) {
        list.add(new ArrayList<String>());
        list.get(i * 7 + j).add(String.valueOf(tiles.get(i).getLetter()));
        list.get(i * 7 + j).add(String.valueOf(tiles.get(j).getLetter()));
      }
    }
    for (int i = 0; i < list.size(); i++) {
      StringBuffer buf = new StringBuffer();
      for (int j = 0; j < list.get(i).size(); j++) {
        buf.append(list.get(i).get(j));
      }
      String s = buf.toString();
      HashMap<BitOptionKeys, ArrayList<String>> map = DataHandler.getBitOptions(s);
      if (map != null) {
        ArrayList<String> suffixe = map.get(BitOptionKeys.SUFFIXES);
        for (int j = 0; j < suffixe.size(); j++) {
          this.gameReference.recallAll();
          ArrayList<Tile> tilescopy = this.copytiles(this.tiles);
          ArrayList<String> buchstaben = this.addArrayLists(list.get(i), suffixe.get(j));
          ArrayList<Square> placedsquares =
              this.checkMatch(buchstaben, new ArrayList<Square>(), tilescopy);
          if (placedsquares != null) {
            if (gameReference.checkMove()) {
              PossibleMove pm = new PossibleMove(placedsquares, gameReference.getTurnValue());
              this.moves.add(pm);
            }
          }
        }
      }
    }
  }

  /**
   * This method checks if another player has already put something on the board or if the AI has
   * the first move, if the board is empty it returns true, otherwise false.
   *
   * @author sisselha
   * @return
   */
  public boolean boardIsEmpty() {
    for (Square s : this.gameReference.getBoard().getSquareList()) {
      if (s.isTaken()) {
        return false;
      }
    }
    return true;
  }

  /**
   * This method first splits a string into its individual letters so that each individual string
   * has a length of one. Then the strings are added to the ArrayList. The ArrayList consists of
   * strings of length 1.
   *
   * @author sisselha
   * @param list1 Arraylist of String
   * @param suf String of the suffix
   * @return
   */
  public ArrayList<String> addArrayLists(ArrayList<String> list1, String suf) {
    ArrayList<String> list = new ArrayList<String>();
    for (int i = 0; i < list1.size(); i++) {
      list.add(list1.get(i));
    }
    char[] c = suf.toCharArray();
    for (int i = 0; i < c.length; i++) {
      list.add(String.valueOf(c[i]));
    }
    return list;
  }

  /**
   * This method recursively checks the last element of the string array list to see if the letter
   * in the list matches a tile in the TileArrayList, if it does, the StringArrayList is reduced by
   * the last element and the matching tile is removed from the TileArrayList.Then the matching tile
   * is placed on the square and added to the Square-ArrayList. Afterwards the method is called
   * again until the String-ArrayList is empty and the placed squares are returned.
   *
   * @author sisselha
   * @param letters The word to be placed on the board is stored in a StringArrayList.
   * @param squares
   * @param tiles
   * @return
   */
  public ArrayList<Square> checkMatch(
      ArrayList<String> letters, ArrayList<Square> squares, ArrayList<Tile> tiles) {
    if (letters.size() == 0) {
      return squares;
    } else {
      for (int i = 0; i < tiles.size(); i++) {
        char[] c = letters.get(letters.size() - 1).toCharArray();
        if (c[0] == tiles.get(i).getLetter()) {
          this.gameReference.placeTile(8 + squares.size(), 8, tiles.get(i));
          Square square = new Square(8 + squares.size(), 8);
          square.placeTile(tiles.get(i));
          squares.add(square);
          tiles.remove(i);
          letters.remove(letters.size() - 1);
          return this.checkMatch(letters, squares, tiles);
        }
      }
      return null;
    }
  }
  /**
   * @author sisselha
   * @return
   */
  public ArrayList<Square> getthebestmove() {
    if (this.boardIsEmpty()) {
      this.getTheFirstMove();
    } else {
      this.setbestmoves();
    }
    PossibleMove max = new PossibleMove(null, 0);
    System.out.println(moves.size() + "m�gliche Z�ge");
    if (this.difficult) {
      for (PossibleMove pm : moves) {
        if (pm.getValue() > max.getValue()) {
          max = pm;
        }
      }
      return max.getsquares();
    } else {
      if (this.moves.size() != 0) {
        int counter = 0;
        int sumValue = 0;
        for (PossibleMove pm : moves) {
          sumValue = sumValue + pm.getValue();
          counter++;
        }
        int avg = 0;
        if (counter != 0) {
          avg = (sumValue / counter);
        }
        System.out.println("Average: " + avg);
        for (PossibleMove pm : moves) {
          if (pm.getValue() == avg) {
            System.out.println("Averagemove returned");
            return pm.getsquares();
          }
        }
        return moves.get(0).getsquares();
      }
      return null;
    }
  }

  public void printwords() {
    for (int i = 0; i < tiles.size(); i++) {
      System.out.print("  " + tiles.get(i).getLetter());
    }
  }

  public void printBoard() {
    System.out.println();
    for (int y = 15; y > 0; y--) {
      System.out.print(y + "\t");
      for (int x = 1; x < 16; x++) {
        Square s = gameReference.getBoard().getSquare(x, y);
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
        "\n\nThere are "
            + gameReference.getBag().getRemainingCount()
            + " tiles remaining in the bag.");
  }
  /**
   * Initializes a complete list of all possible moves that can be placed on the board with the
   * given tiles in the bag to the words that are already lying on the board
   *
   * @author sisselha
   * @param swapTiles
   */
  public void setbestmoves() {
    this.moves = new ArrayList<PossibleMove>();
    this.scanBoardForWords();
    ArrayList<Square> placedsquare = new ArrayList<Square>();
    for (int i = 0; i < words.size(); i++) {
      Word wort = words.get(i);
      ArrayList<Square> squares = wort.getsquares();
      StringBuffer buffer = new StringBuffer();
      for (int j = 0; j < squares.size(); j++) {
        buffer.append(squares.get(j).getTile().getLetter());
      }
      String s = buffer.toString();
      HashMap<BitOptionKeys, ArrayList<String>> map = DataHandler.getBitOptions(s);
      if (map == null) {
        break;
      }
      ArrayList<String> prefixe = map.get(BitOptionKeys.PREFIXES);
      ArrayList<String> suffixe = map.get(BitOptionKeys.SUFFIXES);
      for (int j = 0; j < prefixe.size(); j++) {
        String pre = prefixe.get(j);
        String suf = suffixe.get(j);
        this.gameReference.recallAll();
        char[] laengepre = pre.toCharArray();
        char[] laengesuf = suf.toCharArray();
        ArrayList<String> buchstaben = prefixplussuffix(pre, suf);
        if (wort.getColumn()) {
          ArrayList<Tile> tilescopy = this.copytiles(this.tiles);
          if (checkabove(laengepre.length, wort, this.gameReference.getBoard())
              && checkdown(laengesuf.length, wort, this.gameReference.getBoard())) {
            placedsquare =
                checktilesvertical(
                    buchstaben,
                    new ArrayList<Square>(),
                    tilescopy,
                    wort,
                    laengepre.length,
                    laengesuf.length,
                    this.gameReference.getBoard());
            if (placedsquare != null) {
              this.gameReference.recallAll();
              for (Square x : placedsquare) {
                this.gameReference.placeTile(x.getX(), x.getY(), x.getTile());
              }
              if (gameReference.checkMove()) {
                PossibleMove pm = new PossibleMove(placedsquare, gameReference.getTurnValue());
                this.moves.add(pm);
              } else {
              }
            }
          }
        } else {
          ArrayList<Tile> tilescopy = this.copytiles(this.tiles);
          if (checkleft(laengepre.length, wort, this.gameReference.getBoard())
              && checkright(laengesuf.length, wort, this.gameReference.getBoard())) {
            placedsquare =
                checktileshorizontal(
                    buchstaben,
                    new ArrayList<Square>(),
                    tilescopy,
                    wort,
                    laengepre.length,
                    laengesuf.length,
                    this.gameReference.getBoard());
            if (placedsquare != null) {
              if (gameReference.checkMove()) {
                PossibleMove pm = new PossibleMove(placedsquare, gameReference.getTurnValue());
                this.moves.add(pm);
              }
            }
          }
        }
      }
    }
    if (this.moves.size() > 0) {
      return;
    } else {
      this.scanBoardforLetters();
    }
    for (int i = 0; i < words.size(); i++) {
      ArrayList<Tile> tiles = this.copytiles(this.tiles);
      for (int j = 0; j < tiles.size(); j++) {
        Word w = words.get(i);
        if (w.getColumn()) {
          StringBuffer buffer = new StringBuffer();
          buffer.append(String.valueOf(w.getsquares().get(0).getTile().getLetter()));
          String s = buffer.toString();
          HashMap<BitOptionKeys, ArrayList<String>> map = DataHandler.getBitOptions(s);
          if (map == null) {
            break;
          }
          ArrayList<String> suffixe = map.get(BitOptionKeys.SUFFIXES);
          ArrayList<String> prefixe = map.get(BitOptionKeys.PREFIXES);
          for (int z = 0; z < suffixe.size(); z++) {
            ArrayList<Tile> tiles2 = this.copytiles(tiles);
            char[] prefix = prefixe.get(z).toCharArray();
            char[] suffix = suffixe.get(z).toCharArray();
            ArrayList<String> buchstaben = this.prefixplussuffix(prefixe.get(z), suffixe.get(z));
            ArrayList<Square> squares = new ArrayList<Square>();
            this.gameReference.recallAll();
            if (this.checkabove(prefix.length, w, this.gameReference.getBoard())
                && this.checkdown(suffix.length, w, this.gameReference.getBoard())) {
              squares =
                  this.checktilesvertical(
                      buchstaben,
                      new ArrayList<Square>(),
                      tiles2,
                      w,
                      prefix.length,
                      suffix.length,
                      this.gameReference.getBoard());
            }
            if (squares != null && squares.size() > 1) {
              for (Square ss : squares) {}
              if (gameReference.checkMove()) {
                PossibleMove pm = new PossibleMove(squares, gameReference.getTurnValue());
                this.moves.add(pm);
              }
            }
          }
        } else {
          StringBuffer buffer = new StringBuffer();
          buffer.append(String.valueOf(w.getsquares().get(0).getTile().getLetter()));
          String s = buffer.toString();
          HashMap<BitOptionKeys, ArrayList<String>> map = DataHandler.getBitOptions(s);
          if (map == null) {
            break;
          }
          ArrayList<String> suffixe = map.get(BitOptionKeys.SUFFIXES);
          ArrayList<String> prefixe = map.get(BitOptionKeys.PREFIXES);
          for (int z = 0; z < suffixe.size(); z++) {
            char[] prefix = prefixe.get(z).toCharArray();
            char[] suffix = suffixe.get(z).toCharArray();
            ArrayList<Tile> tiles2 = this.copytiles(tiles);
            ArrayList<String> buchstaben = this.prefixplussuffix(prefixe.get(z), suffixe.get(z));
            ArrayList<Square> squares = new ArrayList<Square>();
            this.gameReference.recallAll();
            if (this.checkleft(prefix.length, w, this.gameReference.getBoard())
                && this.checkright(suffix.length, w, this.gameReference.getBoard())) {
              squares =
                  this.checktileshorizontal(
                      buchstaben,
                      new ArrayList<Square>(),
                      tiles2,
                      w,
                      prefix.length,
                      suffix.length,
                      this.gameReference.getBoard());
            }
            if (squares != null && squares.size() > 1) {

              for (Square ss : squares) {}

              if (gameReference.checkMove()) {
                PossibleMove pm = new PossibleMove(squares, gameReference.getTurnValue());
                this.moves.add(pm);
              }
            }
          }
        }
      }
    }
  }

  public ArrayList<Square> anlegen(
      ArrayList<Square> squares,
      ArrayList<Tile> tiles,
      ArrayList<String> buchstaben,
      int x,
      int y,
      boolean colum) {
    if (buchstaben.size() == 0) {
      return squares;
    } else {
      if (colum) {
        if (this.gameReference.getBoard().getSquare(x, y - buchstaben.size()).isTaken()) {
          String s = buchstaben.get(buchstaben.size() - 1);
          char[] c = s.toCharArray();
          if (c[0]
              == this.gameReference
                  .getBoard()
                  .getSquare(x, y - buchstaben.size())
                  .getTile()
                  .getLetter()) {
            buchstaben.remove(buchstaben.size() - 1);
            return this.anlegen(squares, tiles, buchstaben, x, y, colum);
          } else {
            return null;
          }
        } else {
          for (int i = 0; i < tiles.size(); i++) {
            String s = buchstaben.get(buchstaben.size() - 1);
            char[] c = s.toCharArray();
            if (c[0] == tiles.get(i).getLetter()) {

              this.gameReference.placeTile(x, y - buchstaben.size(), tiles.get(i));
              Square square = new Square(x, y - buchstaben.size());
              square.placeTile(tiles.get(i));
              squares.add(square);
              buchstaben.remove(buchstaben.size() - 1);
              tiles.remove(i);
              return this.anlegen(squares, tiles, buchstaben, x, y, colum);
            }
          }
          return null;
        }

      } else {
        if (this.gameReference.getBoard().getSquare(x + buchstaben.size() - 1, y).isTaken()) {
          String s = buchstaben.get(buchstaben.size() - 1);
          char[] c = s.toCharArray();
          if (c[0]
              == this.gameReference
                  .getBoard()
                  .getSquare(x + buchstaben.size() - 1, y)
                  .getTile()
                  .getLetter()) {
            buchstaben.remove(buchstaben.size() - 1);
            return this.anlegen(squares, tiles, buchstaben, x, y, colum);
          } else {
            return null;
          }
        } else {
          for (int i = 0; i < tiles.size(); i++) {
            String s = buchstaben.get(buchstaben.size() - 1);
            char[] c = s.toCharArray();
            if (c[0] == tiles.get(i).getLetter()) {
              this.gameReference.placeTile(x + buchstaben.size() - 1, y, tiles.get(i));
              Square square = new Square(x + buchstaben.size() - 1, y);
              square.placeTile(tiles.get(i));
              squares.add(square);
              buchstaben.remove(buchstaben.size() - 1);
              tiles.remove(i);
              return this.anlegen(squares, tiles, buchstaben, x, y, colum);
            }
          }
          return null;
        }
      }
    }
  }
  /**
   * copies an arraylist and returns it
   *
   * @author sisselha
   * @param swapTiles
   */
  public ArrayList<Tile> copytiles(ArrayList<Tile> list) {
    ArrayList<Tile> tile = new ArrayList<Tile>();
    for (int i = 0; i < list.size(); i++) {
      tile.add(list.get(i));
    }
    return tile;
  }
  /**
   * Checks if the prefix or suffix of the word to be created fits the field
   *
   * @author sisselha
   * @param swapTiles
   */
  public boolean checkabove(int pre, Word wort, Board board) {
    if ((wort.getBeginningY() + pre) > 15) {
      return false;
    } else {
      return true;
    }
  }

  public boolean checkdown(int suf, Word wort, Board board) {
    if ((wort.getEndingY() - suf) < 1) {
      return false;
    } else {
      return true;
    }
  }

  public boolean checkright(int suf, Word wort, Board board) {
    if ((wort.getEndingX() + suf) > 15) {
      return false;
    } else {
      return true;
    }
  }

  public boolean checkleft(int pre, Word wort, Board board) {
    if ((wort.getBeginningX() - pre) < 1) {
      return false;
    } else {
      return true;
    }
  }
  /**
   * Converts a string into a String ArrayList
   *
   * @author sisselha
   * @param swapTiles
   */
  public ArrayList<String> dividestring(String s) {
    char[] feld1 = s.toCharArray();
    ArrayList<String> letters = new ArrayList<String>();
    for (int z = 0; z < feld1.length; z++) {
      letters.add(String.valueOf(feld1[z]));
    }
    return letters;
  }
  /**
   * Converts a string into a String ArrayList
   *
   * @author sisselha
   * @param swapTiles
   */
  public ArrayList<String> prefixplussuffix(String pre, String suf) {
    char[] feld1 = pre.toCharArray();
    char[] feld2 = suf.toCharArray();
    ArrayList<String> letters = new ArrayList<String>();
    for (int z = 0; z < feld1.length; z++) {
      letters.add(String.valueOf(feld1[z]));
    }
    for (int z = 0; z < feld2.length; z++) {
      letters.add(String.valueOf(feld2[z]));
    }
    return letters;
  }

  public void printtiles(ArrayList<Tile> list) {
    System.out.println("die Tiles sind:");
    for (int i = 0; i < list.size(); i++) {
      System.out.print(" " + String.valueOf(list.get(i).getLetter()));
    }
    System.out.println("das waren die Tiles");
  }

  public void printword(ArrayList<String> list) {
    for (int i = 0; i < list.size(); i++) {
      System.out.print(" " + list.get(i));
    }
  }

  public ArrayList<Square> checktilesvertical(
      ArrayList<String> letters,
      ArrayList<Square> placedsquares,
      ArrayList<Tile> remainingtiles,
      Word wort,
      int pre,
      int suf,
      Board board) {
    if (letters.size() == 0) {
      return placedsquares;
    } else {
      if (letters.size() <= pre) {
        if (board
            .getSquare(wort.getBeginningX(), wort.getBeginningY() + (pre - letters.size() + 1))
            .isTaken()) {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          if (board
                  .getSquare(
                      wort.getBeginningX(), wort.getBeginningY() + (pre - letters.size() + 1))
                  .getTile()
                  .getLetter()
              == c[0]) {
            letters.remove(letters.size() - 1);
            return checktilesvertical(
                letters, placedsquares, remainingtiles, wort, pre, suf, board);
          } else {
            return null;
          }
        } else {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          for (int i = 0; i < remainingtiles.size(); i++) {
            if (remainingtiles.get(i).getLetter() == c[0]) {
              this.gameReference.placeTile(
                  wort.getBeginningX(),
                  wort.getBeginningY() + (pre - letters.size() + 1),
                  remainingtiles.get(i));
              Square square =
                  new Square(
                      wort.getBeginningX(), wort.getBeginningY() + (pre - letters.size() + 1));
              square.placeTile(remainingtiles.get(i));
              remainingtiles.remove(i);
              placedsquares.add(square);
              letters.remove(letters.size() - 1);
              return checktilesvertical(
                  letters, placedsquares, remainingtiles, wort, pre, suf, board);
            }
          }
        }
      } else {
        if (board.getSquare(wort.getBeginningX(), wort.getEndingY() - suf).isTaken()) {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          if (board.getSquare(wort.getBeginningX(), wort.getEndingY() - suf).getTile().getLetter()
              == c[0]) {
            letters.remove(letters.size() - 1);
            suf = suf - 1;
            return checktilesvertical(
                letters, placedsquares, remainingtiles, wort, pre, suf, board);
          } else {
            return null;
          }
        } else {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          for (int i = 0; i < remainingtiles.size(); i++) {
            if (remainingtiles.get(i).getLetter() == c[0]) {
              this.gameReference.placeTile(
                  wort.getBeginningX(), (wort.getEndingY() - suf), remainingtiles.get(i));
              Square square = new Square(wort.getBeginningX(), (wort.getEndingY() - suf));
              square.placeTile(remainingtiles.get(i));
              remainingtiles.remove(i);
              placedsquares.add(square);
              letters.remove(letters.size() - 1);
              suf = suf - 1;
              return checktilesvertical(
                  letters, placedsquares, remainingtiles, wort, pre, suf, board);
            }
          }
        }
      }
      return null;
    }
  }

  public ArrayList<Square> checktileshorizontal(
      ArrayList<String> letters,
      ArrayList<Square> placedsquares,
      ArrayList<Tile> remainingtiles,
      Word wort,
      int pre,
      int suf,
      Board board) {
    int size = letters.size();
    if (size == 0) {
      return placedsquares;
    } else {
      if (letters.size() <= pre) {
        if (board
            .getSquare((wort.getBeginningX() - pre) + letters.size() - 1, wort.getBeginningY())
            .isTaken()) {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          if (board
                  .getSquare(
                      (wort.getBeginningX() - pre) + letters.size() - 1, wort.getBeginningY())
                  .getTile()
                  .getLetter()
              == c[0]) {
            letters.remove(letters.size() - 1);
            return checktileshorizontal(
                letters, placedsquares, remainingtiles, wort, pre, suf, board);
          } else {
            return null;
          }
        } else {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          for (int i = 0; i < remainingtiles.size(); i++) {
            String s = String.valueOf(c);
            if (remainingtiles.get(i).getLetter() == c[0]) {
              this.gameReference.placeTile(
                  wort.getBeginningX() - pre + letters.size() - 1,
                  wort.getBeginningY(),
                  remainingtiles.get(i));
              Square square =
                  new Square(wort.getBeginningX() - pre + letters.size() - 1, wort.getBeginningY());
              square.placeTile(remainingtiles.get(i));
              remainingtiles.remove(i);
              placedsquares.add(square);
              letters.remove(letters.size() - 1);
              return checktileshorizontal(
                  letters, placedsquares, remainingtiles, wort, pre, suf, board);
            }
          }
        }
      } else {
        if (board.getSquare(wort.getEndingX() + suf, wort.getEndingY()).isTaken()) {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          if (board.getSquare(wort.getEndingX() + suf, wort.getEndingY()).getTile().getLetter()
              == c[0]) {
            letters.remove(letters.size() - 1);
            suf = suf - 1;
            return checktileshorizontal(
                letters, placedsquares, remainingtiles, wort, pre, suf, board);
          } else {
            return null;
          }
        } else {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          for (int i = 0; i < remainingtiles.size(); i++) {
            String s = String.valueOf(c[0]);
            if (remainingtiles.get(i).getLetter() == c[0]) {
              this.gameReference.placeTile(
                  wort.getEndingX() + suf, wort.getEndingY(), remainingtiles.get(i));
              Square square = new Square(wort.getEndingX() + suf, wort.getEndingY());
              square.placeTile(remainingtiles.get(i));
              remainingtiles.remove(i);
              placedsquares.add(square);
              letters.remove(letters.size() - 1);
              suf = suf - 1;
              return checktileshorizontal(
                  letters, placedsquares, remainingtiles, wort, pre, suf, board);
            }
          }
        }
      }
      return null;
    }
  }

  public void scanBoardForWords() {
    this.words = new ArrayList<Word>();
    for (int i = 0; i < this.gameReference.getBoard().getSquareList().size(); i++) {

      if (this.gameReference.getBoard().getSquareList().get(i).isTaken()) {
        Square lowerneighbour =
            this.gameReference
                .getBoard()
                .getLowerNeighbour(this.gameReference.getBoard().getSquareList().get(i));
        Square rightneighbour =
            this.gameReference
                .getBoard()
                .getRightNeighbour(this.gameReference.getBoard().getSquareList().get(i));
        if (lowerneighbour != null) {
          if ((lowerneighbour.isTaken())
              && lowerneighbour != null
              && (!this.gameReference.getBoard().getSquareList().get(i).isWithinColumnWord())) {
            Square upperneighbour =
                this.gameReference
                    .getBoard()
                    .getUpperNeighbour(this.gameReference.getBoard().getSquareList().get(i));
            if (upperneighbour != null) {
              while (upperneighbour.isTaken() && (!upperneighbour.isWithinColumnWord())) {
                upperneighbour = this.gameReference.getBoard().getUpperNeighbour(upperneighbour);
                if (upperneighbour == null) {
                  break;
                }
              }
            }
            Word word = new Word();
            ArrayList<Square> squares = new ArrayList<Square>();
            buffer = new StringBuffer();
            if (upperneighbour != null) {
              lowerneighbour = this.gameReference.getBoard().getLowerNeighbour(upperneighbour);
            }
            if (lowerneighbour != null) {
              word.setBeginningX(lowerneighbour.getX());
              word.setBeginningY(lowerneighbour.getY());
              while ((lowerneighbour.isTaken()) && (!lowerneighbour.isWithinColumnWord())) {
                buffer.append(lowerneighbour.getTile().getLetter());
                squares.add(lowerneighbour);
                word.setEndingX(lowerneighbour.getX());
                word.setEndingY(lowerneighbour.getY());
                lowerneighbour = this.gameReference.getBoard().getLowerNeighbour(lowerneighbour);
                if (lowerneighbour == null) {
                  break;
                }
              }
            }
            String nword = buffer.toString();
            if (buffer.length() > 1) {
              if (DataHandler.checkWord(nword)) {
                word.setSquares(squares);
                word.setColumn(true);
                this.words.add(word);
                this.setWithinColumnWord(squares);
              }
            }
          }
        }
        if (rightneighbour != null) {
          if (rightneighbour.isTaken()
              && (!this.gameReference.getBoard().getSquareList().get(i).isWithinRowWord())) {
            Word word = new Word();
            buffer = new StringBuffer();
            ArrayList<Square> squares = new ArrayList<Square>();
            buffer.append(
                this.gameReference.getBoard().getSquareList().get(i).getTile().getLetter());
            squares.add(this.gameReference.getBoard().getSquareList().get(i));
            word.setBeginningX(this.gameReference.getBoard().getSquareList().get(i).getX());
            word.setBeginningY(this.gameReference.getBoard().getSquareList().get(i).getY());
            if (rightneighbour != null) {
              while (rightneighbour.isTaken() && (!rightneighbour.isWithinRowWord())) {
                buffer.append(rightneighbour.getTile().getLetter());
                squares.add(rightneighbour);
                word.setEndingX(rightneighbour.getX());
                word.setEndingY(rightneighbour.getY());
                rightneighbour = this.gameReference.getBoard().getRightNeighbour(rightneighbour);
                if (rightneighbour == null) {
                  break;
                }
              }
            }
            String nword = buffer.toString();
            if (buffer.length() > 1) {
              if (DataHandler.checkWord(nword)) {
                word.setSquares(squares);
                word.setColumn(false);
                this.words.add(word);
                this.setWithinRowWord(squares);
              }
            }
          }
        }
      }
    }
  }

  public void setWithinColumnWord(ArrayList<Square> squares) {
    for (int i = 0; i < squares.size(); i++) {
      int x = squares.get(i).getX();
      int y = squares.get(i).getY();
      this.gameReference.getBoard().getSquare(x, y).setWithinColumnWord();
    }
  }

  public void setWithinRowWord(ArrayList<Square> squares) {
    for (int i = 0; i < squares.size(); i++) {
      int x = squares.get(i).getX();
      int y = squares.get(i).getY();
      this.gameReference.getBoard().getSquare(x, y).setWithinRowWord();
    }
  }

  public void updateGameSession(GameState state) {
    this.gameReference.synchronise(state);
  }

  public void makeMove() {
    Timer timer = new Timer();
    timer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            tiles = gameReference.getPlayer().getRack().getTiles();
            printtiles(tiles);
            if (!dictionaryInitialized) {
              DataHandler.botDictionaryFile(dictionary);
              dictionaryInitialized = true;
            }
            ArrayList<Square> squares = getthebestmove();
            if (squares != null) {
              gameReference.recallAll();
              for (Square s : squares) {
                Tile t = s.getTile();
                t.setPlacedTemporarily(false);
                t.setPlacedFinally(true);
                gameReference.placeTile(s.getX(), s.getY(), t);
              }
            } else {
              gameReference.skipTurn();
            }
            if (gameReference.checkMove()) {
              gameReference.makePlay();
            }
          }
        },
        5000);
  }

  public ArrayList<Word> getwords() {
    return this.words;
  }

  public void scanBoardforLetters() {
    for (int i = 0; i < this.gameReference.getBoard().getSquareList().size(); i++) {
      if (this.gameReference.getBoard().getSquareList().get(i).isTaken()) {
        Square rightneighbour =
            this.gameReference
                .getBoard()
                .getRightNeighbour(this.gameReference.getBoard().getSquareList().get(i));
        Square lowerneighbour =
            this.gameReference
                .getBoard()
                .getLowerNeighbour(this.gameReference.getBoard().getSquareList().get(i));
        if ((rightneighbour != null) && (lowerneighbour != null)) {
          if (rightneighbour.isTaken() && (!lowerneighbour.isTaken())) {
            System.out.println(rightneighbour.getTile().getLetter());
            int x = this.gameReference.getBoard().getSquareList().get(i).getX();
            int y = this.gameReference.getBoard().getSquareList().get(i).getY();
            char c = this.gameReference.getBoard().getSquareList().get(i).getTile().getLetter();
            Word w = new Word();
            w.setBeginningX(x);
            w.setEndingX(x);
            w.setBeginningY(y);
            w.setEndingY(y);
            w.setColumn(true);
            ArrayList<Square> squares = new ArrayList<Square>();
            squares.add(this.gameReference.getBoard().getSquareList().get(i));
            w.setSquares(squares);
            words.add(w);
            System.out.println("Colum geaddet: " + c);
          } else if ((!rightneighbour.isTaken()) && lowerneighbour.isTaken()) {
            System.out.println(lowerneighbour.getTile().getLetter());
            int x = this.gameReference.getBoard().getSquareList().get(i).getX();
            int y = this.gameReference.getBoard().getSquareList().get(i).getY();
            char c = this.gameReference.getBoard().getSquareList().get(i).getTile().getLetter();
            Word w = new Word();
            w.setBeginningX(x);
            w.setEndingX(x);
            w.setBeginningY(y);
            w.setEndingY(y);
            w.setColumn(false);
            ArrayList<Square> squares = new ArrayList<Square>();
            squares.add(this.gameReference.getBoard().getSquareList().get(i));
            w.setSquares(squares);
            words.add(w);
          }
        }
      }
    }
  }

  public Player getPlayer() {
    return gameReference.getPlayer();
  }

  public boolean getDifficulty() {
    return difficult;
  }
}
