package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;
import dungeonmania.Movement;

public class Move extends Reaction {

    // should occur last
    private static final int priority = 8;

    private Movement toBeMoved;
    private Position dest;

    public Move(Movement toBeMoved, Position dest) {
        this.toBeMoved = toBeMoved;
        this.dest = dest;
    }

    public void execute(Dungeon d, Collection<Reaction> reactions){
        toBeMoved.move(this.dest);       
    }

    public int getPriority(){
        return priority;
    }

    public String getType(){
        return "Move";
    }
    
}
