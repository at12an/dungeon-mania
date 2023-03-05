package dungeonmania.reactions;

import java.util.Collection;

import dungeonmania.Dungeon;
import dungeonmania.Entities.*;

public class ActivateSwitch extends Reaction {
    private static final int priority = 10;

    Switch sw;
    Boulder boulder;

    public ActivateSwitch(Switch sw, Boulder boulder) {
        this.sw = sw;
        this.boulder = boulder;
    }

    public void execute(Dungeon d, Collection<Reaction> reactions){
        reactions.add(new Move(boulder, sw.getPosition()));
        d.checkWin();
        reactions.add(new ToggleLogical(sw, "activate"));
    }

    public int getPriority(){
        return priority;
    }    

    public String getType(){
        return "ActivateSwitch";
    }
}
