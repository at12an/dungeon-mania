package dungeonmania;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dungeonmania.TestUtils.getPlayer;

public class DoorTests {
    
    @Test
    @DisplayName("Test Basic Door Opening")
    public void testOpenDoor(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_doorTest_openDoor", "c_boulderTest_simpleSetupForAll");
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertEquals(new Position(3, 0), finalPlayerState.getPosition());
    }

    @Test
    @DisplayName("Test Opening Door with No Key")
    public void testNoKey(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT
        };
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_doorTest_noKey", "c_boulderTest_simpleSetupForAll");
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertEquals(new Position(1, 0), finalPlayerState.getPosition());
    }

    @Test
    @DisplayName("Test Moving Through Open Door")
    public void testDoorAlreadyOpen(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_doorTest_openDoor", "c_boulderTest_simpleSetupForAll");
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertEquals(new Position(4, 0), finalPlayerState.getPosition());
        cmds = new Direction[]{
            Direction.LEFT,
            Direction.LEFT
        };
        for (Direction d : cmds){
            dmc.tick(d);
        }
        finalDungeonRes = dmc.getDungeonResponseModel();
        finalPlayerState = getPlayer(finalDungeonRes).get();
        assertEquals(new Position(2, 0), finalPlayerState.getPosition());
    }

    @Test
    @DisplayName("Test door blocks boulder")
    public void testBoulderDoor(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT,
            Direction.RIGHT
        };
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_doorTest_boulder", "c_boulderTest_simpleSetupForAll");
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertEquals(new Position(1, 0), finalPlayerState.getPosition());
    }
}