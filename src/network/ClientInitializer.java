package network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Initializes the client communication so that messages can be received and sent.
 *
 * @author tikrause
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {

  /**
   * initializes the client channel.
   *
   * @author tikrause
   * @param socketCh client socket channel
   */
  @Override
  protected void initChannel(SocketChannel socketCh) throws Exception {
    ChannelPipeline pipeline = socketCh.pipeline();

    // implements list of channel handlers for the client channel
    pipeline.addLast(
        new ObjectEncoder(),
        new ObjectDecoder(536870912, ClassResolvers.cacheDisabled(getClass().getClassLoader())),
        new ClientHandler());
  }
}
