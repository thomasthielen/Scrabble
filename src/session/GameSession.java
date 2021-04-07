package session;

import java.util.ArrayList;

import gameentities.*;

/**
 * Class used to manage all game entities and necessary methods.
 * 
 * @author tthielen
 */
public class GameSession {
	private ArrayList<Player> players; // Holds all participating players
	private Bag bag; // The bag of the game
	private Board board; // The board of the game

	private boolean isActive = false; // Indicates whether the game is live
	private boolean ownTurn = false; // Indicates if it is the turn of this GameSession object

	private Player currentPlayer; // The Player who currently is at turn
	private Player ownPlayer; // The Player of this GameSession object

	private ArrayList<Square> placedSquares = new ArrayList<Square>(); // List of squares on which the tiles of this
																		// turn are (temporarly) placed
	private int turnValue = 0; // Value of the current turn (including bonuses of premium squares)

	int dwsCount = 0;
	int twsCount = 0;

	/**
	 * Constructor: Creates a GameSession object and creates the player-list, the
	 * bag and the board.
	 * 
	 * @author tthielen
	 */
	public GameSession() {
		players = new ArrayList<Player>();
		bag = new Bag();
		board = new Board();
		initialise();
	}

	/**
	 * Initialises the GameSession TODO: Add features
	 * 
	 * @author tthielen
	 */
	public void initialise() {
		// TODO: Implement addPlayer() through Database/Connection handling
		// TODO: Implement function to choose first player / player order
	}

	/**
	 * Synchronises the GameState objects between players
	 * 
	 * @author tthielen
	 */
	public void synchronise() {
		if (ownTurn) {
			GameState gs = new GameState(this.players, this.currentPlayer, this.bag, this.board);
			// TODO: Send GameState object to other users
			ownTurn = false;
		} else {
			// TODO: Receive GameState object and set own game entities to the given values
		}
	}

	/**
	 * Adds a Player with a given username and avatar to the players list
	 * 
	 * @author tthielen
	 * @param username
	 * @param avatar
	 */
	public void addPlayer(String username, Avatar avatar) {
		if (players.size() < 4) {
			players.add(new Player(username, avatar, this));
		} else {
			System.out.println("There are already 4 players in this GameSession!");
		}
	}

	// PLAYER TURN OPTIONS:

	/**
	 * Skips the turn of the player by switching to the next player.
	 * 
	 * @author tthielen
	 */
	public void skipTurn() {
		nextPlayer();
	}

	/**
	 * Swaps the selected Tiles for new ones by calling the exchangeTiles() function
	 * of the currentPlayer.
	 * 
	 * @author tthielen
	 * @param swapTiles
	 */
	public void exchangeTiles(ArrayList<Tile> swapTiles) {
		currentPlayer.exchangeTiles(swapTiles);
		nextPlayer();
	}

	/**
	 * Temporarily places the given tile on the square corresponding to the given
	 * coordinates. Additionally, it saves the square in occupiedSquares, which is
	 * used to test if a legal moved is being played.
	 * 
	 * @author tthielen
	 * @param posX
	 * @param posY
	 * @param tile
	 */
	public void placeTile(int posX, int posY, Tile tile) {
		board.placeTile(posX, posY, tile);
		placedSquares.add(board.getSquare(posX, posY));
	}

	/**
	 * Checks whether the current move (represented by occupiedSquares) is a legal
	 * move. If it isn't, it returns false, otherwise it returns true and calculates
	 * the value of the move.
	 * 
	 * @author tthielen
	 * @return legalMove
	 */
	public boolean checkMove() {

		boolean column = true;
		boolean row = true;

		// Checks whether a tile has already been placed.
		if (placedSquares.size() == 0) {
			return false;
		}

		// Checks whether any tile of the placedSquares is adjacent to the a previously
		// played square.
		boolean adjacent = false;
		for (Square s : placedSquares) {
			if (board.hasPreviouslyPlayedNeighbour(s)) {
				adjacent = true;
				break;
			}
		}
		if (!adjacent) {
			return false;
		}

		// Checks whether the placed tiles all are either in a row or a column.
		// Returns false if neither is the case.
		if (placedSquares.size() > 1) {

			int posX = placedSquares.get(0).getX();
			int posY = placedSquares.get(0).getY();

			if (posX == placedSquares.get(1).getX()) {
				for (Square s : placedSquares) {
					if (s.getX() != posX) {
						column = false;
						break;
					}
				}
			}

			if (posY == placedSquares.get(1).getY()) {
				for (Square s : placedSquares) {
					if (s.getY() != posY) {
						row = false;
						break;
					}
				}
			}

			if (!(row || column)) {
				return false;
			}
		}

		// Checks whether the placed tiles are coherent (including already placed tiles)
		// & CHECKS the MAIN word and calculates its VALUE

		if (column) { // COLUMN

			// Search for lowest square on which a tile was placed
			Square lowestSquare = placedSquares.get(0);
			for (int i = 1; i < placedSquares.size(); i++) {
				if (placedSquares.get(i).getY() < lowestSquare.getY()) {
					lowestSquare = placedSquares.get(i);
				}
			}

			// Search for highest square on which a tile was placed
			Square highestSquare = placedSquares.get(0);
			for (int i = 1; i < placedSquares.size(); i++) {
				if (placedSquares.get(i).getY() > highestSquare.getY()) {
					highestSquare = placedSquares.get(i);
				}
			}

			// Start on the highest square or a square with a previous tile above
			Square iterator = highestSquare;
			while (board.getUpperNeighbour(iterator).isTaken()) {
				iterator = board.getUpperNeighbour(iterator);
			}

			StringBuffer sb = new StringBuffer();
			int wordValue = 0;
			dwsCount = 0;
			twsCount = 0;

			// Go down to the lowest square with a tile
			while (board.getLowerNeighbour(iterator).isTaken()) {
				sb.append(iterator.getTile().getLetter());

				// Score the Tile
				// Premium Squares apply only when newly placed tiles cover them
				wordValue += scoreSquare(iterator);

				iterator = board.getLowerNeighbour(iterator);
			}

			for (int i = 0; i < dwsCount; i++) {
				wordValue = 2 * wordValue;
			}
			for (int i = 0; i < twsCount; i++) {
				wordValue = 3 * wordValue;
			}

			turnValue += wordValue;

			// If the lowest tile before the first gap is not on the same height or lower
			// than the lowest, newly placed tile, return false
			if (iterator.getY() > lowestSquare.getY()) {
				return false;
			}

			String newWord = sb.toString();
			return data.DataHandler.checkWord(newWord);

		} else { // ROW

			// Search for rightmost square on which a tile was placed
			Square rightmostSquare = placedSquares.get(0);
			for (int i = 1; i < placedSquares.size(); i++) {
				if (placedSquares.get(i).getX() > rightmostSquare.getX()) {
					rightmostSquare = placedSquares.get(i);
				}
			}

			// Search for leftmost square on which a tile was placed
			Square leftmostSquare = placedSquares.get(0);
			for (int i = 1; i < placedSquares.size(); i++) {
				if (placedSquares.get(i).getX() < leftmostSquare.getX()) {
					leftmostSquare = placedSquares.get(i);
				}
			}

			// Start on the leftmost square or a square with a previous tile to the left
			Square iterator = leftmostSquare;
			while (board.getLeftNeighbour(iterator).isTaken()) {
				iterator = board.getLeftNeighbour(iterator);
			}

			StringBuffer sb = new StringBuffer();
			int wordValue = 0;
			dwsCount = 0;
			twsCount = 0;

			// Go to the right to the rightmost square with a tile
			while (board.getRightNeighbour(iterator).isTaken()) {
				sb.append(iterator.getTile().getLetter());

				// Score the Tile
				// Premium Squares apply only when newly placed tiles cover them
				wordValue += scoreSquare(iterator);

				iterator = board.getRightNeighbour(iterator);
			}

			for (int i = 0; i < dwsCount; i++) {
				wordValue = 2 * wordValue;
			}
			for (int i = 0; i < twsCount; i++) {
				wordValue = 3 * wordValue;
			}

			turnValue += wordValue;

			// If the lowest tile before the first gap is not on the same height or lower
			// than the lowest, newly placed tile, return false
			if (iterator.getX() < rightmostSquare.getX()) {
				return false;
			}

			String newWord = sb.toString();
			if (!data.DataHandler.checkWord(newWord)) {
				return false;
			}
		}

		// CHECKS all OTHER words formed for correctness & calculates their VALUE

		if (column) {
			// Go through all squares with newly placed Tiles
			for (Square s : placedSquares) {

				// If a square has a Tile to the left or to the right
				// (because we are in a column)
				if (board.getRightNeighbour(s).isTaken() || board.getLeftNeighbour(s).isTaken()) {

					boolean success = rowCheck(s);

					if (!success) {
						return false;
					}
				}
			}
		} else if (row) {
			// Go through all squares with newly placed Tiles
			for (Square s : placedSquares) {

				// If a square has a Tile to the top or to the bottom
				// (because we are in a row)
				if (board.getUpperNeighbour(s).isTaken() || board.getLowerNeighbour(s).isTaken()) {

					boolean success = columnCheck(s);

					if (!success) {
						return false;
					}
				}
			}
		}

		return false; // Dummy return
	}

	/**
	 * Checks whether the column through this square is a legal word and scores it.
	 * 
	 * @author tthielen
	 * @param originSquare
	 * @return legalWord
	 */
	public boolean columnCheck(Square originSquare) {
		StringBuffer sb = new StringBuffer();

		dwsCount = 0;
		twsCount = 0;

		int wordValue = 0;

		Square iterator = originSquare;
		// Get the highest taken square
		while (board.getUpperNeighbour(iterator).isTaken()) {
			iterator = board.getUpperNeighbour(iterator);
		}
		// Append all chars from left to right to the StringBuffer
		while (board.getLowerNeighbour(iterator).isTaken()) {
			sb.append(iterator.getTile().getLetter());

			// Score the Tile
			// Premium Squares apply only when newly placed tiles cover them
			wordValue += scoreSquare(iterator);

			iterator = board.getLowerNeighbour(iterator);
		}

		for (int i = 0; i < dwsCount; i++) {
			wordValue = 2 * wordValue;
		}
		for (int i = 0; i < twsCount; i++) {
			wordValue = 3 * wordValue;
		}

		turnValue += wordValue;

		String newWord = sb.toString();
		if (data.DataHandler.checkWord(newWord)) {
			return false;
		}
		return true;
	}

	/**
	 * Checks whether the row through this square is a legal word and scores it.
	 * 
	 * @author tthielen
	 * @param originSquare
	 * @return legalWord
	 */
	public boolean rowCheck(Square originSquare) {
		StringBuffer sb = new StringBuffer();
		int wordValue = 0;
		dwsCount = 0;
		twsCount = 0;

		Square iterator = originSquare;
		// Get the leftmost taken square
		while (board.getLeftNeighbour(iterator).isTaken()) {
			iterator = board.getLeftNeighbour(iterator);
		}
		// From Left to Right:
		while (board.getRightNeighbour(iterator).isTaken()) {

			// Append the char of the tile to the StringBuffer
			sb.append(iterator.getTile().getLetter());

			// Score the Tile
			// Premium Squares apply only when newly placed tiles cover them
			wordValue += scoreSquare(iterator);

			iterator = board.getRightNeighbour(iterator);
		}

		for (int i = 0; i < dwsCount; i++) {
			wordValue = 2 * wordValue;
		}
		for (int i = 0; i < twsCount; i++) {
			wordValue = 3 * wordValue;
		}

		turnValue += wordValue;

		String newWord = sb.toString();
		if (data.DataHandler.checkWord(newWord)) {
			return false;
		}
		return true;
	}

	/**
	 * Scores the tile according to the square (i.e. according to premium squares).
	 * 
	 * @author tthielen
	 * @param iterator
	 * @return score
	 */
	private int scoreSquare(Square iterator) {
		if (!iterator.isPreviouslyPlayed()) {
			switch (iterator.getPremium()) {
			case DLS:
				return 2 * iterator.getTile().getValue();
			case TLS:
				return 3 * iterator.getTile().getValue();
			case DWS:
				dwsCount++;
				return iterator.getTile().getValue();
			case TWS:
				twsCount++;
				return iterator.getTile().getValue();
			case STAR:
				// fall through
			case NONE:
				return iterator.getTile().getValue();
			default:
				return 0;
			}
		} else {
			return iterator.getTile().getValue();
		}
	}

	/**
	 * Sets currentPlayer to the next player by following the order of the
	 * ArrayList.
	 * 
	 * @author tthielen
	 */
	public void nextPlayer() {
		// TODO: Missing: Current player order is only connected to order of insertion

		turnValue = 0;

		if (players.indexOf(currentPlayer) < players.size() - 1) {
			// Max-Index = 3 (due to [0,3]), whereas players.size = 4
			currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
		} else {
			currentPlayer = players.get(0);
		}
	}

	/**
	 * Returns the bag of the session. Used to draw tiles in Rack.
	 * 
	 * @author tthielen
	 * @return bag
	 */
	public Bag getBag() {
		return bag;
	}

}
