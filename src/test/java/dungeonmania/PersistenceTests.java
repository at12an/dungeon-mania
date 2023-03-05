package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.*;

public class PersistenceTests {
    @Test
    @DisplayName("Test for save and load for a simple dungeon (player, walls and exit)")
    public void testPersistenceSaveAndLoadSimple() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse initRes = dmc.newGame("d_persistenceTests_simple", "c_persistenceTests_simple");

        EntityResponse initPlayer = getPlayer(initRes).get();
        EntityResponse initWall = getEntities(initRes, "wall").get(0);
        EntityResponse initExit = getEntities(initRes, "exit").get(0);

        dmc.saveGame("persistence_test_1");

        // Game is in available saves and can be loaded
        assertTrue(dmc.allGames().stream().anyMatch(game -> game.equals("persistence_test_1")));

        assertDoesNotThrow(() -> dmc.loadGame("persistence_test_1"));

        // Check entities are in same positions
        DungeonResponse res = dmc.getDungeonResponseModel();

        EntityResponse player = getPlayer(res).get();
        EntityResponse wall = getEntities(res, "wall").get(0);
        EntityResponse exit = getEntities(res, "exit").get(0);

        assertEquals(initPlayer.getPosition(), player.getPosition());
        assertEquals(initWall.getPosition(), wall.getPosition());
        assertEquals(initExit.getPosition(), exit.getPosition());
    }

    @Test
    @DisplayName("Test for load exception, loading non-existent game")
    public void testPersistenceLoadException() {
        DungeonManiaController dmc = new DungeonManiaController();

        assertThrows(IllegalArgumentException.class, () -> dmc.loadGame("does_not_exist"));
    }

    @Test
    @DisplayName("Test spider movement follows correct pattern after load")
    public void testPersistenceSpiderMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_basicMovement", "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x  , y-1));
        movementTrajectory.add(new Position(x+1, y-1));
        movementTrajectory.add(new Position(x+1, y));
        movementTrajectory.add(new Position(x+1, y+1));
        movementTrajectory.add(new Position(x  , y+1));
        movementTrajectory.add(new Position(x-1, y+1));
        movementTrajectory.add(new Position(x-1, y));
        movementTrajectory.add(new Position(x-1, y-1));

        // Move player for a few ticks
        for (int i = 0; i <= 5; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "spider").get(0).getPosition());
            
            nextPositionElement++;
        }

        // Save game and then load from save
        dmc.saveGame("persistence_test_2");
        assertDoesNotThrow(() -> dmc.loadGame("persistence_test_2"));

        // Check movement of spider picks up where it left
        for (int i = 0; i <= 20; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "spider").get(0).getPosition());
            
            nextPositionElement++;
            if (nextPositionElement == 8){
                nextPositionElement = 0;
            }
        }
    }

    @Test
    @DisplayName("Test persistence maintains ally state for mercs that have been bribed")
    public void testPersistenceAlliesMaintained() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercAllyTest_three_mercs_within_three_tiles", "c_TestMercAlly_bribe_radius_four_no_cost");

        // get mercs
        List<EntityResponse> mercenaries = getEntities(res, "mercenary");

        // bribe three mercs
        for (EntityResponse merc : mercenaries) {
            assertDoesNotThrow(() -> dmc.interact(merc.getId()));
        }

        // Save game and then load from save
        dmc.saveGame("persistence_test_3");
        assertDoesNotThrow(() -> dmc.loadGame("persistence_test_3"));

        // Check movement still has the mercs behaving as allies and following player
        for (int i = 0; i < 3; i++) {
            res = dmc.tick(Direction.LEFT);
        }

        EntityResponse player = getPlayer(res).get();
        assertEquals(new Position(-2, 3), player.getPosition());

        // Expected merc positions
        List<Position> expectedPositions = new ArrayList<>();
        expectedPositions.add(new Position(-1, 3));
        expectedPositions.add(new Position(0, 3));
        expectedPositions.add(new Position(1, 3));

        // Where the mercs are
        mercenaries = getEntities(res, "mercenary");
        List<Position> observedPositions = new ArrayList<>();
        for (EntityResponse merc: mercenaries) {
            observedPositions.add(merc.getPosition());
        }

        // Assert the lists are equal (ignore order)
        assertEquals(expectedPositions.size(), observedPositions.size());
        assertTrue(expectedPositions.containsAll(observedPositions));
        assertTrue(observedPositions.containsAll(expectedPositions));

    }

    @Test
    @DisplayName("Test persistence of battle history")
    public void testPersistenceBattleHistory() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleTest_basicMercenary", "c_battleTests_basicMercenaryMercenaryDies");
        int mercenaryCount = countEntityOfType(res, "mercenary");
        
        // Start a battle
        assertEquals(1, countEntityOfType(res, "player"));
        assertEquals(1, mercenaryCount);
        dmc.tick(Direction.RIGHT);

        // Save game and then load from save
        dmc.saveGame("persistence_test_4");
        assertDoesNotThrow(() -> dmc.loadGame("persistence_test_4"));

        // Get battle history after save
        res = dmc.getDungeonResponseModel();
        BattleResponse battle = res.getBattles().get(0);

        assertBattleCalculations("mercenary", battle, true, "c_battleTests_basicMercenaryMercenaryDies");
    }

    @Test
    @DisplayName("Test persistence of inventory")
    public void testPersistencePlayerInventory() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_basic", "c_bombTest_placeBombRadius2");

        EntityResponse bomb = getEntities(res, "bomb").get(0);

        // Movements to collect bomb
        Direction[] cmdsInit = new Direction[]{
            Direction.DOWN,
            Direction.RIGHT,
            Direction.RIGHT,
        };

        for (Direction d : cmdsInit){
            dmc.tick(d);
        }

        // Save game and then load from save
        dmc.saveGame("persistence_test_5");
        assertDoesNotThrow(() -> dmc.loadGame("persistence_test_5"));

        // Bomb is still in inventory and can be placed
        res = dmc.getDungeonResponseModel();
        assertEquals(1, res.getInventory().size());
        assertDoesNotThrow(() -> dmc.tick(bomb.getId()));

        Direction[] cmdsNext = new Direction[] {
            Direction.LEFT,
            Direction.LEFT,
            Direction.UP,
            Direction.RIGHT,
        };

        for (Direction d : cmdsNext){
            dmc.tick(d);
        }
        // Check Bomb exploded with radius 2
        //
        //              Boulder/Switch      Wall            Wall
        //              Bomb                Treasure
        //
        //              Treasure
        res = dmc.getDungeonResponseModel();
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(0, getEntities(res, "wall").size());
        assertEquals(0, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());
    }

    @Test
    @DisplayName("Test logic gates and persistence")
    public void testLogicSwitchPersistence() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTests_gateXor", "c_logicalTestsBasic");

        // Initially bulb is off
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "light_bulb_on").size());

        // Player activates switch
        res = dmc.tick(Direction.RIGHT);

        // Bulb is on
        assertEquals(0, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());

        // Save game and then load from save
        dmc.saveGame("persistence_test_6");
        assertDoesNotThrow(() -> dmc.loadGame("persistence_test_6"));

        // Bulb is still on
        assertEquals(0, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());

        // Player moves boulder off switch
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);

        // bulb is off
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "light_bulb_on").size());
    }

    @Test
    @DisplayName("Test persistence of potion use for the player")
    public void testPersistencePotionUsage() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_potionsTest_queued", "c_potionsQueued");
        EntityResponse potion1 = TestUtils.getEntities(res, "invincibility_potion").get(0);
        EntityResponse potion2 = TestUtils.getEntities(res, "invincibility_potion").get(1);

        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // picked up 2 invincibility potions
        assertEquals(2, res.getInventory().size());

        // Use one potion
        assertDoesNotThrow(() -> dmc.tick(potion1.getId()));

        // Save game and then load from save
        dmc.saveGame("persistence_test_7");
        assertDoesNotThrow(() -> dmc.loadGame("persistence_test_7"));

        // move right 2x
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Check mercenary ran away
        EntityResponse merc = getEntities(res, "mercenary").get(0);
        assertEquals(new Position(9, 1), merc.getPosition());

        // Use the other potion
        assertDoesNotThrow(() -> dmc.tick(potion2.getId()));
    }
}
