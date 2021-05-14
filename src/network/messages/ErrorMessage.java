package network.messages;

import gameentities.Player;

/**
 * Implementation of the error message. A ErrorMessage is created when something is not going as
 * planned
 *
 * @author lsteltma
 */
public class ErrorMessage extends Message {

  /**
   * Default serialization UID
   *
   * @author lsteltma
   */
  private static final long serialVersionUID = 1L;

  private String errorType;

  /**
   * Constructor: If an Error occures, the ErrorMessage is created with the corresponding errorType
   * and the name of the player with the error
   *
   * @author lsteltma
   * @param name
   * @param errorType
   */
  public ErrorMessage(Player p, String errorType) {
    super(MessageType.ERROR, p);
    this.errorType = errorType;
  }

  /**
   * getter method for the error type
   *
   * @author lsteltma
   * @return errorType of the Message
   */
  public String getErrorType() {
    return this.errorType;
  }
}
