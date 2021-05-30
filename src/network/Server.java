package network;

import ai.Bot;
import gameentities.Player;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import network.messages.TooManyPlayerException;
import session.GameState;

/**
 * A Server is initialized to host the game and to allow clients to connect and to communicate with
 * each other.
 *
 * @author tikrause
 */
public class Server {

  private static String ip;
  private static int port;
  private static boolean isRunning = false;
  // responsible for receiving client connections
  private static EventLoopGroup bossGroup;
  // responsible for network reading and writing
  private static EventLoopGroup workerGroup;
  private static Channel channel;

  private static ArrayList<Player> players = new ArrayList<Player>();
  private static ArrayList<Bot> aiPlayers = new ArrayList<Bot>();

  /**
   * initializes the server, lets the server wait for connections at the given port and opens a UDP
   * connection that can be used to send or receive messages and to update the game status.
   *
   * @author tikrause
   * @param bindPort port where the server should wait for connections
   * @throws BindException port is not available
   * @throws InterruptedException thread interrupted
   */
  public static void createServer(int bindPort) throws BindException, InterruptedException {
    port = bindPort;
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }

    // simplifies the connection process
    bossGroup = new NioEventLoopGroup();
    workerGroup = new NioEventLoopGroup();

    ServerBootstrap bootstrap =
        new ServerBootstrap()
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ServerInitializer());

    // creates a connection using UDP
    channel = bootstrap.bind(port).sync().channel();
    resetPlayerLists();
  }

  /**
   * closes all connections, resets the player lists and is no longer waiting at the given port.
   *
   * @author tikrause
   * @throws InterruptedException message channel thread interrupted
   */
  public static void shutdown() throws InterruptedException {
    isRunning = false;
    resetPlayerLists();
    channel.close().sync();
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
    bossGroup = null;
    workerGroup = null;
    channel = null;
  }

  /**
   * Updates all AI instances with the game state object that has been received.
   *
   * @author tikrause
   * @param gs changes that should be updated in the game session of the AI players
   */
  static void updateBots(GameState gs) {
    for (Bot ai : aiPlayers) {
      ai.updateGameSession(gs);
    }
  }

  /**
   * Overwrites the player in players corresponding to the given AI after its initialDraw().
   *
   * @author tthielen
   * @param ai the AI whose rack is meant to be updated
   */
  public static void updateRackOfBotPlayer(Bot ai) {
    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).equals(ai.getPlayer())) {
        players.set(i, ai.getPlayer());
        Client.updateGameSession(new GameState(players));
        break;
      }
    }
  }

  /**
   * Adds the player to the player list.
   *
   * @author tikrause
   * @param p player instance that should be added to the player list
   */
  static void addPlayer(Player p) {
    players.add(p);
  }

  /**
   * Removes a player from the player list. If afterwards only one player is remaining in the game,
   * it informs the remaining player about it.
   *
   * @author tikrause
   * @param p player instance that should be removed from the server list
   */
  static void removePlayer(ArrayList<Player> playerList, Player p) {
    players = playerList;
    players.remove(p);
    if (Client.channelActive()) {
      Client.updateGameSession(new GameState(players));
      Client.sendGameState(new GameState(Client.getGameSession().getPlayerList()));
    }
    if (players.size() < 2) {
      ServerHandler.informTooFew();
    }
  }

  /**
   * Checks if the maximum of 4 players are already in the game session. If not, it adds the AI
   * player to the player list and to the AI player list. If yes, it throws a
   * TooManyPlayerException.
   *
   * @author tikrause
   * @param ai AI instance that should be added to the game
   * @throws TooManyPlayerException 4 players are already in the session and no one else can join
   */
  public static void addBotPlayer(Bot ai) throws TooManyPlayerException {
    if (players.size() >= 4) {
      throw new TooManyPlayerException();
    } else {
      aiPlayers.add(ai);
      players.add(ai.getPlayer());
      Client.updateGameSession(new GameState(players));
      Client.sendGameState(new GameState(Client.getGameSession(), false));
    }
  }

  /**
   * Removes an AI instance from the AI player list and the player list.
   *
   * @author tikrause
   * @param aiPlayer player instance of the AI that should be removed from the game
   */
  public static void removeBotPlayer(Player aiPlayer) {
    Bot removedPlayer = null;
    for (Bot ai : aiPlayers) {
      if (ai.getPlayer().equals(aiPlayer)) {
        removedPlayer = ai;
        break;
      }
    }
    aiPlayers.remove(removedPlayer);
    players.remove(aiPlayer);
    Client.updateGameSession(new GameState(players));
    Client.sendGameState(new GameState(Client.getGameSession().getPlayerList()));
  }

  /**
   * Resets both player list and AI player list.
   *
   * @author tikrause
   */
  public static void resetPlayerLists() {
    players.clear();
    aiPlayers.clear();
  }

  /**
   * getter method for the player list.
   *
   * @author tikrause
   * @return players player list of all players in the game session
   */
  public static ArrayList<Player> getPlayerList() {
    return players;
  }

  /**
   * getter method for the AI player list.
   *
   * @author tikrause
   * @return aiPlayers AI player list of all AI players in the game session
   */
  public static ArrayList<Bot> getBotPlayerList() {
    return aiPlayers;
  }

  /**
   * getter method for the IP address of the server.
   *
   * @author tikrause
   * @return ip IP address of the server
   */
  public static String getIp() {
    return ip;
  }

  /**
   * getter method for the port number on which the server is running.
   *
   * @author tikrause
   * @return port number on which the server waits for connections
   */
  public static int getPort() {
    return port;
  }

  /**
   * getter method if the game on the server is already running.
   *
   * @author tikrause
   * @return isRunning flag that the game has started
   */
  static boolean isActive() {
    return isRunning;
  }

  /**
   * setter method for the flag that shows that the game has already started.
   *
   * @author tikrause
   */
  public static void setActive() {
    isRunning = true;
  }

  /**
   * getter method for the number of easy AI players in the game session.
   *
   * <p>Only used for AI username reasons in the game session
   *
   * @author tikrause
   * @return number of easy AI players
   */
  public static int getEasyBotCount() {
    int count = 0;
    for (Bot ai : aiPlayers) {
      if (!ai.getDifficulty()) {
        count++;
      }
    }
    return count;
  }

  /**
   * getter method for the number of hard AI players in the game session.
   *
   * <p>Only used for AI username reasons in the game session
   *
   * @author tikrause
   * @return number of hard AI players
   */
  public static int getHardBotCount() {
    int count = 0;
    for (Bot ai : aiPlayers) {
      if (ai.getDifficulty()) {
        count++;
      }
    }
    return count;
  }
}
