package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Assassin;
import dungeonmania.response.models.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Entities.Mercenary;

public class SceptreTests {
    @Test
    @DisplayName("Test basic mind control")
    public void testAssassinMindControlSuccessfully() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_sceptreTest_controlAssassin", "c_assassinBasic");
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        for (Direction d : cmds){
            dmc.tick(d);
        }
        assertDoesNotThrow(() -> {dmc.build("sceptre");});
        Entity a = dmc.getDungeon().getEntitiesAtPosition(new Position(4, 0)).get(0);
        assertNotNull(a);
        assertDoesNotThrow(() -> {dmc.interact(a.getId());});

        assertTrue(((Assassin)a).isAllied());
        assertEquals(1, dmc.getDungeon().getPlayer().numAllies());
    }

    @Test
    @DisplayName("Test after mindcontrol, movement of assassin")
    public void testAssassinMindControlMovement() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_sceptreTest_controlAssassin", "c_assassinBasic");
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        for (Direction d : cmds){
            dmc.tick(d);
        }
        assertDoesNotThrow(() -> {dmc.build("sceptre");});
        Entity a = dmc.getDungeon().getEntitiesAtPosition(new Position(4, 0)).get(0);
        assertNotNull(a);
        assertDoesNotThrow(() -> {dmc.interact(a.getId());});

        assertTrue(((Assassin)a).isAllied());
        assertEquals(1, dmc.getDungeon().getPlayer().numAllies());

        // assassin should now follow player
        dmc.tick(Direction.LEFT);
        assertTrue(a.getPosition().equals(new Position(3, 0)), a.getPosition().toString());

        dmc.tick(Direction.LEFT);
        assertTrue(a.getPosition().equals(new Position(2, 0)), a.getPosition().toString());

        dmc.tick(Direction.RIGHT);
        assertTrue(a.getPosition().equals(new Position(1, 0)), a.getPosition().toString());

        for (int i = 0; i < 2; i++) {
            res = dmc.tick(Direction.DOWN);
        }
        assertTrue(a.getPosition().equals(new Position(2, 1)), a.getPosition().toString());
    }

    @Test
    @DisplayName("Test mind control on mercenary")
    public void testMercenaryMindControlSimple(){
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_sceptreTest_controlMercenary", "c_mercenaryTest_basicMovement");
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        for (Direction d : cmds){
            dmc.tick(d);
        }
        assertDoesNotThrow(() -> {dmc.build("sceptre");});
        Entity a = dmc.getDungeon().getEntitiesAtPosition(new Position(4, 0)).get(0);
        assertNotNull(a);
        assertDoesNotThrow(() -> {dmc.interact(a.getId());});

        assertTrue(((Mercenary)a).isAllied());
        assertEquals(1, dmc.getDungeon().getPlayer().numAllies());
    }

    @Test
    @DisplayName("Test Movement After Mind Control")
    public void testMercenarySceptreSimpleMovement(){
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_sceptreTest_controlMercenary", "c_assassinBasic");
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        for (Direction d : cmds){
            dmc.tick(d);
        }
        assertDoesNotThrow(() -> {dmc.build("sceptre");});
        Entity a = dmc.getDungeon().getEntitiesAtPosition(new Position(4, 0)).get(0);
        assertNotNull(a);
        assertDoesNotThrow(() -> {dmc.interact(a.getId());});

        assertTrue(((Mercenary)a).isAllied());
        assertEquals(1, dmc.getDungeon().getPlayer().numAllies());

        // assassin should now follow player
        dmc.tick(Direction.LEFT);
        assertTrue(a.getPosition().equals(new Position(3, 0)), a.getPosition().toString());

        dmc.tick(Direction.LEFT);
        assertTrue(a.getPosition().equals(new Position(2, 0)), a.getPosition().toString());

        dmc.tick(Direction.RIGHT);
        assertTrue(a.getPosition().equals(new Position(1, 0)), a.getPosition().toString());

        for (int i = 0; i < 2; i++) {
            res = dmc.tick(Direction.DOWN);
        }
        assertTrue(a.getPosition().equals(new Position(2, 1)), a.getPosition().toString());

        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.DOWN);
        }

        assertTrue(!((Mercenary)a).isAllied());
        assertEquals(0, dmc.getDungeon().getPlayer().numAllies());
    }
}
