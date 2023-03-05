package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.Entities.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.Entities.Collectables.*;

public class BribeAssassin extends Reaction {
    private static final int priority = 1;

    private Assassin a;
    private Player p;

    public BribeAssassin(Player p, Assassin a) throws InvalidActionException {
        if (p.checkItemCount(Treasure.class) < a.getBribeAmount()){
            throw new InvalidActionException("Not Enough Money to Bribe.");
        }
        if (!a.inBribeableRange(p)){
            throw new InvalidActionException("Not within Range.");
        }
        this.p = p;
        this.a = a;
    }

    public void execute(Dungeon d, Collection<Reaction> reactions) {
        if (a.successfulBribe()) {
            if (p.addAlly(a)) {a.becomeAlly();}
        } else {
            p.wasteGold(a);
        }
    }

    public int getPriority(){
        return priority;
    }    

    public String getType(){
        return "BribeAssassin";
    }
}
