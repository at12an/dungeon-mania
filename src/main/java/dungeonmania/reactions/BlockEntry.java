package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;

public class BlockEntry extends Reaction{
    
    private static final int priority = 2;

    public BlockEntry(){;}

    public void execute(Dungeon d, Collection<Reaction> reactions){
        reactions.clear(); // stops any other reactions that occur on this tile from occuring
    }

    public int getPriority(){
        return priority;
    }    

    public String getType(){
        return "BlockEntry";
    }
}
