package network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import network.messages.TooManyPlayerException;
import session.GameState;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import ai.AI;
import data.DataHandler;
import gameentities.Player;

/**
 * A Server is initialised to host the game and to allow clients to connect and to communicate with
 * each other.
 *
 * @author tikrause
 */
public class Server {

  private static int port;
  private static boolean isRunning = false;
  private static String ip;
  // responsible for receiving client connections
  private static EventLoopGroup bossGroup;
  // responsible for network reading and writing
  private static EventLoopGroup workerGroup;
  private static Channel channel;

  private static ArrayList<Player> players = new ArrayList<Player>();
  private static ArrayList<AI> aiPlayers = new ArrayList<AI>();

  /**
   * initialises the server, lets the server wait for connections at the given port and opens a UDP
   * connection that can be used to send or receive messages and to update the game status.
   *
   * @author tikrause
   * @param bindPort port where the server should wait for connections
   * @throws UnknownHostException
   * @throws BindException
   * @throws InterruptedException
   */
  public static void createServer(int bindPort)
      throws UnknownHostException, BindException, InterruptedException {
    port = bindPort;
    ip = InetAddress.getLocalHost().getHostAddress();
    System.out.println(ip);

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
  }

  public static void serverShutdown() throws InterruptedException {
    isRunning = false;
    resetPlayerList();
    channel.close().sync();
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
    bossGroup = null;
    workerGroup = null;
    channel = null;
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
   * getter method for the IP address of the server.
   *
   * @author tikrause
   * @return ip IP address of the server
   */
  public static String getIp() {
    return ip;
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
   * getter method if the game on the server is already running.
   *
   * @author tikrause
   * @return isRunning flag that the game has started
   */
  public static boolean isActive() {
    return isRunning;
  }

  /**
   * Checks if the maximum of 4 players are already in the game session. If not, it adds the player
   * to the player list. If yes, it throws a TooManyPlayerException.
   *
   * @author tikrause
   * @param p player instance that should be added to the player list
   * @throws TooManyPlayerException
   */
  public static void addPlayer(Player p) throws TooManyPlayerException {
    if (players.size() >= 4) {
      throw new TooManyPlayerException();
    } else {
      players.add(p);
    }
  }

  /**
   * Removes a player from the player list. If afterwards only one player is remaining in the game,
   * it informs the remaining player about it.
   *
   * @author tikrause
   * @param p player instance that should be removed from the server list
   */
  public static void removePlayer(Player p) {
    players.remove(p);
    if (Client.getChannel() != null) {
      Client.updateGameSession(new GameState(players));
      Client.updateGameState(
          DataHandler.getOwnPlayer(), new GameState(Client.getGameSession().getPlayerList()));
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
   * @throws TooManyPlayerException
   */
  public static void addAIPlayer(AI ai) throws TooManyPlayerException {
    if (players.size() >= 4) {
      throw new TooManyPlayerException();
    } else {
      aiPlayers.add(ai);
      players.add(ai.getPlayer());
      Client.updateGameSession(new GameState(players));
      Client.updateGameState(
          DataHandler.getOwnPlayer(), new GameState(Client.getGameSession().getPlayerList()));
    }
  }

  /**
   * Removes an AI instance from the AI player list and the player list.
   *
   * @author tikrause
   * @param aiPlayer player instance of the AI that should be removed from the game
   */
  public static void removeAIPlayer(Player aiPlayer) {
    AI removedPlayer = null;
    for (AI ai : aiPlayers) {
      if (ai.getPlayer().equals(aiPlayer)) {
        removedPlayer = ai;
        break;
      }
    }
    aiPlayers.remove(removedPlayer);
    players.remove(aiPlayer);
    Client.updateGameSession(new GameState(players));
    Client.updateGameState(
        DataHandler.getOwnPlayer(), new GameState(Client.getGameSession().getPlayerList()));
  }

  /**
   * Resets both player list and AI player list.
   *
   * @author tikrause
   */
  public static void resetPlayerList() {
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
  public static ArrayList<AI> getAIPlayerList() {
    return aiPlayers;
  }

  /**
   * Updates all AI instances with the game state object that has been received.
   *
   * @author tikrause
   * @param gs changes that should be updated in the game session of the AI players
   */
  public static void updateAI(GameState gs) {
    for (AI ai : aiPlayers) {
      ai.updateGameSession(gs);
    }
    for (Player p : gs.getPlayers()) System.out.println("spieler" + p.getUsername());
  }

  public static int getHardAICount() {
    int count = 0;
    for (AI ai : aiPlayers) {
      if (ai.getDifficulty()) {
        count++;
      }
    }
    return count;
  }

  public static int getEasyAICount() {
    int count = 0;
    for (AI ai : aiPlayers) {
      if (!ai.getDifficulty()) {
        count++;
      }
    }
    return count;
  }
}
