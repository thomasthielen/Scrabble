package network.messages;

/**
 * Implementation of the TooManyPlayerException. It is thrown when there are already the maximum of
 * 4 players in the lobby and no further player can be added.
 *
 * @author tikrause
 */
public class TooManyPlayerException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * creates an TooManyPlayerException that should be handled.
   *
   * @author tikrause
   */
  public TooManyPlayerException() {
    super();
  }
}
