package data;

import java.util.ArrayList;

/**
 * A WordBit stores all possible prefix and suffix combinations for a substring of an existing word
 * in the dictionary.
 *
 * @author jluellig
 */
class WordBit {

  private ArrayList<String> prefixes;
  private ArrayList<String> suffixes;

  /**
   * Creates a new WordBit with the given prefix and suffix.
   *
   * @param prefix
   * @param suffix
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
   * @param prefix
   * @param suffix
   * @author jluellig
   */
  protected void addBit(String prefix, String suffix) {
    this.prefixes.add(prefix);
    this.suffixes.add(suffix);
  }

  /**
   * Returns the prefixes of this WordBit.
   *
   * @return prefixes
   * @author jluellig
   */
  protected ArrayList<String> getPrefixes() {
    return this.prefixes;
  }

  /**
   * Returns the suffixes of this WordBit.
   *
   * @return suffixes
   * @author jluellig
   */
  protected ArrayList<String> getSuffixes() {
    return this.suffixes;
  }
}
