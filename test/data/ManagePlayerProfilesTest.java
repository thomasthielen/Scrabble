package data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import gameentities.Avatar;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

/**
 * This is the test class to make sure the functional requirements of the UC1: Manage Player
 * Profiles work correctly.
 *
 * @author lsteltma
 */
class ManagePlayerProfilesTest {

  private String[] data;

  // create player profiles
  @Test
  @Order(1)
  void testCreateProfile() {
    DataHandler.addPlayer("name", Avatar.BLACK);

    data = DataHandler.getPlayerInfo().get(DataHandler.getOwnPlayerId());

    assertEquals("name", data[0]);
    assertEquals("BLACK", data[1]);
  }

  // read and update player profiles
  @Test
  @Order(2)
  void testUpdatePlayerProfile() {

    DataHandler.alterPlayerUsername("alteredName", DataHandler.getOwnPlayerId());
    DataHandler.alterPlayerAvatar(Avatar.RED, DataHandler.getOwnPlayerId());

    data = DataHandler.getPlayerInfo().get(DataHandler.getOwnPlayerId());

    assertEquals("alteredName", data[0]);
    assertEquals("RED", data[1]);
  }

  // delete player profiles
  @Test
  @Order(3)
  void testDeletePlayerProfile() {
    DataHandler.deletePlayer(DataHandler.getOwnPlayerId());

    assertNull(DataHandler.getPlayerInfo().get(DataHandler.getOwnPlayerId()));
  }
}
