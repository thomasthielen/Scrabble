package network.messages;

import java.io.File;

/**
 * Implementation of the NewDictionaryMessage. A NewDictionaryMessage is created to send the
 * dictionary file to all clients, if a custom dictionary has been chosen which is not in the
 * resources folder.
 *
 * @author tikrause
 */
public class NewDictionaryMessage extends Message {

  /** Default serialization UID */
  private static final long serialVersionUID = 1L;

  private File file;

  /**
   * Constructor: creating a NewDictionaryMessage with the MessageType.NEW_DICTIONARY, the name of
   * the creator and the file that should be sent to the other clients
   *
   * @author tikrause
   * @param from
   * @param message
   */
  public NewDictionaryMessage(String name, File file) {
    super(MessageType.NEW_DICTIONARY, name);
    this.file = file;
  }

  /**
   * getter method for the dictionary file that is sent
   *
   * @author tikrause
   * @return file
   */
  public File getFile() {
    return this.file;
  }
}
