package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.Movement;
import dungeonmania.Entities.Boulder;
import dungeonmania.Entities.Entity;
import dungeonmania.util.Direction;

public class Push extends Reaction{
    
    private static final int priority = 9; // one more than move

    private Entity pusher;
    private Boulder boulder;

    public Push(Entity pusher, Boulder boulder){
        this.pusher = pusher;
        this.boulder = boulder;
    }

    public int getPriority(){
        return priority;
    }

    public void execute(Dungeon d, Collection<Reaction> q){

        if (!(pusher instanceof Movement)) return;

        // check if tile is okay for the boulder to move there.
        Collection<Reaction> r = d.step(this.boulder, 
            Direction.getDirectionOf(pusher.getPosition(), boulder.getPosition()));

        // if moving the boulder would result in a collision.
        if (r.stream().anyMatch(c -> c.getClass().equals(BlockEntry.class))){
            q.add( new BlockEntry()); // TODO: consider removing
            return;
        }
      
        // else attempt to move the boulder and the dude.
        q.addAll(r); // add 
        q.add(new Move((Movement) pusher, boulder.getPosition()));
    }


    public String getType(){
        return "Push";
    }
}
