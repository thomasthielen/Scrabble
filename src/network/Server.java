package network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A Server instance is created that hosts the game and to which each client can connect and
 * communicate to
 *
 * @author tikrause
 */
public class Server {

  private final int port;
  private boolean isRunning;

  /**
   * main method starts a new server at port 8000 and its run method
   *
   * @author tikrause
   * @param args
   * @throws InterruptedException
   * @throws UnknownHostException
   */
  public static void main(String[] args) throws InterruptedException {
    // TODO: Ausgabe der IP-Adr des Servers
    new Server(8000).run();
  }

  /**
   * Constructor: creates a new server that waits for connections at the given port
   *
   * @author tikrause
   * @param port
   */
  public Server(int port) {
    this.port = port;
    isRunning = true;
  }

  /**
   * creates a connection to the clients that are connected to the server port
   *
   * @author tikrause
   * @throws InterruptedException
   */
  public void run() throws InterruptedException {
    // responsible for receiving client connections
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    // responsible for network reading and writing
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      // simplifies the connection process
      ServerBootstrap bootstrap =
          new ServerBootstrap()
              .group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .childHandler(new ServerInitializer());

      // TODO: Mitteilen der IP an die Clients
      System.out.println(InetAddress.getLocalHost().getHostAddress());

      // creates a connection using UDP
      bootstrap.bind(port).sync().channel().closeFuture().sync();
    } catch (UnknownHostException uhe) {
      // TODO
      System.out.println("Bad gateway 502: No server");
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}
