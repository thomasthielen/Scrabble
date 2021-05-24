package network;

import gameentities.Player;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.File;
import network.messages.*;
import session.GameSession;
import session.GameState;

/**
 * The Client class is responsible for the network communication at the client side.
 *
 * @author tikrause
 */
public class Client {

  private static String ip;
  private static int port;
  private static boolean isRunning = false;
  private static ChannelFuture cf;
  private static boolean isHost;
  private static EventLoopGroup group;

  private static GameSession gameSession;

  /**
   * Initialises the client with the server data to enable the communication to the server.
   *
   * <p>initialiseClient() must be invoked before connectToServer()
   *
   * @author tikrause
   * @param ipKey IP address of the server that should be communicated to
   * @param bindPort port that is used by the server for the communication
   * @param host boolean if the connecting client is the host of the game session
   */
  public static void initialiseClient(String ipKey, int bindPort, boolean host) {
    ip = ipKey;
    port = bindPort;
    isHost = host;
  }

  /**
   * Connects the client instance to the server and opens a TCP connection that can be used to send
   * or receive messages and to update the game status.
   *
   * <p>initialiseClient() must be invoked before connectToServer()
   *
   * @author tikrause
   * @param p player instance that should be connected to the game
   */
  public static void connectToServer(Player p) throws TooManyPlayerException {
    group = new NioEventLoopGroup();

    try {
      // simplifies the connecting process
      Bootstrap bootstrap =
          new Bootstrap()
              .group(group)
              .channel(NioSocketChannel.class)
              .handler(new ClientInitializer());

      // connects the client to the server using TCP
      cf = bootstrap.connect(ip, port).sync();
      isRunning = true;
      gameSession = new GameSession(p, true);
      cf.channel().writeAndFlush(new ConnectMessage(p));
    } catch (InterruptedException e) {
      // TODO
    }
  }

  /**
   * Disconnects the client from the server and informs the other clients that the player has left.
   *
   * @author tikrause
   * @param p player instance that should be disconnected from the game
   */
  public static void disconnectClient(Player p) throws InterruptedException {
    cf.channel().writeAndFlush(new DisconnectMessage(p, isHost));
    isRunning = false;
    cf.channel().close().sync();
    group.shutdownGracefully();
    group = null;
    cf = null;
  }

  public static void sendChat(Player p, String chat) {
    cf.channel().writeAndFlush(new SendChatMessage(p, chat));
  }

  public static void reportEndGame(Player p) {
    cf.channel().writeAndFlush(new EndGameMessage(p));
  }

  public static void reportStartGame(Player p, String chat) {
    cf.channel().writeAndFlush(new StartGameMessage(p, chat));
  }

  public static void sendDictionary(Player p, File f) {
    cf.channel().writeAndFlush(new DictionaryMessage(p, f));
  }

  public static void updateGameState(Player p, GameState game) {
    cf.channel().writeAndFlush(new GameStateMessage(p, game));
  }

  public static void notifyAI(Player p, Player aiPlayer) {
    cf.channel().writeAndFlush(new NotifyAIMessage(p, aiPlayer));
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

  /**
   * getter method if the client is the host of the game session.
   *
   * @author tikrause
   * @return isHost
   */
  public static boolean isHost() {
    return isHost;
  }

  /**
   * @author tikrause
   * @param gameState
   */
  public static void updateGameSession(GameState gameState) {
    gameSession.synchronise(gameState);
  }

  /**
   * @author tikrause
   * @return gameSession
   */
  public static GameSession getGameSession() {
    return gameSession;
  }
}
