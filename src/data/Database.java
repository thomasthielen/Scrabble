package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 * This class is in charge of everything related to the PlayerDB. It provides methods to create new
 * inputs, read data etc.
 *
 * @author jluellig
 */
class Database {

  private static Connection con = null;
  private static Statement stm = null;
  private static ResultSet rs = null;
  private static String url =
      "jdbc:sqlite:"
          + System.getProperty("user.dir")
          + System.getProperty("file.separator")
          + "resources"
          + System.getProperty("file.separator")
          + "PlayerDB.db";

  /**
   * Connects to the PlayerDB database
   *
   * @author jluellig
   */
  protected static void connect() {
    try {
      Class.forName("org.sqlite.JDBC");
      con = DriverManager.getConnection(url);
      createTables();
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
  }

  /**
   * Creates the tables PlayerInfo and Statistics if they don't already exist.
   *
   * @author jluellig
   */
  private static void createTables() {
    try {
      stm = con.createStatement();

      // create table PlayerInfo if it doesn't already exists
      stm.executeUpdate(
          "CREATE TABLE IF NOT EXISTS PlayerInfo (Username TEXT NOT NULL, Avatar TEXT NOT NULL, "
              + "ID INTEGER, PRIMARY KEY(ID AUTOINCREMENT))");

      // create table Statistics if it doesn't already exist
      stm.executeUpdate(
          "CREATE TABLE IF NOT EXISTS Statistics (PlayerID INTEGER NOT NULL, Date TEXT NOT NULL, "
              + "Win INTEGER NOT NULL, Points INTEGER NOT NULL, PRIMARY KEY(Date), FOREIGN KEY(PlayerID) "
              + "references PlayerInfo (ID))");
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
  }

  /**
   * Adds a new Player to the PlayerInfo table of PlayerDB
   *
   * @param username
   * @param avatar
   * @author jluellig
   */
  protected static void addPlayer(String username, String avatar) {
    try {
      stm = con.createStatement();

      // TODO Use Enum as Avatar String
      // Add username and avatar to PlayerInfo
      stm.executeUpdate(
          "INSERT INTO PlayerInfo(Username, Avatar) VALUES('" + username + "', '" + avatar + "')");
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
  }

  /**
   * Changes the username of the player with the given ID in the table PlayerInfo of PlayerDB
   *
   * @param username
   * @param ID
   * @author jluellig
   */
  protected static void alterPlayerUsername(String username, int ID) {
    try {
      stm = con.createStatement();

      // Change the username of the given ID
      stm.executeUpdate("UPDATE PlayerInfo SET Username = '" + username + "' WHERE ID = " + ID);
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
  }

  /**
   * Changes the avatar of the player with the given ID in the table PlayerInfo of PlayerDB
   *
   * @param avatar
   * @param ID
   * @author jluellig
   */
  protected static void alterPlayerAvatar(String avatar, int ID) {
    try {
      stm = con.createStatement();

      // Change the avatar of the given ID
      stm.executeUpdate("UPDATE PlayerInfo SET Avatar = '" + avatar + "' WHERE ID = " + ID);
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
  }

  /**
   * Gives back the ID, username and avatar of each player from the table PlayerInfo in PlayerDB. It
   * returns a HashMap that uses the player's ID as the key and the player Data in a String Array as
   * the value. Index 0 marks the username in the Array, while index 1 stands for the avatar.
   *
   * @return playerInfo
   * @author jluellig
   */
  protected static HashMap<Integer, String[]> getPlayerInfo() {
    HashMap<Integer, String[]> playerInfo = new HashMap<Integer, String[]>();
    String[] playerData;

    try {
      stm = con.createStatement();

      // get the table PlayerInfo
      rs = stm.executeQuery("SELECT * FROM PlayerInfo ORDER BY ID");

      // add Info into HashMap
      while (rs.next()) {
        playerData = new String[2];
        playerData[0] = rs.getString("Username");
        playerData[1] = rs.getString("Avatar");
        playerInfo.put(rs.getInt("ID"), playerData);
      }

    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
    return playerInfo;
  }

  /**
   * Deletes the info of the given player ID and its statistics in PlayerDB
   *
   * @param ID
   * @author jluellig
   */
  protected static void deletePlayer(int ID) {
    try {
      stm = con.createStatement();

      // delete player with given ID from PlayerInfo table in PlayerDB
      stm.executeUpdate("DELETE FROM PlayerInfo WHERE ID = " + ID);

      // delete player with given ID from Statistics table in PlayerDB
      stm.executeUpdate("DELETE FROM Statistics WHERE PlayerID = " + ID);
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
  }

  /**
   * Adds statistics (Win, Points) for one game to the given player ID into Statistics table in
   * PlayerDB
   *
   * @param ID
   * @param win
   * @param points
   * @author jluellig
   */
  protected static void addStatistics(int ID, int win, int points) {
    try {
      stm = con.createStatement();

      // add Statistics for given ID to Statistics table
      stm.executeUpdate(
          "INSERT INTO Statistics(PlayerID, Date, Win, Points) VALUES("
              + ID
              + ", "
              + "datetime('now', 'localtime'), "
              + win
              + ", "
              + points
              + ")");
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
  }

  /**
   * Gives the statistics of the given player ID from table Statistics in PlayerDB in a HashMap.
   * Keys: Matches, Won, PointsAVG
   *
   * @param ID
   * @return
   * @author jluellig
   */
  protected static HashMap<String, Integer> getStatistics(int ID) {
    HashMap<String, Integer> statistics = new HashMap<String, Integer>();

    try {
      stm = con.createStatement();

      // get amount of matches from table Statistics
      rs = stm.executeQuery("SELECT COUNT(*) FROM Statistics WHERE PlayerID = " + ID);
      // add amount of matches to HashMap
      while (rs.next()) {
        statistics.put("Matches", rs.getInt(1));
      }

      // get amount of wins from table Statistics
      rs = stm.executeQuery("SELECT SUM(Win) FROM Statistics WHERE PlayerID = " + ID);
      // add amount of wins to HashMap
      while (rs.next()) {
        statistics.put("Won", rs.getInt(1));
      }

      // get average points per game from table Statistics
      rs = stm.executeQuery("SELECT AVG(Points) FROM Statistics WHERE PlayerID = " + ID);
      // add average points per game to HashMap
      while (rs.next()) {
        statistics.put("PointsAVG", rs.getInt(1));
      }
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
    return statistics;
  }

  /**
   * Disconnects the PlayerDB Database
   *
   * @author jluellig
   */
  protected static void disconnect() {
    try {
      if (rs != null) {
        rs.close();
      }
      if (stm != null) {
        stm.close();
      }
      if (con != null) {
        con.close();
      }
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
  }
}
