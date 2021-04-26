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

  Avatar(String url) {
    this.url = url;
  }

  public String getUrl() {
    return this.url;
  }
}
