package gameentities;

/**
 * Enum to be able to choose from several preset avatars.
 *
 * @author jluellig
 */
public enum Avatar {
  BLACK("resources/avatars/black.png"),
  BLUE("resources/avatars/blue.png"),
  BROWN("resources/avatars/brown.png"),
  CYAN("resources/avatars/cyan.png"),
  GREEN("resources/avatars/green.png"),
  LIME("resources/avatars/lime.png"),
  ORANGE("resources/avatars/orange.png"),
  PINK("resources/avatars/pink.png"),
  PURPLE("resources/avatars/purple.png"),
  RED("resources/avatars/red.png"),
  SILVER("resources/avatars/silver.png"),
  YELLOW("resources/avatars/yellow.png");

  private final String url;

  /**
   * Constructor for Avatar with given String.
   *
   * @param url the url of the given Avatar
   * @author jluellig
   */
  private Avatar(String url) {
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
