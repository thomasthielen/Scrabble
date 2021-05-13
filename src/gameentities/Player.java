package gameentities;

import java.util.ArrayList;
import java.util.HashMap;

import data.DataHandler;
import data.StatisticKeys;
import session.GameSession;

/**
 * Implementation of the Player objects.
 *
 * <p>For each player, a Player object containing all relevant attributes is created.
 *
 * @author tthielen
 */
public class Player {

  private String username;
  private Avatar avatar;
  private int score = 0;
  private Rack rack;
  private HashMap<StatisticKeys, Integer> playerStatistics;

  private boolean currentlyPlaying = false;

  /**
   * Creates a player object with the given username.
   *
   * @author tthielen
   * @param username the username of the player
   * @param avatar one of the avatar enums for the player
   */
  public Player(String username, Avatar avatar) {
    this.username = username;
    this.avatar = avatar;
    this.playerStatistics = DataHandler.getStatistics(DataHandler.getOwnPlayerID());
  }

  /**
   * Creates a rack for the player according to the given GameSession.
   *
   * @author tthielen
   * @param gameReference used to create a rack for the player
   */
  public void createRack(GameSession gameReference) {
    this.rack = new Rack(gameReference);
  }

  /**
   * Fills the Rack of the Player to 7 Tiles
   *
   * @author tthielen
   */
  public void refillRack() {
    rack.refillDraw();
  }

  /**
   * Plays a tile to the board.
   *
   * @author tthielen
   * @param tile
   */
  public void playTile(Tile tile) {
    this.rack.playTile(tile);
    ;
  }

  /**
   * Adds a tile back to the rack.
   *
   * @author tthielen
   * @param tile
   */
  public void returnTile(Tile tile) {
    this.rack.returnTile(tile);
    ;
  }

  /**
   * The Player chooses Tiles he wants to swap with new Tiles from the bag. Those Tiles are returned
   * to the Bag, and the new Tiles are drawn from the Bag before that.
   *
   * @author tthielen
   * @param Tiles that are meant to be swapped
   */
  public void exchangeTiles(ArrayList<Tile> swapTiles, ArrayList<Integer> positions) {
    rack.exchangeTiles(swapTiles, positions);
  }

  /**
   * Sets the player's username
   *
   * @author tthielen
   * @param username
   */
  public void setUsername(String name) {
    this.username = name;
  }

  /**
   * Sets whether the it is the player's turn
   *
   * @author tthielen
   * @param currentlyPlaying
   */
  public void setCurrentlyPlaying(boolean currentlyPlaying) {
    this.currentlyPlaying = currentlyPlaying;
  }

  /**
   * Increases the player score by the given value.
   *
   * @author tthielen
   * @param score
   */
  public void incScore(int score) {
    this.score += score;
  }

  /**
   * Returns the player's username
   *
   * @author tthielen
   * @return username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns the player's score
   *
   * @author tthielen
   * @return score
   */
  public int getScore() {
    return score;
  }

  /**
   * Returns the player's avatar
   *
   * @author tthielen
   * @return avatar
   */
  public Avatar getAvatar() {
    return avatar;
  }

  /**
   * Returns the player's rack
   *
   * @author tthielen
   * @return rack
   */
  public Rack getRack() {
    return rack;
  }

  /**
   * Returns whether it is the player's turn.
   *
   * @author tthielen
   * @return currentlyPlaying
   */
  public boolean isCurrentlyPlaying() {
    return currentlyPlaying;
  }
  
  /**
   * Returns the staticstics hash map of the given player.
   * 
   * @return playerStatistics
   *
   * @author jluellig
   */
  public HashMap<StatisticKeys, Integer> getPlayerStatistics() {
	  return this.playerStatistics;
  }
}
