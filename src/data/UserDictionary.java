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

  // HashMap that stores every word of the dictionary in a specific SortedSet for
  // the first two
  // characters of the word
  private static HashMap<Character, HashMap<Character, SortedSet<String>>> dict;

  /**
   * This method initializes the HashMaps for the dictionary with each letter combination.
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
   * Returns the User Dictionary.
   *
   * @return userDict
   * @author jluellig
   */
  protected static HashMap<Character, HashMap<Character, SortedSet<String>>> getDict() {
    return dict;
  }
  /**
   * Adds the String s to the user dictionary.
   *
   * @param string
   * @author jluellig
   */
  protected static void addWord(String string) {
    if (dict == null) {
      initializeDict();
    }
    string = string.toUpperCase();
    dict.get(string.charAt(0)).get(string.charAt(1)).add(string);
  }

  /**
   * Returns true if the String s is a correct word in the dictionary.
   *
   * @param string
   * @return isWord
   * @author jluellig
   */
  protected static boolean checkWord(String string) {
    string = string.toUpperCase();
    return (dict != null && dict.get(string.charAt(0)).get(string.charAt(1)).contains(string));
  }
}
