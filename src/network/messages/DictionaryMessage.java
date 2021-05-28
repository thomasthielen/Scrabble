package network.messages;

import gameentities.Player;
import java.io.File;
import java.util.HashMap;
import java.util.SortedSet;

/**
 * Implementation of the DictionaryMessage. A DictionaryMessage is created to send the chosen
 * dictionary file to all clients.
 *
 * @author tikrause
 */
public class DictionaryMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  private File file;

  private HashMap<Character, HashMap<Character, SortedSet<String>>> dict;

  /**
   * Constructor: creating a DictionaryMessage with the MessageType.DICTIONARY, the player object of
   * the creator and the file that should be sent to the other clients.
   *
   * @author tikrause
   * @param p player object that has sent the message
   * @param file dictionary file that should be sent
   */
  public DictionaryMessage(Player p, File file) {
    super(MessageType.DICTIONARY, p);
    this.file = file;
  }

  /**
   * Constructor: creating a DictionaryMessage with the MessageType.DICTIONARY, the player object of
   * the creator and the user dictionary that should be sent to the other clients.
   *
   * @author jluellig
   * @param p player object that has sent the message
   * @param dictionary user dictionary that should be sent
   */
  public DictionaryMessage(
      Player p, HashMap<Character, HashMap<Character, SortedSet<String>>> dictionary) {
    super(MessageType.DICTIONARY, p);
    this.dict = dictionary;
  }

  /**
   * getter method for the dictionary file that is sent.
   *
   * @author tikrause
   * @return file dictionary file that should be sent
   */
  public File getFile() {
    return this.file;
  }

  /**
   * Getter method for the user dictionary that is sent.
   *
   * @return dict
   * @author jluellig
   */
  public HashMap<Character, HashMap<Character, SortedSet<String>>> getDict() {
    return this.dict;
  }
}
