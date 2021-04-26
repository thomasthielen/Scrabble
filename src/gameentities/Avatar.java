package gameentities;

/**
 * Enum to be able to choose from several preset avatars (currently only placeholders).
 *
 * @author tthielen
 */
public enum Avatar {
  BLUE("url1"),
  RED("url2"),
  YELLOW("url3");

  private final String url;

  /**
   * Constructor for Avatar with given String.
   *
   * @param url
   * @author jluellig
   */
  Avatar(String url) {
    this.url = url;
  }

  /**
   * Returns the url of the Avatar.
   *
   * @return url
   * @author jluellig
   */
  public String getUrl() {
    return this.url;
  }
}
