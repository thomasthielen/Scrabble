package network;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gameentities.Player;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import network.messages.Message;
import network.messages.SendChatMessage;
import screens.GameScreenController;
import screens.LobbyScreenController;

class ChatTest {

  private Player p1, p2;
  private int port;

  @BeforeEach
  void init() {
    p1 = new Player("p1");
    p2 = new Player("p2");
    port = 8000;
  }

  @Test
  void testSendMessage() {
    
    try {
      Server.createServer(port);
      Client.initialiseClient(Server.getIp(), port, true);
      Client.connectToServer(p1);
      Client.initialiseClient(Server.getIp(), port, true);
      Client.connectToServer(p2);
      Client.sendChat(p1, "Hallo");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @Test
  void testReceiveMessage() {
    
  }
}
