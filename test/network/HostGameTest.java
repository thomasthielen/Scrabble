package network;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gameentities.Player;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import network.messages.ConnectMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is the test class to make sure the functional requirements of the UC3.1: Host Game work
 * correctly.
 *
 * @author lsteltma
 */
class HostGameTest {

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
  void testHostGame() {
    ConnectMessage cm = new ConnectMessage(player);
    ArrayList<Player> players;
    try {
      Server.createServer(port);
      Client.initialiseClient(Server.getIp(), port, true);
      Client.connectToServer(player);
      serverHandler.channelRead0(ctx, cm);
      players = Server.getPlayerList();
      assertFalse(players.isEmpty());
      assertTrue(players.contains(player));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
