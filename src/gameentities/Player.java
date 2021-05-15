package gameentities;

import data.DataHandler;
import data.StatisticKeys;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import session.GameSession;

/**
 * Implementation of the Player objects.
 *
 * <p>For each player, a Player object containing all relevant attributes is created.
 *
 * @author tthielen
 */
public class Player implements Serializable {
  private static final long serialVersionUID = 1L;

  private String username;
  private Avatar avatar;
  private int score = 0;
  private Rack rack;
  private HashMap<StatisticKeys, Integer> playerStatistics;

  private boolean currentlyPlaying = false;

  /**
   * Creates a player object for the current user of the game.
   *
   * @author jluellig
   * @param username the username of the player
   * @param avatar one of the avatar enums for the player
   */
  public Player(String username, Avatar avatar) {
    this.username = username;
    this.avatar = avatar;
    this.playerStatistics = DataHandler.getStatistics(DataHandler.getOwnPlayerId());
  }

  /**
   * Overloaded constructor for foreign players with different statistics.
   *
   * @author jluellig
   * @param username the username of the player
   * @param avatar one of the avatar enums for the player
   * @param playerStatistics the statistics of the player
   */
  public Player(String username, Avatar avatar, HashMap<StatisticKeys, Integer> playerStatistics) {
    this.username = username;
    this.avatar = avatar;
    this.playerStatistics = playerStatistics;
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
   * Fills the Rack of the Player to 7 Tiles.
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
   * @param tile the tile that should be played
   */
  public void playTile(Tile tile) {
    this.rack.playTile(tile);
    ;
  }

  /**
   * Adds a tile back to the rack.
   *
   * @author tthielen
   * @param tile the tile that should be added back to the player's rack
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
   * @param swapTiles tiles that are meant to be swapped
   * @param postions the postions of the tiles
   */
  public void exchangeTiles(ArrayList<Tile> swapTiles, ArrayList<Integer> positions) {
    rack.exchangeTiles(swapTiles, positions);
  }

  /**
   * Sets the player's username.
   *
   * @author tthielen
   * @param username the username of the player
   */
  public void setUsername(String name) {
    this.username = name;
  }

  /**
   * Sets whether it is the player's turn.
   *
   * @author tthielen
   * @param currentlyPlaying true if the player is currently playing, otherwise false
   */
  public void setCurrentlyPlaying(boolean currentlyPlaying) {
    this.currentlyPlaying = currentlyPlaying;
  }

  /**
   * Increases the player score by the given value.
   *
   * @author tthielen
   * @param score the amount of points that were scored and should be added to the player's total
   *     score for the game
   */
  public void incScore(int score) {
    this.score += score;
  }

  /**
   * Returns the player's username.
   *
   * @author tthielen
   * @return username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns the player's score.
   *
   * @author tthielen
   * @return score
   */
  public int getScore() {
    return score;
  }

  /**
   * Returns the player's avatar.
   *
   * @author tthielen
   * @return avatar
   */
  public Avatar getAvatar() {
    return avatar;
  }

  /**
   * Returns the player's rack.
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
   * Returns the statistics hash map of the given player.
   *
   * @return playerStatistics
   * @author jluellig
   */
  public HashMap<StatisticKeys, Integer> getPlayerStatistics() {
    return this.playerStatistics;
  }

  /**
   * Returns if two player objects are the same.
   * 
   * @author tikrause
   * @return if two player objects are the same
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (this.getClass() != o.getClass()) {
      return false;
    }
    Player p = (Player) o;
    return Objects.equals(this.username, p.getUsername())
        && Objects.equals(this.avatar, p.getAvatar())
        && Objects.equals(this.playerStatistics, p.getPlayerStatistics());
  }
}
