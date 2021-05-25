package gameentities;

/**
 * Enum to classify the square as one of the premium squares (or not).
 *
 * @author tthielen
 */
public enum Premium {
  /** Double Letter Square. */
  DLS,
  /** Triple Letter Square. */
  TLS,
  /** Double Word Square. */
  DWS,
  /** Triple Word Square. */
  TWS,
  /** Square on (8,8) through which the first word has to be played. */
  STAR,
  /** Not a premium/special square. */
  NONE
}
