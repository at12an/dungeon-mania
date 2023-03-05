package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.countEntityOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class InterfaceMethodTests {

    @Test
    @DisplayName("Test newGame throws exception for non-existent config")
    public void testNewGameInvalidConfig() {
        DungeonManiaController dmc = new DungeonManiaController();

        // Should throw exception
        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("d_interfaceTests_simple", "non-existent-config-file"));
    }

    @Test
    @DisplayName("Test newGame throws exception for non-existent dungeon")
    public void testNewGameInvalidDungeon() {
        DungeonManiaController dmc = new DungeonManiaController();

        assertThrows(IllegalArgumentException.class, () -> dmc.newGame("non-existent-dungeon-file", "c_interfaceTests_simple"));
    }

    @Test
    @DisplayName("Test simple DungeonResponse for new game")
    public void testNewGameSuccessSimple() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_interfaceTests_simple", "c_interfaceTests_simple");

        // Check player created
        EntityResponse actualPlayer = getPlayer(res).get();

        assertEquals("player", actualPlayer.getType());
        assertEquals(new Position(2, 2), actualPlayer.getPosition());
        assertFalse(actualPlayer.isInteractable());
        assertTrue(actualPlayer.getId() != null);

        // Correct number of walls
        assertEquals(16, countEntityOfType(res, "wall"));
    }

}
