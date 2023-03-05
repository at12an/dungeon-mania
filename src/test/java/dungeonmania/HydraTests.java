package dungeonmania;
  
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static dungeonmania.TestUtils.getEntities;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class HydraTests {

    @Test
    @DisplayName("Test basic hydra movement")
    public void testHydraMovement() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_hydraMovement", "c_hydraMovement");
        Position hydraPos = getEntities(res, "hydra").get(0).getPosition();
        dmc.tick(Direction.DOWN);

        res = dmc.getDungeonResponseModel();

        // Acceptable movement locations
        List<Position> possibleDest = new ArrayList<>();
        possibleDest.add(new Position(hydraPos.getX(), hydraPos.getY() + 1));
        possibleDest.add(new Position(hydraPos.getX(), hydraPos.getY() - 1));
        possibleDest.add(new Position(hydraPos.getX() + 1, hydraPos.getY()));
        possibleDest.add(new Position(hydraPos.getX() - 1, hydraPos.getY()));

        EntityResponse hydraMove = getEntities(res, "hydra").get(0);

        assertTrue(possibleDest.contains(hydraMove.getPosition()));
    }

    @Test
    @DisplayName("Test hydra wins battle with 100% increase rate")
    public void testHydraBattleRateSuccess() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_hydraWinBattle", "c_hydraWinBattle");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        DungeonResponse res1 = dmc.getDungeonResponseModel();
        List<BattleResponse> battles = res1.getBattles();

        assertEquals(dmc.getDungeon().getEnemiesKilled(), 0);
        assertEquals(battles.size(), 1);
    }

    @Test
    @DisplayName("Test hydra wins battle with 0% increase rate")
    public void testHydraBattleRateUnsuccess() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_hydraWinBattle", "c_hydraLoseBattle");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        DungeonResponse res1 = dmc.getDungeonResponseModel();
        List<BattleResponse> battles = res1.getBattles();

        assertEquals(dmc.getDungeon().getEnemiesKilled(), 1);
        assertEquals(battles.size(), 1);
    }

    @Test
    @DisplayName("Test basic hydra movement with invincibility")
    public void testHydraMovementInvincible() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_hydraMovementInvincible", "c_hydraMovementInvincible");
    
        dmc.tick(Direction.RIGHT);

        // check pick up potion
        EntityResponse check = TestUtils.getEntities(res, "invincibility_potion").get(0);
        assertNotNull(check);

        assertDoesNotThrow(() -> dmc.tick(check.getId()));

        DungeonResponse dungres1 = dmc.getDungeonResponseModel();
        EntityResponse hydra = TestUtils.getEntities(dungres1, "hydra").get(0);
        Position originalPos = hydra.getPosition();

        dmc.tick(Direction.RIGHT);

        DungeonResponse dungres2 = dmc.getDungeonResponseModel();
        EntityResponse hydra1 = TestUtils.getEntities(dungres2, "hydra").get(0);
        assertEquals(originalPos.translateBy(1, 0), hydra1.getPosition());

        dmc.tick(Direction.RIGHT);

        DungeonResponse dungres3 = dmc.getDungeonResponseModel();
        EntityResponse hydra2 = TestUtils.getEntities(dungres3, "hydra").get(0);
        assertEquals(originalPos.translateBy(2, 0), hydra2.getPosition());
    }
}

