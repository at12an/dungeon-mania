package dungeonmania;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Entities.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import dungeonmania.Entities.Weapons.*;
import dungeonmania.Entities.Collectables.*;

import java.util.List;


public class TimeTravel {
    
    
    @Test
    @DisplayName("Exceptions (5) for time travelling")
    public void TimeTravelExceptions() {
        
        //move player and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_test_timeTravel", "c_invisibleBattleSpider");

        Direction[] cmd = new Direction[] {
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
        };

        //6 right
        for (Direction d: cmd) {
            dmc.tick(d);
        }

        //test player has time turner
        assertEquals(1, dmc.getDungeon().getPlayer().checkItemCount(TimeTurner.class));

        //go back in time
        assertDoesNotThrow(() -> dmc.rewind(5));

        //test player does not have time turner
        assertEquals(0, dmc.getDungeon().getPlayer().checkItemCount(TimeTurner.class));

        //1 left and 1 right
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.RIGHT);

        //test player has time turner
        assertEquals(1, dmc.getDungeon().getPlayer().checkItemCount(TimeTurner.class));

        //collect and go back in time again
        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(5));

        //test player has time turner
        assertEquals(1, dmc.getDungeon().getPlayer().checkItemCount(TimeTurner.class));
    }
    
    @Test
    @DisplayName("Exceptions (1) for time travelling")
    public void TimeTravelException1() {
        //move player and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_test_timeTravel2", "c_invisibleBattleSpider");

        //give player time turner
        //test player has time turner
        dmc.getDungeon().getPlayer().addItem(new TimeTurner("time_tuner", null));

        //test player has time turner
        assertEquals(1, dmc.getDungeon().getPlayer().checkItemCount(TimeTurner.class));

        //collect and go back in time again
        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(1));
    }

    @Test
    @DisplayName("Exceptions (30) for time travelling")
    public void TimeTravelExceptions30() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_test_Time_Portal", "c_invisibleBattleSpider");

        //collect and go back in time again
        assertDoesNotThrow(() -> dmc.rewind(30));
        List<Entity> players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(0, 0));

        assertDoesNotThrow(() -> dmc.tick(Direction.DOWN));
        assertDoesNotThrow(() -> dmc.tick(Direction.DOWN));
        assertDoesNotThrow(() -> dmc.tick(Direction.DOWN));
        assertDoesNotThrow(() -> dmc.tick(Direction.DOWN));
        assertDoesNotThrow(() -> dmc.tick(Direction.DOWN));
        assertDoesNotThrow(() -> dmc.tick(Direction.DOWN));
        assertDoesNotThrow(() -> dmc.tick(Direction.DOWN));

        assertDoesNotThrow(() -> dmc.tick(Direction.DOWN));
        players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(0, 0));

        Position p = new Position(0, 7);
        assertEquals(dmc.getDungeon().getPlayer().getPosition(), p);
        assertEquals(dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList()).size(), 2);
    }

    @Test
    @DisplayName("Linear time travel")
    public void LinearTimeTravel() {
        //move player and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_test_timeTravel", "c_invisibleBattleSpider");

        Direction[] cmd = new Direction[] {
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
        };

        //6 right
        for (Direction d: cmd) {
            dmc.tick(d);
        }

        //test player has time turner
        assertEquals(1, dmc.getDungeon().getPlayer().checkItemCount(TimeTurner.class));

        //go back in time
        assertDoesNotThrow(() -> dmc.rewind(5));

        //test 2 players and correct position
        List<Entity> players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 2);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(1, 0));
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer == false).findFirst().get().getPosition(), new Position(6, 0));

        //test correct position
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(2, 0));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(3, 0));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(4, 0));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(5, 0));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(6, 0));

        //player removed from map
        dmc.tick(Direction.RIGHT);
        players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 1);
    }

    @Test
    @DisplayName("More advanced time travel")
    public void IntermediateTimeTravle() {
        //move player and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d.test_timeTravelIntermediate", "c_invisibleBattleSpider");

        //do a bunch of stuff
        dmc.tick(Direction.RIGHT);
        String s = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof ZombieToastSpawner).findFirst().get().getId();
        assertDoesNotThrow(() -> dmc.interact(s));
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        assertDoesNotThrow(() -> dmc.build("shield"));
        dmc.tick(Direction.UP);
        dmc.tick(Direction.LEFT);
        String s2 = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof ZombieToastSpawner).findFirst().get().getId();
        dmc.tick(Direction.LEFT);
        assertDoesNotThrow(() -> dmc.interact(s2));
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        String s3 = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof InvisibilityPotion).findFirst().get().getId();
        dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.tick(s3));
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.UP);
        dmc.tick(Direction.RIGHT);

        //test player has time turner
        assertEquals(1, dmc.getDungeon().getPlayer().checkItemCount(TimeTurner.class));

        //rewind game now
        assertDoesNotThrow(() -> dmc.rewind(5));

        //check player size
        //check player is in the correct position
        List<Entity> players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 2);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(2, 5));
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer == false).findFirst().get().getPosition(), new Position(6, 4));

        //check player has sword and shield
        Player OP = (Player) players.stream().filter(e -> e instanceof OldPlayer).findFirst().get();
        assertEquals(OP.checkItemCount(Sword.class), 1);
        assertEquals(OP.checkItemCount(Shield.class), 1);

        //check player has killed zombie toast spawners
        assertEquals(dmc.getDungeon().getEntities().stream().filter(e -> e instanceof ZombieToastSpawner).collect(Collectors.toList()).size(), 0);

        //check player is using a potion
        assertTrue(OP.isPlayerInvisible());

        //check following moves are carried out
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(3, 5));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(4, 5));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(5, 5));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(5, 4));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(6, 4));

        //player removed from map
        dmc.tick(Direction.RIGHT);
        players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 1);
    }

    @Test
    @DisplayName("Complex time travel")
    public void ComplexTimeTravel() {
        //move player and collect time turner
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_test_advancedTimePortal", "c_invisibleBattleSpider");

        //do a bunch of stuff
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        String s = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof InvincibilityPotion).findFirst().get().getId();
        dmc.tick(Direction.UP);
        assertDoesNotThrow(() -> dmc.tick(s));
        String s2 = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof ZombieToastSpawner).findFirst().get().getId();
        dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.interact(s2));
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        String s3 = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof InvisibilityPotion).findFirst().get().getId();
        dmc.tick(Direction.LEFT);
        assertDoesNotThrow(() -> dmc.tick(s3));
        String s4 = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof ZombieToastSpawner).findFirst().get().getId();
        dmc.tick(Direction.DOWN);
        assertDoesNotThrow(() -> dmc.interact(s4));
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        assertDoesNotThrow(() -> dmc.build("shield"));
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.UP);
        assertDoesNotThrow(() -> dmc.build("bow"));
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);
        dmc.tick(Direction.DOWN);

        //test time travel worked
        List<Entity> players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 2);
        //test correct positions

        assertEquals(players.stream().filter(e -> e instanceof OldPlayer == false).findFirst().get().getPosition(), new Position(10, 10));
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(1, 0));
        //a bunch of assert testing
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(1, 1));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(2, 1));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(2, 0));
        //test collects and uses potion
        Player p = (Player) players.stream().filter(e -> e instanceof OldPlayer).findFirst().get();
        assertEquals(p.checkItemCount(InvincibilityPotion.class), 1);
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(2, 0));
        Player Pl = (Player) players.stream().filter(e -> e instanceof OldPlayer).findFirst().get();
        assertEquals(Pl.isPlayerInvincible(), true);
        assertEquals(p.checkItemCount(InvincibilityPotion.class), 0);
        dmc.tick(Direction.RIGHT);
        //test interaction
        assertEquals(dmc.getDungeon().getEntities().stream().filter(e -> e instanceof ZombieToastSpawner).collect(Collectors.toList()).size(), 1);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(3, 0));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(3, 1));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(4, 1));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(5, 1));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(5, 2));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(4, 2));
        //test potion again
        dmc.tick(Direction.RIGHT);
        assertEquals(p.checkItemCount(InvisibilityPotion.class), 0);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(4, 2));
        dmc.tick(Direction.RIGHT);
        //another interaction
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(4, 3));
        assertEquals(dmc.getDungeon().getEntities().stream().filter(e -> e instanceof ZombieToastSpawner).collect(Collectors.toList()).size(), 0);
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(4, 4));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(5, 4));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(6, 4));
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(6, 6));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(5, 6));
        //test build
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(5, 5));
        assertEquals(dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Treasure).collect(Collectors.toList()).size(), 0);
        assertEquals(p.checkItemCount(Shield.class), 1);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(3, 7));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(3, 6));
        assertEquals(p.checkItemCount(Bow.class), 1);
        assertEquals(p.getInventory().size(), 3);
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(2, 6));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(2, 7));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(2, 8));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(2, 9));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(2, 10));
        dmc.tick(Direction.RIGHT);
        assertEquals(players.stream().filter(e -> e instanceof OldPlayer).findFirst().get().getPosition(), new Position(2, 11));

        //player is removed from the map
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 1);
    }

    @Test
    @DisplayName("Fight player")
    public void TimeTravelFightPlayer() {
        //fight player and test player kills oldplayer
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_OldPlayer", "c_invisibleBattleSpider");

        //move player to time turner
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        //time travel
        assertDoesNotThrow(() -> dmc.rewind(5));

        //test player does not have time turner
        assertEquals(0, dmc.getDungeon().getPlayer().checkItemCount(TimeTurner.class));

        //test 2 players and correct position
        List<Entity> players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 2);

        //fight player test there was a battle and check the other player is dead
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);

        players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 1);

        assertEquals(dmc.getDungeon().getDungeonResponseModel().getBattles().size(), 1);
        assertNotEquals(dmc.getDungeon().getPlayer().getHealth(), dmc.getDungeon().getConfig().getPlayerHealth());
    }

    @Test
    @DisplayName("Player lose")
    public void TimeTravelPlayerLose() {
        //fight player and test player kills oldplayer
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_OldPlayer", "c_invisibleBattleSpider");

        //move player to time turner
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        //time travel
        assertDoesNotThrow(() -> dmc.rewind(5));

        //test player does not have time turner
        assertEquals(0, dmc.getDungeon().getPlayer().checkItemCount(TimeTurner.class));

        //test 2 players and correct position
        List<Entity> players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 2);

        //fight player test there was no battle and check the other player is not dead
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);

        players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 1);

        assertEquals(dmc.getDungeon().getDungeonResponseModel().getBattles().size(), 1);
        dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.rewind(5));

        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);

        players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(dmc.getDungeon().getDungeonResponseModel().getBattles().size(), 2);
        assertEquals(players.size(), 0);
        assertTrue(dmc.getDungeon().getPlayer() == null);
    }

    @Test
    @DisplayName("Test no battle for invisibility")
    public void TimeTravelInvisibility() {
        //fight player and test player kills oldplayer
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_OldPlayer", "c_invisibleBattleSpider");

        //move player to time turner
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        //time travel
        assertDoesNotThrow(() -> dmc.rewind(5));

        //test player does not have time turner
        assertEquals(0, dmc.getDungeon().getPlayer().checkItemCount(TimeTurner.class));

        //test 2 players and correct position
        List<Entity> players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 2);

        //use potion
        String s2 = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof InvisibilityPotion).findFirst().get().getId();
        assertDoesNotThrow(() -> dmc.tick(s2));

        //fight player test there was no battle and check the other player is not dead
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);

        players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 2);

        assertEquals(dmc.getDungeon().getDungeonResponseModel().getBattles().size(), 0);
        assertEquals(dmc.getDungeon().getPlayer().getHealth(), dmc.getDungeon().getConfig().getPlayerHealth());
    }

    @Test
    @DisplayName("Test bribe merc works for time travel")
    public void TimeTravelBribeMerc() {
        //fight player and test player kills oldplayer
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_test_timeTravel_merc", "c_invisibleBattleSpider");

        //move and then interact with merc
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        //get id
        String id = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Mercenary).findFirst().get().getId();

        assertDoesNotThrow(() -> dmc.interact(id));
        //interact with merc
        assertDoesNotThrow(() -> dmc.rewind(5));

        Mercenary m = (Mercenary) dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Mercenary).findFirst().get();

        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        //test if interaction with mercenary
        assertEquals(m.isAllied(), true);;
        dmc.tick(Direction.RIGHT);

        assertDoesNotThrow(() -> dmc.rewind(1));
        assertEquals(m.isAllied(), true);;
    }

    @Test
    @DisplayName("Test no battle for midnight armour")
    public void TimeTravelMidnightArmour() {
            //fight player and test player kills oldplayer
            DungeonManiaController dmc = new DungeonManiaController();
            dmc.newGame("d_battleTest_OldPlayer", "c_invisibleBattleSpider");
        
            //move player to time turner
            dmc.tick(Direction.RIGHT);
            dmc.tick(Direction.RIGHT);
            dmc.tick(Direction.RIGHT);
            dmc.tick(Direction.RIGHT);
            dmc.tick(Direction.RIGHT);
            dmc.tick(Direction.RIGHT);
        
            //time travel
            assertDoesNotThrow(() -> dmc.rewind(5));
        
            //test player does not have time turner
            assertEquals(0, dmc.getDungeon().getPlayer().checkItemCount(TimeTurner.class));
        
            //test 2 players and correct position
            List<Entity> players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
            assertEquals(players.size(), 2);
        
            //use potion
            String s2 = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof InvisibilityPotion).findFirst().get().getId();
            assertDoesNotThrow(() -> dmc.tick(s2));
        
            //give player midnight armour
            dmc.getDungeon().getPlayer().addItem(new MidnightArmour("midnight_armour", null, 10, 10));
        
            //fight player test there was no battle and check the other player is not dead
            dmc.tick(Direction.LEFT);
            dmc.tick(Direction.LEFT);
            dmc.tick(Direction.LEFT);
        
            players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
            assertEquals(players.size(), 2);
        
            assertEquals(dmc.getDungeon().getDungeonResponseModel().getBattles().size(), 0);
            assertEquals(dmc.getDungeon().getPlayer().getHealth(), dmc.getDungeon().getConfig().getPlayerHealth());
    }

    @Test
    @DisplayName("Test no battle for sun stone")
    public void TimeTravelSunStone() {
        //fight player and test player kills oldplayer
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_OldPlayer", "c_invisibleBattleSpider");

        //move player to time turner
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);

        //time travel
        assertDoesNotThrow(() -> dmc.rewind(5));

        //test player does not have time turner
        assertEquals(0, dmc.getDungeon().getPlayer().checkItemCount(TimeTurner.class));

        //test 2 players and correct position
        List<Entity> players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 2);

        //use potion
        String s2 = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof InvisibilityPotion).findFirst().get().getId();
        assertDoesNotThrow(() -> dmc.tick(s2));

        //give player sceptre
        dmc.getDungeon().getPlayer().addItem(new SunStone("sun_stone", null));

        //fight player test there was no battle and check the other player is not dead
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);

        players = dmc.getDungeon().getEntities().stream().filter(e -> e instanceof Player).collect(Collectors.toList());
        assertEquals(players.size(), 2);

        assertEquals(dmc.getDungeon().getDungeonResponseModel().getBattles().size(), 0);
        assertEquals(dmc.getDungeon().getPlayer().getHealth(), dmc.getDungeon().getConfig().getPlayerHealth());
    }
}
