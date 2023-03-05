package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Mercenary;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.Collectables.Treasure;
import dungeonmania.exceptions.InvalidActionException;

public class BribeMercOldPlayer extends Reaction {

    private static final int priority = 12;

    private Mercenary m;
    private Player p;

    public BribeMercOldPlayer(Player p, Mercenary m) throws InvalidActionException{
        this.p = p;
        this.m = m;
    }

    /**
     * @param d
     * @param reactions, should be be null;
     */
    public void execute(Dungeon d, Collection<Reaction> reactions){
        if (p.checkItemCount(Treasure.class) < m.getBribeAmount() || !m.inBribeableRange(p)) {
            return;
        }
        if (p.addAlly(m)) {m.becomeAlly();} // your frontend doesn't work and is interacting with merc's too many times.
    }

    public int getPriority(){
        return priority;
    }    

    public String getType(){
        return "BribeMercOldPlayer";
    }
}
