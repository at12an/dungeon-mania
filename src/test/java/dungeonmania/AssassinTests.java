/*
 * package dungeonmania;
 * 
 * import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
 * import static org.junit.jupiter.api.Assertions.assertEquals;
 * import static org.junit.jupiter.api.Assertions.assertNotNull;
 * import static org.junit.jupiter.api.Assertions.assertThrows;
 * import static org.junit.jupiter.api.Assertions.assertTrue;
 * 
 * import static dungeonmania.TestUtils.getEntities;
 * 
 * import java.util.ArrayList;
 * import java.util.List;
 * 
 * import org.junit.jupiter.api.DisplayName;
 * import org.junit.jupiter.api.Test;
 * 
 * import dungeonmania.Entities.Entity;
 * import dungeonmania.Entities.Assassin;
 * import dungeonmania.Entities.Collectables.InvincibilityPotion;
 * import dungeonmania.Entities.Collectables.Treasure;
 * import dungeonmania.exceptions.InvalidActionException;
 * import dungeonmania.response.models.*;
 * import dungeonmania.util.Direction;
 * import dungeonmania.util.Position;
 * 
 * public class AssassinTest {
 * 
 * @Test
 * 
 * @DisplayName("Test basic assassin movement with player")
 * public void testBasicAssassinMovementWithPlayer() {
 * DungeonManiaController dmc = new DungeonManiaController();
 * 
 * DungeonResponse res = dmc.newGame("d_mercenaryTest_basicMovement",
 * "c_mercenaryTest_basicMovement");
 * 
 * Position pos = getEntities(res, "mercenary").get(0).getPosition();
 * 
 * List<Position> movementTrajectory = new ArrayList<Position>();
 * 
 * Position temp = pos.translateBy(Direction.LEFT);
 * movementTrajectory.add(temp);
 * temp = temp.translateBy(Direction.LEFT);
 * movementTrajectory.add(temp);
 * temp = temp.translateBy(Direction.LEFT);
 * movementTrajectory.add(temp);
 * temp = temp.translateBy(Direction.UP);
 * movementTrajectory.add(temp);
 * temp = temp.translateBy(Direction.LEFT);
 * movementTrajectory.add(temp);
 * temp = temp.translateBy(Direction.UP);
 * movementTrajectory.add(temp);
 * temp = temp.translateBy(Direction.LEFT);
 * movementTrajectory.add(temp);
 * temp = temp.translateBy(Direction.UP);
 * movementTrajectory.add(temp);
 * temp = temp.translateBy(Direction.LEFT);
 * movementTrajectory.add(temp);
 * temp = temp.translateBy(Direction.UP);
 * movementTrajectory.add(temp);
 * 
 * for (int i = 0; i < 10; ++i) {
 * res = dmc.tick(Direction.LEFT);
 * assertEquals(movementTrajectory.get(i), getEntities(res,
 * "mercenary").get(0).getPosition());
 * }
 * }
 * 
 * @Test
 * 
 * @DisplayName("Test assassin movement based on wall interference")
 * public void testAssassinWallMovement() {
 * DungeonManiaController dmc = new DungeonManiaController();
 * 
 * DungeonResponse res = dmc.newGame("d_mercenaryTest_movementBlocked",
 * "c_mercenaryTest_basicMovement");
 * 
 * for (int i = 0; i < 6; i++) {
 * res = dmc.tick(Direction.RIGHT);
 * }
 * Position mercPosition = getEntities(res, "mercenary").get(0).getPosition();
 * 
 * assertTrue(mercPosition.equals(new Position(7, 5)), mercPosition.toString());
 * }
 * 
 * @Test
 * 
 * @DisplayName("Test basic bribing based on successful bribe")
 * public void testAssassinBribedSuccessfully() {
 * DungeonManiaController dmc = new DungeonManiaController();
 * 
 * DungeonResponse res = dmc.newGame("d_assassinTest_bribeSuccess",
 * "c_assassinBribeSuccess");
 * 
 * for (int i = 0; i < 3; i++) {
 * res = dmc.tick(Direction.RIGHT);
 * }
 * 
 * Entity a = dmc.getDungeon().getEntitiesAtPosition(new Position(4, 0)).get(0);
 * assertNotNull(a);
 * assertDoesNotThrow(() -> {dmc.interact(a.getId());});
 * 
 * int numItems =
 * dmc.getDungeon().getPlayer().getInventory().stream().filter(e ->
 * e.getClass().equals(Treasure.class)).toArray().length;
 * assertEquals(0, numItems); // no more money :(
 * 
 * assertTrue(((Assassin)a).isAllied());
 * assertEquals(1, dmc.getDungeon().getPlayer().numAllies());
 * }
 * 
 * @Test
 * 
 * @DisplayName("Test basic bribing based on unsuccessful bribe")
 * public void testAssassinBribedUnsuccessfully() {
 * DungeonManiaController dmc = new DungeonManiaController();
 * 
 * DungeonResponse res = dmc.newGame("d_assassinTest_bribeSuccess",
 * "c_assassinBribeUnsuccess");
 * 
 * for (int i = 0; i < 3; i++) {
 * res = dmc.tick(Direction.RIGHT);
 * }
 * 
 * Entity a = dmc.getDungeon().getEntitiesAtPosition(new Position(4, 0)).get(0);
 * assertNotNull(a);
 * assertDoesNotThrow(() -> {dmc.interact(a.getId());});
 * 
 * int numItems =
 * dmc.getDungeon().getPlayer().getInventory().stream().filter(e ->
 * e.getClass().equals(Treasure.class)).toArray().length;
 * assertEquals(0, numItems); // no more money :(
 * 
 * assertTrue(!((Assassin)a).isAllied());
 * assertEquals(0, dmc.getDungeon().getPlayer().numAllies());
 * }
 * 
 * @Test
 * 
 * @DisplayName("Test no bribe not enough money")
 * public void testNoBribeNoMoney() {
 * DungeonManiaController dmc = new DungeonManiaController();
 * DungeonResponse res = dmc.newGame("d_assassinTest_NoBribe",
 * "c_assassinBribeUnsuccess");
 * 
 * dmc.tick(Direction.RIGHT);
 * 
 * Entity a = dmc.getDungeon().getEntitiesAtPosition(new Position(2, 0)).get(0);
 * assertNotNull(a);
 * assertThrows(InvalidActionException.class, () -> {dmc.interact(a.getId());});
 * }
 * 
 * @Test
 * 
 * @DisplayName("Test enough money but out of bribing range")
 * public void testEnoughMoneyNotInRange() {
 * DungeonManiaController dmc = new DungeonManiaController();
 * DungeonResponse res = dmc.newGame("d_assassinTest_bribeSuccess",
 * "c_assassinOutOfBribeRadius");
 * 
 * for (int i = 0; i < 3; i++) {
 * res = dmc.tick(Direction.RIGHT);
 * }
 * 
 * Entity a = dmc.getDungeon().getEntitiesAtPosition(new Position(4, 0)).get(0);
 * assertNotNull(a);
 * assertThrows(InvalidActionException.class, () -> {dmc.interact(a.getId());});
 * }
 * 
 * @Test
 * 
 * @DisplayName("Test after bribe, movement of assassin")
 * public void testSuccessfulBribeMovement() {
 * DungeonManiaController dmc = new DungeonManiaController();
 * 
 * DungeonResponse res = dmc.newGame("d_assassinTest_bribeSuccess",
 * "c_assassinBribeSuccess");
 * 
 * for (int i = 0; i < 3; i++) {
 * res = dmc.tick(Direction.RIGHT);
 * }
 * Entity a = dmc.getDungeon().getEntitiesAtPosition(new Position(4, 0)).get(0);
 * assertNotNull(a);
 * assertDoesNotThrow(() -> {dmc.interact(a.getId());});
 * 
 * int numItems =
 * dmc.getDungeon().getPlayer().getInventory().stream().filter(e ->
 * e.getClass().equals(Treasure.class)).toArray().length;
 * assertEquals(0, numItems); // no more money :(
 * 
 * assertTrue(((Assassin)a).isAllied());
 * assertEquals(1, dmc.getDungeon().getPlayer().numAllies());
 * 
 * // assassin should now follow player
 * dmc.tick(Direction.LEFT);
 * assertTrue(a.getPosition().equals(new Position(3, 0)),
 * a.getPosition().toString());
 * 
 * dmc.tick(Direction.LEFT);
 * assertTrue(a.getPosition().equals(new Position(2, 0)),
 * a.getPosition().toString());
 * 
 * dmc.tick(Direction.RIGHT);
 * assertTrue(a.getPosition().equals(new Position(1, 0)),
 * a.getPosition().toString());
 * 
 * for (int i = 0; i < 2; i++) {
 * res = dmc.tick(Direction.DOWN);
 * }
 * assertTrue(a.getPosition().equals(new Position(2, 1)),
 * a.getPosition().toString());
 * }
 * 
 * @Test
 * 
 * @DisplayName("Test movement of invisibility on assassin outside recon")
 * public void testAssassinMovementInvisibleOutsideRecon() {
 * DungeonManiaController dmc = new DungeonManiaController();
 * DungeonResponse res = dmc.newGame("d_assassinTest_InvisibleMove",
 * "c_assassinInvisibleMove");
 * EntityResponse check = TestUtils.getEntities(res,
 * "invisibility_potion").get(0);
 * 
 * dmc.tick(Direction.RIGHT);
 * 
 * Entity a = dmc.getDungeon().getEntitiesAtPosition(new Position(7, 0)).get(0);
 * assertNotNull(a);
 * DungeonResponse dr1 = dmc.getDungeonResponseModel();
 * Position assassinPos = getEntities(dr1, "assassin").get(0).getPosition();
 * 
 * List<Position> possibleDest = new ArrayList<>();
 * possibleDest.add(new Position(assassinPos.getX(), assassinPos.getY() + 1));
 * possibleDest.add(new Position(assassinPos.getX(), assassinPos.getY() - 1));
 * possibleDest.add(new Position(assassinPos.getX() + 1, assassinPos.getY()));
 * possibleDest.add(new Position(assassinPos.getX() - 1, assassinPos.getY()));
 * 
 * assertDoesNotThrow(() -> dmc.tick(check.getId()));
 * 
 * assertTrue(possibleDest.contains(a.getPosition()));
 * }
 * 
 * @Test
 * 
 * @DisplayName("Test movement of invisibility on assassin inside recon")
 * public void testAssassinInsideReconInvisible() {
 * DungeonManiaController dmc = new DungeonManiaController();
 * DungeonResponse res = dmc.newGame("d_assassinTest_InvisibleMove",
 * "c_assassinInsideRecon");
 * EntityResponse check = TestUtils.getEntities(res,
 * "invisibility_potion").get(0);
 * 
 * dmc.tick(Direction.RIGHT);
 * 
 * assertDoesNotThrow(() -> dmc.tick(check.getId()));
 * Entity a = dmc.getDungeon().getEntitiesAtPosition(new Position(6, 0)).get(0);
 * assertNotNull(a);
 * 
 * // should still move towards player as in recon radius
 * for (int i = 0; i < 3; i++) {
 * res = dmc.tick(Direction.LEFT);
 * }
 * 
 * assertTrue(a.getPosition().equals(new Position(3, 0)),
 * a.getPosition().toString());
 * }
 * }
 */