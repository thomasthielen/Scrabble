package session;

import java.net.BindException;
import java.net.UnknownHostException;

import data.DataHandler;
import gameentities.Player;
import network.Server;
import network.Client;

/**
 * MultiPlayerLobby is started by the host of the game itself. It is used to collect the Player
 * objects and synchronize the first GameState message.
 *
 * @author tikrause
 */
public class MultiPlayerLobby extends SinglePlayerLobby {

  /**
   * Constructor: Creates a first GameState object and the necessary Server/Client objects of the
   * host.
   *
   * @author tikrause
   */
  public MultiPlayerLobby(Player p) {
    super(p);
    int port = 8000;
    while (port < 65535) {
      try {
        Server.createServer(port);
        // TODO
        System.out.println(
            "Your lobby has been created. IP: " + Server.getIp() + ", Port: " + Server.getPort());
        Client.initialiseClient("localhost", port, true);
        Client.connectToServer(DataHandler.getOwnPlayer());
        break;
      } catch (BindException e) {
        port++;
      } catch (UnknownHostException e) {
        // TODO
      } catch (InterruptedException e) {
        // TODO
      }
    }
  }
}
