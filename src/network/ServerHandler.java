package network;

import AI.AI;
import data.DataHandler;
import gameentities.Player;
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

        // if a player tries to join a running game, he should be rejected
        if (!Server.isActive()) {
          System.out.println("hilfe");
          Server.addPlayer(cm.getPlayer());
          Client.updateGameSession(new GameState(Server.getPlayerList()));
          for (Channel channel : channels) {
            channel.writeAndFlush(msg);
            channel.writeAndFlush(
                new GameStateMessage(
                    DataHandler.getOwnPlayer(), new GameState(Server.getPlayerList())));
          }
        } else {
          ctx.channel().writeAndFlush(new GameRunningMessage(DataHandler.getOwnPlayer()));
          handlerRemoved(ctx);
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
        if (dcm.isHost()) {
          // Server.serverShutdown();
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

      case DICTIONARY:
        DictionaryMessage dm = (DictionaryMessage) msg;
        Channel c = ctx.channel();
        for (Channel channel : channels) {
          if (channel != c) {
            channel.writeAndFlush(dm);
          }
        }
        for (AI ai : Server.getAIPlayerList()) {
          ai.setDictionary(dm.getFile());
        }
        break;

      case NOTIFY_AI:
        NotifyAIMessage nam = (NotifyAIMessage) msg;
        for (AI ai : Server.getAIPlayerList()) {
          if (ai.getPlayer().equals(nam.getAIPlayer())) {
            ai.makeMove();
          }
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

  protected static void informTooFew() {
    for (Channel c : channels) {
      c.writeAndFlush(new TooFewPlayerMessage(DataHandler.getOwnPlayer()));
    }
  }
}
