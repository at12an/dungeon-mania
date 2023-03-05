package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Entity;

public class KillEnemy extends Reaction{

    private Entity e;
    private static final int priority = 3;

    public KillEnemy(Entity enemy) {
        this.e = enemy;
    }

    public void execute(Dungeon d, Collection<Reaction> reactions) {
        d.removeEntity(e);
        d.checkWin();
    }

    public String getType() {
        return "KillEnemy";
    }

    public int getPriority(){
        return priority;
    } 
}
