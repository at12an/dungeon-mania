package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.*;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Entities.*;

import static dungeonmania.TestUtils.getPlayer;
import java.util.*;

public class PotionsTest {
    
    @Test
    @DisplayName("Test if the potion has been consumed") 
    public void testBasicPotionUsage() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_collectableTest_intermediate", "c_collectableTest_intermediate");

        EntityResponse check = TestUtils.getEntities(res, "invincibility_potion").get(0);

        Direction[] cmds = {
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT, 
        };

        for (Direction d : cmds) {
            dmc.tick(d);
        }

        DungeonResponse finalDungeon = dmc.getDungeonResponseModel();
        EntityResponse finalState = getPlayer(finalDungeon).get();

        Position potionPos = new Position(6, 0);

        assertEquals(potionPos, finalState.getPosition());

        // check if used
        assertDoesNotThrow(() -> dmc.tick(check.getId()));

        // check potion out of inventory
        DungeonResponse finalDungeon1 = dmc.getDungeonResponseModel();
        ItemResponse item = new ItemResponse(check.getId(), check.getType());
        assertTrue(!TestUtils.getInventory(finalDungeon1, "invincibility_potion").contains(item));
    }

    @Test
    @DisplayName("Test if used invisible, still go through portal")
    public void testIsInvisibleGoThroughPortal() {
        
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_potionTest_portalSuccess", "c_potionTest_noBattleInvisible");

        DungeonResponse finalDungeon1 = dmc.getDungeonResponseModel();
        EntityResponse check = TestUtils.getEntities(finalDungeon1, "invisibility_potion").get(0);

        // go right pick up the potion and use it
        dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.tick(check.getId()));

        // go through portal under invisibility
        dmc.tick(Direction.RIGHT);

        EntityResponse player = TestUtils.getPlayer(finalDungeon1).get();
        Position oldPos = new Position(2, 0);

        assertFalse(oldPos.equals(player.getPosition()));
    }

    @Test
    @DisplayName("Test if invisible, no battle")
    public void testNoBattleInvisible() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_potionTest_noBattleInvisible", "c_potionTest_noBattleInvisible");

        DungeonResponse finalDungeon = dmc.getDungeonResponseModel();
        EntityResponse check = TestUtils.getEntities(finalDungeon, "invisibility_potion").get(0);
        EntityResponse checkSword = TestUtils.getEntities(finalDungeon, "sword").get(0);

        // Move 2x right
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Use the potion
        assertDoesNotThrow(() -> dmc.tick(check.getId()));

        // tick 3x using a sword for spider to be in right place
        for (int i = 0; i < 3; i++) {
            assertThrows(IllegalArgumentException.class, () -> {
                dmc.tick(checkSword.getId());
            });
        }

        // move the player to meet spider, ensure no battle
        dmc.tick(Direction.DOWN);
        DungeonResponse finalDungeon1 = dmc.getDungeonResponseModel();
        List<BattleResponse> battle = finalDungeon1.getBattles();

        assertEquals(0, battle.size());
    }

    @Test 
    @DisplayName("Test if player invincible battle is won after 1 round")
    public void testInvincibleBattleWon() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_potionTest_invincibleBattleWin", "c_potionTest_invincibleBattle");

        DungeonResponse finalDungeon = dmc.getDungeonResponseModel();
        EntityResponse check = TestUtils.getEntities(finalDungeon, "invincibility_potion").get(0);
        EntityResponse checkSword = TestUtils.getEntities(finalDungeon, "sword").get(0);

        // Move 2x right
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // Use the potion
        assertDoesNotThrow(() -> dmc.tick(check.getId()));

        // tick 3x using a sword for spider to be in right place
        for (int i = 0; i < 3; i++) {
            assertThrows(IllegalArgumentException.class, () -> {
                dmc.tick(checkSword.getId());
            });
        }

        // check if spider and player in right position
        DungeonResponse finalDungeon1 = dmc.getDungeonResponseModel();
        EntityResponse player = TestUtils.getPlayer(finalDungeon1).get();
        EntityResponse checkSpider = TestUtils.getEntities(finalDungeon1, "spider").get(0);

        Position playerPos = new Position(2, 0);
        Position spiderPos = new Position(2, 2);

        assertEquals(playerPos, player.getPosition());
        assertEquals(spiderPos, checkSpider.getPosition());

        // check when meet on same square, player wins in one round
        dmc.tick(Direction.DOWN);
        DungeonResponse finalDungeon2 = dmc.getDungeonResponseModel();
        List<BattleResponse> battle = finalDungeon2.getBattles();

        assertEquals(1, battle.size());
        
        BattleResponse oneBattle = battle.get(0);
        assertEquals(1, oneBattle.getRounds().size());
    }

    @Test 
    @DisplayName("Test the effects of the invincibility potion only last for a limited time")
    public void testInvincibilityLastsLimited() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse check1 = dmc.newGame("d_testPotionDuration", "c_collectableTest_intermediate");
        EntityResponse check = TestUtils.getEntities(check1, "invincibility_potion").get(0);
    
        // Move one right to pick potion up
        dmc.tick(Direction.RIGHT);

        // Check if picked up
        ItemResponse potion1 = new ItemResponse(check.getId(), check.getType());
        DungeonResponse dungeonRes1 = dmc.getDungeonResponseModel();
        assertTrue(TestUtils.getInventory(dungeonRes1, "invincibility_potion").contains(potion1));

        EntityResponse checkMerc = TestUtils.getEntities(dungeonRes1, "mercenary").get(0);
        Position mercPos = new Position(3, 0);
        Position mercPos1 = new Position(4, 0);
        Position mercPos2 = new Position(5, 0);
        Position mercPos3 = new Position(6, 0);
        assertEquals(mercPos1, checkMerc.getPosition());

        // Use Potion
        assertDoesNotThrow(() -> dmc.tick(check.getId()));

        DungeonResponse dungeonRes2 = dmc.getDungeonResponseModel();

        // check merc position
        EntityResponse checkMerc1 = TestUtils.getEntities(dungeonRes2, "mercenary").get(0);
        assertEquals(mercPos2, checkMerc1.getPosition());

        // Now on next tick the merc should move away 
        dmc.tick(Direction.RIGHT);

        DungeonResponse dungeonRes3 = dmc.getDungeonResponseModel();
        EntityResponse checkMerc2 = TestUtils.getEntities(dungeonRes3, "mercenary").get(0);
        assertEquals(mercPos1, checkMerc2.getPosition());

        // Now the potion has run out and merc should move towards player
        dmc.tick(Direction.DOWN);

        DungeonResponse dungeonRes4 = dmc.getDungeonResponseModel();
        EntityResponse checkMerc3 = TestUtils.getEntities(dungeonRes4, "mercenary").get(0);

        assertEquals(mercPos, checkMerc3.getPosition());
    }

    @Test
    @DisplayName("Test queued potions duration")
    public void testQueuedPotions() {

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse dr1 = dmc.newGame("d_potionsTest_queued", "c_potionsQueued");
        EntityResponse potion1 = TestUtils.getEntities(dr1, "invincibility_potion").get(0);
        EntityResponse potion2 = TestUtils.getEntities(dr1, "invincibility_potion").get(1);

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // picked up 2 invincibility potions
        Entity m = dmc.getDungeon().getEntitiesAtPosition(new Position(6, 1)).get(0);
        assertNotNull(m);

        // use 1 potion
        assertDoesNotThrow(() -> dmc.tick(potion1.getId()));
        Entity m1 = dmc.getDungeon().getEntitiesAtPosition(new Position(7, 1)).get(0);
        assertEquals(m1.getType(), "mercenary");
        assertNotNull(m1);

        // move right 2x
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        Entity m2 = dmc.getDungeon().getEntitiesAtPosition(new Position(9, 1)).get(0);
        assertNotNull(m2);

        // use second potion
        assertDoesNotThrow(() -> dmc.tick(potion2.getId()));

        // go right 6x
        for (int i = 0; i < 6; i++) {
            dmc.tick(Direction.RIGHT);
        }

        Entity m3 = dmc.getDungeon().getEntitiesAtPosition(new Position(16, 1)).get(0);
        assertNotNull(m3);

        // effects worn off
        dmc.tick(Direction.RIGHT);
        Entity m4 = dmc.getDungeon().getEntitiesAtPosition(new Position(15, 1)).get(0);
        assertNotNull(m4);
    }
}
