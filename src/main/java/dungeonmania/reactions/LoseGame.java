package dungeonmania.reactions;

import java.util.Collection;
import dungeonmania.Entities.*;

import dungeonmania.Dungeon;

public class LoseGame extends Reaction {
    private Player p;
    private static final int priority = 1;

    public LoseGame(Player p) {
        this.p = p;
    }

    public void execute(Dungeon d, Collection<Reaction> reactions) {
        d.removeEntity(p);
        Entity oldPlayer = d.getEntities().stream().filter(e -> e instanceof OldPlayer).findFirst().orElse(null);
        d.removeEntity(oldPlayer);
        reactions.clear();
    }

    public String getType() {
        return "LoseGame";
    }

    public int getPriority(){
        return priority;
    } 
}
