package network.messages;

import gameentities.Player;
import session.GameState;

/**
 * Implementation of the GameStateMessage. A GameStateMessage is created to update the current state
 * of the game in the local game session.
 *
 * @author lsteltma
 */
public class GameStateMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  private GameState game;

  /**
   * Constructor: creating a GameStateMessage with the MessageType.GAME_STATE, the player object of
   * the creator and the current GameState that should be sent in the message.
   *
   * @author tikrause
   * @param p player object that has sent the message
   * @param game game state object that should be sent
   */
  public GameStateMessage(Player p, GameState game) {
    super(MessageType.GAME_STATE, p);
    this.game = game;
    System.out.println("nachricht erstellt");
  }

  /**
   * getter method for the GameState instance that should be sent in the message.
   *
   * @author tikrause
   * @return game game state object that should be sent
   */
  public GameState getGameState() {
    return this.game;
  }
}
