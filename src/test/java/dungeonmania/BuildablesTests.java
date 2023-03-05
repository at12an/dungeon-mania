package dungeonmania;

import dungeonmania.Entities.Player;
import dungeonmania.Entities.Weapons.Bow;
import dungeonmania.Entities.Weapons.Shield;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.Collectables.*;


public class BuildablesTests {
    @Test
    @DisplayName("Testing bow is created")
    public void CreateBowTest() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_build_bow", "c_goalTestSimple");

        //collect ingredients
        Direction[] cmds = new Direction[] {
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
        };

        for (Direction d : cmds){
            dmc.tick(d);
        }

        //check player has right amount of items
        Player p = dmc.getDungeon().getPlayer();
    
        assertEquals(p.checkItemCount(Arrow.class), 3);
        assertEquals(p.checkItemCount(Wood.class), 1);

        //build bow
        List<String> expected = new ArrayList<>();
        expected.add("bow");
        assertArrayEquals(expected.toArray(), dmc.getDungeon().getPlayer().getBuildableResponse().toArray());
        assertDoesNotThrow(() -> dmc.build("bow"));
        
        //get player

        //check a bow now exists
        p = dmc.getDungeon().getPlayer();
        assertEquals(p.checkItemCount(Bow.class), 1);
        assertEquals(p.checkItemCount(Arrow.class), 0);
        assertEquals(p.checkItemCount(Wood.class), 0);
        
    }

    @Test
    @DisplayName("Testing shield is created using treasure")
    public void CreateShieldTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_build_shield", "c_goalTestSimple");

        //collect ingredients
        Direction[] cmds = new Direction[] {
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
        };

        for (Direction d : cmds){
            dmc.tick(d);
        }

        Player p = dmc.getDungeon().getPlayer();
        assertEquals(p.checkItemCount(Wood.class), 2);
        assertEquals(p.checkItemCount(Treasure.class), 1);
        assertEquals(p.checkItemCount(Key.class), 1);

        //build shield
        List<String> expected = new ArrayList<>();
        expected.add("shield");
        assertArrayEquals(expected.toArray(), dmc.getDungeon().getPlayer().getBuildableResponse().toArray());
        assertDoesNotThrow(() -> dmc.build("shield"));
        
        //get player

        //check a bow now exists
        p = dmc.getDungeon().getPlayer();
        assertEquals(p.checkItemCount(Shield.class), 1);
        assertEquals(p.checkItemCount(Wood.class), 0);
        assertEquals(p.checkItemCount(Treasure.class), 0);
        assertEquals(p.checkItemCount(Key.class), 1);
    }


    @Test
    @DisplayName("Testing shield is created using key")
    public void CreateShieldTest2() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_build_shield", "c_goalTestSimple");

        //collect ingredients
        Direction[] cmds = new Direction[] {
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
        };

        for (Direction d : cmds){
            dmc.tick(d);
        }

        Player p = dmc.getDungeon().getPlayer();
        assertEquals(p.checkItemCount(Wood.class), 2);
        assertEquals(p.checkItemCount(Treasure.class), 0);
        assertEquals(p.checkItemCount(Key.class), 1);

        //build shield
        List<String> expected = new ArrayList<>();
        expected.add("shield");
        assertArrayEquals(expected.toArray(), dmc.getDungeon().getPlayer().getBuildableResponse().toArray());
        assertDoesNotThrow(() -> dmc.build("shield"));
        
        //get player

        //check a bow now exists
        p = dmc.getDungeon().getPlayer();
        assertEquals(p.checkItemCount(Shield.class), 1);
        assertEquals(p.checkItemCount(Wood.class), 0);
        assertEquals(p.checkItemCount(Treasure.class), 0);
        assertEquals(p.checkItemCount(Key.class), 0);
    }

    @Test
    @DisplayName("Test exception when buildable not bow or shield")
    public void testBuildableExceptionInvalidType() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_build_shield", "c_goalTestSimple");

        assertThrows(IllegalArgumentException.class, () -> dmc.build("something"));
    }

    @Test
    @DisplayName("Test exception when insufficient materials for buildable")
    public void testBuildableExceptionInsufficientMaterials() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_build_shield", "c_goalTestSimple");

        //collect some ingredients (not all)
        Direction[] cmds = new Direction[] {
            Direction.RIGHT,
            Direction.RIGHT,
        };

        for (Direction d : cmds){
            dmc.tick(d);
        }

        DungeonResponse res = dmc.getDungeonResponseModel();

        assertEquals(2, res.getInventory().size());

        //test you can build the shield
        List<String> expected = new ArrayList<>();

        assertArrayEquals(expected.toArray(), dmc.getDungeon().getPlayer().getBuildableResponse().toArray());
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
    }
}
