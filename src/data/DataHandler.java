package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * This class is used to handle all kinds of Data that has to be stored,
 * processed or used.
 * 
 * @author jluellig
 */

public class DataHandler {

	/**
	 * Imports File f as the Dictionary of the current game. The File has to be in
	 * the specific format to be correctly read. Each (and also the first and last)
	 * line has to start with the word that should be added to the dictionary. It
	 * has to be separated from any following info in this line (if there is any) by
	 * any whitespace.
	 * 
	 * @param file
	 * @throws IOException
	 *
	 * @author jluellig
	 */
	public static void useDictionaryFile(File file) throws IOException {
		try {
			BufferedReader inputReader = new BufferedReader(new FileReader(file));
			String z;
			while ((z = inputReader.readLine()) != null) {
				String[] a = z.split("\\s+");
				Dictionary.addWord(a[0]);
			}
			inputReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if the dictionary contains a specific word.
	 * 
	 * @param string
	 * @return boolean
	 *
	 * @author jluellig
	 */
	public static boolean checkWord(String string) {
		return Dictionary.checkWord(string);
	}
	
	/**
	 * Creates a new player  in the PlayerInfo table of 
	 * PlayerDB with the given username and avatar.
	 * 
	 * @param username
	 * @param avatar
	 * @throws Exception
	 *
	 * @author jluellig
	 */
	public static void addPlayer(String username, String avatar) {
		Database.connect();
		Database.addPlayer(username, avatar);
		Database.disconnect();
	}
	
	/**
	 * Changes the username of the player in the database
	 * 
	 * @param username
	 * @throws Exception
	 *
	 * @author jluellig
	 */
	public static void alterPlayerUsername(String username) throws Exception{
		Connection con = null;
		Statement stm = null;

		try {
			// connect to Database
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + System.getProperty("file.separator") + 
					"resources" + System.getProperty("file.separator") + 
					"PlayerDB.db");
			
			stm = con.createStatement();
			
			// Alter Username
			stm.executeUpdate("UPDATE PlayerInfo SET Username = '" + username + "'");
			
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage() ); 
			System.exit(0);
		} finally {
			if (con != null && stm != null) {
				stm.close();
				con.close();
			}
		}
	}
	
	/**
	 * Changes the avatar of the player in the database
	 * 
	 * @param avatar
	 * @throws Exception
	 *
	 * @author jluellig
	 */
	public static void alterPlayerAvatar(String avatar) throws Exception{
		Connection con = null;
		Statement stm = null;

		try {
			// connect to Database
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + System.getProperty("file.separator") + 
					"resources" + System.getProperty("file.separator") + 
					"PlayerDB.db");
			
			stm = con.createStatement();
			
			// Alter Avatar
			stm.executeUpdate("UPDATE PlayerInfo SET Avatar = '" + avatar + "'");
			
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage() ); 
			System.exit(0);
		} finally {
			if (con != null && stm != null) {
				stm.close();
				con.close();
			}
		}
	}
	
	/**
	 * Returns the Usernames of the Players which are stored in the Database
	 * 
	 * @return Player Usernames
	 * @throws Exception
	 *
	 * @author jluellig
	 */
	public static String[] getPlayerUsernames() throws Exception{
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		ArrayList<String> usernames = new ArrayList<String>(); 
		
		try {
			// connect to Database
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + System.getProperty("file.separator") + 
					"resources" + System.getProperty("file.separator") + 
					"PlayerDB.db");
			
			stm = con.createStatement();
			
			// Get player Username
			rs = stm.executeQuery("SELECT Username FROM PlayerInfo");
			
			while(rs.next()) {
				usernames.add(rs.getString("Username"));
			}
			
				
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage() ); 
			System.exit(0);
		} finally {
			if (con != null && stm != null) {
				if (rs != null) {
					rs.close();
				}
				con.close();
				stm.close();
			}
		}
		return (String[]) usernames.toArray();
	}
	
	/**
	 * Returns the Avatar of the player which is stored in the database
	 * 
	 * @param username
	 * @return avatar
	 * @throws Exception
	 *
	 * @author jluellig
	 */
	public static String getPlayerAvatar(String username) throws Exception{
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		StringBuffer avatar = new StringBuffer();
		
		try {
			// connect to Database
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + System.getProperty("file.separator") + 
					"resources" + System.getProperty("file.separator") + 
					"PlayerDB.db");
			
			stm = con.createStatement();
			
			// Get player Avatar
			rs = stm.executeQuery("SELECT Avatar FROM PlayerInfo WHERE (Username = '" + username + "')");
			
			while(rs.next()) {
				avatar.append(rs.getString("Avatar"));
			}
			
				
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage() ); 
			System.exit(0);
		} finally {
			if (con != null && stm != null) {
				if (rs != null) {
					rs.close();
				}
				con.close();
				stm.close();
			}
		}
		return avatar.toString();
	}
}