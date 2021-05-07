package session;

/**
 * Enum to be able to choose from different existing dictionaries and their URLs
 *
 * @author tikrause
 */
public enum Dictionary {
  COLLINS("resources/Collins Scrabble Words (2019).txt"),
  ENABLE("resources/ENABLE (Words With Friends).txt"),
  SOWPODS("resources/SOWPODS (Europe Scrabble Word List).txt"),
  TWL06("resources/TWL06 (North America Scrabble Word List).txt");

  private final String url;

  /**
   * Constructor for Dictionary with given String.
   *
   * @param url
   * @author tikrause
   */
  Dictionary(String url) {
    this.url = url;
  }

  /**
   * Returns the URL of the chosen existing dictionary.
   *
   * @return url
   * @author tikrause
   */
  public String getUrl() {
    return this.url;
  }
}
