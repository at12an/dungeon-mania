package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.ZombieToastSpawner;
import dungeonmania.Entities.Weapons.Weapon;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

public class KillToasterOldPlayer extends Reaction {

    private static final int priority = 12;

    private ZombieToastSpawner toaster;
    private Player p;

    public KillToasterOldPlayer(Player p, ZombieToastSpawner zt) throws InvalidActionException{
        this.toaster = zt;
        this.p = p;
    }

    /**
     * @param d
     * @param reactions, can be null;
     */
    public void execute(Dungeon d, Collection<Reaction> reactions) {
        if (!p.getInventory().stream().anyMatch(e -> e instanceof Weapon) || !Position.isAdjacent(p.getPosition(), toaster.getPosition())) {
            return;
        }
        d.removeEntity(this.toaster);
    }

    public int getPriority(){
        return priority;
    }    

    public String getType(){
        return "KillToasterOldPlayer";
    }
}
