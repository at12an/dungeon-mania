package dungeonmania;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntitiesStream;


public class BoulderTests {
    
    @Test
    @DisplayName("Test Basic Boulder Movement")
    public void testCanPushBoulderEveryDirection(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.LEFT,
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT,
            Direction.DOWN,
            Direction.UP,
        };
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_boulderTest_CanPushBoulderEveryDirection", "c_boulderTest_simpleSetupForAll");


        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();

        Position[] finalBoulderPositions = {
            new Position(1, 3),
            new Position(5, 3),
            new Position(3, 1),
            new Position(3, 5)
        };
        for (Position p : finalBoulderPositions){
            assertTrue(getEntitiesStream(finalDungeonRes, "boulder")
                .anyMatch(b -> b.getPosition().equals(p)));
        }

        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertEquals(new Position(3, 3), finalPlayerState.getPosition());
    }

    @Test
    @DisplayName("Test Trying to Push a Boulder with a Boulder Directly in Front of its Path")
    public void testBoulderCollision(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.RIGHT
        };
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_boulderTest_BoulderCollision", "c_boulderTest_simpleSetupForAll");


        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();

        Position[] finalBoulderPositions = {
            new Position(3, 3),
            new Position(4, 3),
        };
        for (Position p : finalBoulderPositions){
            assertTrue(getEntitiesStream(finalDungeonRes, "boulder")
                .anyMatch(b -> b.getPosition().equals(p)));
        }

        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertEquals(new Position(2, 3), finalPlayerState.getPosition());
    }

    @Test
    @DisplayName("Test Trying To Push A Boulder With a Wall Blocking It")
    public void testPushingBoulderIntoWall(){

        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_boulderTest_CollisionWithWall", "c_boulderTest_simpleSetupForAll");


        dmc.tick(Direction.RIGHT);
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();

        Position finalBoulderPosition = new Position(5, 3);        
        assertTrue(getEntitiesStream(finalDungeonRes, "boulder").anyMatch(b -> b.getPosition().equals(finalBoulderPosition)));


        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertEquals(new Position(4, 3), finalPlayerState.getPosition());
    }
}
