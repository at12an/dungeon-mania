package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getEntities;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Mercenary;
import dungeonmania.Entities.Collectables.Treasure;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MercenaryTest {
    @Test
    @DisplayName("Test basic mercenary movement")
    public void testMercenaryMovement() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_mercenaryTest_basicMovement", "c_mercenaryTest_basicMovement");

        Position pos = getEntities(res, "mercenary").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();

        Position temp = pos.translateBy(Direction.LEFT);
        movementTrajectory.add(temp);
        temp = temp.translateBy(Direction.LEFT);
        movementTrajectory.add(temp);
        temp = temp.translateBy(Direction.LEFT);
        movementTrajectory.add(temp);
        temp = temp.translateBy(Direction.UP);
        movementTrajectory.add(temp);
        temp = temp.translateBy(Direction.LEFT);
        movementTrajectory.add(temp);
        temp = temp.translateBy(Direction.UP);
        movementTrajectory.add(temp);
        temp = temp.translateBy(Direction.LEFT);
        movementTrajectory.add(temp);
        temp = temp.translateBy(Direction.UP);
        movementTrajectory.add(temp);
        temp = temp.translateBy(Direction.LEFT);
        movementTrajectory.add(temp);
        temp = temp.translateBy(Direction.UP);
        movementTrajectory.add(temp);

        for (int i = 0; i < 10; ++i) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(movementTrajectory.get(i), getEntities(res, "mercenary").get(0).getPosition());
        }
    }

    @Test
    @DisplayName("Test mercenary movement when blocked by wall")
    public void testMercenaryMovementBlocked() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_mercenaryTest_movementBlocked", "c_mercenaryTest_basicMovement");

        for (int i = 0; i < 6; i++) {
            res = dmc.tick(Direction.RIGHT);
        }
        Position mercPosition = getEntities(res, "mercenary").get(0).getPosition();

        assertTrue(mercPosition.equals(new Position(7, 5)), mercPosition.toString());
    }

    @Test
    @DisplayName("Test Basic Bribing")
    public void testMercenaryBribeSimple(){

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercAllyTest_simple", "c_TestMercAlly_bribe_amount_3");

        for (int i = 0; i < 3; i++) {
            res = dmc.tick(Direction.RIGHT);
        }
        // should only be one
        Entity m = dmc.getDungeon().getEntitiesAtPosition(new Position(5, 1)).get(0);
        assertNotNull(m);
        assertDoesNotThrow(() -> {dmc.interact(m.getId());});
        
        int numItems = 
            dmc.getDungeon().getPlayer().getInventory().stream().filter(e -> e.getClass().equals(Treasure.class)).toArray().length;
        assertEquals(0, numItems); // no more money :(
        assertTrue(((Mercenary)m).isAllied());
        assertEquals(1, dmc.getDungeon().getPlayer().numAllies());
    }

    @Test
    @DisplayName("Test Movement After Bribing")
    public void testMercenaryBribeSimpleMovement(){

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercAllyTest_simple", "c_TestMercAlly_bribe_amount_3");

        for (int i = 0; i < 3; i++) {
            res = dmc.tick(Direction.RIGHT);
        }
        // should only be one
        Entity m = dmc.getDungeon().getEntitiesAtPosition(new Position(5, 1)).get(0);
        assertNotNull(m);
        assertDoesNotThrow(() -> {dmc.interact(m.getId());});
        
        for (int i = 0; i < 2; i++) {
            res = dmc.tick(Direction.LEFT);
        }

        assertTrue(m.getPosition().equals(new Position(3, 1)), m.getPosition().toString());
        assertTrue(dmc.getDungeon().getPlayer().getPosition().equals(new Position(2, 1)));
    }

    @Test
    @DisplayName("Test can bribe three and proper snake movement")
    public void testThreeBribeAndSnakeMovement(){

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercAllyTest_three_mercs_within_three_tiles", "c_TestMercAlly_bribe_radius_four_no_cost");

        // should only be one
        Entity m1 = dmc.getDungeon().getEntitiesAtPosition(new Position(1, 0)).get(0);
        Entity m2 = dmc.getDungeon().getEntitiesAtPosition(new Position(1, 6)).get(0);
        Entity m3 = dmc.getDungeon().getEntitiesAtPosition(new Position(4, 3)).get(0);

        assertNotNull(m1);
        assertNotNull(m2);
        assertNotNull(m3);

        assertDoesNotThrow(() -> {dmc.interact(m1.getId());});
        assertDoesNotThrow(() -> {dmc.interact(m2.getId());});
        assertDoesNotThrow(() -> {dmc.interact(m3.getId());});

        
        for (int i = 0; i < 3; i++) {
            res = dmc.tick(Direction.LEFT);
        }

        assertTrue(m1.getPosition().equals(new Position(-1, 3)), m1.getPosition().toString());
        assertTrue(m2.getPosition().equals(new Position(0, 3)), m2.getPosition().toString());
        assertTrue(m3.getPosition().equals(new Position(1, 3)), m3.getPosition().toString());

        assertTrue(dmc.getDungeon().getPlayer().getPosition().equals(new Position(-2, 3)));
    }

    @Test
    @DisplayName("Test can bribe three and proper money deductions.")
    public void testThreeBribeAndMoneyRequirements(){

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercAllyTest_threeMerc", "c_TestMercAlly_bribe_amount_1");

        // should only be one
        Entity m1 = dmc.getDungeon().getEntitiesAtPosition(new Position(8, 1)).get(0);
        Entity m2 = dmc.getDungeon().getEntitiesAtPosition(new Position(10, 1)).get(0);
        Entity m3 = dmc.getDungeon().getEntitiesAtPosition(new Position(12, 1)).get(0);

        assertNotNull(m1);
        assertNotNull(m2);
        assertNotNull(m3);

        for (int i = 0; i < 3; i++) {
            res = dmc.tick(Direction.RIGHT);
        }

        assertDoesNotThrow(() -> {dmc.interact(m1.getId());});
        res = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> {dmc.interact(m2.getId());});
        res = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> {dmc.interact(m3.getId());});
    
        int numItems = 
            dmc.getDungeon().getPlayer().getInventory().stream().filter(e -> e.getClass().equals(Treasure.class)).toArray().length;
        assertEquals(0, numItems); // no more money :(
        assertTrue(((Mercenary)m1).isAllied());
        assertTrue(((Mercenary)m2).isAllied());
        assertTrue(((Mercenary)m3).isAllied());

        assertEquals(3, dmc.getDungeon().getPlayer().numAllies());
    }

    @Test
    @DisplayName("Test not enough Money")
    public void testNotEnoughMoney(){

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercAllyTest_rightNextToEachother", "c_TestMercAlly_bribe_amount_1");

        Entity m= dmc.getDungeon().getEntitiesAtPosition(new Position(2, 1)).get(0);
        assertThrows(InvalidActionException.class, () -> {dmc.interact(m.getId());});
        
    }

    @Test
    @DisplayName("Test out of bribing range")
    public void testOutOfBribingRange(){

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercAllyTest_three_mercs_within_three_tiles", "c_TestMercAlly_bribe_amount_1");

        Entity m= dmc.getDungeon().getEntitiesAtPosition(new Position(4, 3)).get(0);
        assertThrows(InvalidActionException.class, () -> {dmc.interact(m.getId());});
    }

    @Test
    @DisplayName("Test out of bribing range")
    public void testBadId(){

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercAllyTest_three_mercs_within_three_tiles", "c_TestMercAlly_bribe_amount_1");

        assertThrows(IllegalArgumentException.class, () -> {dmc.interact("m");});
    }

    @Test
    @DisplayName("test Can Move Through Portals")
    public void testCanMoveThroughPortals(){

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercAllyTest_portalsAndMercs", "c_TestMerAlly_PortalsAndMercs");

        // should only be one
        Entity m1 = dmc.getDungeon().getEntitiesAtPosition(new Position(10, -1)).get(0);
        Entity m2 = dmc.getDungeon().getEntitiesAtPosition(new Position(12, -1)).get(0);
        Entity m3 = dmc.getDungeon().getEntitiesAtPosition(new Position(14, -1)).get(0);

        assertNotNull(m1);
        assertNotNull(m2);
        assertNotNull(m3);

        Direction[] cmds = {
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.LEFT,
            Direction.LEFT,
            Direction.LEFT,
            Direction.LEFT,
            Direction.DOWN,
            Direction.LEFT,
            Direction.LEFT,
            Direction.UP,
            Direction.UP,
            Direction.UP,
        };
        
        int i = 0;
        for (Direction d : cmds){

            if (i == 2) assertDoesNotThrow(() -> {dmc.interact(m1.getId());});
            if (i == 4) assertDoesNotThrow(() -> {dmc.interact(m2.getId());}, m2.getId());
            if (i == 6) assertDoesNotThrow(() -> {dmc.interact(m3.getId());});
            dmc.tick(d);
            i++;

        }

        assertTrue(m3.getPosition().equals(new Position(1, -1)), m3.getPosition().toString());
        assertTrue(m2.getPosition().equals(new Position(1, -2)));
        assertTrue(m1.getPosition().equals(new Position(1, -3)));

    
        int numItems = 
            dmc.getDungeon().getPlayer().getInventory().stream().filter(e -> e.getClass().equals(Treasure.class)).toArray().length;
        assertEquals(0, numItems); // no more money :(
        assertTrue(((Mercenary)m1).isAllied());
        assertTrue(((Mercenary)m2).isAllied());
        assertTrue(((Mercenary)m3).isAllied());
        assertEquals(3, dmc.getDungeon().getPlayer().numAllies());
    }

    @Test
    @DisplayName("Test Running away Behaviour")
    public void testRunningAway(){
        
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercMovement_mercRunsAwayWhenPotionUsed", "c_TestMerAlly_PortalsAndMercs");
        Entity p = dmc.getDungeon().getEntitiesAtPosition(new Position(2, 1)).get(0);
        Entity m = dmc.getDungeon().getEntitiesAtPosition(new Position(8, 1)).get(0); 

        dmc.tick(Direction.RIGHT);
        assertTrue(dmc.getDungeon().getPlayer().getInventory().get(0).equals(p));
        assertDoesNotThrow(() -> dmc.tick(p.getId()));
        
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        
        assertEquals(new Position(6, 1), m.getPosition());
    }
}
