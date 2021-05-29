package network;

import ai.Bot;
import data.DataHandler;
import gameentities.Player;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import network.messages.ConnectMessage;
import network.messages.DictionaryMessage;
import network.messages.DisconnectMessage;
import network.messages.GameRunningMessage;
import network.messages.GameStateMessage;
import network.messages.Message;
import network.messages.MessageType;
import network.messages.NotifyBotsMessage;
import network.messages.PlayerExistentMessage;
import network.messages.TooFewPlayerMessage;
import network.messages.TooManyPlayerMessage;
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
   * @param ctx channel from which the message has been received
   * @param msg message that has been received
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
    MessageType mt = msg.getMessageType();
    switch (mt) {
      case CONNECT:
        ConnectMessage cm = (ConnectMessage) msg;

        // if a player tries to join with a username which is already existing in the lobby, he
        // should be rejected
        boolean playerExistent = false;
        for (Player p : Server.getPlayerList()) {
          if (p.getUsername().equals(cm.getPlayer().getUsername())) {
            ctx.channel().writeAndFlush(new PlayerExistentMessage(cm.getPlayer()));
            playerExistent = true;
            break;
          }
        }

        // if a player tries to join a running game, he should be rejected
        if (Server.isActive()) {
          ctx.channel().writeAndFlush(new GameRunningMessage(null));

          // if a player tries to join a game with already 4 players in the lobby, he should be
          // rejected
        } else if (Server.getPlayerList().size() >= 4) {
          ctx.channel().writeAndFlush(new TooManyPlayerMessage(null));

          // player is added to the game
        } else if (!playerExistent) {
          Server.addPlayer(cm.getPlayer());
          Client.updateGameSession(new GameState(Server.getPlayerList()));
          for (Channel channel : channels) {
            channel.writeAndFlush(msg);
            channel.writeAndFlush(
                new GameStateMessage(null, new GameState(Server.getPlayerList())));
          }
        }
        break;

      case DISCONNECT:
        DisconnectMessage dcm = (DisconnectMessage) msg;
        Server.removePlayer(dcm.getPlayerList(), dcm.getPlayer());
        Client.updateGameSession(new GameState(Server.getPlayerList()));
        for (Channel channel : channels) {
          channel.writeAndFlush(msg);
          channel.writeAndFlush(new GameStateMessage(null, new GameState(Server.getPlayerList())));
        }
        break;

      case START_GAME:
        for (Channel channel : channels) {
          channel.writeAndFlush(msg);
          channel.writeAndFlush(
              new GameStateMessage(
                  null,
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
            channel.writeAndFlush(new DictionaryMessage(null, DataHandler.getUserDictionary()));
          }
        }
        for (Bot ai : Server.getBotPlayerList()) {
          ai.initializeAI(dm.getFile());
        }
        break;

      case NOTIFY_AI:
        NotifyBotsMessage nam = (NotifyBotsMessage) msg;
        for (Bot ai : Server.getBotPlayerList()) {
          if (ai.getPlayer().equals(nam.getBotPlayer())) {
            ai.makeMove();
          }
        }
        break;

      case GAME_STATE:
        GameStateMessage gsm = (GameStateMessage) msg;
        Server.updateBots(gsm.getGameState());
        Client.updateGameSession(gsm.getGameState());
        Channel ownChannel = ctx.channel();
        for (Channel channel : channels) {
          if (channel != ownChannel) {
            channel.writeAndFlush(msg);
          }
        }
        break;

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

  /**
   * Informs the remaining client that the other players have left and that the game can't continue
   * because there are not enough players remaining.
   *
   * @author tikrause
   */
  static void informTooFew() {
    for (Channel c : channels) {
      c.writeAndFlush(new TooFewPlayerMessage(null));
    }
  }
}
