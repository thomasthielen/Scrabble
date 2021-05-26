package network;

import gameentities.Player;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.File;
import network.messages.ConnectMessage;
import network.messages.DictionaryMessage;
import network.messages.DisconnectMessage;
import network.messages.EndGameMessage;
import network.messages.GameStateMessage;
import network.messages.NotifyAIMessage;
import network.messages.SendChatMessage;
import network.messages.StartGameMessage;
import network.messages.TooManyPlayerException;
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
      gameSession = new GameSession(p);
      cf.channel().writeAndFlush(new ConnectMessage(p));
    } catch (InterruptedException e) {
      e.printStackTrace();
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
    cf.channel().close().sync();
    group.shutdownGracefully();
    group = null;
    cf = null;
  }

  /**
   * Updates the own game session if a GameStateMessage is received.
   *
   * @author tikrause
   * @param gameState changes that should be updated in the game session
   */
  static void updateGameSession(GameState gameState) {
    gameSession.synchronise(gameState);
  }

  /**
   * Sends a GameStateMessage to update the game session of all clients that are connected to the
   * server.
   *
   * @author tikrause
   * @param p player instance of the player that has performed an action
   * @param game state object that contains all changes that have to be updated
   */
  public static void sendGameState(Player p, GameState game) {
    cf.channel().writeAndFlush(new GameStateMessage(p, game));
  }

  /**
   * Informs all clients but the host that the host has started the game. In addition, it sends the
   * chat history from the lobby screen to all clients.
   *
   * @author tikrause
   * @param p player instance of the host
   * @param chat chat history from the lobby screen that should be taken over to the game screen
   */
  public static void reportStartGame(Player p, String chat) {
    cf.channel().writeAndFlush(new StartGameMessage(p, chat));
  }

  /**
   * Informs all other clients that someone has ended the game via the "END GAME"-button and that
   * the game has ended.
   *
   * @author tikrause
   * @param p player instance that has ended the game
   */
  public static void reportEndGame(Player p) {
    cf.channel().writeAndFlush(new EndGameMessage(p));
  }

  /**
   * Sends a chat message to all clients that are connected to the server.
   *
   * @author tikrause
   * @param p player instance that has sent a chat message
   * @param chat message that should be sent
   */
  public static void sendChat(Player p, String chat) {
    cf.channel().writeAndFlush(new SendChatMessage(p, chat));
  }

  /**
   * Sends the dictionary file that has been chosen to all clients.
   *
   * @author tikrause
   * @param p player instance of the host
   * @param f dictionary file that should be sent
   */
  public static void sendDictionary(Player p, File f) {
    cf.channel().writeAndFlush(new DictionaryMessage(p, f));
  }

  /**
   * Informs the server that the next player is an AI player that should make a move.
   *
   * @author tikrause
   * @param p player instance of the player that has finished his move
   * @param aiPlayer that should make a move now
   */
  public static void notifyAI(Player p, Player aiPlayer) {
    cf.channel().writeAndFlush(new NotifyAIMessage(p, aiPlayer));
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
   * getter method for the game session object of the client.
   *
   * @author tikrause
   * @return gameSession of the client
   */
  public static GameSession getGameSession() {
    return gameSession;
  }

  /**
   * returns if the channel is active to check if a game state message has to be sent when a player
   * is removed from the session.
   *
   * @author tikrause
   * @return if the client is connected to the server
   */
  static boolean channelActive() {
    return cf != null;
  }
}
