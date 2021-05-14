package network.messages;

import gameentities.Player;
import session.Dictionary;

/**
 * Implementation of the DictionaryMessage. A DictionaryMessage is created to send the URL of the
 * chosen dictionary to all clients.
 *
 * @author tikrause
 */
public class DictionaryMessage extends Message {

  /** Default serialization UID */
  private static final long serialVersionUID = 1L;

  private Dictionary url;

  /**
   * Constructor: creating a DictionaryMessage with the MessageType.DICTIONARY, the name of the
   * creator and the URL which is represented by an enum of all available dictionaries
   *
   * @author tikrause
   * @param from
   * @param message
   */
  public DictionaryMessage(Player p, Dictionary url) {
    super(MessageType.DICTIONARY, p);
    this.url = url;
  }

  /**
   * TODO getter method for the text of the message that is sent
   *
   * @author tikrause
   * @return file
   */
  public Dictionary getUrl() {
    return this.url;
  }
}
