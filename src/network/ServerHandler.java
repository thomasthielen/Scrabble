package network;

import java.io.File;

import data.DataHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import network.messages.*;

/**
 * Handles the received messages of the server
 *
 * @author tikrause
 */
public class ServerHandler extends SimpleChannelInboundHandler<Message> {

  // contains all open channels
  private static final ChannelGroup channels =
      new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  /**
   * notifies all connected channels that a new client has been added
   *
   * @author tikrause
   * @param ctx
   */
  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    channels.add(ctx.channel());
  }

  /**
   * notifies all connected channels that a new client has been removed
   *
   * @author tikrause
   * @param ctx
   */
  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    channels.remove(ctx.channel());
  }

  /**
   * specifies and handles all received messages
   *
   * @author tikrause
   * @param ctx
   * @param msg
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
    Channel in = ctx.channel();
    for (Channel channel : channels) {
      if (channel != in) {
        channel.writeAndFlush(msg);
      }
    }
  }
}
