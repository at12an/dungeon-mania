package dungeonmania.Entities.Weapons;

import dungeonmania.Entities.Entity;
import dungeonmania.util.*;
import dungeonmania.reactions.*;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entities.Player;
import dungeonmania.Entities.Collectables.Key;
import dungeonmania.Entities.Collectables.SunStone;
import dungeonmania.Entities.Collectables.Treasure;
import dungeonmania.Entities.Collectables.Wood;

public class Shield extends Entity implements Buildable, Weapon {

    int durability;
    int defense;

    public Shield(String type, Position position, int durability, int defense) {
        super(type, position);
        this.durability = durability;
        this.defense = defense;
    }

    public void useWeapon(List<Entity> bin) {
        durability--;
        if (durability == 0) bin.add(this);
    };

    public Reaction interact(Entity e) {
        return null;
    };

    public boolean isBuildable(Player p) {

        return (p.checkItemCount(Wood.class) >= 2 && 
        (p.checkItemCount(Key.class) >= 1 || p.checkItemCount(Treasure.class) >= 1 || p.checkItemCount(SunStone.class) >= 1) &&
        p.checkItemCount(this.getClass()) < 1);
        
    }


    public static List<Entity> getIngredients(Player p) {
        List<Entity> returnList = new ArrayList<>();
        returnList.add(new Wood("wood", null));
        returnList.add(new Wood("wood", null));
        if (
            (p.checkItemCount(Key.class) >= 1 && p.checkItemCount(Treasure.class) >= 1) ||
            (p.checkItemCount(Key.class) < 1 && p.checkItemCount(Treasure.class) >= 1)) 
            {
                if (p.checkItemCount(SunStone.class) < 1) {
                    returnList.add(new Treasure("treasure", null));
                }
        } else if (p.checkItemCount(Key.class) >= 1 && p.checkItemCount(Treasure.class) < 1)
        {
            if (p.checkItemCount(SunStone.class) < 1) {
                returnList.add(new Key("key", null, 0));
            }
        }

        return returnList;
    }

    public int getDefense() {
        return defense;
    }

}