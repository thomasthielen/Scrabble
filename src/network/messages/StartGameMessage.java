package network.messages;

import gameentities.Player;

/**
 * Implementation of the StartGameMessage. A StartGameMessage is created when the host starts the
 * game and the other players should be informed.
 *
 * @author tikrause
 */
public class StartGameMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  private String chat;

  /**
   * Constructor: creating a StartGameMessage with the MessageType.START_GAME, the name of the
   * creator and the lobby chat that should be taken over into the game screen.
   *
   * @author tikrause
   * @param p player object of the creator
   * @param chat chat history from the lobby screen
   */
  public StartGameMessage(Player p, String chat) {
    super(MessageType.START_GAME, p);
    this.chat = chat;
  }

  /**
   * getter method for the chat history that should be taken over from the lobby screen into the
   * game screen.
   *
   * @author tikrause
   * @return chat history
   */
  public String getChat() {
    return chat;
  }
}
