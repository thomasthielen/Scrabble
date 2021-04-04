package session;

import java.util.ArrayList;

import gameentities.*;

/**
 * Class used to manage all game entities and necessary methods.
 * 
 * @author tthielen
 */
public class GameSession {
	private ArrayList<Player> players;
	private Bag bag;
	private Board board;

	private boolean isActive = false;
	private Player currentPlayer;

	private ArrayList<Square> occupiedSquares = new ArrayList<Square>();

	/**
	 * Constructor: Creates a GameSession object and creates the player-list, the
	 * bag and the board.
	 * 
	 * @author tthielen
	 */
	public GameSession() {
		// Could be outsourced to declaration
		players = new ArrayList<Player>();
		bag = new Bag();
		board = new Board();
		initialize();
	}

	/**
	 * Initializes the GameSession, currently by filling the player-list (other
	 * functions could be added).
	 * 
	 * @author tthielen
	 */
	public void initialize() {
		// TODO: Implement addPlayer() through Database/Connection handling
		// TODO: Implement function to choose first player / player order
	}

	/**
	 * Adds a Player with a given username and avatar
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
		occupiedSquares.add(board.getSquare(posX, posY));
	}

	public boolean legalMove() {

		boolean column = true;
		boolean row = true;

		// Checks whether a tile has already been placed.
		if (occupiedSquares.size() == 0) {
			return false;
		}

		// Checks whether the placed tiles all are either in a row or a column.
		// Returns false if neither is the case.
		if (occupiedSquares.size() > 1) {

			int posX = occupiedSquares.get(0).getX();
			int posY = occupiedSquares.get(0).getY();

			if (posX == occupiedSquares.get(1).getX()) {
				for (Square s : occupiedSquares) {
					if (s.getX() != posX) {
						column = false;
						break;
					}
				}
			}

			if (posY == occupiedSquares.get(1).getY()) {
				for (Square s : occupiedSquares) {
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
		if (column) {

			// Search for placed Tile on the leftmost square (minimal posX)
			int minX = occupiedSquares.get(0).getX();
			for (Square s : occupiedSquares) {
				if (s.getX() < minX) {
					minX = s.getX();
				}
			}

			// Search for placed Tile on the rightmost square (maximal posX)
			int maxX = occupiedSquares.get(0).getX();
			for (Square s : occupiedSquares) {
				if (s.getX() > maxX) {
					maxX = s.getX();
				}
			}

			// Go from the leftmost to the rightmost square and return false,
			// if any of those squares is occupied neither by a newly placed tile nor a tile
			// from an older turn (i.e. square.getTaken() == false )
			int posY = occupiedSquares.get(0).getY();
			for (int x = minX; x <= maxX; x++) {
				if (!board.getSquare(x, posY).getTaken()) {
					return false;
				}
			}

		} else if (row) {

			// Search for placed Tile on the lowest square (minimal posY)
			int minY = occupiedSquares.get(0).getY();
			for (Square s : occupiedSquares) {
				if (s.getY() < minY) {
					minY = s.getY();
				}
			}

			// Search for placed Tile on the highest square (maximal posY)
			int maxY = occupiedSquares.get(0).getY();
			for (Square s : occupiedSquares) {
				if (s.getY() > maxY) {
					maxY = s.getY();
				}
			}

			// Go from the lowest to the highest square and return false,
			// if any of those squares is occupied neither by a newly placed tile nor a tile
			// from an older turn (i.e. square.getTaken() == false )
			int posX = occupiedSquares.get(0).getY();
			for (int y = minY; y <= maxY; y++) {
				if (!board.getSquare(posX, y).getTaken()) {
					return false;
				}
			}
		}

		if (column) {
			// Go through all squares with newly placed Tiles
			for (Square s : occupiedSquares) {

				// If a square has a Tile to the left or to the right
				// (because we are in a column)
				if (board.getRightNeighbour(s).getTaken() || board.getLeftNeighbour(s).getTaken()) {
					StringBuffer sb = new StringBuffer();
					Square leftmost = s;
					// Get the leftmost taken square
					while (board.getLeftNeighbour(leftmost).getTaken()) {
						leftmost = board.getLeftNeighbour(leftmost);
					}
					// Append all chars from left to right to the StringBuffer
					while (board.getRightNeighbour(leftmost).getTaken()) {
						sb.append(leftmost.getTile().getLetter());
						leftmost = board.getRightNeighbour(leftmost);
					}

					String newWord = sb.toString();
					// TODO: Implement matching of newWord to a dictionary
					// Return false if newWord does not exist
					// => cooperation with @jluellig needed
					// It should also be possible to implement the scoring in this step
				}
			}
		} else if (row) {
			// Go through all squares with newly placed Tiles
			for (Square s : occupiedSquares) {

				// If a square has a Tile to the top or to the bottom
				// (because we are in a row)
				if (board.getUpperNeighbour(s).getTaken() || board.getLowerNeighbour(s).getTaken()) {
					StringBuffer sb = new StringBuffer();
					Square highest = s;
					// Get the highest taken square
					while (board.getUpperNeighbour(highest).getTaken()) {
						highest = board.getUpperNeighbour(highest);
					}
					// Append all chars from left to right to the StringBuffer
					while (board.getLowerNeighbour(highest).getTaken()) {
						sb.append(highest.getTile().getLetter());
						highest = board.getLowerNeighbour(highest);
					}

					String newWord = sb.toString();
					// TODO: Implement matching of newWord to a dictionary
					// Return false if newWord does not exist
					// => cooperation with @jluellig needed
					// It should also be possible to implement the scoring in this step
				}
			}
		}

		return false; // Dummy return
	}

	/**
	 * Sets currentPlayer to the next player by following the order of the
	 * ArrayList.
	 * 
	 * @author tthielen
	 */
	public void nextPlayer() {
		// TODO: Missing: Current player order is only connected to order of insertion

		if (players.indexOf(currentPlayer) < players.size() - 1) {
			// Max-Index = 3 (due to [0,3]), whereas players.size = 4
			currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
		} else {
			currentPlayer = players.get(0);
		}
	}

	public Bag getBag() {
		return bag;
	}

}
