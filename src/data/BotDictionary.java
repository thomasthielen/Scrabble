package data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class adds words to the dictionary used by the AI. It builds a structure that helps the AI
 * finding words to add to the game table for a given string that is already on the table.
 *
 * @author jluellig
 */
class BotDictionary {

  // HashMap that stores a Wordbit for every (partial) string of the dictionary
  private static HashMap<String, WordBit> wordbits;

  /**
   * This method initializes the HashMap wordbits as a new HashMap.
   *
   * @author jluellig
   */
  private static void initializeDict() {
    wordbits = new HashMap<String, WordBit>();
  }

  /**
   * This method adds the given word to the AI dictionary.
   *
   * @param string
   * @author jluellig
   */
  protected static void addWord(String string) {
    if (wordbits == null) {
      initializeDict();
    }
    string = string.toUpperCase();
    String prefix;
    String wordbit;
    String suffix;
    char[] word = string.toCharArray();

    for (int i = 0; i < word.length; i++) {
      int x = word.length;
      if (i != 0) {
        x += 1;
      }
      for (int j = 1; j + i < x; j++) {
        prefix = String.valueOf(word, 0, i);
        wordbit = String.valueOf(word, i, j);
        suffix = String.valueOf(word, j + i, word.length - (j + i));
        if (wordbits.putIfAbsent(wordbit, new WordBit(prefix, suffix)) != null) {
          wordbits.get(wordbit).addBit(prefix, suffix);
        }
      }
    }
  }

  /**
   * Returns all prefix and suffix options which create, combined with the given string, an existing
   * word of the dictionary. Returns a HashMap with two ArrayLists, one for the Key "Prefixes" and
   * one for the Key "Suffixes". These ArrayLists give the right prefix / suffix combination for the
   * same index. Returns null if there are no prefixes or suffixes for this word.
   *
   * @param string
   * @return bitoptions
   * @author jluellig
   */
  protected static HashMap<String, ArrayList<String>> getBitOptions(String string) {
    string = string.toUpperCase();
    if (wordbits == null || !wordbits.containsKey(string)) {
      return null;
    }
    HashMap<String, ArrayList<String>> bitOptions = new HashMap<String, ArrayList<String>>();
    // TODO ENUM
    WordBit wordbit = wordbits.get(string);
    bitOptions.put("Prefixes", wordbit.getPrefixes());
    bitOptions.put("Suffixes", wordbit.getSuffixes());
    return bitOptions;
  }
}
