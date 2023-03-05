package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dungeonmania.TestUtils.getEntities;


public class BombTests {
    @Test
    @DisplayName("Test Basic Bomb Placement")
    public void testBombPlacement(){
        // Movements
        Direction[] cmds = new Direction[]{
            Direction.DOWN,
            Direction.RIGHT,
        };
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_basic", "c_boulderTest_simpleSetupForAll");

        EntityResponse bomb = getEntities(res, "bomb").get(0);
        // Run movements
        for (Direction d : cmds){
            dmc.tick(d);
        }
        // Commands to check info
        // Picked up bomb
        res = dmc.getDungeonResponseModel();
        assertEquals(1, res.getInventory().size());
        assertDoesNotThrow(() -> dmc.tick(bomb.getId()));
        res = dmc.getDungeonResponseModel();
        assertEquals(0, res.getInventory().size());
        // Check if bomb is placed
    }

    // Check for bomb explosion on placement
    // Check for bomb explosion on switch activation 
    @Test
    @DisplayName("Test bomb explodes when switch becomes active")
    public void testBombExplodesOnSwitchActivating() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_basic", "c_bombTest_placeBombRadius2");

        EntityResponse bomb = getEntities(res, "bomb").get(0);

        // Movements
        Direction[] cmdsInit = new Direction[]{
            Direction.DOWN,
            Direction.RIGHT,
            Direction.RIGHT,
        };

        for (Direction d : cmdsInit){
            dmc.tick(d);
        }

        // Bomb is collected and placed
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
    @DisplayName("Test exception for placing bomb not in inventory")
    public void testBombExceptionNotInInventory() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_basic", "c_boulderTest_simpleSetupForAll");

        EntityResponse bomb = getEntities(res, "bomb").get(0);
        assertThrows(InvalidActionException.class, () -> dmc.tick(bomb.getId()));
    }
}