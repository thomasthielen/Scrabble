package session;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gameentities.Player;
import session.GameSession;

/**
 * JUnit Test Class for the GameSession class
 *
 * @author lsteltma
 */
class GameSessionTest {
  private GameSession gameSession;

  @BeforeEach
  void init() {
    // initializing the GameSession
    gameSession = new GameSession(new Player("lsteltma"), false);
  }

  @Test
  void testSuccessiveScorelessTurns() {
    // skip turn to increase successiveScorelessTurns by 1
    gameSession.skipTurn();
    // successiveScorelessTurns should be 1
    assertEquals(1, gameSession.getSuccessiveScorelessTurns());

    // create a List to swap
    ArrayList<Integer> positions = new ArrayList<Integer>();
    // swap Tiles to increase successiveScorelessTurns by 1
    gameSession.exchangeTiles(positions);
    // successiveScorelessTurns should be 2
    assertEquals(2, gameSession.getSuccessiveScorelessTurns());
    
    // make a turn with score to reset successiveScorelessTurns
    gameSession.makePlay();
    // check if successiveScorelessTurns is reseted
    assertEquals(0, gameSession.getSuccessiveScorelessTurns());
  }
}
