package network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A Server is initialised to host the game and to allow clients to connect and to communicate with
 * each other
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

  /**
   * main method starts a new server at port 8000 and its run method
   *
   * @author tikrause
   * @param args
   * @throws InterruptedException
   * @throws UnknownHostException
   */
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
   * creates a new server that waits for connections at the given port
   *
   * @author tikrause
   * @param port
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
    channel.close().sync();
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
    bossGroup = null;
    workerGroup = null;
    channel = null;
  }

  /**
   * getter method for the port number on which the server is running
   *
   * @author tikrause
   * @return port
   */
  public static int getPort() {
    return port;
  }

  /**
   * getter method for the IP address of the server
   *
   * @author tikrause
   * @return ip
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
}
