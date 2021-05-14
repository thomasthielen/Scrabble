package network.messages;

import gameentities.Player;
import java.io.File;

/**
 * Implementation of the NewDictionaryMessage. A NewDictionaryMessage is created to send the
 * dictionary file to all clients, if a custom dictionary has been chosen which is not in the
 * resources folder.
 *
 * @author tikrause
 */
public class NewDictionaryMessage extends Message {

  // Default serialization UID
  private static final long serialVersionUID = 1L;

  private File file;

  /**
   * Constructor: creating a NewDictionaryMessage with the MessageType.NEW_DICTIONARY, the player
   * object of the creator and the file that should be sent to the other clients.
   *
   * @author tikrause
   * @param p player object that has sent the message
   * @param file dictionary file that should be sent
   */
  public NewDictionaryMessage(Player p, File file) {
    super(MessageType.NEW_DICTIONARY, p);
    this.file = file;
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
}
