package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to handle all kinds of Data that has to be stored,
 * processed or used.
 * 
 * @author jluellig
 */

public class DataHandler {

	/**
	 * Imports the given file as the dictionary of the current game. The File has to be in
	 * the right format to be correctly read. Each (and also the first and last)
	 * line has to start with the word that should be added to the dictionary. It
	 * has to be separated from any following info in this line (if there is any) by
	 * any whitespace.
	 * Note: this file will be imported as the user dictionary & AI dictionary, so that both play 
	 * with the same words.
	 * 
	 * @param file
	 * @throws IOException
	 *
	 * @author jluellig
	 */
	public static void useDictionaryFile(File file) {
		try {
			BufferedReader inputReader = new BufferedReader(new FileReader(file));
			String z;
			// initialize user Dictionary in order to be able to fill it
			UserDictionary.initializeDict();
			// initialize AI Dictionary
			AIDictionary.initializeDict();

			while ((z = inputReader.readLine()) != null) {
				String[] a = z.split("\\s+");
				if (a[0].length() < 2) { continue; }
				// Add string to user dictionary
				UserDictionary.addWord(a[0]);
				// Add string to AI dictionary
				AIDictionary.addWord(a[0]);
			}
			inputReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns true if the user dictionary contains the given word.
	 * 
	 * @param string
	 * @return boolean
	 *
	 * @author jluellig
	 */
	public static boolean checkWord(String string) {
		return UserDictionary.checkWord(string);
	}
	
	/**
	 * Returns all prefix and suffix options which create, combined with the given string, an existing word of the dictionary.
	 * Returns a HashMap with two ArrayLists, one for the Key "Prefixes" and one for the Key "Suffixes".
	 * These ArrayLists give the right prefix / suffix combination for the same index.
	 * 
	 * @param string
	 * @return bitoptions
	 *
	 * @author jluellig
	 */
	public static HashMap<String, ArrayList<String>> getBitOptions(String string) {
		//TODO ENUM
		return AIDictionary.getBitOptions(string);
	}
	
	/**
	 * Creates a new player  in the PlayerInfo table of 
	 * PlayerDB with the given username and avatar.
	 * 
	 * @param username
	 * @param avatar
	 *
	 * @author jluellig
	 */
	public static void addPlayer(String username, String avatar) {
		// TODO Use Enum as avatar String
		Database.connect();
		Database.addPlayer(username, avatar);
		Database.disconnect();
	}
	
	/**
	 * Changes the username of the player in the database
	 * 
	 * @param username
	 * @param ID
	 *
	 * @author jluellig
	 */
	public static void alterPlayerUsername(String username, int ID) {
		Database.connect();
		Database.alterPlayerUsername(username, ID);
		Database.disconnect();
	}
	
	/**
	 * Changes the avatar of the player in the database
	 * 
	 * @param avatar
	 * @param ID
	 *
	 * @author jluellig
	 */
	public static void alterPlayerAvatar(String avatar, int ID) {
		Database.connect();
		Database.alterPlayerAvatar(avatar, ID);
		Database.disconnect();
	}
	
	/**
	 * Returns the Usernames, IDs, Avatars of the Players which are stored in the Database.
	 * The information is stored in a HashMap with the key beeing the player's ID with a String array for the player data as value.
	 * Note that in the String Array the index 0 marks the player's username and the index 1 the avatar.
	 * 
	 * @return playerInfo 
	 *
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
	 * Deletes the player of given ID in the PlayerDB
	 * 
	 * @param ID
	 *
	 * @author jluellig
	 */
	public static void deletePlayer (int ID) {
		Database.connect();
		Database.deletePlayer(ID);
		Database.disconnect();
	}
	
	/**
	 * Adds Statistics (if the player won and the points) for one game to the player of given ID
	 * 
	 * @param ID
	 * @param win
	 * @param points
	 *
	 * @author jluellig
	 */
	public static void addStatistics(int ID, boolean win, int points) {
		Database.connect();
		if (win) {
			Database.addStatistics(ID, 1, points);
		} else {
			Database.addStatistics(ID, 0, points);
		}
		Database.disconnect();
	}
	
	/**
	 * Gives the statistics of the given player ID in a HashMap.
	 * Keys: Matches, Won, PointsAVG
	 * 
	 * @return Statistics
	 * @param ID
	 *
	 * @author jluellig
	 */
	public static HashMap<String, Integer> getStatistics(int ID) {
		HashMap<String, Integer> statistics = new HashMap<String, Integer>();
		Database.connect();
		statistics = Database.getStatistics(ID);
		// TODO create HashKey Enum
		Database.disconnect();
		return statistics;
	}
}