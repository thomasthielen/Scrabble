package network;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gameentities.Player;
import io.netty.channel.ChannelHandlerContext;
import network.messages.ConnectMessage;

class JoinGameTest {

  private Player p1, p2;
  private int port;
  private ServerHandler serverHandler;
  private ChannelHandlerContext ctx;
  
  @BeforeEach
  void init() {
    p1 = new Player("host");
    p2 = new Player("join");
    port = 8000;
    serverHandler = new ServerHandler();
  }
  
  @Test
  void testJoinGame() {
    ConnectMessage cm1 = new ConnectMessage(p1);
    ConnectMessage cm2 = new ConnectMessage(p2);
    ArrayList<Player> players;
    try {
      Server.createServer(port);
      Client.initialiseClient(Server.getIp(), port, true);
      Client.connectToServer(p1);
      Client.initialiseClient(Server.getIp(), port, false);
      Client.connectToServer(p2);
      serverHandler.channelRead0(ctx, cm1);
      serverHandler.channelRead0(ctx, cm2);
      players = Server.getPlayerList();
      assertFalse(players.isEmpty());
      assertTrue(players.contains(p1));
      assertTrue(players.contains(p2));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
