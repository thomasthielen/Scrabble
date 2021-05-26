package network;

import ai.AI;
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
import network.messages.NotifyAIMessage;
import network.messages.TooFewPlayerMessage;
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

        // if a player tries to join a running game, he should be rejected
        if (!Server.isActive()) {
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
          ai.initializeAI(dm.getFile());
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
        Client.updateGameSession(gsm.getGameState());

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
  protected static void informTooFew() {
    for (Channel c : channels) {
      c.writeAndFlush(new TooFewPlayerMessage(DataHandler.getOwnPlayer()));
    }
  }

  /**
   * Sends a game state object with the updated player list to all players, when a new AI player has
   * been added.
   *
   * @author tikrause
   */
  protected static void updateAIPlayersInLobby() {
//	  System.out.println("Players:");
//	  for (Player p : Server.getPlayerList()) {
//		  System.out.println(p.getUsername()); 
//	  }
    for (Channel c : channels) {
      c.writeAndFlush(
          new GameStateMessage(DataHandler.getOwnPlayer(), new GameState(Server.getPlayerList())));
    }
  }
}
