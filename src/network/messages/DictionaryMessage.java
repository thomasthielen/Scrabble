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

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  private Dictionary url;

  /**
   * Constructor: creates a DictionaryMessage with the MessageType.DICTIONARY, the player object of
   * the creator and the URL which is represented by an enum of all available dictionaries.
   *
   * @author tikrause
   * @param p player object that has sent the message
   * @param url chosen dictionary
   */
  public DictionaryMessage(Player p, Dictionary url) {
    super(MessageType.DICTIONARY, p);
    this.url = url;
  }

  /**
   * getter method for the enum of the chosen dictionary.
   *
   * @author tikrause
   * @return url chosen dictionary
   */
  public Dictionary getUrl() {
    return this.url;
  }
}
