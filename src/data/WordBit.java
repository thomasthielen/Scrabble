package data;

import java.util.ArrayList;

/**
 * A WordBit stores all possible prefix and suffix combinations for a substring of an existing word
 * in the dictionary.
 *
 * @author jluellig
 */
class WordBit {

  // every prefix, that gives a correct word of the dictionary if it is combined with the String and
  // the specific suffix
  private ArrayList<String> prefixes;
  // every suffix, that gives a correct word of the dictionary if it is combined with the String and
  // the specific prefix
  private ArrayList<String> suffixes;

  /**
   * Creates a new WordBit with the given prefix and suffix.
   *
   * @param prefix new prefix for a wordbit
   * @param suffix new suffix for a wordbit
   * @author jluellig
   */
  protected WordBit(String prefix, String suffix) {
    this.prefixes = new ArrayList<String>();
    this.prefixes.add(prefix);
    this.suffixes = new ArrayList<String>();
    this.suffixes.add(suffix);
  }

  /**
   * Adds a prefix and suffix to the WordBit.
   *
   * @param prefix new prefix for a wordbit
   * @param suffix new suffix for a wordbit
   * @author jluellig
   */
  protected void addBit(String prefix, String suffix) {
    this.prefixes.add(prefix);
    this.suffixes.add(suffix);
  }

  /**
   * Returns the {@link #prefixes} of this WordBit.
   *
   * @return prefixes
   * @author jluellig
   */
  protected ArrayList<String> getPrefixes() {
    return this.prefixes;
  }

  /**
   * Returns the {@link #suffixes} of this WordBit.
   *
   * @return suffixes
   * @author jluellig
   */
  protected ArrayList<String> getSuffixes() {
    return this.suffixes;
  }
}
