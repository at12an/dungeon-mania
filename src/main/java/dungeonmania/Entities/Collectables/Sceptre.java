package dungeonmania.Entities.Collectables;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entities.Collectable;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.OldPlayer;
import dungeonmania.Entities.Player;
import dungeonmania.Entities.Weapons.Buildable;
import dungeonmania.reactions.Reaction;
import dungeonmania.util.Position;

public class Sceptre extends Collectable implements Buildable {

    int duration;

    public Sceptre(String type, Position position, int duration) {
        super(type, position);
        this.duration = duration;
    }

    public Reaction interact(Entity e) {
        return null;
    };

    public boolean isBuildable(Player p) {
        if (p instanceof OldPlayer) {
        }
        return ((p.checkItemCount(Wood.class) >= 1 || p.checkItemCount(Arrow.class) >= 2) && p.checkItemCount(SunStone.class) >= 1) && (p.checkItemCount(Key.class) >= 1 || p.checkItemCount(Treasure.class) >= 1 || p.checkItemCount(SunStone.class) >= 2);
        
    }


    public static List<Entity> getIngredients(Player p) {
        List<Entity> returnList = new ArrayList<>();
        returnList.add(new SunStone("sun_stone", null));
        if (
            (p.checkItemCount(Wood.class) >= 1 && p.checkItemCount(Arrow.class) >= 1) ||
            (p.checkItemCount(Arrow.class) < 2 && p.checkItemCount(Wood.class) >= 1)) 
            {
                returnList.add(new Wood("wood", null));
        } else if (p.checkItemCount(Arrow.class) >= 2 && p.checkItemCount(Wood.class) < 1)
        {
            returnList.add(new Arrow("arrow", null));
            returnList.add(new Arrow("arrow", null));
        }
        if (
            (p.checkItemCount(Key.class) >= 1 && p.checkItemCount(Treasure.class) >= 1) ||
            (p.checkItemCount(Key.class) < 1 && p.checkItemCount(Treasure.class) >= 1)) 
            {
                if (p.checkItemCount(SunStone.class) < 2) {
                    returnList.add(new Treasure("treasure", null));
                }
        } else if (p.checkItemCount(Key.class) >= 1 && p.checkItemCount(Treasure.class) < 1)
        {
            if (p.checkItemCount(SunStone.class) < 2) {
                returnList.add(new Key("key", null, 0));
            }
        }
        return returnList;
    }

    public int getDuration() {
        return duration;
    }

    
}
