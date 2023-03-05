package dungeonmania.reactions;

import java.util.Collection;
import dungeonmania.util.Position;
import dungeonmania.Entities.*;

import dungeonmania.Dungeon;

public class ExitReached extends Reaction{
    
    private Position dest;
    private Player p;
    public static final int priority = 1;
    
    public ExitReached(Position dest, Player p) {
        this.dest = dest;
        this.p = p;
    }

    public ExitReached(){;}

    public void execute(Dungeon d, Collection<Reaction> reactions){
        p.move(this.dest);  
        
        d.checkWin();
    }

    public int getPriority(){
        return priority;
    } 

    public String getType(){
        return "ExitReached";
    }
}
