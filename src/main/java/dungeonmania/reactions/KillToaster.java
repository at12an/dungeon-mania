package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.ZombieToastSpawner;
import dungeonmania.Entities.Weapons.Weapon;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

public class KillToaster extends Reaction {

    private static final int priority = 1;

    private ZombieToastSpawner toaster;

    public KillToaster(Player p, ZombieToastSpawner zt) throws InvalidActionException{

        if (!p.getInventory().stream().anyMatch(e -> e instanceof Weapon)){
            // if player aint got no weapon
            throw new InvalidActionException("Player must have a weapon to destroy a toaster.");
        } 

        // toaster is not adjacent to player
        if (!Position.isAdjacent(p.getPosition(), zt.getPosition())) {
            throw new InvalidActionException("Player must be adjacent to a toaster to destroy it.");
        }

        this.toaster = zt;
    }

    /**
     * @param d
     * @param reactions, can be null;
     */
    public void execute(Dungeon d, Collection<Reaction> reactions) {
        d.removeEntity(this.toaster);
    }

    public int getPriority(){
        return priority;
    }    

    public String getType(){
        return "KillToaster";
    }
}
