package data;

import gameentities.Avatar;
import gameentities.Player;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.regex.Pattern;

/**
 * This class is used to handle all kinds of Data that has to be stored, processed or used.
 *
 * @author jluellig
 */
public class DataHandler {

  // the user's own Player object
  private static Player ownPlayer;
  // the ID of the user's profile in the database
  private static int ownPlayerID;

  /**
   * Imports the given file as the dictionary of the current game. The txt-File has to be in the
   * right format to be correctly read: Each (and also the first and last) line has to start with
   * the word that should be added to the dictionary. It has to be separated from any following info
   * in this line (if there is any) by any whitespace. This file will be imported as the user
   * dictionary. Uses {@link UserDictionary#initializeDict()} and {@link
   * UserDictionary#addWord(String)}.
   *
   * @param dictFile txt-file to read as the user dictionary
   * @author jluellig
   */
  public static void userDictionaryFile(File dictFile) {
    try {
      BufferedReader inputReader = new BufferedReader(new FileReader(dictFile));
      // initialize user dictionary in order to read a new dictionary
      UserDictionary.initializeDict();

      String z;
      while ((z = inputReader.readLine()) != null) {
        String[] a = z.split("\\s+");
        if (!Pattern.matches("[a-zA-Z]{2,}", a[0])) {
          continue;
        }
        // Add string to user dictionary
        UserDictionary.addWord(a[0]);
      }
      inputReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the UserDictionary.
   *
   * @return dict
   * @author jluellig
   */
  public static HashMap<Character, HashMap<Character, SortedSet<String>>> getUserDictionary() {
    return UserDictionary.getDict();
  }

  /**
   * Sets the User Dictionary.
   *
   * @param dict the user dictionary HashMap
   * @author jluellig
   */
  public static void setUserDictionary(
      HashMap<Character, HashMap<Character, SortedSet<String>>> dict) {
    UserDictionary.setDict(dict);
  }

  /**
   * Same import of the dictionary txt-file as for the user dictionary ({@link
   * #userDictionaryFile(File)}), but for the bot dictionary. Uses {@link
   * BotDictionary#initializeDict()} and {@link BotDictionary#addWord(String)}.
   *
   * @param dictFile txt-file to read as the user dictionary
   * @author jluellig
   */
  public static void botDictionaryFile(File dictFile) {
    try {
      BufferedReader inputReader = new BufferedReader(new FileReader(dictFile));
      // initialize bot dictionary in order to read a new dictionary
      BotDictionary.initializeDict();

      String z;
      while ((z = inputReader.readLine()) != null) {
        String[] a = z.split("\\s+");
        if (!Pattern.matches("[a-zA-Z]{2,}", a[0])) {
          continue;
        }
        // Add string to bot dictionary
        BotDictionary.addWord(a[0]);
      }
      inputReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns true if the user dictionary contains the given word. Uses {@link
   * UserDictionary#checkWord(String)}.
   *
   * @param word word that should be checked if it is a correct word in the dictionary
   * @return boolean
   * @author jluellig
   */
  public static boolean checkWord(String word) {
    return UserDictionary.checkWord(word);
  }

  /**
   * Returns all prefix and suffix options which create, combined with the given string, an existing
   * word of the dictionary. Returns a HashMap with two ArrayLists, one for each Key from {@link
   * BitOptionKeys}. These ArrayLists give the right prefix / suffix combination for the same index.
   * Returns {@link null} if there are no prefixes or suffixes for this word. Uses {@link
   * BotDictionary#getBitOptions(String)}.
   *
   * @param characters one or more characters to search pre-/suffixes for
   * @return bitOptions
   * @author jluellig
   */
  public static HashMap<BitOptionKeys, ArrayList<String>> getBitOptions(String characters) {
    return BotDictionary.getBitOptions(characters);
  }

  /**
   * Creates a new player in the PlayerInfo table of PlayerDB with the given username and {@link
   * gameentities.Avatar}. Uses {@link Database#addPlayer(String, Avatar)}.
   *
   * @param username name the user chooses for his profile
   * @param avatar avatar the user chooses for his profile
   * @author jluellig
   */
  public static void addPlayer(String username, Avatar avatar) {
    Database.connect();
    Database.addPlayer(username, avatar);
    Database.disconnect();
  }

  /**
   * Changes the username of the player in the database. Uses {@link
   * Database#alterPlayerUsername(String, int)}.
   *
   * @param username new name the user chooses for his profile
   * @param id user id for the username that should be changed
   * @author jluellig
   */
  public static void alterPlayerUsername(String username, int id) {
    Database.connect();
    Database.alterPlayerUsername(username, id);
    Database.disconnect();
  }

  /**
   * Changes the {@link gameentities.Avatar} of the player in the database. Uses {@link
   * Database#alterPlayerAvatar(Avatar, int)}.
   *
   * @param avatar new avatar the user chooses for his profile
   * @param id user id for the avatar that should be changed
   * @author jluellig
   */
  public static void alterPlayerAvatar(Avatar avatar, int id) {
    Database.connect();
    Database.alterPlayerAvatar(avatar, id);
    Database.disconnect();
  }

  /**
   * Returns the Usernames, IDs, {@link gameentities.Avatar} of the Players which are stored in the
   * Database. The information is stored in a HashMap with the key beeing the player's ID with an
   * array for the player data as value. Note that in this array the index 0 marks the player's
   * username and the index 1 the {@link gameentities.Avatar}. Uses {@link
   * Database#getPlayerInfo()}.
   *
   * @return playerInfo
   * @author jluellig
   */
  public static HashMap<Integer, String[]> getPlayerInfo() {
    HashMap<Integer, String[]> playerInfo = new HashMap<Integer, String[]>();
    Database.connect();
    playerInfo = Database.getPlayerInfo();
    Database.disconnect();
    return playerInfo;
  }

  /**
   * Deletes the player of given ID in the PlayerDB. Uses {@link Database#deletePlayer(int)}.
   *
   * @param id user id that should be deleted
   * @author jluellig
   */
  public static void deletePlayer(int id) {
    Database.connect();
    Database.deletePlayer(id);
    Database.disconnect();
  }

  /**
   * Adds Statistics (if the player won and the points) for one game to the player of given ID. Uses
   * {@link Database#addStatistics(int, int, int)}.
   *
   * @param id user id for the statistics that should be added
   * @param win if the user won the game or not
   * @param points how many points the user scored in this game
   * @author jluellig
   */
  public static void addStatistics(int id, boolean win, int points) {
    Database.connect();
    if (win) {
      Database.addStatistics(id, 1, points);
    } else {
      Database.addStatistics(id, 0, points);
    }
    Database.disconnect();
  }

  /**
   * Gives the statistics of the given player ID in a HashMap. {@link StatisticKeys} represent the
   * keys for this HashMap. Uses {@link Database#getStatistics(int)}.
   *
   * @param id user id for the statistics that should be returned
   * @return statistics
   * @author jluellig
   */
  public static HashMap<StatisticKeys, Integer> getStatistics(int id) {
    HashMap<StatisticKeys, Integer> statistics = new HashMap<StatisticKeys, Integer>();
    Database.connect();
    statistics = Database.getStatistics(id);
    Database.disconnect();
    return statistics;
  }

  /**
   * Sets the users own Player object.
   *
   * @param ownPlayer the Player object that should be used as the user's player.
   * @author jluellig
   */
  public static void setOwnPlayer(Player ownPlayer) {
    DataHandler.ownPlayer = ownPlayer;
  }

  /**
   * Returns the player object of the user.
   *
   * @return ownPlayer
   * @author jluellig
   */
  public static Player getOwnPlayer() {
    return DataHandler.ownPlayer;
  }

  /**
   * Sets the users own database ID.
   *
   * @param id ID for the user's database entry
   * @author jluellig
   */
  public static void setOwnPlayerId(int id) {
    DataHandler.ownPlayerID = id;
  }

  /**
   * Returns the id of the user in the database.
   *
   * @return ownPlayerID
   * @author jluellig
   */
  public static int getOwnPlayerId() {
    return DataHandler.ownPlayerID;
  }
}
