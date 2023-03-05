package dungeonmania;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.response.models.ItemResponse;

public class MidnightArmourTests {
    @Test
    @DisplayName("Test battle with midnight armour and no zombies")
    public void TestMidnightArmourBattle() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_midnightArmourTest_battle", "c_shieldBattle");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        assertDoesNotThrow(() -> dmc.build("midnight_armour"));

        DungeonResponse dungeonRes1 = dmc.getDungeonResponseModel();
        List<ItemResponse> expected = new ArrayList<>();
        expected.add(new ItemResponse("Entity2", "midnight_armour"));

        dmc.tick(Direction.RIGHT);

        assertEquals(expected, dungeonRes1.getInventory());

        assertEquals(dmc.getDungeon().getEnemiesKilled(), 1);
    }

    @Test
    @DisplayName("Test battle with midnight armour with zombies")
    public void TestMidnightArmourBattleWZombie() {
        
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_midnightArmourTest_battleZombie", "c_shieldBattle");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        assertDoesNotThrow(() -> dmc.build("midnight_armour"));

        DungeonResponse dungeonRes1 = dmc.getDungeonResponseModel();
        List<ItemResponse> expected = new ArrayList<>();
        expected.add(new ItemResponse("Entity2", "midnight_armour"));

        dmc.tick(Direction.RIGHT);

        assertEquals(expected, dungeonRes1.getInventory());

        assertEquals(dmc.getDungeon().getEnemiesKilled(), 0);
    }
}
