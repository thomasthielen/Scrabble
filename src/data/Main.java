package data;

import java.io.File;

/**
 * This Class tests the dictionary
 *
 * @author jluellig
 */
public class Main {

  /**
   * Initializes a dictionary and tests it with some console outputs
   *
   * @param args
   * @author jluellig
   */
  public static void main(String[] args) {
    long before = System.currentTimeMillis();
    DataHandler.useDictionaryFile(new File("resources/Collins Scrabble Words (2019).txt"));
    long mid = System.currentTimeMillis();
    String p = "";
    String s = "";
    if (BotDictionary.getBitOptions("Mall") != null) {
      p = BotDictionary.getBitOptions("Mall").get("Prefixes").toString();
      s = BotDictionary.getBitOptions("Mall").get("Suffixes").toString();
    }
    long after = System.currentTimeMillis();
    System.out.println("Import: " + ((mid - before) / 1000.0) + "s");
    System.out.println("Search AI: " + ((after - mid) / 1000.0) + "s");
    System.out.println(p);
    System.out.println(s);
    mid = System.currentTimeMillis();
    System.out.println("Search User:" + DataHandler.checkWord("Mall"));
    after = System.currentTimeMillis();
    System.out.println("Search User: " + ((after - mid) / 1000.0) + "s");
  }
}
