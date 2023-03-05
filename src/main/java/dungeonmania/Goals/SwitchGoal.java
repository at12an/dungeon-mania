package dungeonmania.Goals;

import dungeonmania.Dungeon;
import dungeonmania.Entities.*;
import java.util.stream.Collectors;

/**
 * @author Max Ovington
 */
public class SwitchGoal implements Goal {

    public boolean checkGoal(Dungeon d) {
        return d.checkSwitches();
    }
    
    public String toString(Dungeon d) {
        if (!checkGoal(d)) {
            return ":boulders(" + d.getEntities().stream().filter(e -> e instanceof Boulder).collect(Collectors.toList()).size() + ")/:switch(" + d.getEntities().stream().filter(e -> e instanceof Switch).collect(Collectors.toList()).size() + ")";
        }
        return "";
    }

}
