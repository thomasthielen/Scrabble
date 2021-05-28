package network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Initializes the server so that messages can be received and sent.
 *
 * @author tikrause
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

  /**
   * initializes the server channel.
   *
   * @author tikrause
   * @param socketCh server socket channel
   */
  @Override
  protected void initChannel(SocketChannel socketch) throws Exception {
    ChannelPipeline pipeline = socketch.pipeline();

    // implements list of server handlers for the server channel
    pipeline.addLast(
        new ObjectEncoder(),
        new ObjectDecoder(536870912, ClassResolvers.cacheDisabled(getClass().getClassLoader())),
        new ServerHandler());
  }
}
