package network;

import data.DataHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import network.messages.*;
import session.GameState;

/**
 * Handles the received messages of the server.
 *
 * @author tikrause
 */
public class ServerHandler extends SimpleChannelInboundHandler<Message> {

  // contains all open channels
  private static final ChannelGroup channels =
      new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

  /**
   * adds the new channel to the channel list when a client has connected to the server.
   *
   * @author tikrause
   * @param ctx channel that has joined the server
   */
  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    channels.add(ctx.channel());
  }

  /**
   * removes the channel that has left when a client has disconnected from the server.
   *
   * @author tikrause
   * @param ctx channel that has left the server
   */
  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    channels.remove(ctx.channel());
  }

  /**
   * specifies and handles all received messages.
   *
   * @author tikrause
   * @param ctx
   * @param msg
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
    MessageType mt = msg.getMessageType();
    switch (mt) {
      case CONNECT:
        ConnectMessage cm = (ConnectMessage) msg;
        Server.addPlayer(cm.getPlayer());
        Client.updateGameSession(new GameState(Server.getPlayerList()));
        for (Channel channel : channels) {
          channel.writeAndFlush(msg);
          channel.writeAndFlush(
              new GameStateMessage(
                  DataHandler.getOwnPlayer(), new GameState(Server.getPlayerList())));
        }
        break;
      case DISCONNECT:
        DisconnectMessage dcm = (DisconnectMessage) msg;
        Server.removePlayer(dcm.getPlayer());
        Client.updateGameSession(new GameState(Server.getPlayerList()));
        for (Channel channel : channels) {
          channel.writeAndFlush(msg);
          channel.writeAndFlush(
              new GameStateMessage(
                  DataHandler.getOwnPlayer(), new GameState(Server.getPlayerList())));
        }
        break;
      case START_GAME:
        for (Channel channel : channels) {
          channel.writeAndFlush(msg);
          channel.writeAndFlush(
              new GameStateMessage(
                  DataHandler.getOwnPlayer(),
                  new GameState(
                      Server.getPlayerList(),
                      Client.getGameSession().getBag(),
                      Client.getGameSession().getBoard())));
        }
        break;
      case GAME_STATE:
    	  GameStateMessage gsm = (GameStateMessage) msg;
    	  Server.updateAI(gsm.getGameState());
      default:
        Channel in = ctx.channel();
        for (Channel channel : channels) {
          if (channel != in) {
            channel.writeAndFlush(msg);
          }
        }
        break;
    }
  }
}
