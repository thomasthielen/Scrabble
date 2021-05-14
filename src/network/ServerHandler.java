package network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import network.messages.*;
import session.GameState;

import java.util.ArrayList;

import data.DataHandler;
import gameentities.Player;

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
    MessageType mt = msg.getMessageType();
    if (mt == MessageType.CONNECT) {
      ConnectMessage cm = (ConnectMessage) msg;
      Server.addPlayer(cm.getPlayer());
      Client.updateGameSession(new GameState(Server.getPlayerList()));
      for (Channel channel : channels) {
        channel.writeAndFlush(msg);
        channel.writeAndFlush(
            new UpdateGameStateMessage(
                DataHandler.getOwnPlayer(),
                new GameState(
                    Server.getPlayerList(),
                    Client.getGameSession().getBag(),
                    Client.getGameSession().getBoard())));
      }
    } else if (mt == MessageType.DISCONNECT) {
      DisconnectMessage dcm = (DisconnectMessage) msg;
      Server.removePlayer(dcm.getPlayer());
      Client.updateGameSession(new GameState(Server.getPlayerList()));
      for (Channel channel : channels) {
        channel.writeAndFlush(msg);
        channel.writeAndFlush(
            new UpdateGameStateMessage(
                DataHandler.getOwnPlayer(), new GameState(Server.getPlayerList())));
      }
    } else {
      for (Channel channel : channels) {
        if (channel != in) {
          channel.writeAndFlush(msg);
        }
      }
    }
  }
}
