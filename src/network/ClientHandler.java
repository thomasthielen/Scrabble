package network;

import data.DataHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;
import network.messages.ConnectMessage;
import network.messages.DictionaryMessage;
import network.messages.DisconnectMessage;
import network.messages.GameStateMessage;
import network.messages.Message;
import network.messages.MessageType;
import network.messages.SendChatMessage;
import network.messages.StartGameMessage;
import session.GameState;

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
        ConnectMessage cm = (ConnectMessage) msg;
        if (Client.getGameSession().getLobbyScreenController() != null) {
          Client.getGameSession()
              .getLobbyScreenController()
              .receivedMessage(cm.getPlayer(), " has joined!");
          Client.getGameSession().getLobbyScreenController().refreshPlayerList();
        }
        break;

      case DISCONNECT:
        DisconnectMessage dcm = (DisconnectMessage) msg;

        // Option 1: The disconnecting player is the host of the game session

        if (dcm.isHost()) {

          // Option 1.1: The game is already running, but alre
          if (Client.getGameSession().getEndScreenController() == null
              && Client.getGameSession().getGameScreenController() != null) {
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    Client.getGameSession().getGameScreenController().hostHasLeft();
                  }
                });

            // Option 1.2: The game is not running yet, but the lobby is already created
          } else if (Client.getGameSession().getLobbyScreenController() != null) {
            Client.getGameSession()
                .getLobbyScreenController()
                .receivedMessage(dcm.getPlayer(), " has left!");
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    Client.getGameSession().getLobbyScreenController().hostHasLeft();
                  }
                });
          }

          // Option 2: The disconnecting player is not the host

        } else {

          // Option 2.1: The game is already running
          if (Client.getGameSession().getGameScreenController() != null) {
            Client.getGameSession()
                .getGameScreenController()
                .receivedMessage(dcm.getPlayer(), " has left!");
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    Client.getGameSession().getLobbyScreenController().refreshPlayerList();
                  }
                });

            // Option 2.2: The game is not running yet, but the lobby is already created
          } else if (Client.getGameSession().getLobbyScreenController() != null) {
            Client.getGameSession()
                .getLobbyScreenController()
                .receivedMessage(dcm.getPlayer(), " has left!");
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    Client.getGameSession().getLobbyScreenController().refreshPlayerList();
                  }
                });
          }
        }
        break;

      case START_GAME:
        StartGameMessage sgm = (StartGameMessage) msg;
        if (!Client.isHost()) {
          Client.getGameSession().switchToGameScreen(sgm.getChat());
          Client.getGameSession().setPlayable();
        }
        break;

      case END_GAME:
        Client.getGameSession().endGame();
        Platform.runLater(
            new Runnable() {
              @Override
              public void run() {
                Client.getGameSession().getGameScreenController().switchToEndScreen();
              }
            });
        break;

      case SEND_CHAT:
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
        GameStateMessage gsm = (GameStateMessage) msg;
        GameState gs = gsm.getGameState();
        Client.updateGameSession(gs);
        if (Client.getGameSession().getSinglePlayerLobbyScreenController() != null) {
          Platform.runLater(
              new Runnable() {
                @Override
                public void run() {
                  Client.getGameSession()
                      .getSinglePlayerLobbyScreenController()
                      .refreshPlayerList();
                }
              });
        }
        break;

      case DICTIONARY:
        DictionaryMessage dm = (DictionaryMessage) msg;
        DataHandler.setUserDictionary(dm.getDict());
        break;

      case TOO_FEW:
        if (Client.isHost()) {
          if (Client.getGameSession().getGameScreenController() != null) {
            Client.getGameSession().cancelTimer();
            Client.disconnectClient(DataHandler.getOwnPlayer());
            if (Server.isActive()) {
              Server.serverShutdown();
            }
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    Client.getGameSession().getGameScreenController().tooFewPlayerAlert();
                  }
                });
          }
        }
        break;

      case GAME_RUNNING:
        Platform.runLater(
            new Runnable() {
              @Override
              public void run() {
                Client.getGameSession().getLobbyScreenController().gameAlreadyRunning();
              }
            });
        break;

      case TOO_MANY:
        Platform.runLater(
            new Runnable() {
              @Override
              public void run() {
                Client.getGameSession().getLobbyScreenController().tooManyPlayers();
              }
            });
        break;

      case PLAYER_EXISTENT:
        Platform.runLater(
            new Runnable() {
              @Override
              public void run() {
                Client.getGameSession().getLobbyScreenController().playerAlreadyExisting();
              }
            });
        break;

      default:
        break;
    }
  }
}
