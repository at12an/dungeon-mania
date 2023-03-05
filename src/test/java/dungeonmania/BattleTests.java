package dungeonmania;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.Entities.Collectables.InvincibilityPotion;
import dungeonmania.Entities.Collectables.InvisibilityPotion;


public class BattleTests {
    // test battle no start if player invisible
    @Test
    @DisplayName("Test if the player is invisible then no battle") 
    public void testInvisiblePlayerNoBattle() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_invisiblePlayerSpiderBattle", "c_invisibleBattleSpider");

        // go to the invisibility potion
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // player should have picked potion up and spider moved to position(0, 2)
        DungeonResponse dungeonRes1 = dmc.getDungeonResponseModel();
        List<ItemResponse> expected = new ArrayList<>();
        expected.add(new ItemResponse("Entity2", "sword"));
        expected.add(new ItemResponse("Entity3", "invisibility_potion"));
        
        // ensure the player has picked up potion and sword
        assertEquals(expected, dungeonRes1.getInventory());

        // use potion
        String correctId = dmc.getDungeon().getPlayer().getInventory().stream().filter(e -> e instanceof InvisibilityPotion).findFirst().orElse(null).getId();
        assertDoesNotThrow(() -> dmc.tick(correctId));
        
        // use sword as a tick until spider in range for player to move onto it
        String id = dmc.getDungeon().getPlayer().getinventoryResponse().get(0).getId();
        for (int i = 0; i < 3; i++) {
            assertThrows(IllegalArgumentException.class, () -> {
                dmc.tick(id);
            });
        }

        // Now the spider and player on same cell
        dmc.tick(Direction.DOWN);

        // Spider should be in x = 2 y = 0 and player in 2, 2
        dmc.tick(Direction.DOWN);

        // check player position
        DungeonResponse dungeonRes2 = dmc.getDungeonResponseModel();
        EntityResponse checkPlayer = TestUtils.getPlayer(dungeonRes2).get();
        assertEquals(new Position(2, 2), checkPlayer.getPosition());

        // check spider existence
        EntityResponse checkSpider = TestUtils.getEntities(dungeonRes2, "spider").get(0);
        assertEquals(new Position(2, 0), checkSpider.getPosition());
    }

    //Test instant win with invincibility
    @Test
    @DisplayName("Test battle is won instantly if player is invincible")
    public void TestInvinciblePlayerWinBattle() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_invincible", "c_invisibleBattleSpider");

        // go to the invisibility potion
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // player should have picked potion up and spider moved to position(0, 2)
        DungeonResponse dungeonRes1 = dmc.getDungeonResponseModel();
        List<ItemResponse> expected = new ArrayList<>();
        expected.add(new ItemResponse("Entity2", "sword"));
        expected.add(new ItemResponse("Entity3", "invincibility_potion"));
        
        // ensure the player has picked up potion and sword
        assertEquals(expected, dungeonRes1.getInventory());

        // use potion
        String correctId = dmc.getDungeon().getPlayer().getInventory().stream().filter(e -> e instanceof InvincibilityPotion).findFirst().orElse(null).getId();
        assertDoesNotThrow(() -> dmc.tick(correctId));
        
        // use sword as a tick until spider in range for player to move onto it
        String id = dmc.getDungeon().getPlayer().getinventoryResponse().get(0).getId();
        for (int i = 0; i < 3; i++) {
            assertThrows(IllegalArgumentException.class, () -> {
                dmc.tick(id);
            });
        }

        // Now the spider and player on same cell
        dmc.tick(Direction.DOWN);

        // Spider should be in x = 2 y = 0 and player in 2, 2
        dmc.tick(Direction.DOWN);

        // check player position
        DungeonResponse dungeonRes2 = dmc.getDungeonResponseModel();
        EntityResponse checkPlayer = TestUtils.getPlayer(dungeonRes2).get();
        assertEquals(new Position(2, 2), checkPlayer.getPosition());

        // check spider existence
        assertEquals(dmc.getDungeon().getEnemiesKilled(), 1);
        assertEquals(dungeonRes2.getBattles().size(), 1);
        for (BattleResponse b : dungeonRes2.getBattles()) {
            assertEquals(b.getRounds().size(), 1);
        }
    }

    @Test
    @DisplayName("Invincibility test")
    public void InvincibilityTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("invincibility_test", "invincibility_test");

        dmc.tick(Direction.LEFT);

        // use potion
        String correctId = dmc.getDungeon().getPlayer().getInventory().stream().filter(e -> e instanceof InvincibilityPotion).findFirst().orElse(null).getId();
        assertDoesNotThrow(() -> dmc.tick(correctId));

        for (int i = 0; i < 20; i++) {
            dmc.tick(Direction.RIGHT);
        }

        //test all battles finished on first round
        DungeonResponse dungeonRes2 = dmc.getDungeonResponseModel();
        for (BattleResponse b : dungeonRes2.getBattles()) {
            assertEquals(b.getRounds().size(), 1);
        }

    }

   // test basic battle
    //      same cell as enemy
    //      wihtin one tick either one is gone
    //      next tick the winner moves
    @Test
    @DisplayName("Test basic battle") 
    public void testBasicBattle() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_invisiblePlayerNoPotion", "c_invisibleBattleSpider");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // check if sword picked up
        DungeonResponse dungeonRes1 = dmc.getDungeonResponseModel();
        List<ItemResponse> expected = new ArrayList<>();
        expected.add(new ItemResponse("Entity2", "sword"));

        assertEquals(expected, dungeonRes1.getInventory());

        // try to use sword 4 times, whilst spider is moving
        String id = dmc.getDungeon().getPlayer().getinventoryResponse().get(0).getId();
        for (int i = 0; i < 4; i++) {
            assertThrows(IllegalArgumentException.class, () -> {dmc.tick(id);});
        }

        // check spider location and players location
        DungeonResponse dungeonRes2 = dmc.getDungeonResponseModel();
        EntityResponse checkSpider1 = TestUtils.getEntities(dungeonRes2, "spider").get(0);
        assertEquals(new Position(2, 2), checkSpider1.getPosition());

        EntityResponse checkPlayer = TestUtils.getPlayer(dungeonRes2).get();
        assertEquals(new Position(2, 0), checkPlayer.getPosition());
        
        // after this player and spider will be on same cell
        dmc.tick(Direction.DOWN);

        //check spider is dead
        assertEquals(dmc.getDungeon().getEnemiesKilled(), 1);
    }

    @Test
    @DisplayName("Test battle with swords")
    public void TestSwordBattle() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_invisiblePlayerNoPotion", "c_swordBattle");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // check if sword picked up
        DungeonResponse dungeonRes1 = dmc.getDungeonResponseModel();
        List<ItemResponse> expected = new ArrayList<>();
        expected.add(new ItemResponse("Entity2", "sword"));

        assertEquals(expected, dungeonRes1.getInventory());

        // try to use sword 4 times, whilst spider is moving
        String id = dmc.getDungeon().getPlayer().getinventoryResponse().get(0).getId();
        for (int i = 0; i < 4; i++) {
            assertThrows(IllegalArgumentException.class, () -> {dmc.tick(id);});
        }

        // check spider location and players location
        DungeonResponse dungeonRes2 = dmc.getDungeonResponseModel();
        EntityResponse checkSpider1 = TestUtils.getEntities(dungeonRes2, "spider").get(0);
        assertEquals(new Position(2, 2), checkSpider1.getPosition());

        EntityResponse checkPlayer = TestUtils.getPlayer(dungeonRes2).get();
        assertEquals(new Position(2, 0), checkPlayer.getPosition());
        
        // after this player and spider will be on same cell
        dmc.tick(Direction.DOWN);

        //check spider is dead
        assertEquals(dmc.getDungeon().getEnemiesKilled(), 1);

        List<RoundResponse> rounds = new ArrayList<>();

        List<ItemResponse> w = new ArrayList<>();
        w.add(new ItemResponse("Entity2", "sword"));

        for (int i = 0; i < 15; i++) {
            rounds.add(new RoundResponse(-0.1, -0.8, w));
        }

        BattleResponse expected2 = new BattleResponse("spider", rounds, 10, 12);

        assertEquals(dungeonRes2.getBattles().get(0).getEnemy(), expected2.getEnemy());
        assertEquals(dungeonRes2.getBattles().get(0).getInitialEnemyHealth(), expected2.getInitialEnemyHealth());
        assertEquals(dungeonRes2.getBattles().get(0).getInitialPlayerHealth(), expected2.getInitialPlayerHealth());
        assertEquals(dungeonRes2.getBattles().get(0).getRounds().size(), expected2.getRounds().size());
        for (int j = 0; j < 10; j++) {
            assertEquals(rounds.get(j).getDeltaCharacterHealth(), expected2.getRounds().get(j).getDeltaCharacterHealth());
            assertEquals(rounds.get(j).getDeltaEnemyHealth(), expected2.getRounds().get(j).getDeltaEnemyHealth());
            assertEquals(rounds.get(j).getWeaponryUsed(), expected2.getRounds().get(j).getWeaponryUsed());
        }
    }

    @Test
    @DisplayName("Test battle with bows")
    public void TestBowBattle() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_bow", "c_bowbattle");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // check if bow picked up
        assertDoesNotThrow(() -> dmc.build("bow"));

        // try to use sword 4 times, whilst spider is moving
        String id = dmc.getDungeon().getPlayer().getinventoryResponse().get(0).getId();
        for (int i = 0; i < 4; i++) {
            assertThrows(IllegalArgumentException.class, () -> {dmc.tick(id);});
        }

        DungeonResponse dungeonRes1 = dmc.getDungeonResponseModel();
        List<ItemResponse> expected = new ArrayList<>();
        expected.add(new ItemResponse("Entity7", "bow"));

        assertEquals(expected, dungeonRes1.getInventory());

        // check spider location and players location
        DungeonResponse dungeonRes2 = dmc.getDungeonResponseModel();
        EntityResponse checkSpider1 = TestUtils.getEntities(dungeonRes2, "spider").get(0);
        assertEquals(new Position(2, 2), checkSpider1.getPosition());

        EntityResponse checkPlayer = TestUtils.getPlayer(dungeonRes2).get();
        assertEquals(new Position(2, 0), checkPlayer.getPosition());
        
        // after this player and spider will be on same cell
        dmc.tick(Direction.DOWN);

        //check spider is dead
        assertEquals(dmc.getDungeon().getEnemiesKilled(), 1);

        List<RoundResponse> rounds = new ArrayList<>();

        List<ItemResponse> w = new ArrayList<>();
        w.add(new ItemResponse("Entity2", "sword"));

        for (int i = 0; i < 10; i++) {
            rounds.add(new RoundResponse(-0.1, -1.2, w));
        }

        BattleResponse expected2 = new BattleResponse("spider", rounds, 10, 12);

        assertEquals(dungeonRes2.getBattles().get(0).getEnemy(), expected2.getEnemy());
        assertEquals(dungeonRes2.getBattles().get(0).getInitialEnemyHealth(), expected2.getInitialEnemyHealth());
        assertEquals(dungeonRes2.getBattles().get(0).getInitialPlayerHealth(), expected2.getInitialPlayerHealth());
        assertEquals(dungeonRes2.getBattles().get(0).getRounds().size(), expected2.getRounds().size());
        for (int j = 0; j < 10; j++) {
            assertEquals(rounds.get(j).getDeltaCharacterHealth(), expected2.getRounds().get(j).getDeltaCharacterHealth());
            assertEquals(rounds.get(j).getDeltaEnemyHealth(), expected2.getRounds().get(j).getDeltaEnemyHealth());
            assertEquals(rounds.get(j).getWeaponryUsed(), expected2.getRounds().get(j).getWeaponryUsed());
        }
    }

    @Test
    @DisplayName("Test battle with shield")
    public void TestShieldBattle() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_shield", "c_shieldBattle");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // check if bow picked up
        assertDoesNotThrow(() -> dmc.build("bow"));

        assertDoesNotThrow(() -> dmc.build("shield"));

        // try to use sword 4 times, whilst spider is moving
        String id = dmc.getDungeon().getPlayer().getinventoryResponse().get(0).getId();
        for (int i = 0; i < 4; i++) {
            assertThrows(IllegalArgumentException.class, () -> {dmc.tick(id);});
        }

        DungeonResponse dungeonRes1 = dmc.getDungeonResponseModel();
        List<ItemResponse> expected = new ArrayList<>();
        expected.add(new ItemResponse("Entity2", "bow"));
        expected.add(new ItemResponse("Entity2", "shield"));

        assertEquals(expected, dungeonRes1.getInventory());

        // check spider location and players location
        DungeonResponse dungeonRes2 = dmc.getDungeonResponseModel();
        EntityResponse checkSpider1 = TestUtils.getEntities(dungeonRes2, "spider").get(0);
        assertEquals(new Position(2, 2), checkSpider1.getPosition());

        EntityResponse checkPlayer = TestUtils.getPlayer(dungeonRes2).get();
        assertEquals(new Position(2, 0), checkPlayer.getPosition());
        
        // after this player and spider will be on same cell
        dmc.tick(Direction.DOWN);

        //check spider is dead
        assertEquals(dmc.getDungeon().getEnemiesKilled(), 1);

        List<RoundResponse> rounds = new ArrayList<>();

        List<ItemResponse> w = new ArrayList<>();
        w.add(new ItemResponse("Entity2", "sword"));

        for (int i = 0; i < 10; i++) {
            rounds.add(new RoundResponse(-0.0, -1.2, w));
        }

        BattleResponse expected2 = new BattleResponse("spider", rounds, 10, 12);

        assertEquals(dungeonRes2.getBattles().get(0).getEnemy(), expected2.getEnemy());
        assertEquals(dungeonRes2.getBattles().get(0).getInitialEnemyHealth(), expected2.getInitialEnemyHealth());
        assertEquals(dungeonRes2.getBattles().get(0).getInitialPlayerHealth(), expected2.getInitialPlayerHealth());
        assertEquals(dungeonRes2.getBattles().get(0).getRounds().size(), expected2.getRounds().size());
        for (int j = 0; j < 10; j++) {
            assertEquals(rounds.get(j).getDeltaCharacterHealth(), expected2.getRounds().get(j).getDeltaCharacterHealth());
            assertEquals(rounds.get(j).getDeltaEnemyHealth(), expected2.getRounds().get(j).getDeltaEnemyHealth());
            assertEquals(rounds.get(j).getWeaponryUsed(), expected2.getRounds().get(j).getWeaponryUsed());
        }

        //test weapons are destroyed
        List<ItemResponse> expected3 = new ArrayList<>();
        DungeonResponse dungeonRes5 = dmc.getDungeonResponseModel();
        assertEquals(expected3, dungeonRes5.getInventory());
    }

    @Test
    @DisplayName("Test player dies")
    public void TestPlayerDead() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_invisiblePlayerNoPotion", "c_playerDead");

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        // check if sword picked up
        DungeonResponse dungeonRes1 = dmc.getDungeonResponseModel();
        List<ItemResponse> expected = new ArrayList<>();
        expected.add(new ItemResponse("Entity2", "sword"));

        assertEquals(expected, dungeonRes1.getInventory());

        // try to use sword 4 times, whilst spider is moving
        String id = dmc.getDungeon().getPlayer().getinventoryResponse().get(0).getId();
        for (int i = 0; i < 4; i++) {
            assertThrows(IllegalArgumentException.class, () -> {dmc.tick(id);});
        }

        // check spider location and players location
        DungeonResponse dungeonRes2 = dmc.getDungeonResponseModel();
        EntityResponse checkSpider1 = TestUtils.getEntities(dungeonRes2, "spider").get(0);
        assertEquals(new Position(2, 2), checkSpider1.getPosition());

        EntityResponse checkPlayer = TestUtils.getPlayer(dungeonRes2).get();
        assertEquals(new Position(2, 0), checkPlayer.getPosition());

        // after this player and spider will be on same cell
        dmc.tick(Direction.DOWN);

        //check spider is not dead
        assertEquals(dmc.getDungeon().getEnemiesKilled(), 0);

        List<ItemResponse> w = new ArrayList<>();
        w.add(new ItemResponse("Entity2", "sword"));

        //test player is dead
        assertEquals(dmc.getDungeon().getPlayer(), null);
    }

    @Test
    @DisplayName("Test that zombies can battle the player")
    public void TestBattleZombie() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_Zombie", "c_bowbattle");

        dmc.tick(Direction.RIGHT);

        //check zombie is dead
        assertEquals(dmc.getDungeon().getEnemiesKilled(), 1);
        assertEquals(dmc.getDungeonResponseModel().getBattles().size(), 1);
        assertEquals(dmc.getDungeonResponseModel().getGoals(), "");
    }

    @Test
    @DisplayName("Test that mercenaries can battle the player")
    public void TestBattleMercenaries() {
        
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_Mercenaries", "c_bowbattle");

        dmc.tick(Direction.RIGHT);

        //check mercenary is dead
        assertEquals(dmc.getDungeon().getEnemiesKilled(), 1);
        assertEquals(dmc.getDungeonResponseModel().getBattles().size(), 1);
        assertEquals(dmc.getDungeonResponseModel().getGoals(), "");
    }

}
