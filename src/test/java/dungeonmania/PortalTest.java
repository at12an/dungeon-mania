package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.getPlayer;


public class PortalTest {
    
    @Test
    @DisplayName("Test Basic Portal Functionality")
    public void testBasicPortal(){
       
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_testPortals_veryBasic", "c_portalTests_allTestsConfig");

        Direction d = Direction.RIGHT;
        dmc.tick(d);

        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();

        Position finalPos = new Position(2,0);
        assertTrue(finalPlayerState.getPosition().equals(finalPos));
    }

    @Test
    @DisplayName("Test can go back through same Portal")
    public void testTeleDoubleUse(){
       
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_testPortals_veryBasic", "c_portalTests_allTestsConfig");

        Direction[] cmds = {
            Direction.RIGHT,
            Direction.DOWN
        };
        for (Direction cmd : cmds){
            dmc.tick(cmd);
        }

        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();

        Position finalPos = new Position(1,0);
        assertTrue(finalPlayerState.getPosition().equals(finalPos));
    }

    @Test
    @DisplayName("Tests 2 walls block tele exit")
    public void testTwoWallsBlockingTele(){
       
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_testPortals_3badPortals", "c_portalTests_allTestsConfig");

        Direction d = Direction.LEFT;
        dmc.tick(d);

        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();

        Position finalPos = new Position(0,0);
        assertTrue(finalPlayerState.getPosition().equals(finalPos));
    }

    @Test
    @DisplayName("Tests 3 walls block tele exit")
    public void testThreeWallsBlockingTele(){
       
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_testPortals_3badPortals", "c_portalTests_allTestsConfig");

        Direction d = Direction.DOWN;
        dmc.tick(d);

        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();

        Position finalPos = new Position(3,4);
        assertTrue(finalPlayerState.getPosition().equals(finalPos));
    }

    @Test
    @DisplayName("Tests tele exist completely blocked")
    public void testFourWallsBlockingTele(){
       
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_testPortals_3badPortals", "c_portalTests_allTestsConfig");

        DungeonResponse initDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse initPlayerState = getPlayer(initDungeonRes).get();

        Direction d = Direction.RIGHT;
        dmc.tick(d);

        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();

        assertTrue(finalPlayerState.getPosition().equals(initPlayerState.getPosition()));
    }


}
