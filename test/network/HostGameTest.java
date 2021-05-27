package network;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gameentities.Player;
import io.netty.channel.ChannelHandlerContext;
import network.messages.ConnectMessage;
import network.messages.DisconnectMessage;

/**
 * This is the test class to make sure the functional requirements of the UC3.1: Host Game work
 * correctly
 *
 * @author lsteltma
 */
class HostGameTest {

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
  void testHostGame() {
    ConnectMessage cm = new ConnectMessage(p);
    ArrayList<Player> players;
    try {
      Server.createServer(port);
      Client.initialiseClient(Server.getIp(), port, true);
      Client.connectToServer(p);
      serverHandler.channelRead0(ctx, cm);
      players = Server.getPlayerList();
      assertFalse(players.isEmpty());
      assertTrue(players.contains(p));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
