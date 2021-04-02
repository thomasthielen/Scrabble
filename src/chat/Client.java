package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import chat.messages.SendChatMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	
	/**
	 * @author tikrause
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main (String[] args) throws InterruptedException, IOException {
		new Client("localhost", 8000).run();
	}

	private final String host;
	private final int port;
	
	/**
	 * @author tikrause
	 * @param host
	 * @param port
	 */
	public Client (String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * @author tikrause
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void run() throws InterruptedException, IOException {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap bootstrap = new Bootstrap()
					.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ClientInitializer());
			
			Channel channel = bootstrap.connect(host, port).sync().channel();
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			
			while (true) {
				// pls change
				channel.writeAndFlush(new SendChatMessage("hugo", in.readLine() + "\r\n"));
			}
		}
		finally {
			group.shutdownGracefully();
		}
	}
}
