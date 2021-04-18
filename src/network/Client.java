package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import network.messages.*;
// import session.GameState;

/**
 * For each client that joins the game a Client instance is created which connects to the server and
 * can send or receive any messages or notifications
 *
 * @author tikrause
 */
public class Client {

  private final String name;
  private final int port;
  private boolean isRunning;
  private Channel channel;

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
    new Client("localhost", 8000).run();
  }

  /**
   * Constructor: creates a Client object at the existing server port
   *
   * @author tikrause
   * @param name
   * @param port
   */
  public Client(String name, int port) {
    this.name = name;
    this.port = port;
    isRunning = true;
  }

  /**
   * connects the client instance to the server and opens a TCP connection that can be used to send
   * or receive messages and to update the game status
   *
   * @author tikrause
   * @throws InterruptedException
   * @throws IOException
   */
  public void run() throws InterruptedException, IOException {
    EventLoopGroup group = new NioEventLoopGroup();

    try {
      // simplifies the connecting process
      Bootstrap bootstrap =
          new Bootstrap()
              .group(group)
              .channel(NioSocketChannel.class)
              .handler(new ClientInitializer());

      // connects the client to the server using TCP
      channel = bootstrap.connect(name, port).sync().channel();
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      while (isRunning) {
        // TODO: INCOMPLETE
        channel.writeAndFlush(new SendChatMessage(name, in.readLine() + "\r\n"));
      }
    } finally {
      group.shutdownGracefully();
    }
  }

  public void connect() {
    // TODO
    channel.writeAndFlush(new ConnectMessage(name));
  }

  public void disconnect() {
    // TODO
    channel.writeAndFlush(new DisconnectMessage(name));
  }

  public void reportError(String reason) {
    // TODO
    channel.writeAndFlush(new ErrorMessage(name, reason));
  }

  public void reportSuccess() {
    // TODO
    channel.writeAndFlush(new SuccessMessage(name));
  }

  /*
   * public void updateGameState(GameState game) {
   * channel.writeAndFlush(new UpdateGameStateMessage(name, game)); }
   */
}
