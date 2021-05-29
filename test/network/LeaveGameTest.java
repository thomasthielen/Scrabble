package network;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gameentities.Player;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import network.messages.ConnectMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaveGameTest {

  private Player player;
  private int port;
  private ServerHandler serverHandler;
  private ChannelHandlerContext ctx;

  @BeforeEach
  void init() {
    player = new Player("host");
    port = 8000;
    serverHandler = new ServerHandler();
  }

  @Test
  void testLeaveGame() {
    ConnectMessage cm = new ConnectMessage(player);
    //DisconnectMessage dm = new DisconnectMessage(p, true);
    ArrayList<Player> players;
    try {
      Server.createServer(port);
      Client.initialiseClient(Server.getIp(), port, true);
      Client.connectToServer(player);
      serverHandler.channelRead0(ctx, cm);
      players = Server.getPlayerList();
      assertFalse(players.isEmpty());
      assertTrue(players.contains(player));
      
      Client.disconnectClient(player);
      //serverHandler.channelRead0(ctx, dm);
      assertTrue(players.isEmpty());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
