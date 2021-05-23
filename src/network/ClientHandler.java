package network;

import data.DataHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import network.messages.*;
import screens.GameScreenController;
import screens.SinglePlayerLobbyScreenController;
import screens.StartScreen;
import session.GameState;
import gameentities.Player;

/**
 * Handles the received messages of the client.
 *
 * @author tikrause
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message> {

  /**
   * specifies and handles the messages received from the server.
   *
   * @author tikrause
   * @param ctx channel from which the message has been received
   * @param msg message that has been received
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
    MessageType mt = msg.getMessageType();
    System.out.println(mt);
    switch (mt) {
      case CONNECT:
        // TODO
        ConnectMessage cm = (ConnectMessage) msg;
        if (Client.getGameSession().getLobbyScreenController() != null) {
          Client.getGameSession()
              .getLobbyScreenController()
              .receivedMessage(cm.getPlayer(), " has joined!");
          Client.getGameSession().getLobbyScreenController().refreshPlayerList();
        }
        System.out.println(cm.getPlayer().getPlayerStatistics());
        break;
      case DISCONNECT:
        // TODO
        DisconnectMessage dcm = (DisconnectMessage) msg;
        if (dcm.isHost()) {
          if (Client.getGameSession().getLobbyScreenController() != null) {
            Client.getGameSession()
                .getLobbyScreenController()
                .receivedMessage(dcm.getPlayer(), " has left!");
            Client.getGameSession().getLobbyScreenController().refreshPlayerList();
          } else {
            // TODO Host leaves when game is still running
          }
          Client.disconnectClient(DataHandler.getOwnPlayer());
        } else {
          if (Client.getGameSession().getGameScreenController() != null) {
            Client.getGameSession()
                .getGameScreenController()
                .receivedMessage(dcm.getPlayer(), " has left!");
            // TODO player list in GScreen:
            // Client.getGameSession().getGameScreenController().refreshPlayerList();
          } else if (Client.getGameSession().getLobbyScreenController() != null) {
            Client.getGameSession()
                .getLobbyScreenController()
                .receivedMessage(dcm.getPlayer(), " has left!");
            Client.getGameSession().getLobbyScreenController().refreshPlayerList();
          }
        }
        break;
      case START_GAME:
        if (!Client.isHost()) {
          Client.getGameSession().switchToGameScreen();
          Client.getGameSession().setPlayable();
        }
        break;
      case END_GAME:
        // TODO
        break;
      case SEND_CHAT:
        // TODO
        SendChatMessage scm = (SendChatMessage) msg;
        if (Client.getGameSession().getGameScreenController() != null) {
          Client.getGameSession()
              .getGameScreenController()
              .receivedMessage(scm.getPlayer(), scm.getMessage());
        } else {
          Client.getGameSession()
              .getLobbyScreenController()
              .receivedMessage(scm.getPlayer(), scm.getMessage());
        }
        break;
      case GAME_STATE:
        // TODO
        GameStateMessage gsm = (GameStateMessage) msg;
        GameState gs = gsm.getGameState();
        Client.updateGameSession(gs);
        for (Player p : Client.getGameSession().getPlayerList()) {
          System.out.println(p.getUsername());
        }
        break;
      case DICTIONARY:
        DictionaryMessage dm = (DictionaryMessage) msg;
        DataHandler.userDictionaryFile(dm.getFile());
        break;
      case TOO_FEW:
    	  Client.getGameSession().getGameScreenController().tooFewPlayerAlert();
    	  break;
      default:
        break;
    }
  }
}
