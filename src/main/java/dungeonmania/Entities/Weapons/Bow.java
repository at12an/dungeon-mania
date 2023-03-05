package dungeonmania.Entities.Weapons;

import dungeonmania.Entities.*;
import dungeonmania.util.*;
import dungeonmania.reactions.*;
import dungeonmania.Entities.Collectables.Wood;
import dungeonmania.Entities.Collectables.Arrow;
import java.util.List;
import java.util.ArrayList;

public class Bow extends Entity implements Buildable, Weapon {

    int damage;
    int durability;

    public Bow(String type, Position position, int damage, int durability) {
        super(type, position);
        this.damage = damage;
        this.durability = durability;
    }

    public void useWeapon(List<Entity> bin) {
        durability--;
        if (durability == 0) bin.add(this);
    };

    public Reaction interact(Entity e) {
        return null;
    };

    public static List<Entity> getIngredients(Player p) {
        List<Entity> returnList = new ArrayList<>();
        returnList.add(new Wood("wood", null));
        returnList.add(new Arrow("arrow", null));
        returnList.add(new Arrow("arrow", null));
        returnList.add(new Arrow("arrow", null));
            
        return returnList;
    }

    public boolean isBuildable(Player p) {
        
        return (p.checkItemCount(Wood.class) >= 1 && p.checkItemCount(Arrow.class) >= 3
        && p.checkItemCount(this.getClass()) < 1);
    }

}
