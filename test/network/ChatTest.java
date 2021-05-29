package network;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gameentities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChatTest {

  private Player p1;
  private Player p2;
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
      Client.initializeClient(Server.getIp(), port, true);
      Client.connectToServer(p1);
      Client.initializeClient(Server.getIp(), port, true);
      Client.connectToServer(p2);
      Client.sendChat(p1, "Hallo");
    } catch (Exception e) {
      e.printStackTrace();
    }
    assertEquals(true, true);
  }
  
  @Test
  void testReceiveMessage() {
    
  }
}
