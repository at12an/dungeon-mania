package dungeonmania;

import dungeonmania.Entities.ZombieToastSpawner;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GoalsTests {

    //Frontend Tests

    @Test
    @DisplayName("Test basic goal rendering") 
    public void TestBasicRender() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_goalTestSimple", "c_goalTestSimple");

        DungeonResponse expected = dmc.getDungeonResponseModel();
        //test dungeonresponse

        assertTrue(expected.getGoals().contains(":exit") && expected.getGoals().contains("OR") && expected.getGoals().contains(":treasure"));
    }

    @Test
    @DisplayName("Test more complex goals rendering") 
    public void TestIntermediateRender() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_goalConditionsTest", "c_complexGoalsTest_andAll");

        DungeonResponse expected = dmc.getDungeonResponseModel();
        //test dungeonresponse

        assertTrue(expected.getGoals().contains(":exit") && 
                   expected.getGoals().contains("AND") && 
                   expected.getGoals().contains(":treasure") &&
                   expected.getGoals().contains(":boulder") &&
                   !expected.getGoals().contains("OR") &&
                   expected.getGoals().contains(":enemies"));
    }

    //Functionality Tests

    @Test
    @DisplayName("Test exiting the game results in a win if only goal")
    public void testExit() {
        //player goes to exit
        
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_collectableTest_basic", "c_goalTestSimple");
        DungeonResponse expected = dmc.getDungeonResponseModel();

        assertTrue(expected.getGoals().contains("exit"));
        dmc.tick(Direction.UP);

        DungeonResponse expected2 = dmc.getDungeonResponseModel();
        assertTrue(expected2.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test collecting certain amount of treasure ends game when only goal")
    public void TestTreasureGoal() {
        //player picks up all treasure
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_collectableGoalTest_basic", "c_collectableGoalTest_basic");
        DungeonResponse expected = dmc.getDungeonResponseModel();

        assertTrue(expected.getGoals().contains("treasure"));

        Direction[] cmds = new Direction[] {
            Direction.RIGHT,
            Direction.RIGHT,
        };

        for (Direction d : cmds) {
            dmc.tick(d);
        }

        expected = dmc.getDungeonResponseModel();
        assertTrue(expected.getGoals().contains("treasure"));

        for (Direction d : cmds) {
            dmc.tick(d);
        }

        expected = dmc.getDungeonResponseModel();
        assertTrue(expected.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test activating all switches ends game when only goal") 
    public void TestSwitchGoal() {
        //player switches on 1/2 boulders, 
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_boulderGoal_basic", "c_goalTestSimple");
        DungeonResponse expected = dmc.getDungeonResponseModel();

        assertTrue(expected.getGoals().contains("boulder"));

        Direction[] cmds = new Direction[] {
            Direction.LEFT,
            Direction.RIGHT,
            Direction.DOWN,
            Direction.DOWN,
        };

        //switches 1 off then 
        for (Direction d : cmds) {
            dmc.tick(d);
        }

        expected = dmc.getDungeonResponseModel();
        assertTrue(expected.getGoals().equals(""));

        //both on
    }

    @Test
    @DisplayName("Test treasure and exit goal") 
    public void TestANDTreasureExit() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_complexGoal_TreasureExit", "c_complexGoal_TreasureExit");
        DungeonResponse expected = dmc.getDungeonResponseModel();
        
        //player goes to exit -> false
        assertTrue(expected.getGoals().contains("treasure") && 
                   expected.getGoals().contains("AND") &&
                   expected.getGoals().contains("exit"));

        Direction[] cmds = new Direction[] {
            Direction.LEFT,
        };

        for (Direction d : cmds) {
            dmc.tick(d);
        }

        //player collects all treasure -> false
        expected = dmc.getDungeonResponseModel();
        assertTrue(expected.getGoals().contains("treasure") && 
                   expected.getGoals().contains("AND") &&
                   !expected.getGoals().contains("exit"));


        Direction[] cmds2 = new Direction[] {
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };

        for (Direction d : cmds2) {
            dmc.tick(d);
        }

        expected = dmc.getDungeonResponseModel();
        assertTrue(!expected.getGoals().contains("treasure") && 
                   expected.getGoals().contains("AND") &&
                   expected.getGoals().contains("exit"));

        Direction[] cmds3 = new Direction[] {
            Direction.LEFT,
            Direction.LEFT,
            Direction.LEFT,
            Direction.LEFT
        };

        for (Direction d : cmds3) {
            dmc.tick(d);
        }

        //player exits -> true
        expected = dmc.getDungeonResponseModel();
        assertTrue(expected.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test switches or exit goal") 
    public void TestORSwitchExit() {
        //player exits
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_complexGoal_BoulderExit", "c_goalTestSimple");
        DungeonResponse expected = dmc.getDungeonResponseModel();

        assertTrue(expected.getGoals().contains("boulder") && 
                   expected.getGoals().contains("OR") &&
                   expected.getGoals().contains("exit"));

        dmc.tick(Direction.LEFT);
        expected = dmc.getDungeonResponseModel();
        assertTrue(expected.getGoals().equals(""));
    }

    @Test
    @DisplayName("Test treasure and switches") 
    public void TestANDTreasureSwitches() {

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_complexGoal_TreasureBoulder", "c_complexGoal_TreasureBoulder");
        DungeonResponse expected = dmc.getDungeonResponseModel();
        assertTrue(expected.getGoals().contains("treasure") && 
                   expected.getGoals().contains("AND") &&
                   expected.getGoals().contains("boulder"));
        
        //player switches on all boulders
        Direction[] cmds = new Direction[] {
            Direction.LEFT,
            Direction.RIGHT,
            Direction.DOWN,
            Direction.DOWN,
            Direction.UP,
            Direction.UP,
        };

        for (Direction d : cmds) {
            dmc.tick(d);
        }

        expected = dmc.getDungeonResponseModel();

        assertTrue(expected.getGoals().contains("treasure") && 
                   expected.getGoals().contains("AND") &&
                   !expected.getGoals().contains("boulders"));

        //player collects all treasure
        Direction[] cmds2 = new Direction[] {
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
        };

        for (Direction d : cmds2) {
            dmc.tick(d);
        }

        expected = dmc.getDungeonResponseModel();
        assertTrue(expected.getGoals().equals(""));
    }

    @Test
    @DisplayName("Testing enemy goals with zombie spawners")
    public void TestZombieSpawnerGoals() {
          
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_battleTest_ZombieSpawner", "c_goalTestSimple");
        DungeonResponse expected = dmc.getDungeonResponseModel();

        assertTrue(expected.getGoals().contains("enemies"));

        dmc.tick(Direction.RIGHT);

        String id = dmc.getDungeon().getEntities().stream().filter(f -> f instanceof ZombieToastSpawner).findFirst().get().getId();
        assertDoesNotThrow(() -> dmc.interact(id));

        //check zombie spawner trigger winning game
        expected = dmc.getDungeonResponseModel();
        assertTrue(expected.getGoals().equals(""));
    }

    @Test
    @DisplayName("Testing OR all goals")
    public void TestORAllGoals() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_complexGoal_OR", "c_goalTestsimple2");
        DungeonResponse expected = dmc.getDungeonResponseModel();

        assertTrue(expected.getGoals().contains("treasure") && 
                   expected.getGoals().contains("OR") &&
                   expected.getGoals().contains("boulder") &&
                   expected.getGoals().contains("enemies") &&
                   expected.getGoals().contains("exit"));
                   

        dmc.tick(Direction.RIGHT);
        expected = dmc.getDungeonResponseModel();
        assertTrue(expected.getGoals().equals(""));

    }

    @Test
    @DisplayName("Testing mix of all goals")
    public void TestMixedAllGoals() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_complexGoalORAND", "c_goalTestSimple");
        DungeonResponse expected = dmc.getDungeonResponseModel();

        assertTrue(expected.getGoals().contains("treasure") && 
                   expected.getGoals().contains("AND") &&
                   expected.getGoals().contains("OR") &&
                   expected.getGoals().contains("boulder") &&
                   expected.getGoals().contains("exit"));
                   

        dmc.tick(Direction.RIGHT);
        expected = dmc.getDungeonResponseModel();
        assertTrue(expected.getGoals().equals(""));
    }

}
