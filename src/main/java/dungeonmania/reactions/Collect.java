package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;
import dungeonmania.Entities.*;
import dungeonmania.Entities.Collectables.*;

public class Collect extends Reaction  {

    private Entity toCollect;
    private Player player;
    private Position dest;

    private static final int priority = 3;

    public Collect(Entity toCollect, Player player, Position dest) {
        this.toCollect = toCollect;
        this.player = player;
        this.dest = dest;
    }

    public int getPriority(){
        return priority;
    } 

    public void execute(Dungeon d, Collection<Reaction> reactions){
        //add item to player inventory
        //check if item is a key
        boolean multipleKey = !(toCollect instanceof Key) || !(player.checkItemCount(Key.class) >= 1);
        if (multipleKey) {
            player.addItem(toCollect);
            d.removeEntity(toCollect);
        } 

        //check collection progress
        d.checkWin();

        //move player
        reactions.add(new Move(player, dest));
    }

    public String getType(){
        return "Collect";
    }

    
}
