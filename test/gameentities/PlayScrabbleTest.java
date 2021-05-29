package gameentities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import network.Client;
import network.Server;
import session.GameSession;
import session.GameState;

/**
 * This is the test class to make sure the functional requirements of the UC2: Play Scrabble work
 * correctly
 *
 * @author lsteltma
 */
class PlayScrabbleTest {
  private Board board;
  private Bag bag;
  private Tile tile;
  private Player player1, player2;
  private int port;
  private GameState gameState;
  private GameSession gameSession;
  private ArrayList<Player> players;

  @BeforeEach
  void init() {
    port = 8000;
    player1 = new Player("player1");
    player2 = new Player("player2");
    players = new ArrayList<Player>();
    players.add(player1);
    players.add(player2);
    gameState = new GameState(players, this.bag = new Bag(), this.board = new Board());

    try {
      Server.createServer(port);
      Client.initialiseClient(Server.getIp(), port, true);
      Client.connectToServer(player1);
      gameSession = Client.getGameSession();
      gameSession.synchronize(gameState);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // initializing the board
    this.board = new Board();
    // initializing the bag
    this.bag = new Bag();
    // initializing the Tile as a wildcard
    tile = new Tile('*', 0);
  }

  @AfterEach
  void end() {
    try {
      Server.shutdown();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSkipTurn() {
    gameSession.synchronize(gameState);
    gameSession.skipTurn();
    assertEquals("player1", gameSession.getPlayer().getUsername());
  }

  @Test
  void testExchangeTiles() {
    gameSession.synchronize(gameState);

    player1.createRack(gameSession);
    ArrayList<Tile> before = player1.getRack().getTiles();
    assertNotNull(before);
    ArrayList<Integer> swap = new ArrayList<Integer>();
    swap.add(2);
    swap.add(4);
    player1.exchangeTiles(swap);
    ArrayList<Tile> after = player1.getRack().getTiles();
    assertNotNull(after);
    assertEquals(before, after);
  }

  @Test
  void testPlayTile() {
    gameSession.synchronize(gameState);

    player1.createRack(gameSession);
    board.placeTile(8, 8, new Tile('A', 1));
    player1.incScore(1);
    assertEquals(1, player1.getScore());
    assertEquals('A', board.getTile(8, 8).getLetter());
  }

  @Test
  // test drawTile() and addTile() from the Bag class
  void testBag() {
    // the bag should already be filled after he got initialized
    assertEquals(false, bag.isEmpty());

    // a new bag should contain 100 Tiles
    assertEquals(100, bag.getRemainingCount());

    // draw one Tile from the Bag
    bag.drawTile();
    // now the bag should contain one Tile less
    assertEquals(99, bag.getRemainingCount());

    // add a new Tile to the Bag
    bag.addTile(new Tile('A', 1));
    // now the bag should contain one Tile more
    assertEquals(100, bag.getRemainingCount());
  }

  @Test
  void testWildcard() {
    // set the Letter of the wildcard
    tile.setLetter('B');
    // check if the char of the Tile got updated
    assertEquals('B', tile.getLetter());
    // check if the Value is correct
    assertEquals(0, tile.getValue());

    // reset the wildcard
    tile.resetLetter();
    // check if the char got reseted
    assertEquals('*', tile.getLetter());
    // check if the Value is correct
    assertEquals(0, tile.getValue());
  }

  @Test
  // test placeTile and recallTile from Board
  void testPlaceTile() {
    // create a new Tile
    Tile tile = new Tile('C', 3);
    // place the Tile on the board
    board.placeTile(8, 8, tile);

    // check if there is a Tile on the selected position
    assertNotNull(board.getTile(8, 8));
    // check if the Tile is a C with the Value 3
    assertEquals('C', board.getTile(8, 8).getLetter());
    assertEquals(3, board.getTile(8, 8).getValue());

    // recall the placed Tile
    board.recallTile(8, 8);
    // check if the position is now empty again
    assertNull(board.getTile(8, 8));
  }

  @Test
  // testing the correct placement of the Premium Squares
  void testPremiumSquares() {
    // test the correct placement of the Premium Squares in the first column
    assertEquals(Premium.TWS, board.getSquare(1, 1).getPremium());
    assertEquals(Premium.DLS, board.getSquare(1, 4).getPremium());
    assertEquals(Premium.TWS, board.getSquare(1, 8).getPremium());
    assertEquals(Premium.DLS, board.getSquare(1, 12).getPremium());
    assertEquals(Premium.TWS, board.getSquare(1, 15).getPremium());

    // test the correct placement of the Premium Squares in the second column
    assertEquals(Premium.DWS, board.getSquare(2, 2).getPremium());
    assertEquals(Premium.TLS, board.getSquare(2, 6).getPremium());
    assertEquals(Premium.TLS, board.getSquare(2, 10).getPremium());
    assertEquals(Premium.DWS, board.getSquare(2, 14).getPremium());

    // test the correct placement of the Premium Squares in the third column
    assertEquals(Premium.DWS, board.getSquare(3, 3).getPremium());
    assertEquals(Premium.DLS, board.getSquare(3, 7).getPremium());
    assertEquals(Premium.DLS, board.getSquare(3, 9).getPremium());
    assertEquals(Premium.DWS, board.getSquare(3, 13).getPremium());

    // test the correct placement of the Premium Squares in the fourth column
    assertEquals(Premium.DLS, board.getSquare(4, 1).getPremium());
    assertEquals(Premium.DWS, board.getSquare(4, 4).getPremium());
    assertEquals(Premium.DWS, board.getSquare(4, 12).getPremium());
    assertEquals(Premium.DLS, board.getSquare(4, 15).getPremium());

    // test the correct placement of the Premium Squares in the fifth column
    assertEquals(Premium.DWS, board.getSquare(5, 5).getPremium());
    assertEquals(Premium.DWS, board.getSquare(5, 11).getPremium());

    // test the correct placement of the Premium Squares in the sixth column
    assertEquals(Premium.TLS, board.getSquare(6, 2).getPremium());
    assertEquals(Premium.TLS, board.getSquare(6, 6).getPremium());
    assertEquals(Premium.TLS, board.getSquare(6, 10).getPremium());
    assertEquals(Premium.TLS, board.getSquare(6, 14).getPremium());

    // test the correct placement of the Premium Squares in the seventh column
    assertEquals(Premium.DLS, board.getSquare(7, 3).getPremium());
    assertEquals(Premium.DLS, board.getSquare(7, 7).getPremium());
    assertEquals(Premium.DLS, board.getSquare(7, 9).getPremium());
    assertEquals(Premium.DLS, board.getSquare(7, 13).getPremium());

    // test the correct placement of the Premium Squares in the eighth column
    assertEquals(Premium.TWS, board.getSquare(8, 1).getPremium());
    assertEquals(Premium.DLS, board.getSquare(8, 4).getPremium());
    assertEquals(Premium.STAR, board.getSquare(8, 8).getPremium());
    assertEquals(Premium.DLS, board.getSquare(8, 12).getPremium());
    assertEquals(Premium.TWS, board.getSquare(8, 15).getPremium());

    // test the correct placement of the Premium Squares in the ninth column
    assertEquals(Premium.DLS, board.getSquare(9, 3).getPremium());
    assertEquals(Premium.DLS, board.getSquare(9, 7).getPremium());
    assertEquals(Premium.DLS, board.getSquare(9, 9).getPremium());
    assertEquals(Premium.DLS, board.getSquare(9, 13).getPremium());

    // test the correct placement of the Premium Squares in the tenth column
    assertEquals(Premium.TLS, board.getSquare(10, 2).getPremium());
    assertEquals(Premium.TLS, board.getSquare(10, 6).getPremium());
    assertEquals(Premium.TLS, board.getSquare(10, 10).getPremium());
    assertEquals(Premium.TLS, board.getSquare(10, 14).getPremium());

    // test the correct placement of the Premium Squares in the eleventh column
    assertEquals(Premium.DWS, board.getSquare(11, 5).getPremium());
    assertEquals(Premium.DWS, board.getSquare(11, 11).getPremium());

    // test the correct placement of the Premium Squares in the twelfth column
    assertEquals(Premium.DLS, board.getSquare(12, 1).getPremium());
    assertEquals(Premium.DWS, board.getSquare(12, 4).getPremium());
    assertEquals(Premium.DWS, board.getSquare(12, 12).getPremium());
    assertEquals(Premium.DLS, board.getSquare(12, 15).getPremium());

    // test the correct placement of the Premium Squares in the thirteenth column
    assertEquals(Premium.DWS, board.getSquare(13, 3).getPremium());
    assertEquals(Premium.DLS, board.getSquare(13, 7).getPremium());
    assertEquals(Premium.DLS, board.getSquare(13, 9).getPremium());
    assertEquals(Premium.DWS, board.getSquare(13, 13).getPremium());

    // test the correct placement of the Premium Squares in the fourteenth column
    assertEquals(Premium.DWS, board.getSquare(14, 2).getPremium());
    assertEquals(Premium.TLS, board.getSquare(14, 6).getPremium());
    assertEquals(Premium.TLS, board.getSquare(14, 10).getPremium());
    assertEquals(Premium.DWS, board.getSquare(14, 14).getPremium());

    // test the correct placement of the Premium Squares in the fifteenth column
    assertEquals(Premium.TWS, board.getSquare(15, 1).getPremium());
    assertEquals(Premium.DLS, board.getSquare(15, 4).getPremium());
    assertEquals(Premium.TWS, board.getSquare(15, 8).getPremium());
    assertEquals(Premium.DLS, board.getSquare(15, 12).getPremium());
    assertEquals(Premium.TWS, board.getSquare(15, 15).getPremium());
  }

  @Test
  // test the correct placement of the Premium.NONE Squares
  void testNonePremiumSquares() {
    // test the correct placement of the Premium Squares in the first column
    assertEquals(Premium.NONE, board.getSquare(1, 2).getPremium());
    assertEquals(Premium.NONE, board.getSquare(1, 3).getPremium());
    assertEquals(Premium.NONE, board.getSquare(1, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(1, 6).getPremium());
    assertEquals(Premium.NONE, board.getSquare(1, 7).getPremium());
    assertEquals(Premium.NONE, board.getSquare(1, 9).getPremium());
    assertEquals(Premium.NONE, board.getSquare(1, 10).getPremium());
    assertEquals(Premium.NONE, board.getSquare(1, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(1, 13).getPremium());
    assertEquals(Premium.NONE, board.getSquare(1, 14).getPremium());

    // test the correct placement of the Premium Squares in the second column
    assertEquals(Premium.NONE, board.getSquare(2, 1).getPremium());
    assertEquals(Premium.NONE, board.getSquare(2, 3).getPremium());
    assertEquals(Premium.NONE, board.getSquare(2, 4).getPremium());
    assertEquals(Premium.NONE, board.getSquare(2, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(2, 7).getPremium());
    assertEquals(Premium.NONE, board.getSquare(2, 8).getPremium());
    assertEquals(Premium.NONE, board.getSquare(2, 9).getPremium());
    assertEquals(Premium.NONE, board.getSquare(2, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(2, 12).getPremium());
    assertEquals(Premium.NONE, board.getSquare(2, 13).getPremium());
    assertEquals(Premium.NONE, board.getSquare(2, 15).getPremium());

    // test the correct placement of the Premium Squares in the third column
    assertEquals(Premium.NONE, board.getSquare(3, 1).getPremium());
    assertEquals(Premium.NONE, board.getSquare(3, 2).getPremium());
    assertEquals(Premium.NONE, board.getSquare(3, 4).getPremium());
    assertEquals(Premium.NONE, board.getSquare(3, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(3, 6).getPremium());
    assertEquals(Premium.NONE, board.getSquare(3, 8).getPremium());
    assertEquals(Premium.NONE, board.getSquare(3, 10).getPremium());
    assertEquals(Premium.NONE, board.getSquare(3, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(3, 12).getPremium());
    assertEquals(Premium.NONE, board.getSquare(3, 14).getPremium());
    assertEquals(Premium.NONE, board.getSquare(3, 15).getPremium());

    // test the correct placement of the Premium Squares in the fourth column
    assertEquals(Premium.NONE, board.getSquare(4, 2).getPremium());
    assertEquals(Premium.NONE, board.getSquare(4, 3).getPremium());
    assertEquals(Premium.NONE, board.getSquare(4, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(4, 6).getPremium());
    assertEquals(Premium.NONE, board.getSquare(4, 7).getPremium());
    assertEquals(Premium.NONE, board.getSquare(4, 9).getPremium());
    assertEquals(Premium.NONE, board.getSquare(4, 10).getPremium());
    assertEquals(Premium.NONE, board.getSquare(4, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(4, 13).getPremium());
    assertEquals(Premium.NONE, board.getSquare(4, 14).getPremium());

    // test the correct placement of the Premium Squares in the fifth column
    assertEquals(Premium.NONE, board.getSquare(5, 1).getPremium());
    assertEquals(Premium.NONE, board.getSquare(5, 2).getPremium());
    assertEquals(Premium.NONE, board.getSquare(5, 3).getPremium());
    assertEquals(Premium.NONE, board.getSquare(5, 4).getPremium());
    assertEquals(Premium.NONE, board.getSquare(5, 6).getPremium());
    assertEquals(Premium.NONE, board.getSquare(5, 7).getPremium());
    assertEquals(Premium.NONE, board.getSquare(5, 8).getPremium());
    assertEquals(Premium.NONE, board.getSquare(5, 9).getPremium());
    assertEquals(Premium.NONE, board.getSquare(5, 10).getPremium());
    assertEquals(Premium.NONE, board.getSquare(5, 12).getPremium());
    assertEquals(Premium.NONE, board.getSquare(5, 13).getPremium());
    assertEquals(Premium.NONE, board.getSquare(5, 14).getPremium());
    assertEquals(Premium.NONE, board.getSquare(5, 15).getPremium());

    // test the correct placement of the Premium Squares in the sixth column
    assertEquals(Premium.NONE, board.getSquare(6, 1).getPremium());
    assertEquals(Premium.NONE, board.getSquare(6, 3).getPremium());
    assertEquals(Premium.NONE, board.getSquare(6, 4).getPremium());
    assertEquals(Premium.NONE, board.getSquare(6, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(6, 7).getPremium());
    assertEquals(Premium.NONE, board.getSquare(6, 8).getPremium());
    assertEquals(Premium.NONE, board.getSquare(6, 9).getPremium());
    assertEquals(Premium.NONE, board.getSquare(6, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(6, 12).getPremium());
    assertEquals(Premium.NONE, board.getSquare(6, 13).getPremium());
    assertEquals(Premium.NONE, board.getSquare(6, 15).getPremium());

    // test the correct placement of the Premium Squares in the seventh column
    assertEquals(Premium.NONE, board.getSquare(7, 1).getPremium());
    assertEquals(Premium.NONE, board.getSquare(7, 2).getPremium());
    assertEquals(Premium.NONE, board.getSquare(7, 4).getPremium());
    assertEquals(Premium.NONE, board.getSquare(7, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(7, 6).getPremium());
    assertEquals(Premium.NONE, board.getSquare(7, 8).getPremium());
    assertEquals(Premium.NONE, board.getSquare(7, 10).getPremium());
    assertEquals(Premium.NONE, board.getSquare(7, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(7, 12).getPremium());
    assertEquals(Premium.NONE, board.getSquare(7, 14).getPremium());
    assertEquals(Premium.NONE, board.getSquare(7, 15).getPremium());

    // test the correct placement of the Premium Squares in the eighth column
    assertEquals(Premium.NONE, board.getSquare(8, 2).getPremium());
    assertEquals(Premium.NONE, board.getSquare(8, 3).getPremium());
    assertEquals(Premium.NONE, board.getSquare(8, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(8, 6).getPremium());
    assertEquals(Premium.NONE, board.getSquare(8, 7).getPremium());
    assertEquals(Premium.NONE, board.getSquare(8, 9).getPremium());
    assertEquals(Premium.NONE, board.getSquare(8, 10).getPremium());
    assertEquals(Premium.NONE, board.getSquare(8, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(8, 13).getPremium());
    assertEquals(Premium.NONE, board.getSquare(8, 14).getPremium());

    // test the correct placement of the Premium Squares in the ninth column
    assertEquals(Premium.NONE, board.getSquare(9, 1).getPremium());
    assertEquals(Premium.NONE, board.getSquare(9, 2).getPremium());
    assertEquals(Premium.NONE, board.getSquare(9, 4).getPremium());
    assertEquals(Premium.NONE, board.getSquare(9, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(9, 6).getPremium());
    assertEquals(Premium.NONE, board.getSquare(9, 8).getPremium());
    assertEquals(Premium.NONE, board.getSquare(9, 10).getPremium());
    assertEquals(Premium.NONE, board.getSquare(9, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(9, 12).getPremium());
    assertEquals(Premium.NONE, board.getSquare(9, 14).getPremium());
    assertEquals(Premium.NONE, board.getSquare(9, 15).getPremium());

    // test the correct placement of the Premium Squares in the tenth column
    assertEquals(Premium.NONE, board.getSquare(10, 1).getPremium());
    assertEquals(Premium.NONE, board.getSquare(10, 3).getPremium());
    assertEquals(Premium.NONE, board.getSquare(10, 4).getPremium());
    assertEquals(Premium.NONE, board.getSquare(10, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(10, 7).getPremium());
    assertEquals(Premium.NONE, board.getSquare(10, 8).getPremium());
    assertEquals(Premium.NONE, board.getSquare(10, 9).getPremium());
    assertEquals(Premium.NONE, board.getSquare(10, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(10, 12).getPremium());
    assertEquals(Premium.NONE, board.getSquare(10, 13).getPremium());
    assertEquals(Premium.NONE, board.getSquare(10, 15).getPremium());

    // test the correct placement of the Premium Squares in the eleventh column
    assertEquals(Premium.NONE, board.getSquare(11, 1).getPremium());
    assertEquals(Premium.NONE, board.getSquare(11, 2).getPremium());
    assertEquals(Premium.NONE, board.getSquare(11, 3).getPremium());
    assertEquals(Premium.NONE, board.getSquare(11, 4).getPremium());
    assertEquals(Premium.NONE, board.getSquare(11, 6).getPremium());
    assertEquals(Premium.NONE, board.getSquare(11, 7).getPremium());
    assertEquals(Premium.NONE, board.getSquare(11, 8).getPremium());
    assertEquals(Premium.NONE, board.getSquare(11, 9).getPremium());
    assertEquals(Premium.NONE, board.getSquare(11, 10).getPremium());
    assertEquals(Premium.NONE, board.getSquare(11, 12).getPremium());
    assertEquals(Premium.NONE, board.getSquare(11, 13).getPremium());
    assertEquals(Premium.NONE, board.getSquare(11, 14).getPremium());
    assertEquals(Premium.NONE, board.getSquare(11, 15).getPremium());

    // test the correct placement of the Premium Squares in the twelfth column
    assertEquals(Premium.NONE, board.getSquare(12, 2).getPremium());
    assertEquals(Premium.NONE, board.getSquare(12, 3).getPremium());
    assertEquals(Premium.NONE, board.getSquare(12, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(12, 6).getPremium());
    assertEquals(Premium.NONE, board.getSquare(12, 7).getPremium());
    assertEquals(Premium.NONE, board.getSquare(12, 9).getPremium());
    assertEquals(Premium.NONE, board.getSquare(12, 10).getPremium());
    assertEquals(Premium.NONE, board.getSquare(12, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(12, 13).getPremium());
    assertEquals(Premium.NONE, board.getSquare(12, 14).getPremium());

    // test the correct placement of the Premium Squares in the thirteenth column
    assertEquals(Premium.NONE, board.getSquare(13, 1).getPremium());
    assertEquals(Premium.NONE, board.getSquare(13, 2).getPremium());
    assertEquals(Premium.NONE, board.getSquare(13, 4).getPremium());
    assertEquals(Premium.NONE, board.getSquare(13, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(13, 6).getPremium());
    assertEquals(Premium.NONE, board.getSquare(13, 8).getPremium());
    assertEquals(Premium.NONE, board.getSquare(13, 10).getPremium());
    assertEquals(Premium.NONE, board.getSquare(13, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(13, 12).getPremium());
    assertEquals(Premium.NONE, board.getSquare(13, 14).getPremium());
    assertEquals(Premium.NONE, board.getSquare(13, 15).getPremium());

    // test the correct placement of the Premium Squares in the fourteenth column
    assertEquals(Premium.NONE, board.getSquare(14, 1).getPremium());
    assertEquals(Premium.NONE, board.getSquare(14, 3).getPremium());
    assertEquals(Premium.NONE, board.getSquare(14, 4).getPremium());
    assertEquals(Premium.NONE, board.getSquare(14, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(14, 7).getPremium());
    assertEquals(Premium.NONE, board.getSquare(14, 8).getPremium());
    assertEquals(Premium.NONE, board.getSquare(14, 9).getPremium());
    assertEquals(Premium.NONE, board.getSquare(14, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(14, 12).getPremium());
    assertEquals(Premium.NONE, board.getSquare(14, 13).getPremium());
    assertEquals(Premium.NONE, board.getSquare(14, 15).getPremium());

    // test the correct placement of the Premium Squares in the fifteenth column
    assertEquals(Premium.NONE, board.getSquare(15, 2).getPremium());
    assertEquals(Premium.NONE, board.getSquare(15, 3).getPremium());
    assertEquals(Premium.NONE, board.getSquare(15, 5).getPremium());
    assertEquals(Premium.NONE, board.getSquare(15, 6).getPremium());
    assertEquals(Premium.NONE, board.getSquare(15, 7).getPremium());
    assertEquals(Premium.NONE, board.getSquare(15, 9).getPremium());
    assertEquals(Premium.NONE, board.getSquare(15, 10).getPremium());
    assertEquals(Premium.NONE, board.getSquare(15, 11).getPremium());
    assertEquals(Premium.NONE, board.getSquare(15, 13).getPremium());
    assertEquals(Premium.NONE, board.getSquare(15, 14).getPremium());
  }

  @Test
  void testPass() {
    for (Square sq : board.getSquareList()) {
      assertFalse(sq.isTaken());
    }
    //    gameSession.skipTurn();
  }
}
