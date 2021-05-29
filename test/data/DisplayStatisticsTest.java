package data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gameentities.Avatar;
import gameentities.Player;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is the test class to make sure the functional requirements of the UC5: Display Statistics
 * work.
 *
 * @author lsteltma
 */
class DisplayStatisticsTest {

  private Player player;

  @BeforeEach
  void init() {
    player = new Player("player", Avatar.BLACK);
    DataHandler.addPlayer(player.getUsername(), player.getAvatar());
    DataHandler.setOwnPlayer(player);
  }

  @Test
  void testPlayerStatistics() {
    DataHandler.addStatistics(DataHandler.getOwnPlayerId(), true, 40);
    HashMap<StatisticKeys, Integer> statistics =
        DataHandler.getStatistics(DataHandler.getOwnPlayerId());
    assertEquals(1, statistics.get(StatisticKeys.WON));
    assertEquals(1, statistics.get(StatisticKeys.MATCHES));
    assertEquals(40, statistics.get(StatisticKeys.POINTSAVG));
    DataHandler.deletePlayer(DataHandler.getOwnPlayerId());
  }
}
