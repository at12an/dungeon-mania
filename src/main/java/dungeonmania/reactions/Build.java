package dungeonmania.reactions;
import java.util.Collection;
import dungeonmania.Entities.*;
import dungeonmania.Entities.Weapons.*;

import dungeonmania.Dungeon;

public class Build extends Reaction{
    
    private static final int priority = 12;
    private Player p;
    private Buildable buildable;

    public Build(Player playable, Buildable buildable) {
        this.p = playable;
        this.buildable = buildable;
    }

    public void execute(Dungeon d, Collection<Reaction> reactions) {
        if (!buildable.isBuildable(p)) {
            return;
        }
        if (buildable instanceof Bow) {
            Bow.getIngredients(p).stream().forEach(i -> p.deleteByType(i));
        } else if (buildable instanceof Shield) {
            Shield.getIngredients(p).stream().forEach(i -> p.deleteByType(i));
        }
        Entity e = (Entity) buildable;
        p.addItem(e);
    }

    public int getPriority(){
        return priority;
    }    

    public String getType(){
        return "Build";
    }
}
