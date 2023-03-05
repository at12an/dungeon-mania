package dungeonmania.Entities.Weapons;

import dungeonmania.Entities.Entity;
import dungeonmania.util.*;
import dungeonmania.reactions.*;

import java.util.ArrayList;
import java.util.List;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.OldPlayer;
import dungeonmania.Entities.Collectables.SunStone;

public class MidnightArmour extends Entity implements Buildable {

    int defense;
    int damage;

    public MidnightArmour(String type, Position position, int defense, int damage) {
        super(type, position);
        this.defense = defense;
        this.damage = damage;
    }

    public Reaction interact(Entity e) {
        return null;
    };

    public boolean isBuildable(Player p) {
        if (p instanceof OldPlayer) {
        }


        return p.checkItemCount(Sword.class) >= 1 && p.checkItemCount(SunStone.class) >= 1;
        
    }


    public static List<Entity> getIngredients(Player p) {
        List<Entity> returnList = new ArrayList<>();
        returnList.add(new SunStone("sun_stone", null));
        returnList.add(new Sword("sword",null, 0, 0));
        return returnList;
    }

    public int getDefense() {
        return defense;
    }

    public int getDamage() {
        return damage;
    }

    
}