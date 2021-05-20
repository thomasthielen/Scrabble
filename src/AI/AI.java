package AI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import data.BitOptionKeys;
import data.DataHandler;
import gameentities.Avatar;
import gameentities.Board;
import gameentities.Player;
import gameentities.Rack;
import gameentities.Square;
import gameentities.Tile;
import session.GameSession;
import session.GameState;

public class AI {
  private ArrayList<PossibleMove> moves;
  private ArrayList<Word> words;
  private GameSession gameReference;
  private int turnValue;
  StringBuffer buffer;
  ArrayList<Tile> tiles;
  private Player player;

  public AI(String username) {
    this.gameReference = new GameSession(new Player(username), false);
    this.tiles = this.gameReference.getPlayer().getRack().getTiles();
  }

  public ArrayList<Square> getthebestmove() {
    this.setbestmoves();
    PossibleMove max = new PossibleMove(null, 0);
    for (PossibleMove pm : moves) {
      System.out.println(pm.getValue());
      if (pm.getValue() > max.getValue()) {
        max = pm;
      }
    }

    this.turnValue = max.getValue();
    return max.getsquares();
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

  public void setbestmoves() {
    this.moves = new ArrayList<PossibleMove>();
    this.scanBoardForWords(); // scan board for words
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
      ArrayList<String> prefixe = map.get(BitOptionKeys.PREFIXES);
      ArrayList<String> suffixe = map.get(BitOptionKeys.SUFFIXES);

      for (int j = 0; j < prefixe.size(); j++) {
        String pre = prefixe.get(j);
        String suf = suffixe.get(j);
        // System.out.println(pre+" "+s+" "+suf);
        char[] laengepre = pre.toCharArray();
        char[] laengesuf = suf.toCharArray();
        ArrayList<String> buchstaben = prefixplussuffix(pre, suf);

        // if()
        System.out.println("Wir überprüfufen jetzt " + pre + " " + s + " " + suf);
        System.out.println();
        this.printtiles(this.tiles);
        System.out.println();
        if (wort.getColumn()) {
          ArrayList<Tile> tilescopy = this.copytiles(this.tiles);
          // this.printtiles(tilescopy);
          if (checkabove(laengepre.length, wort, this.gameReference.getBoard())
              && checkabove(laengesuf.length, wort, this.gameReference.getBoard())) {
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
              for (Square x : placedsquare) {
                gameReference.placeTile(x.getX(), x.getY(), x.getTile());
                //								System.out.println("Buchstabe: "+String.valueOf(x.getTile().getLetter()+"
                // X: "+x.getX()+" Y: "+x.getY()));
                this.printBoard();
              }
              //							System.out.println("Squares");
              this.gameReference.recallAll();
              for (Square x : placedsquare) {
                this.gameReference.placeTile(x.getX(), x.getY(), x.getTile());
              }
              if (gameReference.checkMove()) {
                PossibleMove pm = new PossibleMove(placedsquare, gameReference.getTurnValue());
                System.out.println("Moveffrfrrf hinzugefügt");
                this.moves.add(pm);
              } else {
                this.printBoard();
                System.out.println("Move nicht zulässig");
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
            this.gameReference.recallAll();
            for (Square x : placedsquare) {
              this.gameReference.placeTile(x.getX(), x.getY(), x.getTile());
            }
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
  }

  public ArrayList<Tile> copytiles(ArrayList<Tile> list) {
    ArrayList<Tile> tile = new ArrayList<Tile>();
    for (int i = 0; i < list.size(); i++) {
      tile.add(list.get(i));
    }
    return tile;
  }

  public boolean checkabove(int pre, Word wort, Board board) {
    if ((wort.getBeginningY() + pre) > 15) {
      return false;
    } else {
      return true;
    }
  }

  public boolean checkdown(int suf, Word wort, Board board) {
    if ((wort.getEndingY() - suf) < 0) {
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
    if ((wort.getBeginningX() - pre) < 0) {
      return false;
    } else {
      return true;
    }
  }

  public ArrayList<String> dividestring(String s) {
    char[] feld1 = s.toCharArray();
    ArrayList<String> buchstaben = new ArrayList<String>();
    for (int z = 0; z < feld1.length; z++) {
      buchstaben.add(String.valueOf(feld1[z]));
    }
    return buchstaben;
  }

  public ArrayList<String> prefixplussuffix(String pre, String suf) {
    char[] feld1 = pre.toCharArray();
    char[] feld2 = suf.toCharArray();
    ArrayList<String> buchstaben = new ArrayList<String>();
    for (int z = 0; z < feld1.length; z++) {
      buchstaben.add(String.valueOf(feld1[z]));
    }
    for (int z = 0; z < feld2.length; z++) {
      buchstaben.add(String.valueOf(feld2[z]));
    }
    return buchstaben;
  }

  public void printtiles(ArrayList<Tile> list) {
    for (int i = 0; i < list.size(); i++) {
      System.out.print(" " + String.valueOf(list.get(i).getLetter()));
    }
  }

  public void printword(ArrayList<String> list) {
    for (int i = 0; i < list.size(); i++) {
      System.out.print(" " + list.get(i));
    }
  }

  public ArrayList<Square> checktilesvertical(
      ArrayList<String> buchstaben,
      ArrayList<Square> placedsquares,
      ArrayList<Tile> remainingtiles,
      Word wort,
      int pre,
      int suf,
      Board board) {
    System.out.println("Vertical Buchstabengröße= " + buchstaben.size());
    this.printword(buchstaben);
    if (buchstaben.size() == 0) {
      System.out.println("Move hinzugefügt");
      return placedsquares;
    } else {
      if (buchstaben.size() <= pre) {
        if (board
            .getSquare(wort.getBeginningX(), wort.getBeginningY() + (pre - buchstaben.size() + 1))
            .isTaken()) {
          char[] c = buchstaben.get(buchstaben.size() - 1).toCharArray();
          if (board
                  .getSquare(
                      wort.getBeginningX(), wort.getBeginningY() + (pre - buchstaben.size() + 1))
                  .getTile()
                  .getLetter()
              == c[0]) {
            buchstaben.remove(buchstaben.size() - 1);
            return checktilesvertical(
                buchstaben, placedsquares, remainingtiles, wort, pre, suf, board);

          } else {
            return null;
          }

        } else {
          char[] c = buchstaben.get(buchstaben.size() - 1).toCharArray();
          for (int i = 0; i < remainingtiles.size(); i++) {
            if (remainingtiles.get(i).getLetter() == c[0]) {
              Square square =
                  new Square(
                      wort.getBeginningX(), wort.getBeginningY() + (pre - buchstaben.size() + 1));
              square.placeTile(remainingtiles.get(i));
              remainingtiles.remove(i);
              placedsquares.add(square);
              buchstaben.remove(buchstaben.size() - 1);
              return checktilesvertical(
                  buchstaben, placedsquares, remainingtiles, wort, pre, suf, board);
            }
          }
        }

      } else {
        if (board.getSquare(wort.getBeginningX(), wort.getEndingY() - suf).isTaken()) {
          char[] c = buchstaben.get(buchstaben.size() - 1).toCharArray();
          if (board.getSquare(wort.getBeginningX(), wort.getEndingY() - suf).getTile().getLetter()
              == c[0]) {
            buchstaben.remove(buchstaben.size() - 1);
            suf = suf - 1;
            return checktilesvertical(
                buchstaben, placedsquares, remainingtiles, wort, pre, suf, board);

          } else {
            return null;
          }

        } else {
          char[] c = buchstaben.get(buchstaben.size() - 1).toCharArray();

          for (int i = 0; i < remainingtiles.size(); i++) {
            if (remainingtiles.get(i).getLetter() == c[0]) {
              Square square = new Square(wort.getBeginningX(), wort.getEndingY() - suf);
              square.placeTile(remainingtiles.get(i));
              remainingtiles.remove(i);
              placedsquares.add(square);
              buchstaben.remove(buchstaben.size() - 1);
              suf = suf - 1;
              return checktilesvertical(
                  buchstaben, placedsquares, remainingtiles, wort, pre, suf, board);
            }
          }
        }
      }

      return null;
    }
  }

  public ArrayList<Square> checktileshorizontal(
      ArrayList<String> buchstaben,
      ArrayList<Square> placedsquares,
      ArrayList<Tile> remainingtiles,
      Word wort,
      int pre,
      int suf,
      Board board) {
    System.out.println("Horizontal Buchstabengröße= " + buchstaben.size());
    System.out.println();
    this.printword(buchstaben);
    int size = buchstaben.size();
    if (size == 0) {
      System.out.println("Move hinzugefügt");
      return placedsquares;
    } else {

      if (buchstaben.size() <= pre) {
        if (board
            .getSquare(wort.getBeginningX() + (pre - buchstaben.size() - 1), wort.getBeginningY())
            .isTaken()) {
          char[] c = buchstaben.get(buchstaben.size() - 1).toCharArray();
          if (board
                  .getSquare(
                      wort.getBeginningX() + (pre - buchstaben.size() - 1), wort.getBeginningY())
                  .getTile()
                  .getLetter()
              == c[0]) {
            buchstaben.remove(buchstaben.size() - 1);
            return checktileshorizontal(
                buchstaben, placedsquares, remainingtiles, wort, pre, suf, board);

          } else {
            return null;
          }

        } else {
          char[] c = buchstaben.get(buchstaben.size() - 1).toCharArray();
          for (int i = 0; i < remainingtiles.size(); i++) {
            String s = String.valueOf(c);
            if (remainingtiles.get(i).getLetter() == c[0]) {
              Square square =
                  new Square(
                      wort.getBeginningX() + (pre - buchstaben.size() - 1), wort.getBeginningY());
              square.placeTile(remainingtiles.get(i));
              remainingtiles.remove(i);
              placedsquares.add(square);
              buchstaben.remove(buchstaben.size() - 1);
              return checktileshorizontal(
                  buchstaben, placedsquares, remainingtiles, wort, pre, suf, board);
            }
          }
        }

      } else {
        if (board.getSquare(wort.getEndingX() + suf, wort.getEndingY()).isTaken()) {
          char[] c = buchstaben.get(buchstaben.size() - 1).toCharArray();
          if (board.getSquare(wort.getEndingX() + suf, wort.getEndingY()).getTile().getLetter()
              == c[0]) {
            buchstaben.remove(buchstaben.size() - 1);
            suf = suf - 1;
            return checktileshorizontal(
                buchstaben, placedsquares, remainingtiles, wort, pre, suf, board);

          } else {
            return null;
          }

        } else {
          char[] c = buchstaben.get(buchstaben.size() - 1).toCharArray();
          for (int i = 0; i < remainingtiles.size(); i++) {

            if (remainingtiles.get(i).getLetter() == c[0]) {
              Square square = new Square(wort.getEndingX() + suf, wort.getEndingY());
              square.placeTile(remainingtiles.get(i));
              remainingtiles.remove(i);
              placedsquares.add(square);
              buchstaben.remove(buchstaben.size() - 1);
              suf = suf - 1;
              return checktileshorizontal(
                  buchstaben, placedsquares, remainingtiles, wort, pre, suf, board);
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
            while (upperneighbour.isTaken() && (!upperneighbour.isWithinColumnWord())) {
              upperneighbour = this.gameReference.getBoard().getUpperNeighbour(upperneighbour);
            }
            Word word = new Word();
            ArrayList<Square> squares = new ArrayList<Square>();
            buffer = new StringBuffer();
            lowerneighbour = this.gameReference.getBoard().getLowerNeighbour(upperneighbour);
            word.setBeginningX(lowerneighbour.getX());
            word.setBeginningY(lowerneighbour.getY());
            while ((lowerneighbour.isTaken()) && (!lowerneighbour.isWithinColumnWord())) {
              buffer.append(lowerneighbour.getTile().getLetter());
              squares.add(lowerneighbour);
              word.setEndingX(lowerneighbour.getX());
              word.setEndingY(lowerneighbour.getY());
              lowerneighbour = this.gameReference.getBoard().getLowerNeighbour(lowerneighbour);
            }

            String nword = buffer.toString();
            if (DataHandler.checkWord(nword)) {
              System.out.println("Wort korrekt");
              word.setSquares(squares);
              word.setColumn(true);
              this.words.add(word);
              this.setWithinColumnWord(squares);
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
            while (rightneighbour.isTaken() && (!rightneighbour.isWithinRowWord())) {
              buffer.append(rightneighbour.getTile().getLetter());
              squares.add(rightneighbour);
              word.setEndingX(rightneighbour.getX());
              word.setEndingY(rightneighbour.getY());
              rightneighbour = this.gameReference.getBoard().getRightNeighbour(rightneighbour);
            }

            String nword = buffer.toString();
            // System.out.println(nword);
            if (DataHandler.checkWord(nword)) {
              System.out.println("Wort korrekt");
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
    ArrayList<Square> squares = this.getthebestmove();
    this.gameReference.recallAll();
    for (Square s : squares) {
      this.gameReference.placeTile(s.getX(), s.getY(), s.getTile());
    }
    gameReference.makePlay();
  }

  public ArrayList<Word> getwords() {
    return this.words;
  }

  public Player getPlayer() {
    return gameReference.getPlayer();
  }
}
