package session;

import java.util.ArrayList;

import gameentities.Bag;
import gameentities.Board;
import gameentities.Player;

/**
 * Class used to synchronise game entities between players. At each necessary
 * moment, the synchronise() method of GameSession is called and the momentary
 * GameState object is sent to / received by all other players.
 * 
 * @author tthielen
 */
public class GameState {
	private ArrayList<Player> players;
	private Player currentPlayer;
	private Bag bag;
	private Board board;

	public GameState(ArrayList<Player> players, Player currentPlayer, Bag bag, Board board) {
		this.players = players;
		this.currentPlayer = currentPlayer;
		this.bag = bag;
		this.board = board;
	}
}
