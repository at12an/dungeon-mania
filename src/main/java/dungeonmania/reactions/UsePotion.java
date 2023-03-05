package dungeonmania.reactions;
import dungeonmania.Entities.*;
import java.util.Collection;
import dungeonmania.Entities.Collectables.*;

import dungeonmania.Dungeon;

public class UsePotion extends Reaction {

    private static final int priority = 2;
    private Player player;
    private Potion potion;

    public UsePotion(Potion potion, Player player) {
        this.player = player;
        this.potion = potion;
    }

    public void execute(Dungeon d, Collection<Reaction> reactions) {
        player.addCurrentlyUsing(potion);
        player.deleteItemInventory(potion);
    }

    public int getPriority(){
        return priority;
    }    

    public String getType(){
        return "UsePotion";
    }
}
