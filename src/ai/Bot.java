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

/**
 * an AI instance of the game that calculates a move using given methods.
 *
 * @author sisselha
 */
public class Bot {

  private ArrayList<PossibleMove> moves;
  private ArrayList<Word> words;
  private GameSession gameReference;
  private StringBuffer buffer;
  private ArrayList<Tile> tiles;
  private boolean difficult;
  private boolean dictionaryInitialized = false;
  private File dictionary;

  /**
   * Constructor: creates a new AI Object.
   *
   * @author sisselha
   * @param username username
   * @param difficult difficulty of the AI
   */
  public Bot(String username, boolean difficult) {
    this.gameReference = new GameSession(new Player(username));
    this.difficult = difficult;
  }

  /**
   * @author tikrause
   * @param dictionary
   */
  public void initializeAI(File dictionary) {
    this.dictionary = dictionary;
    this.gameReference.setBag(Client.getGameSession().getBag());
    this.gameReference.getPlayer().getRack().initialDraw();
    Server.updateRackOfBotPlayer(this);
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
          this.gameReference.recallAll(true);
          ArrayList<Tile> tilescopy = this.copyTiles(this.tiles);
          ArrayList<String> letters = this.addArrayLists(list.get(i), suffixe.get(j));
          ArrayList<Square> placedsquares =
              this.checkMatch(letters, new ArrayList<Square>(), tilescopy);
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
   * @return if board is empty or not
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
   * @return updated ArrayList
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
   * @param squares The squres to be placed for this move
   * @param tiles the tiles that the player has available for laying
   * @return the complete squares that are placed on the board for the selected word are stored in
   *     an ArrayList
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
   * this method iterates through all possible moves the AI can make and chooses the move with the
   * best score when the difficulty level is set to difficult. If the difficulty level is easy, then
   * it returns a move that is in the average
   *
   * @author sisselha
   * @return the ArrayList of squares for the selected move is returned
   */
  public ArrayList<Square> getTheBestMove() {
    if (this.boardIsEmpty()) {
      this.getTheFirstMove();
    } else {
      this.setMoves();
    }
    PossibleMove max = new PossibleMove(null, 0);
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
        for (PossibleMove pm : moves) {
          if (pm.getValue() == avg) {
            return pm.getsquares();
          }
        }
        return moves.get(0).getsquares();
      }
      return null;
    }
  }

  /**
   * Initializes a complete list of all possible moves that can be placed on the board with the
   * given tiles in the bag to the words that are already lying on the board.
   *
   * @author sisselha
   */
  public void setMoves() {
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
        this.gameReference.recallAll(true);
        char[] laengepre = pre.toCharArray();
        char[] laengesuf = suf.toCharArray();
        ArrayList<String> buchstaben = prefixPlusSuffix(pre, suf);
        if (wort.getColumn()) {
          ArrayList<Tile> tilescopy = this.copyTiles(this.tiles);
          if (checkAbove(laengepre.length, wort, this.gameReference.getBoard())
              && checkDown(laengesuf.length, wort, this.gameReference.getBoard())) {
            placedsquare =
                checktilesVertical(
                    buchstaben,
                    new ArrayList<Square>(),
                    tilescopy,
                    wort,
                    laengepre.length,
                    laengesuf.length,
                    this.gameReference.getBoard());
            if (placedsquare != null) {
              this.gameReference.recallAll(true);
              for (Square x : placedsquare) {
                this.gameReference.placeTile(x.getX(), x.getY(), x.getTile());
              }
              if (gameReference.checkMove()) {
                PossibleMove pm = new PossibleMove(placedsquare, gameReference.getTurnValue());
                this.moves.add(pm);
              }
            }
          }
        } else {
          ArrayList<Tile> tilescopy = this.copyTiles(this.tiles);
          if (checkLeft(laengepre.length, wort, this.gameReference.getBoard())
              && checkRight(laengesuf.length, wort, this.gameReference.getBoard())) {
            placedsquare =
                checkTilesHorizontal(
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
      this.scanBoardForLetters();
    }
    for (int i = 0; i < words.size(); i++) {
      ArrayList<Tile> tiles = this.copyTiles(this.tiles);
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
            ArrayList<Tile> tiles2 = this.copyTiles(tiles);
            char[] prefix = prefixe.get(z).toCharArray();
            char[] suffix = suffixe.get(z).toCharArray();
            ArrayList<String> buchstaben = this.prefixPlusSuffix(prefixe.get(z), suffixe.get(z));
            ArrayList<Square> squares = new ArrayList<Square>();
            this.gameReference.recallAll(true);
            if (this.checkAbove(prefix.length, w, this.gameReference.getBoard())
                && this.checkDown(suffix.length, w, this.gameReference.getBoard())) {
              squares =
                  this.checktilesVertical(
                      buchstaben,
                      new ArrayList<Square>(),
                      tiles2,
                      w,
                      prefix.length,
                      suffix.length,
                      this.gameReference.getBoard());
            }
            if (squares != null && squares.size() > 1) {
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
            ArrayList<Tile> tiles2 = this.copyTiles(tiles);
            ArrayList<String> buchstaben = this.prefixPlusSuffix(prefixe.get(z), suffixe.get(z));
            ArrayList<Square> squares = new ArrayList<Square>();
            this.gameReference.recallAll(true);
            if (this.checkLeft(prefix.length, w, this.gameReference.getBoard())
                && this.checkRight(suffix.length, w, this.gameReference.getBoard())) {
              squares =
                  this.checkTilesHorizontal(
                      buchstaben,
                      new ArrayList<Square>(),
                      tiles2,
                      w,
                      prefix.length,
                      suffix.length,
                      this.gameReference.getBoard());
            }
            if (squares != null && squares.size() > 1) {

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

  /**
   * copies an arraylist and returns it.
   *
   * @author sisselha
   * @param list list of the tiles
   * @return copied list of tiles
   */
  public ArrayList<Tile> copyTiles(ArrayList<Tile> list) {
    ArrayList<Tile> tile = new ArrayList<Tile>();
    for (int i = 0; i < list.size(); i++) {
      tile.add(list.get(i));
    }
    return tile;
  }

  /**
   * Checks if the prefix of the word to be created fits the field.
   *
   * @author sisselha
   * @param prefix the prefix of the word, which is tried to be created with the given tiles
   * @param word the word that is already on the board
   * @param board board
   * @return returns if the word is in the board or not
   */
  public boolean checkAbove(int prefix, Word word, Board board) {
    if ((word.getBeginningY() + prefix) > 15) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Checks if the suffix of the word to be created fits the field.
   *
   * @author sisselha
   * @param suffix the suffix of the word, which is tried to be created with the given tiles
   * @param word the word that is already on the board
   * @param board board
   * @return returns if the word is in the board or not
   */
  public boolean checkDown(int suffix, Word word, Board board) {
    if ((word.getEndingY() - suffix) < 1) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Checks if the suffix of the word to be created fits the field.
   *
   * @author sisselha
   * @param suffix the suffix of the word, which is tried to be created with the given tiles
   * @param word the word that is already on the board
   * @param board board
   * @return returns if the word is in the board or not
   */
  public boolean checkRight(int suffix, Word word, Board board) {
    if ((word.getEndingX() + suffix) > 15) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Checks if the prefix of the word to be created fits the field.
   *
   * @author sisselha
   * @param prefix the prefix of the word, which is tried to be created with the given tiles
   * @param word the word that is already on the board
   * @param board board
   * @return returns if the word is in the board or not
   */
  public boolean checkLeft(int prefix, Word word, Board board) {
    if ((word.getBeginningX() - prefix) < 1) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Converts a string into a String ArrayList.
   *
   * @author sisselha
   * @param s string to be converted
   * @return letters divided into a String ArrayList
   */
  public ArrayList<String> divideString(String s) {
    char[] feld1 = s.toCharArray();
    ArrayList<String> letters = new ArrayList<String>();
    for (int z = 0; z < feld1.length; z++) {
      letters.add(String.valueOf(feld1[z]));
    }
    return letters;
  }

  /**
   * converts the prefix and suffix of a word into a String ArrayList.
   *
   * @author sisselha
   * @param pre the prefix of the word
   * @param suf the suffix of the word
   * @return divided String ArrayList of prefix and suffix
   */
  public ArrayList<String> prefixPlusSuffix(String pre, String suf) {
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

  /**
   * This method recursively checks the last element of the string array list to see if the letter
   * in the list matches a tile in the TileArrayList, if it does, the StringArrayList is reduced by
   * the last element and the matching tile is removed from the TileArrayList.Then the matching tile
   * is placed on the square and added to the Square-ArrayList. Afterwards the method is called
   * again until the String-ArrayList is empty and the placed squares are returned. this method is
   * for words placed vertically on the board
   *
   * @author sisselha
   * @param letters The word to be placed on the board is stored in a StringArrayList.
   * @param placedsquares The squares to be placed for this move
   * @param remainingtiles the tiles that the player has available for laying
   * @param word the word that is already on the board
   * @param pre the length of the prefix
   * @param suf the length of the suffix
   * @param board board
   * @return the complete squares that are placed on the board for the selected word are stored in
   *     an ArrayList
   */
  public ArrayList<Square> checktilesVertical(
      ArrayList<String> letters,
      ArrayList<Square> placedsquares,
      ArrayList<Tile> remainingtiles,
      Word word,
      int pre,
      int suf,
      Board board) {
    if (letters.size() == 0) {
      return placedsquares;
    } else {
      if (letters.size() <= pre) {
        if (board
            .getSquare(word.getBeginningX(), word.getBeginningY() + (pre - letters.size() + 1))
            .isTaken()) {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          if (board
                  .getSquare(
                      word.getBeginningX(), word.getBeginningY() + (pre - letters.size() + 1))
                  .getTile()
                  .getLetter()
              == c[0]) {
            letters.remove(letters.size() - 1);
            return checktilesVertical(
                letters, placedsquares, remainingtiles, word, pre, suf, board);
          } else {
            return null;
          }
        } else {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          for (int i = 0; i < remainingtiles.size(); i++) {
            if (remainingtiles.get(i).getLetter() == c[0]) {
              this.gameReference.placeTile(
                  word.getBeginningX(),
                  word.getBeginningY() + (pre - letters.size() + 1),
                  remainingtiles.get(i));
              Square square =
                  new Square(
                      word.getBeginningX(), word.getBeginningY() + (pre - letters.size() + 1));
              square.placeTile(remainingtiles.get(i));
              remainingtiles.remove(i);
              placedsquares.add(square);
              letters.remove(letters.size() - 1);
              return checktilesVertical(
                  letters, placedsquares, remainingtiles, word, pre, suf, board);
            }
          }
        }
      } else {
        if (board.getSquare(word.getBeginningX(), word.getEndingY() - suf).isTaken()) {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          if (board.getSquare(word.getBeginningX(), word.getEndingY() - suf).getTile().getLetter()
              == c[0]) {
            letters.remove(letters.size() - 1);
            suf = suf - 1;
            return checktilesVertical(
                letters, placedsquares, remainingtiles, word, pre, suf, board);
          } else {
            return null;
          }
        } else {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          for (int i = 0; i < remainingtiles.size(); i++) {
            if (remainingtiles.get(i).getLetter() == c[0]) {
              this.gameReference.placeTile(
                  word.getBeginningX(), (word.getEndingY() - suf), remainingtiles.get(i));
              Square square = new Square(word.getBeginningX(), (word.getEndingY() - suf));
              square.placeTile(remainingtiles.get(i));
              remainingtiles.remove(i);
              placedsquares.add(square);
              letters.remove(letters.size() - 1);
              suf = suf - 1;
              return checktilesVertical(
                  letters, placedsquares, remainingtiles, word, pre, suf, board);
            }
          }
        }
      }
      return null;
    }
  }

  /**
   * This method recursively checks the last element of the string array list to see if the letter
   * in the list matches a tile in the TileArrayList, if it does, the StringArrayList is reduced by
   * the last element and the matching tile is removed from the TileArrayList.Then the matching tile
   * is placed on the square and added to the Square-ArrayList. Afterwards the method is called
   * again until the String-ArrayList is empty and the placed squares are returned. this method is
   * for words placed horizontally on the board
   *
   * @author sisselha
   * @param letters The word to be placed on the board is stored in a StringArrayList.
   * @param placedsquares The squares to be placed for this move
   * @param remainingtiles the tiles that the player has available for laying
   * @param word the word that is already on the board
   * @param pre the length of the prefix
   * @param suf the length of the suffix
   * @param board board
   * @return the complete squares that are placed on the board for the selected word are stored in
   *     an ArrayList
   */
  public ArrayList<Square> checkTilesHorizontal(
      ArrayList<String> letters,
      ArrayList<Square> placedsquares,
      ArrayList<Tile> remainingtiles,
      Word word,
      int pre,
      int suf,
      Board board) {
    int size = letters.size();
    if (size == 0) {
      return placedsquares;
    } else {
      if (letters.size() <= pre) {
        if (board
            .getSquare((word.getBeginningX() - pre) + letters.size() - 1, word.getBeginningY())
            .isTaken()) {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          if (board
                  .getSquare(
                      (word.getBeginningX() - pre) + letters.size() - 1, word.getBeginningY())
                  .getTile()
                  .getLetter()
              == c[0]) {
            letters.remove(letters.size() - 1);
            return checkTilesHorizontal(
                letters, placedsquares, remainingtiles, word, pre, suf, board);
          } else {
            return null;
          }
        } else {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          for (int i = 0; i < remainingtiles.size(); i++) {
            if (remainingtiles.get(i).getLetter() == c[0]) {
              this.gameReference.placeTile(
                  word.getBeginningX() - pre + letters.size() - 1,
                  word.getBeginningY(),
                  remainingtiles.get(i));
              Square square =
                  new Square(word.getBeginningX() - pre + letters.size() - 1, word.getBeginningY());
              square.placeTile(remainingtiles.get(i));
              remainingtiles.remove(i);
              placedsquares.add(square);
              letters.remove(letters.size() - 1);
              return checkTilesHorizontal(
                  letters, placedsquares, remainingtiles, word, pre, suf, board);
            }
          }
        }
      } else {
        if (board.getSquare(word.getEndingX() + suf, word.getEndingY()).isTaken()) {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          if (board.getSquare(word.getEndingX() + suf, word.getEndingY()).getTile().getLetter()
              == c[0]) {
            letters.remove(letters.size() - 1);
            suf = suf - 1;
            return checkTilesHorizontal(
                letters, placedsquares, remainingtiles, word, pre, suf, board);
          } else {
            return null;
          }
        } else {
          char[] c = letters.get(letters.size() - 1).toCharArray();
          for (int i = 0; i < remainingtiles.size(); i++) {
            if (remainingtiles.get(i).getLetter() == c[0]) {
              this.gameReference.placeTile(
                  word.getEndingX() + suf, word.getEndingY(), remainingtiles.get(i));
              Square square = new Square(word.getEndingX() + suf, word.getEndingY());
              square.placeTile(remainingtiles.get(i));
              remainingtiles.remove(i);
              placedsquares.add(square);
              letters.remove(letters.size() - 1);
              suf = suf - 1;
              return checkTilesHorizontal(
                  letters, placedsquares, remainingtiles, word, pre, suf, board);
            }
          }
        }
      }
      return null;
    }
  }

  /**
   * Scans the whole board for already laid words and adds it to the ArrayList of words.
   *
   * @author sisselha
   */
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
            buffer = new StringBuffer();
            ArrayList<Square> squares = new ArrayList<Square>();
            buffer.append(
                this.gameReference.getBoard().getSquareList().get(i).getTile().getLetter());
            squares.add(this.gameReference.getBoard().getSquareList().get(i));
            Word word = new Word();
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

  /**
   * if a word lays on squares in a column the flag isWithinColumnWord has to be set.
   *
   * @author sisselha
   * @param squares ArrayList of the squares of the word
   */
  public void setWithinColumnWord(ArrayList<Square> squares) {
    for (int i = 0; i < squares.size(); i++) {
      int x = squares.get(i).getX();
      int y = squares.get(i).getY();
      this.gameReference.getBoard().getSquare(x, y).setWithinColumnWord();
    }
  }

  /**
   * if a word lays on squares in a row the flag isWithinRowWord has to be set.
   *
   * @author sisselha
   * @param squares ArrayList of the squares of the word
   */
  public void setWithinRowWord(ArrayList<Square> squares) {
    for (int i = 0; i < squares.size(); i++) {
      int x = squares.get(i).getX();
      int y = squares.get(i).getY();
      this.gameReference.getBoard().getSquare(x, y).setWithinRowWord();
    }
  }

  public void updateGameSession(GameState state) {
    this.gameReference.synchronize(state);
  }

  /**
   * makes the move for the Bot.
   *
   * @author sisselha
   */
  public void makeMove() {

    // get the current time
    long startTime = System.currentTimeMillis();

    tiles = gameReference.getPlayer().getRack().getTiles();
    if (!dictionaryInitialized) {
      DataHandler.botDictionaryFile(dictionary);
      dictionaryInitialized = true;
    }
    ArrayList<Square> squares = getTheBestMove();
    if (squares != null) {
      gameReference.recallAll(true);
      for (Square s : squares) {
        Tile t = s.getTile();
        t.setPlacedTemporarily(false);
        t.setPlacedFinally(true);
        gameReference.placeTile(s.getX(), s.getY(), t);
      }
    } else {
      gameReference.skipTurn();
    }

    // calculate the processing time via the difference of the current time and the start time
    long processingTime = System.currentTimeMillis() - startTime;

    // see if the processing time was longer than our required 5 seconds
    int delay = (int) (5000 - processingTime);

    // if no, wait until 5 seconds are reached
    if (delay > 0) {
      Timer timer = new Timer();
      timer.schedule(
          new TimerTask() {
            @Override
            public void run() {
              if (gameReference.checkMove()) {
                gameReference.makePlay();
              }
            }
          },
          delay);
    } else {
      // otherwise execute the move instantly
      if (gameReference.checkMove()) {
        gameReference.makePlay();
      }
    }
  }

  public ArrayList<Word> getWords() {
    return this.words;
  }

  /**
   * Scans the whole board for already laid letters and adds it to the ArrayList of words.
   *
   * @author sisselha
   */
  public void scanBoardForLetters() {
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
            int x = this.gameReference.getBoard().getSquareList().get(i).getX();
            int y = this.gameReference.getBoard().getSquareList().get(i).getY();
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
          } else if ((!rightneighbour.isTaken()) && lowerneighbour.isTaken()) {
            int x = this.gameReference.getBoard().getSquareList().get(i).getX();
            int y = this.gameReference.getBoard().getSquareList().get(i).getY();
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

  /**
   * getter Method for Player.
   *
   * @author sisselha
   * @return returns the Player Object
   */
  public Player getPlayer() {
    return gameReference.getPlayer();
  }

  /**
   * Getter-Method for Difficulty-Level of the AI.
   *
   * @author sisselha
   * @return the difficulty level
   */
  public boolean getDifficulty() {
    return difficult;
  }
}
