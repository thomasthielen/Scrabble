package data;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class is in charge of everything related to the dictionary used in the game for the user
 * (not the AI). It stores words in the dictionary and provides methods to search the dictionary or
 * check if a word is a part of the dictionary.
 *
 * @author jluellig
 */
class UserDictionary {

  // HashMap that stores every word of the user dictionary in a specific SortedSet for the first two
  // characters of the word
  private static HashMap<Character, HashMap<Character, SortedSet<String>>> dict;

  /**
   * This method initializes the HashMaps for the user dictionary {@link #dict} with each letter
   * combination.
   *
   * @author jluellig
   */
  protected static void initializeDict() {
    dict = new HashMap<Character, HashMap<Character, SortedSet<String>>>();
    for (char letter = 'A'; letter <= 'Z'; letter++) {
      dict.put(letter, new HashMap<Character, SortedSet<String>>());
      for (char letter2 = 'A'; letter2 <= 'Z'; letter2++) {
        dict.get(letter).put(letter2, new TreeSet<String>());
      }
    }
  }

  /**
   * Returns the UserDictionary.
   *
   * @return dict
   * @author jluellig
   */
  protected static HashMap<Character, HashMap<Character, SortedSet<String>>> getDict() {
    return dict;
  }

  /**
   * Sets the User Dictionary.
   *
   * @param dict the user dictionary HashMap
   * @author jluellig
   */
  protected static void setDict(
      HashMap<Character, HashMap<Character, SortedSet<String>>> dictionary) {
    dict = dictionary;
  }

  /**
   * Adds the String to the user dictionary {@link #dict}.
   *
   * @param word new word for the user dictionary
   * @author jluellig
   */
  protected static void addWord(String word) {
    if (dict == null) {
      initializeDict();
    }
    word = word.toUpperCase();
    dict.get(word.charAt(0)).get(word.charAt(1)).add(word);
  }

  /**
   * Returns true if the word is a correct word in the dictionary {@link #dict}.
   *
   * @param word word that should be checked if it is a correct word in the dictionary
   * @return isWord
   * @author jluellig
   */
  protected static boolean checkWord(String word) {
    word = word.toUpperCase();
    return (dict != null && dict.get(word.charAt(0)).get(word.charAt(1)).contains(word));
  }
}
