package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import network.messages.SendChatMessage;

/**
 * For each client that joins the game a Client instance is created which connects to the server
 * and can send or receive any messages or notifications
 * 
 * @author tikrause
 *
 */
public class Client {
	
	private final String host;
	private final int port;
	
	/**
	 * main method starts a new client and its run method
	 * 
	 * @author tikrause
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main (String[] args) throws InterruptedException, IOException {
		new Client("localhost", 8000).run();
	}

	/**
	 * Constructor: creates a Client object at the existing server port
	 * 
	 * @author tikrause
	 * @param host
	 * @param port
	 */
	public Client (String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * connects the client instance to the server and opens a TCP connection 
	 * that can be used to send or receive messages and to update the game status
	 * 
	 * @author tikrause
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void run() throws InterruptedException, IOException {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			// simplifies the connecting process
			Bootstrap bootstrap = new Bootstrap()
					.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ClientInitializer());
			
			// connects the client to the server using TCP
			Channel channel = bootstrap.connect(host, port).sync().channel();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			
			while (true) {
				// TODO: INCOMPLETE
				String send = in.readLine();
				channel.writeAndFlush(new SendChatMessage("hugo", send + "\r\n"));
			}
		}
		finally {
			group.shutdownGracefully();
		}
	}
}
