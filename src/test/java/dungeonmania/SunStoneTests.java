package dungeonmania;

import dungeonmania.Entities.Player;
import dungeonmania.Entities.Weapons.MidnightArmour;
import dungeonmania.Entities.Weapons.Shield;
import dungeonmania.Entities.Weapons.Sword;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static dungeonmania.TestUtils.getPlayer;
import dungeonmania.util.Position;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.Entities.Collectables.Sceptre;
import dungeonmania.Entities.Collectables.SunStone;
import dungeonmania.Entities.Collectables.Wood;

public class SunStoneTests {
    @Test
    @DisplayName("Test Basic Door Opening w Sun Stone")
    public void testOpenDoor(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_sunStoneTest_openDoor", "c_boulderTest_simpleSetupForAll");
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        Player p = dmc.getDungeon().getPlayer();

        //Check player has opened and moved through door
        assertEquals(new Position(3, 0), finalPlayerState.getPosition());

        //Check player still has sun stone
        assertEquals(p.checkItemCount(SunStone.class), 1);

        cmds = new Direction[]{
            Direction.RIGHT,
            Direction.LEFT,
            Direction.LEFT
        };
        for (Direction d : cmds){
            dmc.tick(d);
        }
        finalDungeonRes = dmc.getDungeonResponseModel();
        finalPlayerState = getPlayer(finalDungeonRes).get();

        //Check player can move back through open door
        assertEquals(new Position(2, 0), finalPlayerState.getPosition());
    }

    @Test
    @DisplayName("Test Building Shield w Sun Stone")
    public void testBuildShield(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_sunStoneTest_buildShield", "c_boulderTest_simpleSetupForAll");
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        Player p = dmc.getDungeon().getPlayer();
        assertEquals(new Position(3, 0), finalPlayerState.getPosition());

        // Check player has picked up 2 wood and 1 sunstone
        assertEquals(p.checkItemCount(SunStone.class), 1);
        assertEquals(p.checkItemCount(Wood.class), 2);
        assertEquals(3, finalDungeonRes.getInventory().size());

        assertDoesNotThrow(() -> dmc.build("shield"));
        p = dmc.getDungeon().getPlayer();

        // Check sunstone is retained and bow is made
        assertEquals(p.checkItemCount(SunStone.class), 1);
        assertEquals(p.checkItemCount(Wood.class), 0);
        assertEquals(p.checkItemCount(Shield.class), 1);
        
    }

    @Test
    @DisplayName("Test Building Sceptre w Sun Stone")
    public void testBuildSceptre(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_sunStoneTest_buildSceptre", "c_boulderTest_simpleSetupForAll");
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        Player p = dmc.getDungeon().getPlayer();
        assertEquals(new Position(3, 0), finalPlayerState.getPosition());

        // Check player has picked up 1 wood and 1 sunstone
        assertEquals(p.checkItemCount(SunStone.class), 2);
        assertEquals(p.checkItemCount(Wood.class), 1);
        assertEquals(3, finalDungeonRes.getInventory().size());

        assertDoesNotThrow(() -> dmc.build("sceptre"));
        p = dmc.getDungeon().getPlayer();

        // Check sunstone is retained and bow is made
        assertEquals(p.checkItemCount(SunStone.class), 1);
        assertEquals(p.checkItemCount(Wood.class), 0);
        assertEquals(p.checkItemCount(Sceptre.class), 1);
        
    }
    
    @Test
    @DisplayName("Test Building Midnight Armour w Sun Stone")
    public void testBuildArmour(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_sunStoneTest_buildArmour", "c_boulderTest_simpleSetupForAll");
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        Player p = dmc.getDungeon().getPlayer();

        // Check player has picked up 1 sword and 1 sunstone
        assertEquals(p.checkItemCount(SunStone.class), 1);
        assertEquals(p.checkItemCount(Sword.class), 1);
        assertEquals(2, finalDungeonRes.getInventory().size());

        assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        p = dmc.getDungeon().getPlayer();

        // Check sunstone is retained and bow is made
        assertEquals(p.checkItemCount(SunStone.class), 0);
        assertEquals(p.checkItemCount(Sword.class), 0);
        assertEquals(p.checkItemCount(MidnightArmour.class), 1);
        
    }

    @Test
    @DisplayName("Test Building Sceptre w Sun Stone2")
    public void testBuildSceptre2(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.UP,
            Direction.UP,
            Direction.UP,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_sunStoneTest_useSunStone", "c_boulderTest_simpleSetupForAll");
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertDoesNotThrow(() -> dmc.build("sceptre"));
        
    }

    @Test
    @DisplayName("Test Building Sceptre w Sun Stone3")
    public void testBuildSceptre3(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.UP,
            Direction.UP,
            Direction.UP,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_sunStoneTest_useSunstone2", "c_boulderTest_simpleSetupForAll");
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertDoesNotThrow(() -> dmc.build("sceptre"));
        
    }
}
