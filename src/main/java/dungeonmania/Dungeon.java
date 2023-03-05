package dungeonmania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import dungeonmania.Entities.Collectable;
import dungeonmania.Entities.*;
import dungeonmania.Entities.Player;
import dungeonmania.reactions.Move;
import dungeonmania.reactions.Reaction;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Entities.Collectables.ItemsToUseFactory;
import dungeonmania.Entities.Collectables.Sceptre;
import dungeonmania.Entities.Logical.Logical;
import dungeonmania.Entities.Weapons.BuildableFactory;
import dungeonmania.exceptions.*;
import dungeonmania.Goals.*;
import dungeonmania.Entities.Weapons.*;

public class Dungeon implements Serializable {
    private static int idNum;
    private String dungeonId;
    private String dungeonName;
    private Player player;
    private Config config;
    private List<Entity> allEntities;
    private Goal goals;
    private int enemiesKilled;
    private List<BattleResponse> battles = new ArrayList<BattleResponse>();

    //time travel class
    private TimeController timecontroller;
    private boolean isTimeTravelling;
    private boolean enteredTimePortal;

    /**
     * Create new dungeon
     * @param config
     * @param allEntities
     * @param goals
     */
    public Dungeon(Config config, List<Entity> allEntities, String dungeonName, Goal goals) {
        this.config = config;
        this.allEntities = allEntities;
        this.dungeonName = dungeonName;
        this.goals = goals;

        // Find player
        this.player = (Player) allEntities.stream().filter(e -> e instanceof Player).findFirst().get();

        // Attach player to observers
        attachObservers();

        //set default entities
        setTimeController(allEntities);

        // Set id
        this.dungeonId = "Dungeon" + idNum;
        idNum++;

    }

    private void setTimeController(List<Entity> entities) {
        timecontroller = new TimeController();
        isTimeTravelling = true;
        enteredTimePortal = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Config getConfig() {
        return this.config;
    }

    public DungeonResponse getDungeonResponseModel() {

        List<EntityResponse> entitiesResponse = new ArrayList<>();
        for (Entity entity : allEntities) {
            entitiesResponse.add(entity.getEntityResponse());
        }

        List<ItemResponse> inventoryResponse = new ArrayList<>();
        List<String> buildablesResponse = new ArrayList<>();
        if (player == null) {
            buildablesResponse = null;
            inventoryResponse = null;
        } else {
            buildablesResponse = player.getBuildableResponse();
            inventoryResponse = player.getinventoryResponse();
        }

        String g;
        if (goals == null) {
            g = "";
        } else {
            g = goals.toString(this);
        }
        return new DungeonResponse(dungeonId, dungeonName, entitiesResponse, inventoryResponse, battles, buildablesResponse, g);
    }

    public List<Entity> getEntitiesAtPosition(Position p){
        return allEntities.stream().filter(e -> e.getPosition().equals(p)).collect(Collectors.toList());
    }

    public PriorityQueue<Reaction> step(Entity e, Direction d){
        PriorityQueue<Reaction> reactions = new PriorityQueue<>();

        if (e instanceof OldPlayer && timecontroller.inPast() && !isTimeTravelling) {
           reactions.addAll(timecontroller.getCurrentTimePeriod().execute(this, config));
        }
        Position futureP = e.getPosition().translateBy(d);
        for (Entity eAtPos : getEntitiesAtPosition(futureP)){
            Reaction r = eAtPos.interact(e);
            if (r != null) reactions.add(r);
        }
        if (reactions.isEmpty() && e instanceof Movement){
            reactions.add(new Move((Movement) e, futureP));
        }
        return reactions;
    }

    public PriorityQueue<Reaction> step(Entity e, Position p){
        PriorityQueue<Reaction> reactions = new PriorityQueue<>();

        if (e instanceof OldPlayer && timecontroller.inPast() && !isTimeTravelling) {
            reactions.addAll(timecontroller.getCurrentTimePeriod().execute(this, config));
        }
        for (Entity eAtPos : getEntitiesAtPosition(p)){
            Reaction r = eAtPos.interact(e);
            if (r != null) reactions.add(r);
        }
        if (reactions.isEmpty() && e instanceof Movement){
            reactions.add(new Move((Movement) e, p));
        }
        return reactions;
    }
    /**
     * 
     * @param e -> entity
     */
    public void removeEntity(Entity e) {
        if (e instanceof Enemy) {
            enemiesKilled++;
        }
        else if (e instanceof Player) {
            player = null;
            
        }
        allEntities.removeIf(entity -> entity.equals(e));
    }

    public void addEntity(Entity e) {
        allEntities.add(e);
    }


    /**
     * moves player in direction
     * 
     * @param movementDirection
     */
    public void tick(Direction movementDirection) {

        timecontroller.shiftTime("move", movementDirection.toString());
        PriorityQueue<Reaction> reactions = this.step(this.player, movementDirection);
        while (!reactions.isEmpty()){
            reactions.remove().execute(this, reactions);
        }
        tick();
    }

    /**
     * Run all other actions for the tick,
     * Moving entities, and spawning them
     */
    private void tick() {

        if (player == null) {
            return;
        }

        // manage potions
        this.player.managePotions();

        // Move all enemies and execute their reactions
        List<Entity> toMove = allEntities.stream().filter(e -> e instanceof Enemy).collect(Collectors.toList());
        for (Entity entity : toMove) {
            PriorityQueue<Reaction> reactions = this.step(entity, ((Enemy) entity).getNextPosition());

            while (!reactions.isEmpty()){
                Reaction r = reactions.remove();
                if (r != null) {
                    r.execute(this, reactions);
                }
            }
        }
        EntityFactory.spawnEntities(config, allEntities, timecontroller.getTick());

        // manage mind control duration
        if (player != null) {
            this.player.tickMindControlAllies();
        }
        
        resetLogicalEntities();
    }

    /**
     * This tick caters for items used/attempted  
     * @param itemUsedId
     */
    public void tick(String itemUsedId) throws InvalidActionException, IllegalArgumentException {
        List<Entity> allCollectables = this.getPlayer().getInventory();

        //log action in time controller
        timecontroller.shiftTime("use", itemUsedId);

        // Check if the id passed in is not in inventory
        if (!allCollectables.stream().anyMatch(x -> x.getId().equals(itemUsedId))) {
            tick();
            throw new InvalidActionException("The entity id passed is not in player's inventory.");
        }

        for (Entity checkIfIn : allCollectables) {
            // If we have found the id given in players inventory
            if (checkIfIn.getId().equals(itemUsedId)) {
                // The item id can be used
                if (ItemsToUseFactory.canBeUsed(checkIfIn)) {
                    Collectable reCastedEnt = (Collectable) checkIfIn; 
                    PriorityQueue<Reaction> reactions = new PriorityQueue<>();

                    Reaction r = getPlayer().addItemsBeingUsed(reCastedEnt);
                    if (r != null) reactions.add(r);

                    while (!reactions.isEmpty()){
                        r = reactions.remove();
                        if (r != null) {
                            r.execute(this, reactions);
                        }
                    }
                    break;
                } 
                // If item cannot be used
                else {
                    tick(); 
                    throw new IllegalArgumentException("itemUsed must be one of bomb, invincibility_potion, invisibility_potion");
                }
            }
        }

        tick();
    }

    public boolean playerAtExit() {
        if (player == null) {
            return false;
        }
        Exit e = (Exit) allEntities.stream().filter(ex -> ex instanceof Exit).findFirst().get();
        return player.getPosition().equals(e.getPosition());
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public void checkWin() {
        if (goals == null) {
            return;
        }
        else if (goals.checkGoal(this)) {
            goals = null;
        }
    }

    public boolean checkSwitches() {
        return allEntities.stream().filter(e -> e instanceof Switch).map(e -> (Switch) e).allMatch(e -> e.getSwitchStatus());
    }

    public void build(String buildable) throws IllegalArgumentException, InvalidActionException {

        //if not a valid buildable throw
        Entity e = BuildableFactory.getEntity(config, buildable);
        if (e instanceof Buildable == false) {
            throw new IllegalArgumentException("The buildable " + buildable + " does not exist!");
        } 

        //check if it can be built 
        Buildable b = (Buildable) e;
        if (!b.isBuildable(player)) {
            throw new InvalidActionException("Cannot build this item!");
        }
        List<Entity> ingredients = new ArrayList<>();

        if (b instanceof Bow) {
            ingredients = Bow.getIngredients(player);
        } else if (b instanceof Shield) {
            ingredients = Shield.getIngredients(player);
        } else if (b instanceof Sceptre) {
            ingredients = Sceptre.getIngredients(player);
        } else if (b instanceof MidnightArmour) {
            ingredients = MidnightArmour.getIngredients(player);
        }

        //if yes add to player's inventory
        //convert to reaction
        ingredients.stream().forEach(i -> this.player.deleteByType(i));
        this.player.addItem(e);

        //log action in time controller
        timecontroller.shiftTime("build", buildable);
    }

    public void interact(String entityId) throws IllegalArgumentException, InvalidActionException{

        //log action in time controller
        timecontroller.shiftTime("interact", entityId);
        for (Entity e : this.allEntities){
            if (e.getId().equals(entityId)){
                try{
                    this.player.interactOnClick(e).execute(this, null);
                    return;
                } catch (InvalidActionException exp){
                    throw exp;
                }
            }
        }
        throw new IllegalArgumentException(entityId + " not found -> entityId is not a valid ID");
    }

    public void addBattle(BattleResponse b) {
        battles.add(b);
    }

    public List<Entity> getEntities() {
        return allEntities;
    }

    public void setBattles(List<BattleResponse> b) {
        this.battles = b;
    }

    public TimeController getTimecontroller() {
        return this.timecontroller;
    }

    public String getDungeonName() {
        return dungeonName;
    }

    public void setPlayer(Player p) {
        this.player = p;
    }

    public boolean isTimeTravelling() {
        return isTimeTravelling;
    }

    public void settimeTravel(boolean expression) {
        isTimeTravelling = expression;
    }

    public void setTimecontroller(TimeController timecontroller) {
        this.timecontroller = timecontroller;
    }

    public void SetTimePortal(boolean expression) {
        enteredTimePortal = expression;
    }

    public boolean getTimePortalStatus() {
        return enteredTimePortal;
    }

    public <T> int checkEntityCount(Class<T> type) {
        return allEntities.stream().filter(e -> e.getClass().equals(type)).collect(Collectors.toList()).size();
    }
    
    private void resetLogicalEntities() {
        List<Entity> logicaEntities = allEntities.stream().filter(e -> e instanceof Logical).collect(Collectors.toList());

        for (Entity e : logicaEntities) {
            ((Logical) e).resetWasActivatedThisTick();
        }
    }

    private void attachObservers() {
        List<Entity> observers = allEntities.stream().filter(e -> e instanceof PlayerObserver).collect(Collectors.toList());
        for (Entity observer : observers) {
            ((PlayerObserver) observer).attachPlayer(this.player);
        }
    }
}

