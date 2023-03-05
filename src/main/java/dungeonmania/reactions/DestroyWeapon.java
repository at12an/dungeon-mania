package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.Entities.*;

public class DestroyWeapon extends Reaction  {

    private Entity weapon;

    private static final int priority = 3;

    public DestroyWeapon(Entity weapon) {
        this.weapon = weapon;
    }

    public int getPriority(){
        return priority;
    } 

    public void execute(Dungeon d, Collection<Reaction> reactions){
        d.getPlayer().deleteItemInventory(weapon);
    }

    public String getType(){
        return "DestroyWeapon";
    }
}
