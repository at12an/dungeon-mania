package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Mercenary;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.Collectables.Treasure;
import dungeonmania.Entities.Collectables.SunStone;
import dungeonmania.exceptions.InvalidActionException;

public class BribeMerc extends Reaction {

    private static final int priority = 1;

    private Mercenary m;
    private Player p;

    public BribeMerc(Player p, Mercenary m) throws InvalidActionException{
        if (p.checkItemCount(Treasure.class) - p.checkItemCount(SunStone.class) < m.getBribeAmount()){
            throw new InvalidActionException("Not Enough Money to Bribe.");
        }
        if (!m.inBribeableRange(p)){
            throw new InvalidActionException("Not within Range.");
        }
        this.p = p;
        this.m = m;
    }

    /**
     * @param d
     * @param reactions, should be be null;
     */
    public void execute(Dungeon d, Collection<Reaction> reactions){
        if (p.addAlly(m)) {m.becomeAlly();} // your frontend doesn't work and is interacting with merc's too many times.
    }

    public int getPriority(){
        return priority;
    }    

    public String getType(){
        return "BribeMerc";
    }
}
