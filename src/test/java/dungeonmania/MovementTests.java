package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static dungeonmania.TestUtils.getPlayer;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovementTests {
    
    @Test
    @DisplayName("Test Player can Move All directions")
    public void testUniversalMovement(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.UP,
            Direction.LEFT,
            Direction.LEFT,
            Direction.DOWN,
            Direction.DOWN,
            Direction.RIGHT,
            Direction.RIGHT
        };
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_move_all_directions", "c_movementTest_move_all_directions");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        assertEquals(new Position(2, 2), initPlayer.getPosition());
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertEquals(new Position(3, 3), finalPlayerState.getPosition());
        
    }

    @Test
    @DisplayName("Test Wall Bumping")
    public void testWallBump(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.UP,
            Direction.LEFT,
            Direction.DOWN,
        };
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_test_wall_bumping.", "c_movementTest_test_wall_bumping");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        assertEquals(new Position(1, 1), initPlayer.getPosition());
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertEquals(new Position(1, 1), finalPlayerState.getPosition());
    }

    @Test
    @DisplayName("Test Longer Movement in straight Line")
    public void testLongMovement(){
        Direction[] cmds = new Direction[]{
            Direction.DOWN,
            Direction.DOWN,
            Direction.DOWN,
            Direction.DOWN,
            Direction.DOWN,
        };
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_test_long_path_movement", "c_movementTest_test_long_path_movement");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        assertEquals(new Position(0, 0), initPlayer.getPosition());
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertEquals(new Position(0, 4), finalPlayerState.getPosition());
    }

    @Test
    @DisplayName("Test Out of Bounds Prevention")
    public void TestOutOfBoundPrevention(){
        Direction[] cmds = new Direction[]{
            Direction.RIGHT,
            Direction.UP,
            Direction.LEFT,
            Direction.DOWN,
        };
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_out_of_bounds_prevention", "c_movementTest_out_of_bounds_prevention");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        assertEquals(new Position(0, 0), initPlayer.getPosition());
        for (Direction d : cmds){
            dmc.tick(d);
        }
        DungeonResponse finalDungeonRes = dmc.getDungeonResponseModel();
        EntityResponse finalPlayerState = getPlayer(finalDungeonRes).get();
        assertEquals(new Position(0, 0), finalPlayerState.getPosition(), String.format("%s", finalPlayerState.getPosition().toString()));
    }
}
