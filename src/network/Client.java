package network;

import gameentities.Player;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import data.DataHandler;
import network.messages.*;
import session.GameSession;
import session.GameState;

/**
 * For each client that joins the game a Client instance is created which connects to the server and
 * can send or receive any messages or notifications
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
   * main method starts a new client and its run method
   *
   * @author tikrause
   * @param args
   * @throws InterruptedException
   * @throws IOException
   */
  public static void main(String[] args) throws InterruptedException, IOException {
    // TODO: nicht mit localhost, sondern mit Server-IP verbinden
    Client.initialiseClient("localhost", 8000, false);
    Client.connectToServer(new Player("tikrause", null));
  }

  /**
   * Constructor: creates a Client object at the existing server port
   *
   * @author tikrause
   * @param username
   * @param ip
   * @param port
   */
  public static void initialiseClient(String ipKey, int bindPort, boolean host) {
    ip = ipKey;
    port = bindPort;
    isHost = host;
  }

  /**
   * connects the client instance to the server and opens a TCP connection that can be used to send
   * or receive messages and to update the game status
   *
   * @author tikrause
   */
  public static void connectToServer(Player p) {
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
      gameSession = new GameSession();
      cf.channel().writeAndFlush(new ConnectMessage(p));
      // BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      while (!isRunning) {
        // TODO: INCOMPLETE
        // channel.writeAndFlush(new SendChatMessage(ip, in.readLine()));
      }
    } catch (InterruptedException e) {
      // TODO
    } // catch (IOException e) {
    // TODO
    finally {
      // group.shutdownGracefully();
    }
  }

  public static void sendChat(Player p, String msg) {
    cf.channel().writeAndFlush(new SendChatMessage(p, msg));
  }

  public static void reportError(Player p, String reason) {
    // TODO
    cf.channel().writeAndFlush(new ErrorMessage(p, reason));
  }

  public static void reportSuccess(Player p) {
    // TODO
    cf.channel().writeAndFlush(new SuccessMessage(p));
  }

  public static void updateGameState(Player p, GameState game) {
    cf.channel().writeAndFlush(new UpdateGameStateMessage(p, game));
  }

  /**
   * disconnects the client from the server and informs the other clients that the player with the
   * given username has left
   *
   * @author tikrause
   * @param name
   */
  public static void disconnectClient(Player p) throws InterruptedException {
    cf.channel().writeAndFlush(new DisconnectMessage(p, isHost));
    isRunning = false;
    cf.channel().close().sync();
    group.shutdownGracefully();
    group = null;
    cf = null;
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
   * getter method if the client is a host
   *
   * @author tikrause
   * @return isHost
   */
  public static boolean isHost() {
    return isHost;
  }

  public static String getIp() {
    return ip;
  }

  public static int getPort() {
    return port;
  }

  public static void updateGameSession(GameState gameState) {
    gameSession.synchronise(gameState);
  }
  
  public static GameSession getGameSession() {
	  return gameSession;
  }
}
