package network;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gameentities.Player;
import io.netty.channel.ChannelHandlerContext;
import network.messages.ConnectMessage;
import network.messages.DisconnectMessage;
import network.messages.Message;
import network.messages.MessageType;

class LeaveGameTest {

  private Player p;
  private int port;
  private ServerHandler serverHandler;
  private ChannelHandlerContext ctx;

  @BeforeEach
  void init() {
    p = new Player("host");
    port = 8000;
    serverHandler = new ServerHandler();
  }

  @Test
  void testLeaveGame() {
    ConnectMessage cm = new ConnectMessage(p);
    DisconnectMessage dm = new DisconnectMessage(p, true);
    ArrayList<Player> players;
    try {
      Server.createServer(port);
      Client.initialiseClient(Server.getIp(), port, true);
      Client.connectToServer(p);
      serverHandler.channelRead0(ctx, cm);
      players = Server.getPlayerList();
      assertFalse(players.isEmpty());
      assertTrue(players.contains(p));
      
      Client.disconnectClient(p);
      serverHandler.channelRead0(ctx, dm);
      assertTrue(players.isEmpty());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
