package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.Entities.Mercenary;
import dungeonmania.Entities.Assassin;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Player;

public class MindControl extends Reaction {

    private static final int priority = 1;
    private int duration;
    private Entity e;
    private Player p;

    public MindControl(Player p, Entity e, int duration){
        this.p = p;
        this.e = e;
        this.duration = duration;
    }

    /**
     * @param d
     * @param reactions, should be be null;
     */
    public void execute(Dungeon d, Collection<Reaction> reactions){
        if (e instanceof Mercenary) {
            Mercenary m = (Mercenary) e;
            if (p.addAlly2(m)) {
                m.becomeAlly();
                m.setControlled(true);
                m.setDuration(duration);
            } // your frontend doesn't work and is interacting with merc's too many times.
        } else {
            Assassin a = (Assassin) e;
            if (p.addAlly2(a)) {
                a.becomeAlly();
                a.setControlled(true);
                a.setDuration(duration);
            } // your frontend doesn't work and is interacting with merc's too many times.
        }
    }

    public int getPriority(){
        return priority;
    }    

    public String getType(){
        return "MindControl";
    }

    public int getDuration() {
        return duration;
    }
}
