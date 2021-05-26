package session;

/**
 * Enum to be able to choose from different existing dictionaries and their URLs.
 *
 * @author tikrause
 */
public enum Dictionary {
  COLLINS("src/resources/Collins Scrabble Words (2019).txt"),
  ENABLE("src/resources/ENABLE (Words With Friends).txt"),
  SOWPODS("src/resources/SOWPODS (Europe Scrabble Word List).txt"),
  TWL06("src/resources/TWL06 (North America Scrabble Word List).txt");

  private final String url;

  /**
   * Constructor for Dictionary with given String.
   *
   * @author tikrause
   * @param url URL of the dictionary
   */
  Dictionary(String url) {
    this.url = url;
  }

  /**
   * Returns the URL of the chosen existing dictionary.
   *
   * @author tikrause
   * @return url URL of the dictionary
   */
  public String getUrl() {
    return this.url;
  }
}
