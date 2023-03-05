package dungeonmania;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.response.models.ItemResponse;

import static dungeonmania.TestUtils.getEntities;

public class CollectableTests {

    @Test
    @DisplayName("Basic Collection Functionality")
    public void testCollectableBasic() {
        Direction[] cmds = new Direction[] {
                Direction.RIGHT,
                Direction.RIGHT,
                Direction.RIGHT,
                Direction.RIGHT,
        };

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_collectableTest_basic", "c_collectableTest_basic");

        for (Direction d : cmds) {
            dmc.tick(d);
        }

        // test items have been collected and are in the player's inventory
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();

        List<ItemResponse> expected = new ArrayList<>();
        expected.add(new ItemResponse("Entity2", "treasure"));
        expected.add(new ItemResponse("Entity3", "treasure"));
        expected.add(new ItemResponse("Entity4", "treasure"));

        assertEquals(3, TestUtils.getInventory(finalDungeonRes, "treasure").size());
        assertEquals(expected, TestUtils.getInventory(finalDungeonRes, "treasure"));
    }

    @Test
    @DisplayName("More advanced Collection Functionality")
    public void testCollectableIntermediate() {
        // test objects are collected for complicated case
        Direction[] cmd1 = new Direction[] {
                Direction.RIGHT,
                Direction.RIGHT,
                Direction.RIGHT,
                Direction.RIGHT,
                Direction.RIGHT,
                Direction.RIGHT,
                Direction.RIGHT,
                Direction.RIGHT,
                Direction.RIGHT,
                Direction.RIGHT,
        };

        Direction[] cmd2 = new Direction[] {
                Direction.DOWN,
                Direction.DOWN,
                Direction.DOWN,
                Direction.DOWN,
                Direction.DOWN,
                Direction.DOWN,
                Direction.DOWN,
                Direction.DOWN,
        };

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_collectableTest_intermediate", "c_collectableTest_intermediate");

        for (Direction d : cmd1) {
            dmc.tick(d);
        }

        // test half of the items have been collected and are in the player's inventory
        DungeonResponse DungeonRes1 = dmc.getDungeonResponseModel();

        List<ItemResponse> expected = new ArrayList<>();
        expected.add(new ItemResponse("Entity2", "treasure"));
        expected.add(new ItemResponse("Entity3", "bomb"));
        expected.add(new ItemResponse("Entity4", "arrow"));
        expected.add(new ItemResponse("Entity5", "wood"));
        expected.add(new ItemResponse("Entity6", "invincibility_potion"));
        expected.add(new ItemResponse("Entity7", "invisibility_potion"));
        expected.add(new ItemResponse("Entity8", "key"));
        expected.add(new ItemResponse("Entity9", "sword"));

        assertEquals(DungeonRes1.getEntities().size(), 10);
        assertEquals(expected, DungeonRes1.getInventory());

        for (Direction d : cmd2) {
            dmc.tick(d);
        }

        // test all of the items have been collected and are in the player's inventory
        // second key is not collected
        DungeonResponse DungeonRes2 = dmc.getDungeonResponseModel();

        expected.add(new ItemResponse("Entity10", "treasure"));
        expected.add(new ItemResponse("Entity11", "bomb"));
        expected.add(new ItemResponse("Entity12", "arrow"));
        expected.add(new ItemResponse("Entity13", "wood"));
        expected.add(new ItemResponse("Entity14", "invincibility_potion"));
        expected.add(new ItemResponse("Entity15", "invisibility_potion"));
        expected.add(new ItemResponse("Entity17", "sword"));

        assertEquals(DungeonRes2.getEntities().size(), 3);
        assertEquals(expected, DungeonRes2.getInventory());
    }

    @Test
    @DisplayName("Test player cannot pickup collectable when boulder is on same cell and immovable")
    public void testBoulderCollectable() {
        // test when a boulder is moved and the boulder is on top of a collectable the
        // collectable is picked up
        // and
        // test when a boulder cannot be moved and the boulder is on top of a
        // collectable the collectable is not pciked up

        Direction[] cmd1 = new Direction[] {
                Direction.RIGHT,
                Direction.RIGHT,
        };

        Direction[] cmd2 = new Direction[] {
                Direction.LEFT,
                Direction.LEFT,
                Direction.LEFT,
                Direction.LEFT,
        };

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_collectableTest_boulder", "c_collectableTest_intermediate");

        for (Direction d : cmd1) {
            dmc.tick(d);
        }

        DungeonResponse DungeonRes1 = dmc.getDungeonResponseModel();

        assertEquals(TestUtils.countEntityOfType(DungeonRes1, "treasure"), 1);

        List<ItemResponse> expected = new ArrayList<>();
        expected.add(new ItemResponse("Entity2", "treasure"));

        assertArrayEquals(expected.toArray(), DungeonRes1.getInventory().toArray());

        for (Direction d : cmd1) {
            dmc.tick(d);
        }

        DungeonResponse DungeonRes2 = dmc.getDungeonResponseModel();

        assertEquals(TestUtils.countEntityOfType(DungeonRes2, "treasure"), 1);
        assertArrayEquals(expected.toArray(), DungeonRes2.getInventory().toArray());
    }

    @Test
    @DisplayName("Test exception when using item type is not valid")
    public void testItemUsedInvalidType() {
        Direction[] cmds = new Direction[] {
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
        };

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_collectableTest_basic", "c_collectableTest_basic");
        String treasureId = getEntities(res, "treasure").get(0).getId();

        for (Direction d : cmds) {
            dmc.tick(d);
        }

        // test treasure is in inventory
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        assertEquals(3, TestUtils.getInventory(finalDungeonRes, "treasure").size());
        
        assertThrows(IllegalArgumentException.class, () -> dmc.tick(treasureId));
    }
}
