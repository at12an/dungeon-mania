package dungeonmania.Goals;

import dungeonmania.Dungeon;

/**
 * @author Max Ovington
 */
public class ExitGoal implements Goal {

    public boolean checkGoal(Dungeon d) {
        //check player is on exit
        return d.playerAtExit();
    }

    public String toString(Dungeon d) {
        if (!checkGoal(d)) {
            return ":exit";
        }
        return "";
    }
}
