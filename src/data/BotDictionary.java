package data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class adds words to the dictionary used by the bot. It builds a structure that helps the bot
 * finding words to add to the game table for a given string that is already on the table.
 *
 * @author jluellig
 */
class BotDictionary {

  // HashMap that stores a WordBit for every (partial) string of the dictionary
  private static HashMap<String, WordBit> wordbits;

  /**
   * This method initializes the HashMap {@link #wordbits} as a new HashMap.
   *
   * @author jluellig
   */
  protected static void initializeDict() {
    wordbits = new HashMap<String, WordBit>();
  }

  /**
   * This method adds the given word to the bot dictionary {@link #wordbits}. Uses {@link
   * WordBit#WordBit(String, String)} and {@link WordBit#addBit(String, String)}.
   *
   * @param word new word for the bot dictionary
   * @author jluellig
   */
  protected static void addWord(String word) {
    if (wordbits == null) {
      initializeDict();
    }
    word = word.toUpperCase();
    String prefix;
    String wordbit;
    String suffix;
    char[] characters = word.toCharArray();

    for (int i = 0; i < characters.length; i++) {
      for (int j = 1; j + i < characters.length + Integer.signum(i); j++) {
        prefix = String.valueOf(characters, 0, i);
        wordbit = String.valueOf(characters, i, j);
        suffix = String.valueOf(characters, j + i, characters.length - (j + i));
        if (wordbits.putIfAbsent(wordbit, new WordBit(prefix, suffix)) != null) {
          wordbits.get(wordbit).addBit(prefix, suffix);
        }
      }
    }
  }

  /**
   * Returns all prefix and suffix options which create, combined with the given string, an existing
   * word of the dictionary. Returns a HashMap with two ArrayLists, one for each Key from {@link
   * BitOptionKeys}. These ArrayLists give the right prefix / suffix combination for the same index.
   * Returns {@link null} if there are no prefixes or suffixes for this word.
   *
   * @param characters one or more characters to search pre-/suffixes for
   * @return bitOptions
   * @author jluellig
   */
  protected static HashMap<BitOptionKeys, ArrayList<String>> getBitOptions(String characters) {
    characters = characters.toUpperCase();
    if (wordbits == null || !wordbits.containsKey(characters)) {
      return null;
    }
    HashMap<BitOptionKeys, ArrayList<String>> bitOptions =
        new HashMap<BitOptionKeys, ArrayList<String>>();
    WordBit wordbit = wordbits.get(characters);
    bitOptions.put(BitOptionKeys.PREFIXES, wordbit.getPrefixes());
    bitOptions.put(BitOptionKeys.SUFFIXES, wordbit.getSuffixes());
    return bitOptions;
  }
}
