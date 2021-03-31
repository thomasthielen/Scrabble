package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * This class is in charge of everything related to the PlayerDB.
 * It provides methods to create new inputs, read data etc.
 *
 * @author jluellig
 */

class Database {

	private static Connection con = null;
	private static Statement stm = null;
	private static ResultSet rs = null;
	private static String url = "jdbc:sqlite:" + System.getProperty("user.dir") + System.getProperty("file.separator") + 
			"resources" + System.getProperty("file.separator") + "PlayerDB.db";
	
	/**
	 * Connects to the PlayerDB database
	 *
	 * @author jluellig
	 */
	protected static void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage()); 
		}
	}
	
	/**
	 * Adds a new Player to the PlayerInfo table of PlayerDB
	 * 
	 * @param username
	 * @param avatar
	 *
	 * @author jluellig
	 */
	protected static void addPlayer(String username, String avatar) {
		try {
			stm = con.createStatement();
				
			// Create table PlayerInfo if it doesn't already exists
			stm.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerInfo (Username TEXT NOT NULL, Avatar TEXT NOT NULL, "
					+ "ID INTEGER, PRIMARY KEY(ID AUTOINCREMENT))");
		
			// Add username and avatar to PlayerInfo
			stm.executeUpdate("INSERT INTO PlayerInfo(Username, Avatar) VALUES('" + username + "', '" + avatar + "')");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage()); 
		}
	}
	
	/**
	 * Disconnects the PlayerDB Database
	 *
	 * @author jluellig
	 */
	protected static void disconnect() {
		try {
			if (rs != null)  {
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
