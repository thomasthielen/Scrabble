package network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * A Server instance is created that hosts the game and
 * to which each client can connect and communicate to
 * 
 * @author tikrause
 *
 */

public class Server {
	
	private final int port;
	
	/** 
	 * main method starts a new server at port 8000 and its run method
	 * 
	 * @author tikrause
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		new Server(8000).run();
	}
	
	/**
	 * Constructor: creates a new server that waits for connections at the given port
	 * 
	 * @author tikrause
	 * @param port
	 */
	public Server (int port) {
		this.port = port;
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
			ServerBootstrap bootstrap = new ServerBootstrap()
					.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ServerInitializer());
			
			// creates a connection using UDP
			bootstrap.bind(port).sync().channel().closeFuture().sync();
		}
		
		finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
