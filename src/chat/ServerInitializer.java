package chat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

	/**
	 * @author tikrause
	 */
	@Override
	protected void initChannel(SocketChannel socketch) throws Exception {
		ChannelPipeline pipeline = socketch.pipeline();

		pipeline.addLast(new ObjectEncoder(), new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())), new ServerHandler());

	}

}
