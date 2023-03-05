package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getPlayer;

public class LogicalSwitchTests {
    @Test
    @DisplayName("Test logic switches and bombs (OR)")
    public void testLogicSwitchesBombsOr() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTests_bombsOr", "c_logicalTestsBasic");

        // Pick up bomb
        EntityResponse bomb = getEntities(res, "bomb").get(0);
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, res.getInventory().size());

        // place next to unactivated switch
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.tick(bomb.getId()));

        // move to push boulder onto switch
        // player boulder switch switch (or)
        //                        bomb (or)
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);

        res = dmc.getDungeonResponseModel();

        // check bomb, switches and boulder removed
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
    }

    @Test
    @DisplayName("Test logic switches and bombs (AND)")
    public void testLogicSwitchesBombsAnd() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTests_bombsAnd", "c_logicalTestsBasic");

        // Activate first switch
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);

        // Pick up and place bomb
        EntityResponse bomb = getEntities(res, "bomb").get(0);
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, res.getInventory().size());
        assertDoesNotThrow(() -> dmc.tick(bomb.getId()));

        // Check has not detonated yet
        res = dmc.getDungeonResponseModel();
        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(2, getEntities(res, "boulder").size());
        assertEquals(2, getEntities(res, "switch").size());

        // Activate second switch
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);

        // check bomb, switches and boulder removed
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
    }

    @Test
    @DisplayName("Test for logic switch and bombs (xor)")
    public void testLogicSwitchesBombsXor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTests_bombsXor", "c_logicalTestsBasic");

        // Activate first switch
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);

        // Activate second switch
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);

        // Collect bomb and place
        EntityResponse bomb = getEntities(res, "bomb").get(0);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        assertEquals(1, res.getInventory().size());
        assertDoesNotThrow(() -> dmc.tick(bomb.getId()));

        // Check it does not explode
        res = dmc.getDungeonResponseModel();
        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(2, getEntities(res, "boulder").size());
        assertEquals(2, getEntities(res, "switch").size());

        // Move one boulder off switch
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);

        // check bomb, switches and boulder removed
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
    }

    @Test
    @DisplayName("Test logic switches and bombs (CO_AND)")
    public void testLogicSwitchesBombsCoAnd() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTests_bombsCoand", "c_logicalTestsBasic");

        // Activate first switch
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);

        // Pick up and place bomb
        EntityResponse bomb = getEntities(res, "bomb").get(0);
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, res.getInventory().size());
        assertDoesNotThrow(() -> dmc.tick(bomb.getId()));

         // Check has not detonated yet
         res = dmc.getDungeonResponseModel();
         assertEquals(1, getEntities(res, "bomb").size());
         assertEquals(2, getEntities(res, "boulder").size());
         assertEquals(3, getEntities(res, "switch").size());
 
         // Activate second switch
         dmc.tick(Direction.DOWN);
         dmc.tick(Direction.DOWN);
         dmc.tick(Direction.LEFT);
         res = dmc.tick(Direction.UP);

         // Check has not detonated yet
         res = dmc.getDungeonResponseModel();
         assertEquals(1, getEntities(res, "bomb").size());
         assertEquals(2, getEntities(res, "boulder").size());
         assertEquals(3, getEntities(res, "switch").size());

        // Push one boulder out of the way
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);

        // Move around to put the other boulder on the OR switch
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.UP);

        /*
        *   boulder 
        *  switch(AND)  switch(OR) & boulder player
        *  switch(OR)        bomb
        */ 

        // Moving boulder to the next switch sets both the OR switches on same tick, 
        // triggering CO_AND for bomb
        res = dmc.tick(Direction.LEFT);

        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
    }

    @Test
    @DisplayName("Test switches lightbulb on")
    public void testLogicSwitchesBulbOr() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTests_bulbsOr", "c_logicalTestsBasic");

        // Initially bulb is off
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "light_bulb_on").size());

        // Player activates switch
        res = dmc.tick(Direction.RIGHT);

        // Bulb is on
        assertEquals(0, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());
    }

    @Test
    @DisplayName("Test switches wire chain turns lightbulbs on and off")
    public void testLogicSwitchesWiresBulbChain() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTests_wiresBulbChain", "c_logicalTestsBasic");

        // Initially bulb is off
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "light_bulb_on").size());

        // Player activates switch
        res = dmc.tick(Direction.RIGHT);

        // Bulb is on
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
    @DisplayName("Test switches and wires trigger co_and bulb")
    public void testLogicSwitchesWiresBulbCoand() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTests_wiresBulbCoand", "c_logicalTestsBasic");

        // Initially bulb is off
        assertEquals(1, getEntities(res, "light_bulb_off").size());
        assertEquals(0, getEntities(res, "light_bulb_on").size());

        // Player activates switch
        res = dmc.tick(Direction.RIGHT);

        // Bulb is on
        assertEquals(0, getEntities(res, "light_bulb_off").size());
        assertEquals(1, getEntities(res, "light_bulb_on").size());
        
    }

    @Test
    @DisplayName("Test switch opens a switch_door") 
    public void testLogicSwitchDoorOpens() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTests_doorsAnd", "c_logicalTestsBasic");

        // move player to next to the door
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Get position before moving to door
        Position playerPosBefore = getPlayer(res).get().getPosition();

        // Player should not have moved since door blocked
        res = dmc.tick(Direction.DOWN);
        Position playerPosAfter = getPlayer(res).get().getPosition();
        assertEquals(playerPosBefore, playerPosAfter);

        // Move player back to activate switch
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);

        // door is opened
        dmc.tick(Direction.RIGHT);

        // move player back 
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        Position playerPos = getPlayer(res).get().getPosition();
        Position switchDoorPos = getEntities(res, "switch_door").get(0).getPosition();
        assertEquals(playerPos, switchDoorPos);
    }

    @Test
    @DisplayName("Test door opened with key remains open when switch turned off")
    public void testLogicSwitchDoorRemainsOpens() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTests_doorKey", "c_logicalTestsBasic");

        // collect key
        res = dmc.tick(Direction.UP);
        assertEquals(1, res.getInventory().size());

        // move to switch_door
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // Player should be on door
        Position playerPos = getPlayer(res).get().getPosition();
        Position switchDoorPos = getEntities(res, "switch_door").get(0).getPosition();
        assertEquals(playerPos, switchDoorPos);

        // Move player back to activate switch
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);

        // switch is activated
        dmc.tick(Direction.RIGHT);

        // move player back 
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // player can still walk through door
        playerPos = getPlayer(res).get().getPosition();
        switchDoorPos = getEntities(res, "switch_door").get(0).getPosition();
        assertEquals(playerPos, switchDoorPos);

        // move back and deactivate switch
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);

        // move back 
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // player can still walk through door
        playerPos = getPlayer(res).get().getPosition();
        switchDoorPos = getEntities(res, "switch_door").get(0).getPosition();
        assertEquals(playerPos, switchDoorPos);
    }

    @Test
    @DisplayName("Test door closes when switch closed") 
    public void testLogicSwitchDoorCloses() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicalTests_doorCloses", "c_logicalTestsBasic");

        // switch is activated
        dmc.tick(Direction.RIGHT);

        // move player to door 
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // Player should be on door
        Position playerPos = getPlayer(res).get().getPosition();
        Position switchDoorPos = getEntities(res, "switch_door").get(0).getPosition();
        assertEquals(playerPos, switchDoorPos);

        // move back and deactivate switch
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);

        // move back 
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Get position before moving to door
        Position playerPosBefore = getPlayer(res).get().getPosition();

        // Player should not have moved since door closed again
        res = dmc.tick(Direction.DOWN);
        Position playerPosAfter = getPlayer(res).get().getPosition();
        assertEquals(playerPosBefore, playerPosAfter);
    }

}
