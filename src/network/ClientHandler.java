package network;

import data.DataHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import network.messages.*;
import session.GameState;

/**
 * Handles the received messages of the client
 *
 * @author tikrause
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message> {

  /**
   * specifies and handles the messages received from the server
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
        System.out.println(cm.getFrom() + " has joined!");
        System.out.println(cm.getPlayer().getPlayerStatistics());
        break;
      case DISCONNECT:
        DisconnectMessage dcm = (DisconnectMessage) msg;
        if (dcm.isHost()) {
          System.out.println("The host has left!");
        } else {
          System.out.println(dcm.getFrom() + " has left!");
        }
        break;
      case ERROR:
        break;
      case SUCCESS:
        break;
      case SEND_CHAT:
        SendChatMessage scm = (SendChatMessage) msg;
        System.out.println("[" + scm.getFrom() + "]: " + scm.getMessage());
        break;
      case UPDATE_GAME_STATE:
        UpdateGameStateMessage ugsm = (UpdateGameStateMessage) msg;
        GameState gs = ugsm.getGameState();
        Client.updateGameSession(gs);
        break;
      case DICTIONARY:
        break;
      case NEW_DICTIONARY:
        // TODO
        NewDictionaryMessage ndm = (NewDictionaryMessage) msg;
        DataHandler.userDictionaryFile(ndm.getFile());
        System.out.println(DataHandler.checkWord("mall"));
        break;
      case PLAYER:
        PlayerMessage pm = (PlayerMessage) msg;
        System.out.println(pm.getPlayer().getUsername());
        break;
    }
  }
}
