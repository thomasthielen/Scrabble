package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
}