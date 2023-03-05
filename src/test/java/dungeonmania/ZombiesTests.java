package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.*;
import static dungeonmania.TestUtils.countEntityOfType;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.Entity;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position; 

public class ZombiesTests {
    @Test
    @DisplayName("Test one zombie spawns after enough ticks")
    public void testZombiesSpawnOne() { 
        DungeonManiaController dmc = new DungeonManiaController();

        dmc.newGame("d_zombieTest_basicSpawning", "c_zombieTest_basicSpawning");

        // Two ticks pass 
        dmc.tick(Direction.DOWN);
        DungeonResponse res = dmc.tick(Direction.DOWN);

        // Check that a zombie has spawned
        int numZombies = countEntityOfType(res, "zombie_toast");

        assertEquals(2, numZombies);
    }

    @Test
    @DisplayName("Test multiple zombies spawn after enough ticks")
    public void testZombiesSpawnMultiple() {
        DungeonManiaController dmc = new DungeonManiaController();

        dmc.newGame("d_zombieTest_basicSpawning", "c_zombieTest_basicSpawning");

        // Player is surrounded by boulders, so movement has no effect other than ticking time
        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.DOWN);
        }
        DungeonResponse res = dmc.getDungeonResponseModel();

        // Check that multiple zombies have spawned
        int numZombies = countEntityOfType(res, "zombie_toast");
        assertEquals(4, numZombies);
    }

    @Test
    @DisplayName("Test no zombies spawn since no open squares")
    public void testZombiesDontSpawn() {
        DungeonManiaController dmc = new DungeonManiaController();

        dmc.newGame("d_zombieTest_dontSpawn", "c_zombieTest_basicSpawning");

        // Player is surrounded by boulders, so movement has no effect other than ticking time
        for (int i = 0; i < 20; i++) {
            dmc.tick(Direction.DOWN);
        }
        DungeonResponse res = dmc.getDungeonResponseModel();

        // Check that no zombies have spawned (since spawner has no adjacent squares)
        int numZombies = countEntityOfType(res, "zombie_toast");
        assertEquals(0, numZombies);
    }

    @Test
    @DisplayName("Test basic zombie movement that a zombie moves to an adjacent square")
    public void testZombieMovementSimple() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_zombieTest_movementSimple", "c_zombieTest_movement");
        Position zombieInitPosition = getEntities(res, "zombie_toast").get(0).getPosition();

        // Player is surrounded by boulders, so movement has no effect other than ticking time
        dmc.tick(Direction.DOWN);

        res = dmc.getDungeonResponseModel();

        // Acceptable movement locations
        List<Position> possibleDest = new ArrayList<>();
        possibleDest.add(new Position(zombieInitPosition.getX(), zombieInitPosition.getY() + 1));
        possibleDest.add(new Position(zombieInitPosition.getX(), zombieInitPosition.getY() - 1));
        possibleDest.add(new Position(zombieInitPosition.getX() + 1, zombieInitPosition.getY()));
        possibleDest.add(new Position(zombieInitPosition.getX() - 1, zombieInitPosition.getY()));
        
        EntityResponse zombieMoved = getEntities(res, "zombie_toast").get(0);

        assertTrue(possibleDest.contains(zombieMoved.getPosition()));
    }

    @Test
    @DisplayName("Test zombies are blocked by walls")
    public void testZombieMovementBlockedWalls() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_zombieTest_movementWall", "c_zombieTest_movement");
        EntityResponse zombieInit = getEntities(res, "zombie_toast").get(0);

        // Player is surrounded by boulders, so movement has no effect other than ticking time
        dmc.tick(Direction.DOWN);

        res = dmc.getDungeonResponseModel();

        // Zombie does not move since blocked by walls
        
        EntityResponse zombieMoved = getEntities(res, "zombie_toast").get(0);

        assertEquals(zombieMoved.getPosition(), zombieInit.getPosition());
    }

    @Test
    @DisplayName("Test zombies are unable to push boulders")
    public void testZombieMovementBoulder() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_zombieTest_movementBoulder", "c_zombieTest_movement");
        EntityResponse zombieInit = getEntities(res, "zombie_toast").get(0);

        dmc.tick(Direction.DOWN);

        res = dmc.getDungeonResponseModel();

        // Zombie surrounded by boulders, cannot move
        EntityResponse zombieMoved = getEntities(res, "zombie_toast").get(0);

        assertEquals(zombieMoved.getPosition(), zombieInit.getPosition());
    }

    @Test
    @DisplayName("Test zombies can move over portals unaffected")
    public void testZombieMovementPortalUnaffected() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_zombieTest_movementPortal", "c_zombieTest_movement");
        Position zombieInitPosition = getEntities(res, "zombie_toast").get(0).getPosition();

        // Player is surrounded by boulders, so movement has no effect other than ticking time
        dmc.tick(Direction.DOWN);

        res = dmc.getDungeonResponseModel();

        // zombie surrounded by portals, zombie can just sit on top without teleporting
        
        // Acceptable movement locations
        List<Position> possibleDest = new ArrayList<>();
        possibleDest.add(new Position(zombieInitPosition.getX(), zombieInitPosition.getY() + 1));
        possibleDest.add(new Position(zombieInitPosition.getX(), zombieInitPosition.getY() - 1));
        possibleDest.add(new Position(zombieInitPosition.getX() + 1, zombieInitPosition.getY()));
        possibleDest.add(new Position(zombieInitPosition.getX() - 1, zombieInitPosition.getY()));
        
        EntityResponse zombieMoved = getEntities(res, "zombie_toast").get(0);

        assertTrue(possibleDest.contains(zombieMoved.getPosition()));
    }

    @Test
    @DisplayName("Test zombie toast spawners can be destroyed by weapons")
    public void testZombieSpawnersDestroyedByWeapon() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_zombieTest_destroySpawner", "c_zombieTest_destroySpawner");

        EntityResponse zombieToastSpawner = getEntities(res, "zombie_toast_spawner").get(0);

        // Move player right and collect sword
        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.RIGHT);
        }

        // Check sword in inventory
        res = dmc.getDungeonResponseModel();
        List<ItemResponse> items = getInventory(res, "sword");
        assertDoesNotThrow(() -> items.get(0));

        // move adjacent to spawner and interact
        for (int i = 0; i < 2; i++) {
            dmc.tick(Direction.RIGHT);
        }

        assertDoesNotThrow(() -> dmc.interact(zombieToastSpawner.getId()));
        
        // Check no spawners in dungeon
        res = dmc.getDungeonResponseModel();

        assertEquals(0, countEntityOfType(res, "zombie_toast_spawner"));
    }

    @Test
    @DisplayName("Test zombie toast spawners throw exception if no weapon")
    public void testZombieSpawnerExceptionNoWeapon() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_zombieTest_destroySpawner", "c_zombieTest_destroySpawner");

        EntityResponse zombieToastSpawner = getEntities(res, "zombie_toast_spawner").get(0);

        // Move player up and miss collecting sword
        dmc.tick(Direction.UP);

        //
        for (int i = 0; i < 7; i++) {
            dmc.tick(Direction.RIGHT);
        }

        // no sword, throws exception
        assertThrows(InvalidActionException.class, () -> dmc.interact(zombieToastSpawner.getId()));
    }

    @Test
    @DisplayName("Test zombie toast spawners throw exception if not adjacent")
    public void testZombieSpawnerExceptionNotAdjacent() {
        DungeonManiaController dmc = new DungeonManiaController();

        DungeonResponse res = dmc.newGame("d_zombieTest_destroySpawner", "c_zombieTest_destroySpawner");

        EntityResponse zombieToastSpawner = getEntities(res, "zombie_toast_spawner").get(0);

        // Move player right and collect sword
        for (int i = 0; i < 4; i++) {
            dmc.tick(Direction.RIGHT);
        }

        // Check sword in inventory
        res = dmc.getDungeonResponseModel();
        List<ItemResponse> items = getInventory(res, "sword");
        assertDoesNotThrow(() -> items.get(0));

        // not adjacent, throws exception
        assertThrows(InvalidActionException.class, () -> dmc.interact(zombieToastSpawner.getId()));
    }

    @Test
    @DisplayName("Test Running away Behaviour")
    public void testRunningAway() {
        
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_zombieTest_MovementWithPlayerInvincible", "c_zombieTest_movement");
        Entity p = dmc.getDungeon().getEntitiesAtPosition(new Position(2, 1)).get(0);
        Entity z = dmc.getDungeon().getEntities().get(1);

        dmc.tick(Direction.RIGHT);
        assertTrue(dmc.getDungeon().getPlayer().getInventory().get(0).equals(p));
        Position zomOldPos = z.getPosition();
        assertDoesNotThrow(() -> dmc.tick(p.getId()));
        assertEquals(z.getPosition(), zomOldPos.translateBy(1, 0));

        // Worn off effects
        dmc.tick(Direction.RIGHT);
        
        assertFalse(z.getPosition().equals(zomOldPos.translateBy(2, 0)));
    }
}
