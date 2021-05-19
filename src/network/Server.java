package network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import network.messages.GameStateMessage;
import network.messages.TooManyPlayerException;
import session.GameState;
import session.SinglePlayerLobby;

import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import AI.AI;
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

  private static SinglePlayerLobby spl;

  public static void main(String[] args) throws InterruptedException, UnknownHostException {
    // TODO: Ausgabe der IP-Adr des Servers
    int p = 8000;
    while (p < 65535) {
      try {
        Server.createServer(p);
        break;
      } catch (BindException be) {
        p++;
      }
    }
  }

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
    isRunning = true;
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
   * @return port port number on which the server waits for connections
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
   * setter method for the flag that shows whether the server is running
   *
   * @author tikrause
   * @param b
   */
  public static void setActive(boolean b) {
    isRunning = b;
  }

  /**
   * getter method for the current status of the run flag
   *
   * @author tikrause
   * @return isRunning
   */
  public static boolean isActive() {
    return isRunning;
  }

  public static void addPlayer(Player p) throws TooManyPlayerException {
    if (players.size() >= 4) {
      throw new TooManyPlayerException();
    } else {
      players.add(p);
    }
  }

  public static void removePlayer(Player p) {
    players.remove(p);
  }

  public static void addAIPlayer(AI ai) throws TooManyPlayerException {
    if (players.size() >= 4) {
      throw new TooManyPlayerException();
    } else {
      aiPlayers.add(ai);
      players.add(ai.getPlayer());
      if (spl == null) {
        channel.writeAndFlush(
            new GameStateMessage(DataHandler.getOwnPlayer(), new GameState(players)));
      }
    }
  }

  public static void removeAIPlayer(AI ai) {
    aiPlayers.remove(ai);
    players.remove(ai.getPlayer());
  }

  public static void resetPlayerList() {
    players.clear();
    aiPlayers.clear();
  }

  public static ArrayList<Player> getPlayerList() {
    return players;
  }

  public static ArrayList<AI> getAIPlayerList() {
    return aiPlayers;
  }

  public static void updateAI(GameState gs) {
    for (AI ai : aiPlayers) {
      ai.updateGameSession(gs);
    }
  }

  public static SinglePlayerLobby getLobby() {
    return spl;
  }

  public static void initializeLobby() {
    spl = new SinglePlayerLobby();
  }

  public static void resetLobby() {
    spl = null;
  }
}
